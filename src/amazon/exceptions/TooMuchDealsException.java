/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon.exceptions;

/**
 * Eccezione che si verifica quando il bonus degli sconti supera quello del carrello.
 * @author Francesco
 */
public class TooMuchDealsException extends java.lang.Exception {
    public TooMuchDealsException() { super(); }
}
