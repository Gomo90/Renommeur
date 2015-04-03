import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;
import java.beans.EventHandler;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

/* Classe définissant l'interface graphique de l'application */
public class IHM extends JFrame implements ActionListener {

	
	private static final long serialVersionUID = 1L;
	private JPanel conteneur;
	private JMenuBar barredemenu;
	private JMenu fichier, decouper;
	private JMenuItem chargerFichiers, decouperFichiers, quitter, videGrille;
	private JButton renommer;
	private boolean premierChargement = true;
	private boolean decoupageFichier;
	JFileChooser selectionfichier;
	Vector<String> fichiersCharges = new Vector<String>();
	Vector<String> cheminFichiersRenommes = new Vector<String>();
	JTable tableauAffichage;
	GrilleAffichageModel model;
	String libelleColonnes [] = {"Fichiers chargés", "Fichiers renommés"};
	File[] fichiers;
	
	Renommage renommageFichier;
	Decoupage decoupageFichiers;
	
	public static JProgressBar barre;
	
	public IHM(String titre) {
		
		// Création de la barre de menu
		barredemenu = new JMenuBar();
		
		// Création de l'onglet "Fichier"
		fichier = new JMenu("Fichier");
		fichier.setMnemonic('F');
		chargerFichiers = new JMenuItem("Charger des fichiers");
		chargerFichiers.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK));
		quitter = new JMenuItem("Quitter");
		quitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK));
		videGrille = new JMenuItem("Vider la grille");
		videGrille.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_MASK));
		videGrille.setEnabled(false);
		
		fichier.add(chargerFichiers);
		fichier.add(videGrille);
		fichier.add(quitter);
		
		// Création de l'onglet "Découper"
		decouper = new JMenu("Découper");
		decouper.setMnemonic('D');
		decouperFichiers = new JMenuItem("Découper les fichiers");
		decouperFichiers.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_MASK));
		decouperFichiers.setEnabled(false);
		
		decouper.add(decouperFichiers);
		
		// Création du bouton "Renommer"
		renommer = new JButton("Renommer");
		renommer.setVisible(false);
		
		// Création de la barre de progression
		barre = new JProgressBar();
					
		// Affichage du pourcentage effectué
		barre.setStringPainted(true);
		
		// Intégration des onglets dans la barre de menu
		barredemenu.add(fichier);
		barredemenu.add(decouper);
		
		// Placement de la barre de menu sur le conteneur de l'interface
		conteneur = new JPanel();
		conteneur.setLayout(new BorderLayout()) ;
		conteneur.add(barredemenu, BorderLayout.NORTH);
		conteneur.add(renommer, BorderLayout.SOUTH);
		
		
		// Affectation d'écouteurs sur les différents éléments graphiques
		chargerFichiers.addActionListener(this);
		videGrille.addActionListener(this);
		quitter.addActionListener(this);
		renommer.addActionListener(this);
		
		// Utilisation d'un "EventHandler" pour gérer les actions utilisateurs 
		// (ici le decoupage de fichiers)
		decouperFichiers.addActionListener(EventHandler.create(ActionListener.class, this, "decoupageFichiers"));
		
		
		// Affichage de la fenêtre
		this.setTitle(titre);
		this.setContentPane(conteneur);
		this.pack();
		this.setSize(new Dimension(300,100));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		// Utilisation d'un "EventHandler" pour gérer les actions utilisateurs (ici la fermeture)
		this.addWindowListener(EventHandler.create(WindowListener.class, this, "fermetureFenetre"));
		
	}

	// Méthode appelée pour fermer l'application
	public void fermetureFenetre () {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	/* Méthode de gestion des interactions de l'utilisateur */
	public void actionPerformed(ActionEvent e) {

		
		// Traitements pour l'option "Quitter"
		if (e.getSource() == quitter) {
			
			if (fichiers != null) {
				
				// Affichage d'une boite de confirmation
				JOptionPane boitequit = new JOptionPane();
			
				int option = boitequit.showConfirmDialog(null, "Des fichiers ont été chargés voulez-vous vraiment quitter ?", "Confirmation", JOptionPane.YES_NO_OPTION);
				
				// Si l'utilisateur confirme son souhait de fermer l'application
				if(option == JOptionPane.YES_OPTION) {
					
					// Fermeture de l'application
					System.exit(0);
				}
			}
			else {
				
				// Fermeture de l'application
				System.exit(0);
			}
		}
	
		
		
		// Traitements pour l'option "Vider la grille"
		if (e.getSource() == videGrille) {
			
			// Appel de la méthode de réinitialisation de la grille d'affichage
			clearGrille();
			
			// Désactivation de l'option
			videGrille.setEnabled(false);
			
			// Désactivation de l'option pour découper les fichiers chargés
			decouperFichiers.setEnabled(false);
		}
		
		
		// Traitements pour l'option "Charger des fichiers"
		if (e.getSource() == chargerFichiers) {
			
			// Création du sélecteur de fichiers
			selectionfichier = new JFileChooser(); 
			
			// Sélection de plusieurs fichiers en même temps
			selectionfichier.setMultiSelectionEnabled(true);
			
			int retour = selectionfichier.showOpenDialog(this);
	    	
			// Si un fichier a été choisi
	    	if(retour == JFileChooser.APPROVE_OPTION){ 
	    		
	    		// Apparition du bouton "Renommer"
	    		renommer.setVisible(true);
	    		decouper.setVisible(true);
	    		
	    		// Si ce n'est pas le premier chargement de fichiers
	    		if (!premierChargement) {
	    			
	    			// Appel de la fonction de réinitialisation de la grille
	    			clearGrille();
	    			
	    			// Activation du bouton "Renommer"
	    			renommer.setEnabled(true);
	    		}
	    		
	    		
	    		// Récupération du nom des fichiers sélectionnés
	    		fichiers = selectionfichier.getSelectedFiles();   
	    		
	    		for (int i = 0; i < fichiers.length; i++) {
	    			
	    			// Stockage du chemin du ou des fichiers chargés dans un Vector
	    			fichiersCharges.add(fichiers[i].getAbsolutePath());
	    		}
	    		
	    	}
	    	else {
	    		
	    		return;
	    	}
	    	
	    	// Si c'est un premier chargement
	    	if (premierChargement) {
	    	
	    		// Appel de la méthode de création de la grille d'affichage des fichiers chargés
	    		chargementGrille();
	    	}
	    	else {
	    		
	    		// Appel de la méthode de mise à jour de la grille d'affichage
	    		majGrille();
	    			
	    		// Activation de l'option pour vider la grille d'affichage
	    		videGrille.setEnabled(true);	
	    	}
		}
		
		// Si un click a été réalisé sur le bouton "Renommer"
		if (e.getSource() == renommer) {
			
			// Instance de la classe "Renommage"
			renommageFichier = new Renommage(fichiers);
			
			// Appel de la fonction de génération d'un nom aléatoire
			renommageFichier.nomAleatoire();
			
			// Appel de la fonction de renommage d'un fichier
			cheminFichiersRenommes = renommageFichier.renommerFichier();
			
			// Désactivation du bouton "Renommer"
			renommer.setEnabled(false);
			
			// Activation de l'option pour découper les fichiers chargés
			decouperFichiers.setEnabled(true);
			
			// Mise à jour de l'affichage
	    	conteneur.repaint();
		}
	}

	
	/* Méthode pour vider la grille d'affichage des fichiers chargés */
	private void clearGrille() {
		
		// Réinitialisation du tableau des fichiers sélectionnés
		fichiers = null;
		
		// Vidage du vector de stockage des fichiers chargés
		fichiersCharges.clear();
		
		// Si des nouveaux noms de fichiers ont été générés
		if (Renommage.nomsAleatoiresGeneres != null) {
			
			// Vidage du vector de stockage des nouveaux noms de fichiers générés
			Renommage.nomsAleatoiresGeneres.clear();
		}
		
		// Création d'un model vierge de la grille d'affichage des fichiers à renommer
    	model = new GrilleAffichageModel(fichiersCharges, libelleColonnes, fichiersCharges.size(), 2);
    	
    	// Insertion du model dans l'interface
    	tableauAffichage.setModel(model);
    	
    	// Définition d'une taille par défaut des colonnes
		tableauAffichage.getColumnModel().getColumn(0).setPreferredWidth(400);
		tableauAffichage.getColumnModel().getColumn(1).setPreferredWidth(200);
		
		// Remplacement de la barre de progression par le bouton "Renommer" 
		conteneur.remove(barre);
		conteneur.add(renommer, BorderLayout.SOUTH);
    
		// Mise à jour de l'affichage
    	conteneur.repaint();
    	
    	// Réajustement de la fenêtre
    	this.pack();
	}
	
	/* Méthode pour créer la grille d'affichage des fichiers chargés */
	private void chargementGrille() {
		
		// Création du tableau d'affichage
    	tableauAffichage = new JTable();
    
    	// Création du model de la grille d'affichage des fichiers à renommer
    	model = new GrilleAffichageModel(fichiersCharges, libelleColonnes, fichiersCharges.size(), 2);
    	
    	// Insertion du model dans l'interface
    	tableauAffichage.setModel(model);
    	
    	// Création d'ascenceurs pour parcourir la grille
		JScrollPane ascenceurs = new JScrollPane(tableauAffichage);
		
		// Insertion des ascenceurs dans l'interface
		conteneur.add(ascenceurs);
		
		// Annulation de l'auto-resize de la JTable
		tableauAffichage.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		// Définition d'une taille par défaut des colonnes
		tableauAffichage.getColumnModel().getColumn(0).setPreferredWidth(400);
		tableauAffichage.getColumnModel().getColumn(1).setPreferredWidth(200);
		
		// Si une fragmentation de fichiers a été lancée précédemment
		if (decoupageFichier) {
			
			// Remplacement de la barre de progression par le bouton "Renommer" 
			conteneur.remove(barre);
			conteneur.add(renommer, BorderLayout.SOUTH);
			
			decoupageFichier = false;
		}
		
		
		// Mise à jour de l'affichage
		conteneur.repaint();
		
		premierChargement = false;
		
		// Activation de l'option pour vider la grille d'affichage
		videGrille.setEnabled(true);
		
		this.pack();
	}
	
	/* Méthode pour mettre à jour la grille d'affichage des fichiers chargés */
	private void majGrille() {
		
		// Création du model de la grille d'affichage des fichiers à renommer
    	model = new GrilleAffichageModel(fichiersCharges, libelleColonnes, fichiersCharges.size(), 2);
    	
    	// Insertion du model dans l'interface
    	tableauAffichage.setModel(model);
    	
    	// Définition d'une taille par défaut des colonnes
		tableauAffichage.getColumnModel().getColumn(0).setPreferredWidth(400);
		tableauAffichage.getColumnModel().getColumn(1).setPreferredWidth(200);
    	
		// Mise à jour de l'affichage
    	conteneur.repaint();
	}
	
	
	// Méthode permettant de lancer l'exécution de la fragmentation de fichiers
	public void decoupageFichiers () {
		
		// Box de saisie du nombre de fragments souaités
		new IHMNbFragments(this, "Fragmentation de fichiers", true);
		
		if (IHMNbFragments.nbFragmentsSaisis != 0) {
			
			// Remplacement du bouton "Renommer" par la barre de progression
			conteneur.remove(renommer);
			
			// Placement de la barre de progression
			conteneur.add(barre, BorderLayout.SOUTH);
			
			// Mise à jour de l'affichage
	    	conteneur.repaint();
	    	
	    	// Réajustement de la fenêtre 
	    	this.pack();
	    	
	    
			// Pour signaler qu'une fragmentation de fichiers a été lancée
	    	decoupageFichier = true;
			
	    	// Lancement de la fragmentation dans un SwingWorker
			decoupageFichiers = new Decoupage(cheminFichiersRenommes, IHMNbFragments.nbFragmentsSaisis);
			decoupageFichiers.execute();
			
		}
		
	}
}
