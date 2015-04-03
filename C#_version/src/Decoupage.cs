using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections.Specialized;
using System.ComponentModel;
using System.IO;
using System.Drawing;
using System.Windows.Forms;

namespace Renommeur
{
    // http://glarde.developpez.com/dotnet/bgworker/cs/
    class Decoupage 
    {
        StringCollection fichiersADecouper;
        int nbFragmentsDecoupe;
        BackgroundWorker bgwWorker; 


        public Decoupage(StringCollection fichiersADecouper, int nbFragments)
        {
            this.fichiersADecouper = fichiersADecouper;
            this.nbFragmentsDecoupe = nbFragments;

            // Initialisation du backgroundWorker pour la découpe des fichiers
            bgwWorker = new BackgroundWorker();

            // Pour signaler la mise à jour de la progressBar
            bgwWorker.WorkerReportsProgress = true;

            // Permettre d'annuler l'opération de découpe
            bgwWorker.WorkerSupportsCancellation = true;

            // Traitements réalisés en arrière plan lors de l'appel du backgroundWorker
            bgwWorker.DoWork += new DoWorkEventHandler(bgwWorker_DoWork);

            // Se charge d'indiquer l'etat d'avancement du traitement
            bgwWorker.ProgressChanged += new ProgressChangedEventHandler(bgwWorker_ProgressChanged);

            // Une fois le traitment du backgroundWorker terminé cette méthode est appellée
            bgwWorker.RunWorkerCompleted += new RunWorkerCompletedEventHandler(bgwWorker_RunWorkerCompleted);

            // Démarrage du backgroundWorker
            bgwWorker.RunWorkerAsync();
        }


        // Méthode exécuter en tâche de fond par le backgroundWorker
        private void bgwWorker_DoWork (object sender, DoWorkEventArgs e)
        {
            BackgroundWorker worker = sender as BackgroundWorker;

            fragmentation(worker);
        }

        // Méthode appellé pour mettre à jour la progressBar
        private void bgwWorker_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {

            //System.Diagnostics.Debug.WriteLine("val BAR : " + e.ProgressPercentage.ToString());

            //IHM.progressBar.CreateGraphics().DrawString(e.ProgressPercentage.ToString() + "%", new Font("Arial", (float)8.25,
            //            FontStyle.Regular), Brushes.Black, new PointF(IHM.progressBar.Width / 2 - 10, IHM.progressBar.Height / 2 - 7));
            
            IHM.progressBar.Value = e.ProgressPercentage;
        }


        // Méthode exécutée automatiquement après bgwWorker_DoWork()
        private void bgwWorker_RunWorkerCompleted (object sender, RunWorkerCompletedEventArgs e)
        {
            System.Diagnostics.Debug.WriteLine("bgwWorker_RunWorkerCompleted val BAR : " + IHM.progressBar.Value);

            //if (IHM.progressBar.Value == 100)
            //{


                MessageBox.Show("Fragmentation terminée", "Informations", MessageBoxButtons.OK, MessageBoxIcon.Information);
                //IHM.Text = "Renommeur 1.1 (Fragmentation en cours...)";

                /*
                using (Graphics gr = IHM.progressBar.CreateGraphics())
                {

                    IHM.progressBar.Refresh();
                    gr.DrawString("Fragmentation terminée", SystemFonts.DefaultFont, Brushes.Black, new PointF(
                        IHM.progressBar.Width / 2 - (gr.MeasureString("Fragmentation terminée", SystemFonts.DefaultFont).Width / 2.0F),
                        IHM.progressBar.Height / 2 - (gr.MeasureString("Fragmentation terminée", SystemFonts.DefaultFont).Height / 2.0F)));

                    
                }
                */
                /*
                IHM.progressBar.CreateGraphics().DrawString("Fragmentation terminée", new Font("Arial", (float)8.25,
                       FontStyle.Regular), Brushes.Black, new PointF(IHM.progressBar.Width / 2 - 10, IHM.progressBar.Height / 2 - 7));

                IHM.progressBar.Refresh();
                 * */
            //}
        }


