package uk.gov.dwp.uc.pairtest.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvalidPurchaseExceptionTest {

    @Test
    public void testExceptionMessageIsPassedThrough() {
        InvalidPurchaseException invalidPurchaseException = new InvalidPurchaseException("This is a message");

        assertEquals("This is a message", invalidPurchaseException.getMessage(),
                "Invalid exception message provided");
    }

}
