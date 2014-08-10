/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon.utility;

/**
 * Classe di utilizzo degli indirizzi per svariati usi
 * @author Francesco Bosso <fr.bosso at outlook.it>
 */
public class Contatto {
    public Contatto(int id, String nome, String cognome) {
        this.contactId = id;
        this.nome = nome;
        this.cognome = cognome;
    }
    
    public Contatto(int id, String nome, String cognome, String indirizzo1, String cap) {
        this.contactId = id;
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo1 = indirizzo1;
        this.cap = cap;
    }
    
    private final int contactId;
    private String nome, cognome, indirizzo1, indirizzo2, citta, cap, prov;
    
    /**
     * Usato per l'uso in interfaccia.
     * @return "Nome Cognome, Indirizzo1 Città"
     */
    @Override
    public String toString() {
        return nome+" "+cognome+", "+indirizzo1+" "+citta;
    }

    /**
     * @return the id
     */
    public int getId() {
        return contactId;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the cognome
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * @param cognome the cognome to set
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * @return the indirizzo1
     */
    public String getIndirizzo1() {
        return indirizzo1;
    }

    /**
     * @param indirizzo1 the indirizzo1 to set
     */
    public void setIndirizzo1(String indirizzo1) {
        this.indirizzo1 = indirizzo1;
    }

    /**
     * @return the indirizzo2
     */
    public String getIndirizzo2() {
        return indirizzo2;
    }

    /**
     * @param indirizzo2 the indirizzo2 to set
     */
    public void setIndirizzo2(String indirizzo2) {
        this.indirizzo2 = indirizzo2;
    }

    /**
     * @return the citta
     */
    public String getCitta() {
        return citta;
    }

    /**
     * @param citta the citta to set
     */
    public void setCitta(String citta) {
        this.citta = citta;
    }

    /**
     * @return the cap
     */
    public String getCap() {
        return cap;
    }

    /**
     * @param cap the cap to set
     */
    public void setCap(String cap) {
        this.cap = cap;
    }

    /**
     * @return the prov
     */
    public String getProv() {
        return prov;
    }

    /**
     * @param prov the prov to set
     */
    public void setProv(String prov) {
        this.prov = prov;
    }
}
