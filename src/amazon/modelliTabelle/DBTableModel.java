/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon.modelliTabelle;

import amazon.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Francesco
 */
public class DBTableModel extends AbstractTableModel {
    private ResultSet rs; //resultset su cui si basa il modello
    
    public DBTableModel() {
        super();
    }
    
    public DBTableModel(ResultSet r) {
      super();
      rs = r;
    }

   /**
    * Restituisce il numero delle righe
    * @return numero di righe della tabella
    */
    @Override
    public int getRowCount() {
        if (!DBConnection.connected()) {
            return 0;
        }
        if (rs == null)
            return 0;
        try {
            int currentPosition, last;
            currentPosition = rs.getRow();
            rs.last();
            last = rs.getRow();
            rs.absolute(currentPosition);
            return last;
        }
        catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Errore: " + ex.toString(), null, ERROR_MESSAGE);
            return 0;
        }
    }

    /**
     * Restituisce il numero di colonne della tabella
     * @return 
     */
    @Override
    public int getColumnCount() {
        if (rs == null)
            return 0;
        try {
            return rs.getMetaData().getColumnCount();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage(), null, ERROR_MESSAGE);
            return 0;
        }
    }
    
    /**
     * Restituisce il nome della colonn all'indirizzo i
     * @param columnIndex numero di colonna (da 1)
     * @return nome della colonna
     * @throws SQLException 
     */
    public String getDBColumnName(int columnIndex) throws SQLException {
        if (rs == null)
            return "";
        return rs.getMetaData().getColumnName(columnIndex);
    }
    
    /**
     * Restituisce il nome dell'ID della tabella (presumibilmente è la prima
     * colonna)
     * @return
     * @throws SQLException 
     */
    public String getDBID() throws SQLException {
        return getDBColumnName(1);
    }
    
    /**
     * Restituisce un Object relativo alla riga e colonna della tabella
     * @param rowIndex numero di riga
     * @param columnIndex numero di colonna
     * @return valore di ritorno di tipo Object
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        int currentPosition;
        Object ob = null;
        rowIndex++;
        columnIndex++;
        try {
            currentPosition = rs.getRow();
            rs.absolute(rowIndex);
            ob = rs.getObject(columnIndex);
            rs.absolute(currentPosition);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage(), null, ERROR_MESSAGE);
        }
        return ob;
    }
    
    /**
     * Ottiene la somma degli elementi di una colonna
     * @param columnIndex
     * @return 
     */
    public double getColumnSum(int columnIndex) {
        double sum = 0;
        for (int i = 0; i < getRowCount(); i++) {
            try {
                sum += Double.parseDouble(getValueAt(i,columnIndex).toString());
            } catch (NumberFormatException ex) {
                System.out.println("ERRORE SOMMA");
            }
        }
        return sum;
    }
    
    /**
     * Non fa nulla, i dati del DB non possono essere modificati dalla tabella
     * @param aValue
     * @param row
     * @param column 
     */
    @Override
    public void setValueAt(Object aValue, int row, int column) {
        //NON FA NULLA
    }
    
    /**
     * Ottiene il nome della colonna dal resultSet
     * @param col
     * @return 
     */
    @Override
    public String getColumnName(int col) {
        col++;
        String res = "";
        if (rs == null) {
            return res;
        }
        try {
            res = rs.getMetaData().getColumnName(col);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage(), null, ERROR_MESSAGE);
            return "";
        }
        return res;
    }
    
    /**
     * Imposta un nuovo resultSet della tabella
     * @param r 
     */
    public void setRS(ResultSet r) {
      rs = r;
      fireTableStructureChanged();

   }
    
    /**
     * Disattiva la modifica per tutte le celle della tabella
     * @param row
     * @param col
     * @return 
     */
   @Override
   public boolean isCellEditable(int row, int col) {
      return false;
   }
   
   
    
}
