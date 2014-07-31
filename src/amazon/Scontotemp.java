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
    private String codPromo;
    private double sconto;
    
    public Scontotemp(String codPromo, double sconto)   {
        this.codPromo=codPromo;
        this.sconto=sconto;
    }
    
    public String getcodPromo()    {
        return codPromo;
    }
    
    public double getSconto() {
        return sconto;
    }
}
