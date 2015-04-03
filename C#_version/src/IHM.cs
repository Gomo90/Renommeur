using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Collections.Specialized;

namespace Renommeur
{
    public partial class IHM : Form
    {

        private Boolean premierChargement = true;
        private Boolean decoupageFichiers = false;
        private StringCollection fichiersCharges = new StringCollection();
        private StringCollection nomsAleatoiresGeneres = new StringCollection();
        Renommage renommageFichiers;
        IHMNbFragments fenetreSaisieDecoupe;
        Decoupage decouperFichiers;

        public static ProgressBar progressBar = new System.Windows.Forms.ProgressBar();

        public IHM()
        {
            InitializeComponent();
        }

        private void menuItemChargerFichiers_Click(object sender, EventArgs e)
        {
   
            System.Diagnostics.Debug.WriteLine("charge fichiers");

            if (fichiersCharges.Count != 0)
            {
                fichiersCharges.Clear();
            }

           
            // Si des fichiers ont été sélectionnés
            if (openFileDialog1.ShowDialog() == DialogResult.OK)
            {
                // Récupération des chemins des fichiers
                fichiersCharges.AddRange(openFileDialog1.FileNames);
                
                // Si le fenêtre est inférieur aux dimensons de départ
                if (this.Width < 500 || this.Height < 500)
                {
                    // Application des dimensions de départ
                    this.Size = new Size(500, 500);
                }

                // Si un découpage de fichiers a été lancé
                if (decoupageFichiers)
                {
                    // Remplacement de la progressBar par le bouton "renommer" 
                    this.Conteneur.Controls.Remove(progressBar);
                    this.Conteneur.Controls.Add(btnRenommer);

                    decoupageFichiers = false;
                }


                if (premierChargement)
                {

                    // Appel de la méthode de création de la grille d'affichage des fichiers chargés
                    chargementGrille();
                }
                else
                {
                    majGrille();

                    // Activation du bouton "Renommer"
                    btnRenommer.Enabled = true;
                }
               
            }
            
        }

        private void menuItemViderGrille_Click(object sender, EventArgs e)
        {
            // Suppression de toutes les lignes de la grille d'affichage
            grilleAffichage.Rows.Clear();

            // Suppression des chemins de fichiers stockés
            fichiersCharges.Clear();

            // Si un découpage de fichiers a été lancé
            if (decoupageFichiers)
            {
                // Remplacement de la progressBar par le bouton "renommer" 
                this.Conteneur.Controls.Remove(progressBar);
                this.Conteneur.Controls.Add(btnRenommer);

                decoupageFichiers = false;
            }
         
        }

        private void menuItemQuitter_Click(object sender, EventArgs e)
        {
            Application.Exit();
            //this.Close();
        }

        private void btnRenommer_Click(object sender, EventArgs e)
        {
            // Instance de la classe "RenommageFichiers"
            renommageFichiers = new Renommage(fichiersCharges);

            // Appel de la fonction de génération d'un nom aléatoire
            renommageFichiers.nomAleatoire();

            // Appel de la fonction de renommage d'un fichier
            nomsAleatoiresGeneres = renommageFichiers.renommerFichiers();

            // Parcours des noms aléatoires générés
            for (int i = 0; i < nomsAleatoiresGeneres.Count; i++)
            {
                // Affichage dans le colonne correspondante des nouveaux noms des fichiers
                grilleAffichage.Rows[i].Cells[1].Value = nomsAleatoiresGeneres[i];
              
            }

            // Désactivation du bouton "Renommer"
            btnRenommer.Enabled = false;

            // Activation de l'option de découpage de fichiers
            menuItemDécouperFichiers.Enabled = true;

        }

        /* Méthode pour créer la grille d'affichage des fichiers chargés */
        private void chargementGrille()
        {
            System.Diagnostics.Debug.WriteLine("chargementGrille()");

            for (int i = 0; i < fichiersCharges.Count; i++)
            {
                grilleAffichage.Rows.Add(fichiersCharges[i]);
            }

            // Affichage de la grille
            grilleAffichage.Size = new Size(500, 450);
            grilleAffichage.Visible = true;

            // Affichage du bouton "Renommer"
            btnRenommer.Visible = true;

            // Option pour vider la grille accessible
            menuItemViderGrille.Enabled = true;

            premierChargement = false;
        }

        private void majGrille()
        {
            System.Diagnostics.Debug.WriteLine("majGrille()");

            // Suppression de toutes les lignes de la grille d'affichage
            grilleAffichage.Rows.Clear();

            // Affichage des nouveaux fichiers chargés
            for (int i = 0; i < fichiersCharges.Count; i++)
            {
                grilleAffichage.Rows.Add(fichiersCharges[i]);

            }
            
        }

        private void menuItemDécouperFichiers_Click(object sender, EventArgs e)
        {
            // Box de saisie du nombre de fragments souaités
            fenetreSaisieDecoupe = new IHMNbFragments();
            fenetreSaisieDecoupe.ShowDialog();

            // Si un nombre de fragments a bien été saisie
            if (IHMNbFragments.nbFragmentsSaisis != 0)
            {

               
                // Définition et placement de la progressBar sur l'interface
                progressBar.Dock = DockStyle.Fill;
                progressBar.TabIndex = 2;
                progressBar.Size = btnRenommer.Size;
                progressBar.Location = new Point(btnRenommer.Location.X, btnRenommer.Location.Y + 20);
                progressBar.Visible = true;

                // Remplacement du bouton "renommer" par la progressBar
                this.Conteneur.Controls.Remove(btnRenommer);
                this.Conteneur.Controls.Add(progressBar);
                
                /*
                for (int i = 0; i < 101; i++)
                {
                    progressBar.Refresh();
                    progressBar.CreateGraphics().DrawString(i.ToString() + "%", new Font("Arial", (float)8.25,
                        FontStyle.Regular), Brushes.Black, new PointF(progressBar.Width / 2 - 10, progressBar.Height / 2 - 7));
                    progressBar.Value = i;
                    System.Threading.Thread.Sleep(100);
                }

                progressBar.Refresh();
                progressBar.CreateGraphics().DrawString("FINI", new Font("Arial", (float)8.25,
                    FontStyle.Regular), Brushes.Black, new PointF(progressBar.Width / 2 - 10, progressBar.Height / 2 - 7));
                */

                decouperFichiers = new Decoupage(Renommage.cheminFichiersRenommes, IHMNbFragments.nbFragmentsSaisis);

                /*
                using (Graphics gr = progressBar.CreateGraphics())
                {
                    progressBar.Refresh();
                    gr.DrawString("Fragmentation terminée", SystemFonts.DefaultFont, Brushes.Black, new PointF(
                        progressBar.Width / 2 - (gr.MeasureString("Fragmentation terminée", SystemFonts.DefaultFont).Width / 2.0F),
                        progressBar.Height / 2 - (gr.MeasureString("Fragmentation terminée", SystemFonts.DefaultFont).Height / 2.0F)));

                   
                }
                 * */

                decoupageFichiers = true;

                // Désactivation de l'option de découpage de fichiers
                menuItemDécouperFichiers.Enabled = false;
            }
        }

    }
}
