/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon.modelliTabelle;

import amazon.Scontotemp;
import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;

/**
 * Modello di tabella per il contenimento dei codici sconto.
 * @author Francesco
 */
public class ScontiModel extends AbstractTableModel {
    private LinkedList<Scontotemp> sconti;
    
    public ScontiModel(LinkedList<Scontotemp> sconti) {
        this.sconti = sconti;
    }

    @Override
    public int getRowCount() {
        return sconti.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        switch (i1) {
            case 0: return sconti.get(i).getcodPromo();
            case 1: return sconti.get(i).getSconto();
            default: return null;
        }
    }
    
    @Override
    public String getColumnName(int i) {
        switch (i) {
            case 0: return "Codice sconto";
            case 1: return "Sconto";
            default: return "???";
        }
    }
    
    public void setSconti(LinkedList<Scontotemp> sconti) {
        this.sconti = sconti;
        fireTableStructureChanged();
    }
    
}
