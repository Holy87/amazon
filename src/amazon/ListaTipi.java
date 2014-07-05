/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import java.util.LinkedList;
import java.util.List;
import java.util.Hashtable;

/**
 *
 * @author Francesco
 */
public class ListaTipi {
    private Hashtable<String, LinkedList> tabella;
    
    public ListaTipi() {
        super();
        inizializzaTipi();
    }
    
    public Hashtable<String, LinkedList> getTipi() {
        return tabella;
    }
    
    private void inizializzaTipi(){
        tabella = new Hashtable<String, LinkedList>();
        LinkedList utenti = new LinkedList();
        utenti.add('i');
        utenti.add('s');
        utenti.add('s');
        utenti.add('s');
        utenti.add('s');
        utenti.add('i');
        
        LinkedList autori = new LinkedList();
        autori.add('i');
        autori.add('s');
        autori.add('s');
        
        LinkedList libri = new LinkedList();
        libri.add('i');
        libri.add('s');
        libri.add('i');
        libri.add('i');
        libri.add('s');
        libri.add('s');
        libri.add('i');
        libri.add('i');
        libri.add('d');
        libri.add('f');
        libri.add('i');
        
        LinkedList corrieri = new LinkedList();
        corrieri.add('i');
        corrieri.add('s');
        
        LinkedList editori = new LinkedList();
        editori.add('i');
        editori.add('s');
        
        LinkedList magazzinoLibri = new LinkedList();
        magazzinoLibri.add('i');
        magazzinoLibri.add('i');
        magazzinoLibri.add('i');
        magazzinoLibri.add('s');
        magazzinoLibri.add('i');
        magazzinoLibri.add('i');
        magazzinoLibri.add('i');
        
        tabella.put("UTENTI", utenti);
        tabella.put("AUTORI", autori);
        tabella.put("CORRIERI", corrieri);
        tabella.put("EDITORI", editori);
        tabella.put("LIBRI", libri);
        tabella.put("MAGAZZINO_LIBRI", magazzinoLibri);
        
    }
}
