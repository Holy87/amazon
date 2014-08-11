/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon.utility;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class ModPagamento {
    /**
     * Crea una nuova ModPagamento
     * @param id della modalità di pagamento
     * @param numero di carta di credito
     * @param tipo di carta
     */
    public ModPagamento(int id, String numero, String tipo) {
        this.numero = numero;
        this.tipo = tipo;
        this.civ = civ;
        this.id = id;
    }
    
    /**
     * Crea una nuova ModPagamento
     * @param id della modalità di pagamento
     * @param numero della carta di credito
     * @param tipo di carta
     * @param indir indirizzo di fatturazione in formato Contatto
     */
    public ModPagamento(int id, String numero, String tipo, Contatto indir) {
        this.numero = numero;
        this.tipo = tipo;
        this.civ = civ;
        this.id = id;
        indirizzoFatturazione = indir;
    }
    
    private int civ, meseScadenza, annoScadenza, id;
    private String numero, tipo, nomeIntestatario, cognomeIntestatario, dataScadenza;
    private Contatto indirizzoFatturazione;
    
    /**
     * Usato per l'assegnazione in una comboBox
     * @return TIPOCARTA ****-****-****-NUMC
     */
    @Override
    public String toString() {
        return tipo + " ****-****-****-" + numero.substring(numero.length()-5, numero.length()-1);
    }
    
    /**
     * Assegna mese e anno scadenza da una data in stringa AAAA-MM-DD
     * @param data 
     */
    public void setData(String data) {
        dataScadenza = data;
        meseScadenza = Integer.parseInt(data.substring(5, 7));
        annoScadenza = Integer.parseInt(data.substring(0, 4));
    }
    
    /**
     * Restituisce mese e anno di scadenza in una data in stringa
     * @return AAAA-MM-01
     */
    public String getData() {
        //return dataScadenza;
        return annoScadenza+"-"+meseScadenza+"01";
    }

    /**
     * @return the civ
     */
    public int getCiv() {
        return civ;
    }

    /**
     * @param civ the civ to set
     */
    public void setCiv(int civ) {
        this.civ = civ;
    }

    /**
     * @return the meseScadenza
     */
    public int getMeseScadenza() {
        return meseScadenza;
    }

    /**
     * @param meseScadenza the meseScadenza to set
     */
    public void setMeseScadenza(int meseScadenza) {
        this.meseScadenza = meseScadenza;
    }

    /**
     * @return the annoScadenza
     */
    public int getAnnoScadenza() {
        return annoScadenza;
    }

    /**
     * @param annoScadenza the annoScadenza to set
     */
    public void setAnnoScadenza(int annoScadenza) {
        this.annoScadenza = annoScadenza;
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the nomeIntestatario
     */
    public String getNomeIntestatario() {
        return nomeIntestatario;
    }

    /**
     * @param nomeIntestatario the nomeIntestatario to set
     */
    public void setNomeIntestatario(String nomeIntestatario) {
        this.nomeIntestatario = nomeIntestatario;
    }

    /**
     * @return the cognomeIntestatario
     */
    public String getCognomeIntestatario() {
        return cognomeIntestatario;
    }

    /**
     * @param cognomeIntestatario the cognomeIntestatario to set
     */
    public void setCognomeIntestatario(String cognomeIntestatario) {
        this.cognomeIntestatario = cognomeIntestatario;
    }

    /**
     * @return the indirizzoFatturazione
     */
    public Contatto getIndirizzoFatturazione() {
        return indirizzoFatturazione;
    }

    /**
     * @param indirizzoFatturazione the indirizzoFatturazione to set
     */
    public void setIndirizzoFatturazione(Contatto indirizzoFatturazione) {
        this.indirizzoFatturazione = indirizzoFatturazione;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
}
