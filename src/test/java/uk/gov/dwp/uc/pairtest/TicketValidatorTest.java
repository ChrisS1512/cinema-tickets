package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TicketValidatorTest {

    private TicketValidator ticketValidator = new TicketValidatorImpl();

    private static final String INVALID_ID_MESSAGE = "Account ID must be populated and > 0";
    private static final String INVALID_TICKET_COUNT_MESSAGE = "Total requested tickets must be between 1 and 20";
    private static final String INVALID_INDIVIDUAL_TICKET_COUNT_MESSAGE =
            "Each ticket request must have a request count >= 1";
    private static final String INVALID_REQUEST_TYPE_MESSAGE = "Each ticket request must have a ticket request type";
    private static final String INVALID_ADULT_TICKET_MESSAGE = "At least one adult ticket must be purchased";
    private static final String NULL_TICKET_REQUEST_MESSAGE = "Ticket requests must not be null";
    private static final String INVALID_EXCEPTION_MESSAGE = "Invalid exception message shown";

    @Test
    public void validateExceptionWhenNoAccountId() {
        TicketTypeRequest[] ticketTypeRequests = {new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2)};
        InvalidPurchaseException invalidPurchaseException = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketRequest(null, ticketTypeRequests));

        assertEquals(INVALID_ID_MESSAGE, invalidPurchaseException.getMessage(), INVALID_EXCEPTION_MESSAGE);
    }

    @Test
    public void validateExceptionWhenInvalidAccountId() {
        TicketTypeRequest[] ticketTypeRequests = {new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2)};
        InvalidPurchaseException invalidPurchaseException = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketRequest(0L, ticketTypeRequests));

        assertEquals(INVALID_ID_MESSAGE, invalidPurchaseException.getMessage(), INVALID_EXCEPTION_MESSAGE);
    }

    @Test
    public void validateExceptionWhenGreaterThanMaxTickets() {
        TicketTypeRequest[] ticketTypeRequests = {
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 15),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 8)
        };

        InvalidPurchaseException invalidPurchaseException = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketRequest(1L, ticketTypeRequests));

        assertEquals(INVALID_TICKET_COUNT_MESSAGE, invalidPurchaseException.getMessage(), INVALID_EXCEPTION_MESSAGE);
    }

    @Test
    public void validateExceptionWhenNoTicketsRequested() {
        TicketTypeRequest[] ticketTypeRequests = {};

        InvalidPurchaseException invalidPurchaseException = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketRequest(1L, ticketTypeRequests));

        assertEquals(INVALID_TICKET_COUNT_MESSAGE, invalidPurchaseException.getMessage(), INVALID_EXCEPTION_MESSAGE);
    }

    @Test
    public void validateExceptionWhenNullTicketsRequested() {
        TicketTypeRequest[] ticketTypeRequests = {null};

        InvalidPurchaseException invalidPurchaseException = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketRequest(1L, ticketTypeRequests));

        assertEquals(NULL_TICKET_REQUEST_MESSAGE, invalidPurchaseException.getMessage(), INVALID_EXCEPTION_MESSAGE);
    }

    @Test
    public void validateExceptionWhenNullPassedInToTicketRequestsField() {
        InvalidPurchaseException invalidPurchaseException = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketRequest(1L, null));

        assertEquals(NULL_TICKET_REQUEST_MESSAGE, invalidPurchaseException.getMessage(), INVALID_EXCEPTION_MESSAGE);
    }

    @Test
    public void validateExceptionWhenTicketRequestedWithoutRequestType() {
        TicketTypeRequest[] ticketTypeRequests = {
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 15),
                new TicketTypeRequest(null, 4)
        };

        InvalidPurchaseException invalidPurchaseException = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketRequest(1L, ticketTypeRequests));

        assertEquals(INVALID_REQUEST_TYPE_MESSAGE, invalidPurchaseException.getMessage(), INVALID_EXCEPTION_MESSAGE);
    }

    @Test
    public void validateExceptionWhenTicketRequestedWithInvalidRequestCount() {
        TicketTypeRequest[] ticketTypeRequests = {
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 4),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 0)
        };

        InvalidPurchaseException invalidPurchaseException = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketRequest(1L, ticketTypeRequests));

        assertEquals(INVALID_INDIVIDUAL_TICKET_COUNT_MESSAGE, invalidPurchaseException.getMessage(), INVALID_EXCEPTION_MESSAGE);
    }

    @Test
    public void validateExceptionWhenNoAdultTicketsPurchased() {
        TicketTypeRequest[] ticketTypeRequests = {
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 8)
        };

        InvalidPurchaseException invalidPurchaseException = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketRequest(1L, ticketTypeRequests));

        assertEquals(INVALID_ADULT_TICKET_MESSAGE, invalidPurchaseException.getMessage(), INVALID_EXCEPTION_MESSAGE);
    }
}
