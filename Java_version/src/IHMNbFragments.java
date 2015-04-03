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
		
		// Dimension de la fen�tre
		this.setSize(new Dimension(350,150));
		
		// Fen�tre au centre de l'�cran
		this.setLocationRelativeTo(null);
		
		// Montre toujours la fen�tre de saisie au 1er plan
		this.setAlwaysOnTop(true);   
		
		// Fen�tre non redimensionnable
		this.setResizable(false);
		
		// Interface de saisie du nombre de fragments souhait�
		donneesFragments = new JPanel();
		
		// Contr�le de saisie num�rique uniquement
		numFragmentsFormat = NumberFormat.getNumberInstance();
		
		// R�cup�ration uniquement des valeurs num�riques pr�sentes dans la saisie
		numFragmentsFormat.setParseIntegerOnly (true);
		
		// Champs de saisie
		lNbFragments = new JLabel("Nombre de fragments :");
		nbFragments = new JFormattedTextField(numFragmentsFormat);
		nbFragments.setPreferredSize(new Dimension (75, 25));
		
		// Boutons
		jpBoutons = new JPanel();
		decouper = new JButton("D�couper");
		annule = new JButton("Annuler");
		
		// Utilisation d'"EventHandler" pour g�rer les actions utilisateurs 
		decouper.addActionListener(EventHandler.create(ActionListener.class, this, "decoupageFichiers"));
		annule.addActionListener(EventHandler.create(ActionListener.class, this, "fermetureFenetre"));
		
		// Ajout des boutons au JPanel cr�� sp�cialement pour
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
		
		// Affichage de la fen�tre
		this.setVisible(true);
	}
	
	
	
	
	// M�thode permettant de fermer la box de saisie
	public void fermetureFenetre () {
		
		// Initialisation des fragments saisies pour ne pas garder la pr�c�dente valeur
		// en cas de fragmentation d�j� lanc�e auparavant
		nbFragmentsSaisis = 0;
		
		// Fermeture de la fen�tre de saisie
		this.dispose();
	}
	
	
	// M�thode permettant de r�cup�rer le nombre de fragments saisies
	public void decoupageFichiers () {
		
		// Si rien n'a �t� saisie
		if (nbFragments.getValue() == null) {
			
			// Affichage d'un message d'erreur
			JOptionPane.showMessageDialog(this, "Certaines informations saisies sont incorrectes.", "Erreur de saisie",JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		
		// R�cup�ration du nombre de fragments saisis
		nbFragmentsSaisis = Integer.parseInt(nbFragments.getValue().toString());
		
		// Fermeture de la fen�tre de saisie
		this.dispose();
	}
	
}
