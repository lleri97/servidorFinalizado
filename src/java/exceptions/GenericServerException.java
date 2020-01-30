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
public class GenericServerException extends Exception {

    /**
     * Creates a new instance of <code>GenericServerException</code> without
     * detail message.
     */
    public GenericServerException() {
    }

    /**
     * Constructs an instance of <code>GenericServerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GenericServerException(String msg) {
        super(msg);
    }
}
