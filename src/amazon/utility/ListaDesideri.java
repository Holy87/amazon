/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon.utility;

/**
 * Classe di memorizzazione efficiente delle liste dei desideri
 * @author Francesco Bosso <fr.bosso at outlook.it>
 */
public class ListaDesideri {
    private int idLista;
    private String nomeLista;
    
    public ListaDesideri(int id, String nome) {
        idLista = id;
        nomeLista = nome;
    }

    /**
     * @return the idLista
     */
    public int getIdLista() {
        return idLista;
    }

    /**
     * @return the nomeLista
     */
    public String getNomeLista() {
        return nomeLista;
    }
    
}
