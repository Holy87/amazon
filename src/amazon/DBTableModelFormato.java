/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import amazon.modelliTabelle.DBTableModel;
import java.sql.ResultSet;
import java.util.Hashtable;

/**
 *
 * @author frbos_000
 */
public class DBTableModelFormato extends DBTableModel {
    private Hashtable<String, String> listaFormati;
    private Hashtable<String, String> formatiInversi;
    
    public DBTableModelFormato() {
        super();
        setHashtable();
    }
    
    public void setResultSet(ResultSet r) {
        super.setResultSet(r);
        changeValues();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object risultato = super.getValueAt(rowIndex, columnIndex);
        if (columnIndex == 0)
            risultato = formatiInversi.get(risultato);
        return risultato;
    }
    
    private void changeValues() {
        for (int i = 0; i < getRowCount(); i++) {
            setValueAt(listaFormati.get(getValueAt(i, 0).toString()), i, 0);
        }
    }
    
    private void setHashtable() {
        listaFormati = new Hashtable();
        listaFormati.put("2001", "Copertina flessibile");
        listaFormati.put("2002", "Copertina rigida");
        listaFormati.put("2003", "Download digitale");
        formatiInversi = new Hashtable();
        formatiInversi.put(listaFormati.get("2001"), "2001");
        formatiInversi.put(listaFormati.get("2002"), "2002");
        formatiInversi.put(listaFormati.get("2003"), "2003");
    }
    
}
