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

/* Classe d�finissant l'interface graphique de l'application */
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
	String libelleColonnes [] = {"Fichiers charg�s", "Fichiers renomm�s"};
	File[] fichiers;
	
	Renommage renommageFichier;
	Decoupage decoupageFichiers;
	
	public static JProgressBar barre;
	
	public IHM(String titre) {
		
		// Cr�ation de la barre de menu
		barredemenu = new JMenuBar();
		
		// Cr�ation de l'onglet "Fichier"
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
		
		// Cr�ation de l'onglet "D�couper"
		decouper = new JMenu("D�couper");
		decouper.setMnemonic('D');
		decouperFichiers = new JMenuItem("D�couper les fichiers");
		decouperFichiers.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_MASK));
		decouperFichiers.setEnabled(false);
		
		decouper.add(decouperFichiers);
		
		// Cr�ation du bouton "Renommer"
		renommer = new JButton("Renommer");
		renommer.setVisible(false);
		
		// Cr�ation de la barre de progression
		barre = new JProgressBar();
					
		// Affichage du pourcentage effectu�
		barre.setStringPainted(true);
		
		// Int�gration des onglets dans la barre de menu
		barredemenu.add(fichier);
		barredemenu.add(decouper);
		
		// Placement de la barre de menu sur le conteneur de l'interface
		conteneur = new JPanel();
		conteneur.setLayout(new BorderLayout()) ;
		conteneur.add(barredemenu, BorderLayout.NORTH);
		conteneur.add(renommer, BorderLayout.SOUTH);
		
		
		// Affectation d'�couteurs sur les diff�rents �l�ments graphiques
		chargerFichiers.addActionListener(this);
		videGrille.addActionListener(this);
		quitter.addActionListener(this);
		renommer.addActionListener(this);
		
		// Utilisation d'un "EventHandler" pour g�rer les actions utilisateurs 
		// (ici le decoupage de fichiers)
		decouperFichiers.addActionListener(EventHandler.create(ActionListener.class, this, "decoupageFichiers"));
		
		
		// Affichage de la fen�tre
		this.setTitle(titre);
		this.setContentPane(conteneur);
		this.pack();
		this.setSize(new Dimension(300,100));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		// Utilisation d'un "EventHandler" pour g�rer les actions utilisateurs (ici la fermeture)
		this.addWindowListener(EventHandler.create(WindowListener.class, this, "fermetureFenetre"));
		
	}

	// M�thode appel�e pour fermer l'application
	public void fermetureFenetre () {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	/* M�thode de gestion des interactions de l'utilisateur */
	public void actionPerformed(ActionEvent e) {

		
		// Traitements pour l'option "Quitter"
		if (e.getSource() == quitter) {
			
			if (fichiers != null) {
				
				// Affichage d'une boite de confirmation
				JOptionPane boitequit = new JOptionPane();
			
				int option = boitequit.showConfirmDialog(null, "Des fichiers ont �t� charg�s voulez-vous vraiment quitter ?", "Confirmation", JOptionPane.YES_NO_OPTION);
				
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
			
			// Appel de la m�thode de r�initialisation de la grille d'affichage
			clearGrille();
			
			// D�sactivation de l'option
			videGrille.setEnabled(false);
			
			// D�sactivation de l'option pour d�couper les fichiers charg�s
			decouperFichiers.setEnabled(false);
		}
		
		
		// Traitements pour l'option "Charger des fichiers"
		if (e.getSource() == chargerFichiers) {
			
			// Cr�ation du s�lecteur de fichiers
			selectionfichier = new JFileChooser(); 
			
			// S�lection de plusieurs fichiers en m�me temps
			selectionfichier.setMultiSelectionEnabled(true);
			
			int retour = selectionfichier.showOpenDialog(this);
	    	
			// Si un fichier a �t� choisi
	    	if(retour == JFileChooser.APPROVE_OPTION){ 
	    		
	    		// Apparition du bouton "Renommer"
	    		renommer.setVisible(true);
	    		decouper.setVisible(true);
	    		
	    		// Si ce n'est pas le premier chargement de fichiers
	    		if (!premierChargement) {
	    			
	    			// Appel de la fonction de r�initialisation de la grille
	    			clearGrille();
	    			
	    			// Activation du bouton "Renommer"
	    			renommer.setEnabled(true);
	    		}
	    		
	    		
	    		// R�cup�ration du nom des fichiers s�lectionn�s
	    		fichiers = selectionfichier.getSelectedFiles();   
	    		
	    		for (int i = 0; i < fichiers.length; i++) {
	    			
	    			// Stockage du chemin du ou des fichiers charg�s dans un Vector
	    			fichiersCharges.add(fichiers[i].getAbsolutePath());
	    		}
	    		
	    	}
	    	else {
	    		
	    		return;
	    	}
	    	
	    	// Si c'est un premier chargement
	    	if (premierChargement) {
	    	
	    		// Appel de la m�thode de cr�ation de la grille d'affichage des fichiers charg�s
	    		chargementGrille();
	    	}
	    	else {
	    		
	    		// Appel de la m�thode de mise � jour de la grille d'affichage
	    		majGrille();
	    			
	    		// Activation de l'option pour vider la grille d'affichage
	    		videGrille.setEnabled(true);	
	    	}
		}
		
		// Si un click a �t� r�alis� sur le bouton "Renommer"
		if (e.getSource() == renommer) {
			
			// Instance de la classe "Renommage"
			renommageFichier = new Renommage(fichiers);
			
			// Appel de la fonction de g�n�ration d'un nom al�atoire
			renommageFichier.nomAleatoire();
			
			// Appel de la fonction de renommage d'un fichier
			cheminFichiersRenommes = renommageFichier.renommerFichier();
			
			// D�sactivation du bouton "Renommer"
			renommer.setEnabled(false);
			
			// Activation de l'option pour d�couper les fichiers charg�s
			decouperFichiers.setEnabled(true);
			
			// Mise � jour de l'affichage
	    	conteneur.repaint();
		}
	}

	
	/* M�thode pour vider la grille d'affichage des fichiers charg�s */
	private void clearGrille() {
		
		// R�initialisation du tableau des fichiers s�lectionn�s
		fichiers = null;
		
		// Vidage du vector de stockage des fichiers charg�s
		fichiersCharges.clear();
		
		// Si des nouveaux noms de fichiers ont �t� g�n�r�s
		if (Renommage.nomsAleatoiresGeneres != null) {
			
			// Vidage du vector de stockage des nouveaux noms de fichiers g�n�r�s
			Renommage.nomsAleatoiresGeneres.clear();
		}
		
		// Cr�ation d'un model vierge de la grille d'affichage des fichiers � renommer
    	model = new GrilleAffichageModel(fichiersCharges, libelleColonnes, fichiersCharges.size(), 2);
    	
    	// Insertion du model dans l'interface
    	tableauAffichage.setModel(model);
    	
    	// D�finition d'une taille par d�faut des colonnes
		tableauAffichage.getColumnModel().getColumn(0).setPreferredWidth(400);
		tableauAffichage.getColumnModel().getColumn(1).setPreferredWidth(200);
		
		// Remplacement de la barre de progression par le bouton "Renommer" 
		conteneur.remove(barre);
		conteneur.add(renommer, BorderLayout.SOUTH);
    
		// Mise � jour de l'affichage
    	conteneur.repaint();
    	
    	// R�ajustement de la fen�tre
    	this.pack();
	}
	
	/* M�thode pour cr�er la grille d'affichage des fichiers charg�s */
	private void chargementGrille() {
		
		// Cr�ation du tableau d'affichage
    	tableauAffichage = new JTable();
    
    	// Cr�ation du model de la grille d'affichage des fichiers � renommer
    	model = new GrilleAffichageModel(fichiersCharges, libelleColonnes, fichiersCharges.size(), 2);
    	
    	// Insertion du model dans l'interface
    	tableauAffichage.setModel(model);
    	
    	// Cr�ation d'ascenceurs pour parcourir la grille
		JScrollPane ascenceurs = new JScrollPane(tableauAffichage);
		
		// Insertion des ascenceurs dans l'interface
		conteneur.add(ascenceurs);
		
		// Annulation de l'auto-resize de la JTable
		tableauAffichage.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		// D�finition d'une taille par d�faut des colonnes
		tableauAffichage.getColumnModel().getColumn(0).setPreferredWidth(400);
		tableauAffichage.getColumnModel().getColumn(1).setPreferredWidth(200);
		
		// Si une fragmentation de fichiers a �t� lanc�e pr�c�demment
		if (decoupageFichier) {
			
			// Remplacement de la barre de progression par le bouton "Renommer" 
			conteneur.remove(barre);
			conteneur.add(renommer, BorderLayout.SOUTH);
			
			decoupageFichier = false;
		}
		
		
		// Mise � jour de l'affichage
		conteneur.repaint();
		
		premierChargement = false;
		
		// Activation de l'option pour vider la grille d'affichage
		videGrille.setEnabled(true);
		
		this.pack();
	}
	
	/* M�thode pour mettre � jour la grille d'affichage des fichiers charg�s */
	private void majGrille() {
		
		// Cr�ation du model de la grille d'affichage des fichiers � renommer
    	model = new GrilleAffichageModel(fichiersCharges, libelleColonnes, fichiersCharges.size(), 2);
    	
    	// Insertion du model dans l'interface
    	tableauAffichage.setModel(model);
    	
    	// D�finition d'une taille par d�faut des colonnes
		tableauAffichage.getColumnModel().getColumn(0).setPreferredWidth(400);
		tableauAffichage.getColumnModel().getColumn(1).setPreferredWidth(200);
    	
		// Mise � jour de l'affichage
    	conteneur.repaint();
	}
	
	
	// M�thode permettant de lancer l'ex�cution de la fragmentation de fichiers
	public void decoupageFichiers () {
		
		// Box de saisie du nombre de fragments souait�s
		new IHMNbFragments(this, "Fragmentation de fichiers", true);
		
		if (IHMNbFragments.nbFragmentsSaisis != 0) {
			
			// Remplacement du bouton "Renommer" par la barre de progression
			conteneur.remove(renommer);
			
			// Placement de la barre de progression
			conteneur.add(barre, BorderLayout.SOUTH);
			
			// Mise � jour de l'affichage
	    	conteneur.repaint();
	    	
	    	// R�ajustement de la fen�tre 
	    	this.pack();
	    	
	    
			// Pour signaler qu'une fragmentation de fichiers a �t� lanc�e
	    	decoupageFichier = true;
			
	    	// Lancement de la fragmentation dans un SwingWorker
			decoupageFichiers = new Decoupage(cheminFichiersRenommes, IHMNbFragments.nbFragmentsSaisis);
			decoupageFichiers.execute();
			
		}
		
	}
}
