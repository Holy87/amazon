/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

/**
 *
 * @author Claudio Renza
 */
public class PrezzoTotale {
    public double totale;    
    
    public double setPrezzo(double netto, double costosped, double sconto)  {
        totale = netto + costosped - sconto;
        return totale;
    }
}