        // Méthode assurant la fragmentation des fichiers
        private void fragmentation(BackgroundWorker worker)
        {
            // Taille du buffer
            int buffer = 1048576;

            int valBarreProgression, numFragment;
            int valStartProgress = 0;
            int tailleFragment, bytesLus;
            String cheminDestinationFragment, nomFragment;
            
            // Calcul de la valeur de progression de la progressBar en fonction du nombre de fragments a créé
            valBarreProgression = (100 - valStartProgress) / (nbFragmentsDecoupe * fichiersADecouper.Count);

            // Parcours des fichiers à fragmenter
            for (int i = 0; i < fichiersADecouper.Count; i++)
            {
                // Numéro du fragment (initialiser à chaque fichier découpé)
                numFragment = 0;

                // Récupération d'informations sur le fichier à fragmenter
                FileInfo fi = new FileInfo(fichiersADecouper[i]);

                // Taille des fragments à créer selon la saisie utilisateur
                tailleFragment = (int)fi.Length / nbFragmentsDecoupe;

                // Récupération de l'emplacement de création du fragment (même que le fichier source)
                cheminDestinationFragment = fichiersADecouper[i].Substring(0, fichiersADecouper[i].LastIndexOf("\\")+1);

                System.Diagnostics.Debug.WriteLine("tailleFichier : " + fi.Length);
                System.Diagnostics.Debug.WriteLine("tailleFragment : " + tailleFragment);
                System.Diagnostics.Debug.WriteLine("cheminDestinationFragment : " + cheminDestinationFragment);

                StreamReader fichierADecouper = new StreamReader(fichiersADecouper[i]);

                BinaryReader brr = new BinaryReader(fichierADecouper.BaseStream);

                // Parcours du nombre de fragments à créer
                for (int j = 0; j < nbFragmentsDecoupe; j++)
                {
                    // Numéro du fragment
                    numFragment++;

                    // Nom du fragment
                    nomFragment = getNomFragment(i, numFragment);

                    // Ouverture d'un flux de données en "Ecriture" sur le fragment généré
                    StreamWriter fragmentCree = new StreamWriter(cheminDestinationFragment + nomFragment);

                    BinaryWriter brw = new BinaryWriter(fragmentCree.BaseStream);

                    // Compteur des données lues
                    bytesLus = 0;

                    byte[] bytesTab;

                    // Tant que la taille du fragment dépasse celle du buffer
                    while ((tailleFragment - bytesLus) > buffer)
                    {
                        // Fragmentation du fichier source
                        bytesTab = new byte[buffer];

                        bytesTab = brr.ReadBytes(buffer);

                        brw.Write(bytesTab);

                        // Mise à jour du compteur
                        bytesLus += buffer;
                    }

                    // Ecriture des données restantes
                    bytesTab = new byte[(tailleFragment - bytesLus)];
                    //brr.Read(bytesTab, bytesLus, tailleFragment);
                    brw.Write(bytesTab);

                    // Valeur de progression de la progressBar
                    valStartProgress += valBarreProgression;

                    // MAJ de la barre de progression
                    worker.ReportProgress(valStartProgress);

                    fragmentCree.Close();
                    brw.Close();
                }

                fichierADecouper.Close();
                brr.Close();

                // Suppression du fichier source
                fi.Delete();
            }
        }


        // Méthode pour nommer les fragments générés du fichier
        private String getNomFragment(int numFichier, int numFragment)
        {
            StringBuilder nomFragment = new StringBuilder();
            int startIndex;

            // Défini l'endroit où commence la découpe de la sous-chaine pour récupérer le nom
            // du fichier à fragmenter
            startIndex = fichiersADecouper[numFichier].LastIndexOf("\\") + 1;

            // Extraction du nom du fichier découpé servant de base aux fragments générés
            // (2ème param de "Substring" est la taille de la sous-chaine et non l'index de fin pour l'extraction de la
            // sous-chaine comme en Java)
            nomFragment.Append(fichiersADecouper[numFichier].Substring(startIndex,
                              fichiersADecouper[numFichier].Length - startIndex));


            // Insertion du numéro aléatoire dans le nom du fragment
            nomFragment.Insert(nomFragment.ToString().LastIndexOf("."), numFragment);

            return nomFragment.ToString();
        }
    }
}
