import java.awt.Component;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

/* Classe définissant le modèle appliqué à la grille d'affichage */
public class GrilleAffichageModel extends AbstractTableModel implements TableCellEditor {

	
	private static final long serialVersionUID = 1L;
	Vector<String> fichierscharges;
	String[] titreColonnes;
	int nbLignes;
	int nbColonnes;
	
	
	/* Constructeur avec le nombre de lignes, de colonnes et les données à afficher
	   qui va composer la grille */
	public GrilleAffichageModel (Vector<String> fichiers, String [] titresCol, int ligne, int colonne) {
		
		this.fichierscharges = fichiers;
		this.titreColonnes = titresCol;
		this.nbLignes = ligne;
		this.nbColonnes = colonne;
		
	}
	
	
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return nbColonnes;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return nbLignes;
	}

	@Override
	public Object getValueAt(int ligne, int colonne) {
		// TODO Auto-generated method stub
		
		// Affichage des fichiers chargés (1ère colonne)
		if (colonne == 0 && !fichierscharges.isEmpty()) {
			
			return fichierscharges.elementAt(ligne);
		}
		// Affichage des fichiers renommés
		else if (Renommage.nomsAleatoiresGeneres != null && !Renommage.nomsAleatoiresGeneres.isEmpty() && 
				!Renommage.nomsAleatoiresGeneres.elementAt(ligne).equals("") && colonne == 1) {
			
			return Renommage.nomsAleatoiresGeneres.elementAt(ligne);
		}
		else {
			
			return null;
		}
		
		
	}

	/* Libellé des colonnes de la grille d'affichage */
	public String getColumnName(int col){

		   return titreColonnes[col];
	}
	

	@Override
	public void addCellEditorListener(CellEditorListener arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void cancelCellEditing() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public boolean isCellEditable(EventObject arg0) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void removeCellEditorListener(CellEditorListener arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean shouldSelectCell(EventObject arg0) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean stopCellEditing() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		// TODO Auto-generated method stub
		return null;
	}	
	
}
