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
public class LoginException extends Exception {

    /**
     * Creates a new instance of <code>loginException</code> without detail
     * message.
     */
    public LoginException() {
    }

    /**
     * Constructs an instance of <code>loginException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public LoginException(String msg) {
        super(msg);
    }
}
