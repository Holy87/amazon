/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

/**
 *
 * @author pixel89
 */
public class Scontotemp {
    private String codPromo; //codice promozionale
    private double sconto;
    
    /**
     * Costruttore
     * @param codPromo Stringa, codice alfanumerico
     * @param sconto valore con la virgola, sconto assoluto
     */
    public Scontotemp(String codPromo, double sconto)   {
        this.codPromo=codPromo;
        this.sconto=sconto;
    }
    
    /**
     * Ottiene il codice promozionale
     * @return 
     */
    public String getcodPromo()    {
        return codPromo;
    }
    
    /**
     * Ottiene lo sconto assoluto
     * @return 
     */
    public double getSconto() {
        return sconto;
    }
}
