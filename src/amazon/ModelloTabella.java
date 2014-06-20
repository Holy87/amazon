/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Francesco
 */
public class ModelloTabella extends AbstractTableModel {
    private ResultSet rs; //resultset su cui si basa il modello
    
    public ModelloTabella() {
        super();
    }
    
    public ModelloTabella(ResultSet r) {
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
            JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage(), null, ERROR_MESSAGE);
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

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        int currentPosition;
        Object ob = null;
        rowIndex++;
        columnIndex++;
        try {
            currentPosition = rs.getRow();
            rs.absolute(rowIndex);
            ob = rs.getObject(currentPosition);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage(), null, ERROR_MESSAGE);
            return null;
        }
        return ob;
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
