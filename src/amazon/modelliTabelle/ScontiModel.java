/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon.modelliTabelle;

import amazon.utility.Scontotemp;
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

    /**
     * Ottieni il conteggio delle righe
     * @return numero di righe
     */
    @Override
    public int getRowCount() {
        return sconti.size();
    }

    /**
     * ottieni il conteggio delle colonne
     * @return sempre 2
     */
    @Override
    public int getColumnCount() {
        return 2;
    }

    /**
     * Ottieni il valore alla cella specificata
     * Se i1 è 0, restituisce il codice promozionale
     * Se i1 è 1, restituisce il bonus sconto
     * Se i1 è un altro numero, restituisce null
     * @param i riga
     * @param i1 colonna
     * @return 
     */
    @Override
    public Object getValueAt(int i, int i1) {
        switch (i1) {
            case 0: return sconti.get(i).getcodPromo();
            case 1: return sconti.get(i).getSconto();
            default: return null;
        }
    }
    
    /**
     * Ottiene il nome della colonna
     * @param i indice di colonna
     * @return nome colonna
     */
    @Override
    public String getColumnName(int i) {
        switch (i) {
            case 0: return "Codice sconto";
            case 1: return "Sconto";
            default: return "???";
        }
    }
    
    /**
     * Imposta la lista degli sconti nella tabella
     * @param sconti lista di elementi sconti
     */
    public void setSconti(LinkedList<Scontotemp> sconti) {
        this.sconti = sconti;
        fireTableStructureChanged();
    }
    
}
