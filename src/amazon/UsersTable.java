/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import java.sql.*;
import javax.swing.*;
/**
 *
 * @author Francesco
 */
public class UsersTable extends javax.swing.JTable implements UsaLookup {

    ModelloTabella modello = new ModelloTabella(); //modello della tabellat
    
    public UsersTable() {
        super();
        setModel(modello);
    }
    
    @Override
    public void setProprietaPadre(String proprieta, String valore) {
      int riga;
      if (proprieta.equals("Caratteristica")) {
         riga = getSelectedRow();
         modello.setValueAt(valore, riga, 0);
         modello.fireTableRowsUpdated(riga, 0);
         setColumnSelectionInterval(1, 1);
      }
   }
    
}
