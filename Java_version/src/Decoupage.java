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


/* Classe assurant le processus de fragmentation des fichiers sélectionnés à l'aide d'un SwingWorker
 * pour une exécution en tâche de fond
 */
public class Decoupage extends SwingWorker<Void, Void> {

	
	Vector<String> cheminFichiersADecouper;
	int nbFragmentsDecoupe;
	
	
	// Constructeur de la classe avec en paramètre le chemin des fichiers à découper
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
					
					
					// Mise à jour de la valeur de la barre de progression
					IHM.barre.setValue((Integer) pce.getNewValue());
				}
			}
		});
		
	}
	
	
	// Méthode qui se charge de fragmenter les fichiers
	public void fragmentation () {
		
		 /* la taille du buffer */
		final int buffer = 1048576; 
		
		String cheminDestinationFragment, nomFragment;
		long tailleFichier, tailleFragment, bytesLus;
		File fragmentCree;
		double valBarreProgression;
		int valStartProgress = 0;
		int numFragment;
	
		
		// Calcul de la valeur de progression de la progressBar en fonction du nombre de fragments a créé
		valBarreProgression = (100 - valStartProgress) / (nbFragmentsDecoupe * cheminFichiersADecouper.size());
		
		// Parcours des fichiers à fragmenter
		for (int i = 0; i < cheminFichiersADecouper.size(); i++) {
			
			try {
				
				// Numéro du fragment (initialiser à chaque fichier découpé)
				numFragment = 0;
				
				// Ouverture d'un flux de données en "Lecture" sur le fichier à fragmenter
				FileInputStream fis = new FileInputStream(cheminFichiersADecouper.elementAt(i));
				
				// Récupération de la taille du fichier à fragmenter
				tailleFichier = fis.available();
				
				// Taille des fragments à créer selon la saisie utilisateur
				tailleFragment = tailleFichier/nbFragmentsDecoupe;
				
				// Récupération de l'emplacement de création du fragment (même que le fichier source)
				cheminDestinationFragment = getDestinationFragment(i);
				
				// Parcours du nombre de fragments à créer
				for (int j = 0; j < nbFragmentsDecoupe; j++) {
					
					// Numéro du fragment
					numFragment++;
					
					// Nom du fragment
					nomFragment = getNomFragment(i, numFragment);
					
					// Valeur de progression de la progressBar
					valStartProgress += valBarreProgression;
					
					// Création du fragment généré
					fragmentCree = new File(cheminDestinationFragment, nomFragment);
					
					// Ouverture d'un flux de données en "Ecriture" sur le fragment généré
					FileOutputStream fos = new FileOutputStream(fragmentCree);
					
					// Compteur des données lues
					bytesLus = 0;
					
					byte[] bytesTab;
					
					// Tant que la taille du fragment dépasse celle du buffer
					while ((tailleFragment - bytesLus) > buffer) {	
						
						// Fragmentation du fichier source
						bytesTab = new byte[buffer];
						fis.read(bytesTab);
						fos.write(bytesTab);
						
						// Mise à jour du compteur
						bytesLus += buffer;
					}
					
					// Ecriture des données restantes
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
	
	
	// Méthode pour récupérer le chemin de destination des fragments générés
	private String getDestinationFragment (int numFichier) {
		
		String cheminDestination;
		String osDetecte;
		String osAdaptation;
		
		// Détection du système d'exploitation où est exécuté l'application
		osDetecte = System.getProperty("os.name").toLowerCase();
		
		// Si c'est un système d'exploitation de type "Windows"
		if (osDetecte.indexOf("win") >= 0) {
					
			// Adaptation du chemin du fichier renommé
			osAdaptation = "\\";
		}
		// Sinon adaptation pour les autres systèmes (Linux notamment)
		else {
					
			osAdaptation = "//";
		}
		
		// Extraction du chemin de destination des fragments du fichier découpé à partir
		// du chemin complet de celui-ci
		cheminDestination = cheminFichiersADecouper.elementAt(numFichier).substring(0, 
				cheminFichiersADecouper.elementAt(numFichier).lastIndexOf(osAdaptation)+1);
		
		return cheminDestination;
		
	}
	
	
	// Méthode pour nommer les fragments générés du fichier
	private String getNomFragment (int numFichier, int numFragment) {
		
		StringBuilder nomFragment = new StringBuilder();
		String osDetecte;
		String osAdaptation;
		
		
		// Détection du système d'exploitation où est exécuté l'application
		osDetecte = System.getProperty("os.name").toLowerCase();
				
		// Si c'est un système d'exploitation de type "Windows"
		if (osDetecte.indexOf("win") >= 0) {
							
			// Adaptation du chemin du fichier renommé
			osAdaptation = "\\";
		}
		// Sinon adaptation pour les autres systèmes (Linux notamment)
		else {
							
			osAdaptation = "//";
		}
		
		
		// Extraction du nom du fichier découpé servant de base aux fragments générés
		nomFragment.append(cheminFichiersADecouper.elementAt(numFichier).substring(cheminFichiersADecouper.elementAt(numFichier).lastIndexOf(osAdaptation)+1,
				cheminFichiersADecouper.elementAt(numFichier).length()));
		
		// Insertion du numéro aléatoire dans le nom du fragment
		nomFragment.insert(nomFragment.lastIndexOf("."), numFragment);
		
		
		return nomFragment.toString();
	}

	// Méthode exécuter en tâche de fond par le SwingWorker
	@Override
	protected Void doInBackground() throws Exception {
		
		// Initialisation de la progressBar
		setProgress(0);
		
		// Appel de la méthode de fragmentation des fichiers
		fragmentation();	
		
		return null;
	}
	
	
	// Méthode exécutée automatiquement après doInBackground()
	public void done() {
	     
		// Remonté d'erreurs possibles
		try { 

		    get(); 

		  } catch (InterruptedException e) { 

		    System.out.println("InterruptedException :" + e.getMessage()); 

		  } catch (ExecutionException e) { 

		    System.out.println("ExecutionException : " + e.getCause().getMessage()); 

		  } 
		
		// Remplissage de la progressBar au maximum
		setProgress(100);
		
		// Message de confirmation affiché dans la progressBar
		IHM.barre.setString("Fragmentation terminée");
	}
}
