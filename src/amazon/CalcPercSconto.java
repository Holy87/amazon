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
public class CalcPercSconto {
    public int percento;    
    
    public int setPercSconto(double listino, double vendita)  {
        percento = (int) (100 - ((vendita / listino) * 100));
        return percento;
    }
}
