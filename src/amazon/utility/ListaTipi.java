/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon.utility;

import java.util.LinkedList;
import java.util.Hashtable;

/**
 * Questa classe serve per memorizzare i tipi di dati delle colonne in modo
 * da eseguire query intelligenti e automatiche dalla tabOggetti. Il metodo
 * viene chiamato in TabOggeti al metodo eseguiQuerySuTabella.
 * @author Francesco
 */
public class ListaTipi {
    private Hashtable<String, LinkedList> tabella;
    
    public ListaTipi() {
        super();
        inizializzaTipi();
    }
    
    /**
     * Non c'è bisogno di spiegare a cosa serve.
     * @return 
     */
    public Hashtable<String, LinkedList> getTipi() {
        return tabella;
    }
    
    /**
     * Inizializzazione. Viene chiamato dal costruttore.
     */
    private void inizializzaTipi(){
        /*
        i: tipo intero
        s: tipo stringa
        f: con la virgola
        d: tipo data
        */
        tabella = new Hashtable();
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
        libri.add('s');//verifica che si tratti dell'ISBN
        libri.add('i');
        libri.add('s');
        libri.add('s');
        libri.add('s');
        libri.add('i');
        libri.add('i');
        libri.add('d');
        
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
        
        LinkedList venditori = new LinkedList();
        venditori.add('i');
        venditori.add('s');
        
        tabella.put("UTENTI", utenti);
        tabella.put("AUTORI", autori);
        tabella.put("CORRIERI", corrieri);
        tabella.put("EDITORI", editori);
        tabella.put("LIBRI", libri);
        tabella.put("MAGAZZINO_LIBRI", magazzinoLibri);
        tabella.put("VENDITORI",venditori);
        
    }
}
