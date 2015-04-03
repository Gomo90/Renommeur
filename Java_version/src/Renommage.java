import java.io.File;
import java.util.Random;
import java.util.Vector;

import javax.swing.JOptionPane;


/* Classe assurant le renommage des fichiers charg�s */
public class Renommage {

	private String caracteres = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz" +
			" 0123456789-+�!�%$()_{}=�&";
	
	private String [] extensions = {"mp1", "mp2", "mp3", "mp4", "mov", "xls", "doc", "odt", "java", "c", "css", "xml", "html",
		 "php", "js", "avi", "flv", "pdf", "png", "jpeg", "gif", "bat", "cpp", "rar", "zip", "tar", "jar", "iss", "WL", "wav", 
		 "nfo", "dll", "cda", "apk", "bin", "rb", "csv", "data", "ini", "m3u", "sfv", "data", "mkv", "exe", "ts", "dvr", "INF",
		 "wma", "sln", "suo", "pdb", "lck", "properties", "log", "jsp", "war"};
		 
	
	File[] fichiersARenommer = null;
	public static Vector <String> nomsAleatoiresGeneres;
	JOptionPane boiteErreur = new JOptionPane();
	
	
	// Constructeur de la classe avec en param�tre les fichiers � renommer
	// sous forme de tableau de fichiers
	public Renommage (File[] fichiersARenommer) {
		
		this.fichiersARenommer = fichiersARenommer;
		nomsAleatoiresGeneres = new Vector<String>(fichiersARenommer.length);
	}
	
	
	// M�thode assurant les nouveaux noms al�atoires des fichiers
	public Vector<String> nomAleatoire () {
		
		int tailleNom, numExtensions;
		Random randTaille = new Random();
		Random randCaractere = new Random();
		Random randTypeExe = new Random();
		
		// Initialis� avec une valeur vide dans le cas o� le premier fichier a renommer
		// n'a pas d'extension (evite erreur nullpointerexception condition while)
		String extensionDeBase = null;
		
		for (int i = 0; i < fichiersARenommer.length; i++) {
			
			try {
				
				// R�cup�ration de l'extension du fichier avant renommage
				extensionDeBase = fichiersARenommer[i].getName().substring(fichiersARenommer[i].getName().lastIndexOf(".")).substring(1);
					
			} catch (StringIndexOutOfBoundsException e) {
				
				System.err.println("Extension manquante ou invalide pour le fichier : " + fichiersARenommer[i]);
				//e.printStackTrace();
			}
				
				
				// Taille du nom du fichier comprise entre 1 et 25 caract�res
				tailleNom = 1 + randTaille.nextInt(27 - 1);
				
				// Nom al�atoire g�n�r�
				StringBuilder nomAleatoire = new StringBuilder();
				
				// Constitution du nom al�atoire en fonction de la taille tir�e
				for (int j = 0; j < tailleNom; j++) {
					
					// S�lection de caract�res al�atoires
					int k = 0 + randCaractere.nextInt(caracteres.length());
					
					// Construction du nouveau nom du fichier
					nomAleatoire.append(caracteres.charAt(k));
						
				}
			
				// Ajout du point d�finissant la nouvelle extension du fichier
				nomAleatoire.append(".");
				
				// Extension al�atoire ajout�
				// G�n�rer une nouvelle extension tant que celle-ci est la m�me que l'extension
				// de base du fichier � renommer
				do  {
						
					numExtensions =  0 + randTaille.nextInt(extensions.length);
						
				}while(extensionDeBase.equals(extensions[numExtensions]));
					
					// Insertion de l'extension choisie au nom al�atoire g�n�r�
					nomAleatoire.append(extensions[numExtensions]);
					
					// Si l'extension choisie est ".exe"
					if (extensions[numExtensions] == "exe") {
						
						// Ajout al�atoirement d'une seconde extension
						int l = randTypeExe.nextInt(3);
						
						switch (l) 
						{
							case 0 : nomAleatoire.append(".old"); break;
							case 1 : nomAleatoire.append(".config"); break;
						}
							
					}
					
					// Si l'extension choisie est ".rar" ou ".tar"
					if (extensions[numExtensions] == "rar" || extensions[numExtensions] == "tar") {
						
						// Ajout al�atoirement d'une seconde extension
						int m = randTypeExe.nextInt(2);
						
						switch (m) 
						{
							case 0 : nomAleatoire.append(".part"); break;
						}
						
					}
					
					// Insertion du nom al�atoire dans le Vector
					nomsAleatoiresGeneres.add(nomAleatoire.toString());	
		}
		
		return nomsAleatoiresGeneres;
	}


	// M�thode qui va renommer le ou les fichiers s�lectionn�s avec les noms al�atoires
	// g�n�r�s
	public Vector<String> renommerFichier() {
		
		String osDetecte;
		String osAdaptation;
		Vector<String> cheminFichiersRenommes = new Vector<String>(fichiersARenommer.length);
		
		// D�tection du syst�me d'exploitation o� est ex�cut� l'application
		osDetecte = System.getProperty("os.name").toLowerCase();
		
		// Si c'est un syst�me d'exploitation de type "Windows"
		if (osDetecte.indexOf("win") >= 0) {
			
			// Adaptation du chemin du fichier renomm�
			osAdaptation = "\\";
		}
		// Sinon adaptation pour les autres syst�mes (Linux notamment)
		else {
			
			osAdaptation = "//";
		}
		
		
		
		for (int i = 0; i < fichiersARenommer.length; i++) {
			
			System.out.println("Nom de base : " + fichiersARenommer[i]);
			
			// Renommage du fichier
			boolean etat = fichiersARenommer[i].renameTo(new File(fichiersARenommer[i].getParentFile() + osAdaptation + nomsAleatoiresGeneres.elementAt(i)));
				
			// Si le renommage n'a pas pu se faire correctement
			if (etat == false) {
				
				// Affichage d'un message d'erreur
				boiteErreur.showMessageDialog(null, "Une erreur est survenue lors du renommage du fichier " + fichiersARenommer[i] + ".", "Erreur", JOptionPane.ERROR_MESSAGE);
				
				// Signalement au niveau de la grille d'affichage
				nomsAleatoiresGeneres.insertElementAt("Erreur lors du renommage", i);
				
			}
			else {
				
				cheminFichiersRenommes.add(fichiersARenommer[i].getParentFile()+ osAdaptation + nomsAleatoiresGeneres.elementAt(i));
				System.out.println("renomm� : " + fichiersARenommer[i].getParentFile()+ osAdaptation + nomsAleatoiresGeneres.elementAt(i));
			}
			
		}
		
		return cheminFichiersRenommes;
	}
}
