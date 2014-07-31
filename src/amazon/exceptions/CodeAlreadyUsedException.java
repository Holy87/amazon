/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon.exceptions;

/**
 * Eccezione che viene lanciata quando il codice è già usato
 * @author frbos_000
 */
public class CodeAlreadyUsedException extends java.lang.Exception {
    public CodeAlreadyUsedException() { super(); }
    public CodeAlreadyUsedException(String message) { super(message); }
    public CodeAlreadyUsedException(String message, Throwable cause) { super(message, cause); }
    public CodeAlreadyUsedException(Throwable cause) { super(cause); }
}
