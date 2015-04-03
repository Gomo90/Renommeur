using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections.Specialized;
using System.IO;
using System.Windows.Forms;

namespace Renommeur
{
    class Renommage
    {

        private String caracteres = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz" +
            " 0123456789-+§!ù%$()_{}=²&";

        private String[] extensions = {"mp1", "mp2", "mp3", "mp4", "mov", "xls", "doc", "odt", "java", "c", "css", "xml", "html"
		, "avi", "flv", "pdf", "png", "jpeg", "gif", "bat", "cpp", "rar", "zip", "tar", "jar", "iss", "WL", "wav", "nfo", "dll"
		, "cda", "apk", "bin", "rb", "csv", "data", "ini", "m3u", "sfv", "data", "mkv", "exe", "ts", "dvr", "INF", "wma", "sln"
        , "suo", "pdb", "lck", "properties", "log", "jsp", "war"};


        StringCollection fichiersARenommer;
        StringCollection nomsAleatoiresGeneres;
        public static StringCollection cheminFichiersRenommes;

        public Renommage(StringCollection fichiersARenommer)
        {
            this.fichiersARenommer = fichiersARenommer;
            nomsAleatoiresGeneres = new StringCollection();
            cheminFichiersRenommes = new StringCollection();
            System.Diagnostics.Debug.WriteLine("Constructeur Renommage");
        }

        // Méthode assurant les nouveaux noms aléatoires des fichiers
        public void nomAleatoire()
        {
            int tailleNom, numExtensions;
            Random randTaille = new Random();
            Random randCaracteres = new Random();
            Random randTypeExe = new Random();
            String extensionDeBase = null;

            // Parcours de tous les fichiers à renommer
            for (int i = 0; i < fichiersARenommer.Count; i++)
            {
                try
                {

                    // Récupération de l'extension du fichier avant renommage
                    extensionDeBase = fichiersARenommer[i].Substring(fichiersARenommer[i].LastIndexOf(".")).Substring(1);
                
                }catch(ArgumentOutOfRangeException e) {

                    // Affichage d'un message d'erreur
                    MessageBox.Show("Extension manquante ou invalide pour le fichier " + fichiersARenommer[i] + ".", "Erreur",
                                MessageBoxButtons.OK, MessageBoxIcon.Error);
                    e.StackTrace.ToString();
                }


                // Taille du nom du fichier comprise entre 1 et 30 caractères
                tailleNom = 1 + randTaille.Next(30);

                // Nom aléatoire généré
                StringBuilder nomAleatoire = new StringBuilder();

                // Constitution du nom aléatoire en fonction de la taille tirée
                for (int j = 0; j < tailleNom; j++)
                {
                    // Sélection de caractères aléatoires
                    int k = randCaracteres.Next(caracteres.Length);

                    // Construction du nouveau nom du fichier
                    nomAleatoire.Append(caracteres.ElementAt(k));

                }

                // Ajout du point définissant la nouvelle extension du fichier
                nomAleatoire.Append(".");

                // Extension aléatoire ajouté
                // Générer une nouvelle extension tant que celle-ci est la même que l'extension
                // de base du fichier à renommer
                do {

                    numExtensions = randTaille.Next(extensions.Length);

                }while (extensionDeBase.Equals(extensions[numExtensions]));

                // Insertion de l'extension choisie au nom aléatoire généré
                nomAleatoire.Append(extensions[numExtensions]);

                // Si l'extension choisie est ".exe"
                if (extensions[numExtensions] == "exe")
                {
                    // Ajout aléatoirement d'une seconde extension
                    int l = randTypeExe.Next(3);

                    switch (l)
                    {
                        case 0: nomAleatoire.Append(".old"); break;
                        case 1: nomAleatoire.Append(".config"); break;
                    }
                }

                // Si l'extension choisie est ".rar" ou ".tar"
                if (extensions[numExtensions] == "rar" || extensions[numExtensions] == "tar")
                {

                    // Ajout aléatoirement d'une seconde extension
                    int m = randTypeExe.Next(2);

                    switch (m)
                    {
                        case 0: nomAleatoire.Append(".part"); break;
                    }

                }

                // Stockage du nom aléatoire généré
                nomsAleatoiresGeneres.Add(nomAleatoire.ToString());

            }

            //return nomsAleatoiresGeneres;
        }


        // Méthode qui va renommer le ou les fichiers sélectionnés avec les noms aléatoires
        // générés
        public StringCollection renommerFichiers()
        {
            for (int i = 0; i < fichiersARenommer.Count; i++)
            {
                // Si le fichier à renommer existe bien
                if (File.Exists(fichiersARenommer[i])) 
                {
                    // Récupération du chemin du fichier à renommer (pour faire le 2ème param de la fonction "Move") 
                    String chemin = fichiersARenommer[i].Substring(0, fichiersARenommer[i].LastIndexOf("\\"));

                    // Renommage du fichier
                    File.Move(fichiersARenommer[i], chemin + "\\" + nomsAleatoiresGeneres[i]);

                    // Stockage des chemins des fichiers renommés (en cas de découpage)
                    cheminFichiersRenommes.Add(chemin + "\\" + nomsAleatoiresGeneres[i]);
                }
                else 
                {
                    // Affichage d'un message d'erreur
                    MessageBox.Show("Une erreur est survenue lors du renommage du fichier " + fichiersARenommer[i] + ".", "Erreur",
                                MessageBoxButtons.OK, MessageBoxIcon.Error);

                    // Suppression du nom aléatoire qui était destiné au fichier
                    nomsAleatoiresGeneres.RemoveAt(i);
                    
                    // Remplacement par un message d'erreur au niveau de la grille d'affichage
                    nomsAleatoiresGeneres.Insert(i, "Erreur lors du renommage");
                }

            }

            return nomsAleatoiresGeneres;
        }

    }
}
