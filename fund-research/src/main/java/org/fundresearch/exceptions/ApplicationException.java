package org.fundresearch.exceptions;

/**
 * Custom checked exception to wrap around Java checked exceptions.
 * Created by aditya on 02-Feb-17.
 */
public class ApplicationException extends Exception {


    public ApplicationException(String s) {
        super(s);
    }

    public ApplicationException(String s, Throwable e) {
        super(s, e);
    }
}
