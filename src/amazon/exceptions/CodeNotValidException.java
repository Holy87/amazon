/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon.exceptions;

/**
 *
 * @author frbos_000
 */
public class CodeNotValidException extends java.lang.Exception {
    public CodeNotValidException() { super(); }
    public CodeNotValidException(String message) { super(message); }
    public CodeNotValidException(String message, Throwable cause) { super(message, cause); }
    public CodeNotValidException(Throwable cause) { super(cause); }
}
