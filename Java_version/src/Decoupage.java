import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;


/* Classe assurant le processus de fragmentation des fichiers s�lectionn�s � l'aide d'un SwingWorker
 * pour une ex�cution en t�che de fond
 */
public class Decoupage extends SwingWorker<Void, Void> {

	
	Vector<String> cheminFichiersADecouper;
	int nbFragmentsDecoupe;
	
	
	// Constructeur de la classe avec en param�tre le chemin des fichiers � d�couper
	// sous forme d'unh Vector de string
	public Decoupage (Vector<String> fichiersADecouper, int nbFragments) {
			
		this.cheminFichiersADecouper = fichiersADecouper;
		this.nbFragmentsDecoupe = nbFragments;
		
		// Ecouteur sur la barre de progression
		addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				//System.out.println("proper val : " + pce.getPropertyName());
				if ("progress".equals(pce.getPropertyName())) {
					
					
					// Mise � jour de la valeur de la barre de progression
					IHM.barre.setValue((Integer) pce.getNewValue());
				}
			}
		});
		
	}
	
	
	// M�thode qui se charge de fragmenter les fichiers
	public void fragmentation () {
		
		 /* la taille du buffer */
		final int buffer = 1048576; 
		
		String cheminDestinationFragment, nomFragment;
		long tailleFichier, tailleFragment, bytesLus;
		File fragmentCree;
		double valBarreProgression;
		int valStartProgress = 0;
		int numFragment;
	
		
		// Calcul de la valeur de progression de la progressBar en fonction du nombre de fragments a cr��
		valBarreProgression = (100 - valStartProgress) / (nbFragmentsDecoupe * cheminFichiersADecouper.size());
		
		// Parcours des fichiers � fragmenter
		for (int i = 0; i < cheminFichiersADecouper.size(); i++) {
			
			try {
				
				// Num�ro du fragment (initialiser � chaque fichier d�coup�)
				numFragment = 0;
				
				// Ouverture d'un flux de donn�es en "Lecture" sur le fichier � fragmenter
				FileInputStream fis = new FileInputStream(cheminFichiersADecouper.elementAt(i));
				
				// R�cup�ration de la taille du fichier � fragmenter
				tailleFichier = fis.available();
				
				// Taille des fragments � cr�er selon la saisie utilisateur
				tailleFragment = tailleFichier/nbFragmentsDecoupe;
				
				// R�cup�ration de l'emplacement de cr�ation du fragment (m�me que le fichier source)
				cheminDestinationFragment = getDestinationFragment(i);
				
				// Parcours du nombre de fragments � cr�er
				for (int j = 0; j < nbFragmentsDecoupe; j++) {
					
					// Num�ro du fragment
					numFragment++;
					
					// Nom du fragment
					nomFragment = getNomFragment(i, numFragment);
					
					// Valeur de progression de la progressBar
					valStartProgress += valBarreProgression;
					
					// Cr�ation du fragment g�n�r�
					fragmentCree = new File(cheminDestinationFragment, nomFragment);
					
					// Ouverture d'un flux de donn�es en "Ecriture" sur le fragment g�n�r�
					FileOutputStream fos = new FileOutputStream(fragmentCree);
					
					// Compteur des donn�es lues
					bytesLus = 0;
					
					byte[] bytesTab;
					
					// Tant que la taille du fragment d�passe celle du buffer
					while ((tailleFragment - bytesLus) > buffer) {	
						
						// Fragmentation du fichier source
						bytesTab = new byte[buffer];
						fis.read(bytesTab);
						fos.write(bytesTab);
						
						// Mise � jour du compteur
						bytesLus += buffer;
					}
					
					// Ecriture des donn�es restantes
					bytesTab = new byte[(int)(tailleFragment - bytesLus)];
					fis.read(bytesTab);
					fos.write(bytesTab);
					
					// MAJ de la barre de progression
					setProgress(valStartProgress);
					
					fos.close();
				}
				
				fis.close();
				
				// Suppression du fichier source
				File fichierSource = new File(cheminFichiersADecouper.elementAt(i));
				fichierSource.delete();
				
			
			} catch (FileNotFoundException e) {
				
				System.out.println("FileNotFoundException : " + e.getMessage());
			
			} catch (IOException e) {
				
				System.out.println("IOException : " + e.getMessage());
			}
		}
	}
	
	
	// M�thode pour r�cup�rer le chemin de destination des fragments g�n�r�s
	private String getDestinationFragment (int numFichier) {
		
		String cheminDestination;
		String osDetecte;
		String osAdaptation;
		
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
		
		// Extraction du chemin de destination des fragments du fichier d�coup� � partir
		// du chemin complet de celui-ci
		cheminDestination = cheminFichiersADecouper.elementAt(numFichier).substring(0, 
				cheminFichiersADecouper.elementAt(numFichier).lastIndexOf(osAdaptation)+1);
		
		return cheminDestination;
		
	}
	
	
	// M�thode pour nommer les fragments g�n�r�s du fichier
	private String getNomFragment (int numFichier, int numFragment) {
		
		StringBuilder nomFragment = new StringBuilder();
		String osDetecte;
		String osAdaptation;
		
		
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
		
		
		// Extraction du nom du fichier d�coup� servant de base aux fragments g�n�r�s
		nomFragment.append(cheminFichiersADecouper.elementAt(numFichier).substring(cheminFichiersADecouper.elementAt(numFichier).lastIndexOf(osAdaptation)+1,
				cheminFichiersADecouper.elementAt(numFichier).length()));
		
		// Insertion du num�ro al�atoire dans le nom du fragment
		nomFragment.insert(nomFragment.lastIndexOf("."), numFragment);
		
		
		return nomFragment.toString();
	}

	// M�thode ex�cuter en t�che de fond par le SwingWorker
	@Override
	protected Void doInBackground() throws Exception {
		
		// Initialisation de la progressBar
		setProgress(0);
		
		// Appel de la m�thode de fragmentation des fichiers
		fragmentation();	
		
		return null;
	}
	
	
	// M�thode ex�cut�e automatiquement apr�s doInBackground()
	public void done() {
	     
		// Remont� d'erreurs possibles
		try { 

		    get(); 

		  } catch (InterruptedException e) { 

		    System.out.println("InterruptedException :" + e.getMessage()); 

		  } catch (ExecutionException e) { 

		    System.out.println("ExecutionException : " + e.getCause().getMessage()); 

		  } 
		
		// Remplissage de la progressBar au maximum
		setProgress(100);
		
		// Message de confirmation affich� dans la progressBar
		IHM.barre.setString("Fragmentation termin�e");
	}
}
