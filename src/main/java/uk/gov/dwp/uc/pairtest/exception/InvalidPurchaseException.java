package uk.gov.dwp.uc.pairtest.exception;

public class InvalidPurchaseException extends RuntimeException {

    /**
     * Constructor for the Invalid Purchase Exception class.
     * @param message The message to pass up to the super class.
     */
    public InvalidPurchaseException(String message) {
        super(message);
    }

}
