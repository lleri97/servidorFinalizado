/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author 2dam
 */
public class LoginPasswordException extends Exception {

    /**
     * Creates a new instance of <code>LoginPasswordException</code> without
     * detail message.
     */
    public LoginPasswordException() {
    }

    /**
     * Constructs an instance of <code>LoginPasswordException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public LoginPasswordException(String msg) {
        super(msg);
    }
}
