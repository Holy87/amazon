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
    * Imposta il Resultset su cui si basa il modello.
    * 
    * @param r il ResultSet su cui basare il modello
    */
   public void setResultSet(ResultSet r) {
      rs = r;
      fireTableStructureChanged();
   }

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
    
    public String getDBColumnName(int columnIndex) throws SQLException {
        if (rs == null)
            return "";
        return rs.getMetaData().getColumnName(columnIndex);
    }
    
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
    
    @Override
    public void setValueAt(Object aValue, int row, int column) {
        //devo metterci qua qualcosa
    }
    
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
    
    public void setRS(ResultSet r) {
      rs = r;
      fireTableStructureChanged();

   }
    
   @Override
   public boolean isCellEditable(int row, int col) {
      return false;
   }
   
   
    
}
