/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Peiko
 */
public class SelectException extends Exception {

    /**
     * Creates a new instance of <code>SelectException</code> without detail
     * message.
     */
    public SelectException() {
    }

    /**
     * Constructs an instance of <code>SelectException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public SelectException(String msg) {
        super(msg);
    }
}
