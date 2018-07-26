package com.danydiaz.callcenter.exception;

/**
 * The type Call interrupted exception.
 *
 * @version 1.0
 * @date 25/07/2018
 */
public class CallInterruptedException extends RuntimeException {

    /**
     * Instantiates a new Call interrupted exception.
     *
     * @param cause the cause
     */
    public CallInterruptedException(Throwable cause) {
        super(cause);
    }
}
