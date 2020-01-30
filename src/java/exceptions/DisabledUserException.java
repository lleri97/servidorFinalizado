/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Jon Gonzalez
 */
public class DisabledUserException extends Exception {

    /**
     * Creates a new instance of <code>DisabledUserException</code> without
     * detail message.
     */
    public DisabledUserException() {
    }

    /**
     * Constructs an instance of <code>DisabledUserException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DisabledUserException(String msg) {
        super(msg);
    }
}
