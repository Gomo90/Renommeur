import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/* Box de saisie du nombre de fragments voulus */
public class IHMNbFragments extends JDialog {

	
	private static final long serialVersionUID = 1L;
	JLabel lNbFragments;
	JPanel donneesFragments, jpBoutons;
	JFormattedTextField nbFragments;
	NumberFormat numFragmentsFormat;
	JButton decouper, annule;
	public static int nbFragmentsSaisis;
	
	
	public IHMNbFragments (JFrame parent, String titre, boolean modal) {
		
		super (parent, titre, modal);
		
		// Dimension de la fenêtre
		this.setSize(new Dimension(350,150));
		
		// Fenêtre au centre de l'écran
		this.setLocationRelativeTo(null);
		
		// Montre toujours la fenêtre de saisie au 1er plan
		this.setAlwaysOnTop(true);   
		
		// Fenêtre non redimensionnable
		this.setResizable(false);
		
		// Interface de saisie du nombre de fragments souhaité
		donneesFragments = new JPanel();
		
		// Contrôle de saisie numérique uniquement
		numFragmentsFormat = NumberFormat.getNumberInstance();
		
		// Récupération uniquement des valeurs numériques présentes dans la saisie
		numFragmentsFormat.setParseIntegerOnly (true);
		
		// Champs de saisie
		lNbFragments = new JLabel("Nombre de fragments :");
		nbFragments = new JFormattedTextField(numFragmentsFormat);
		nbFragments.setPreferredSize(new Dimension (75, 25));
		
		// Boutons
		jpBoutons = new JPanel();
		decouper = new JButton("Découper");
		annule = new JButton("Annuler");
		
		// Utilisation d'"EventHandler" pour gérer les actions utilisateurs 
		decouper.addActionListener(EventHandler.create(ActionListener.class, this, "decoupageFichiers"));
		annule.addActionListener(EventHandler.create(ActionListener.class, this, "fermetureFenetre"));
		
		// Ajout des boutons au JPanel créé spécialement pour
		jpBoutons.add(decouper);
		jpBoutons.add(annule);
		
		
		// Initialisation d'une GridBagLayout pour contenir les champs de saisie
		donneesFragments.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Disposition du champs de saisie du nombre de fragments
		gbc.gridx = gbc.gridy = 0;
		gbc.gridheight = gbc.gridwidth = 1;
		gbc.insets = new Insets(0, -30, 0, 0);
		donneesFragments.add(lNbFragments, gbc);
		
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, -50);
        donneesFragments.add(nbFragments, gbc);
        
       
		this.add(donneesFragments);
		this.add(jpBoutons, BorderLayout.SOUTH);
		
		// Affichage de la fenêtre
		this.setVisible(true);
	}
	
	
	
	
	// Méthode permettant de fermer la box de saisie
	public void fermetureFenetre () {
		
		// Initialisation des fragments saisies pour ne pas garder la précédente valeur
		// en cas de fragmentation déjà lancée auparavant
		nbFragmentsSaisis = 0;
		
		// Fermeture de la fenêtre de saisie
		this.dispose();
	}
	
	
	// Méthode permettant de récupérer le nombre de fragments saisies
	public void decoupageFichiers () {
		
		// Si rien n'a été saisie
		if (nbFragments.getValue() == null) {
			
			// Affichage d'un message d'erreur
			JOptionPane.showMessageDialog(this, "Certaines informations saisies sont incorrectes.", "Erreur de saisie",JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		
		// Récupération du nombre de fragments saisis
		nbFragmentsSaisis = Integer.parseInt(nbFragments.getValue().toString());
		
		// Fermeture de la fenêtre de saisie
		this.dispose();
	}
	
}
