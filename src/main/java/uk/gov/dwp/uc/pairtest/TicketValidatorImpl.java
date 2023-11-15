package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;
import java.util.Objects;

/**
 * Class which handles the validation for tickets
 */
public class TicketValidatorImpl implements TicketValidator {

    /**
     * Entry method for the validation service, which starts the validation process.
     * @param accountId The account ID to validate.
     * @param ticketTypeRequests The ticket requests to validate.
     * @throws InvalidPurchaseException Thrown with an appropriate error message if validation fails.
     */
    @Override
    public void validateTicketRequest(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        validateAccountId(accountId);
        validateNullTicketTypeRequests(ticketTypeRequests);
        validateTicketRequestType(ticketTypeRequests);
        validateTicketIndividualRequestCount(ticketTypeRequests);
        validateTicketTotalRequestCount(ticketTypeRequests);
    }

    private void validateAccountId(Long accountId) throws InvalidPurchaseException {
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException("Account ID must be populated and > 0");
        }
    }

    private void validateTicketTotalRequestCount(TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        int totalTickets = Arrays.stream(ticketTypeRequests)
                .mapToInt(TicketTypeRequest::getNoOfTickets)
                .sum();

        if (totalTickets <= 0 || totalTickets > 20) {
            throw new InvalidPurchaseException("Total requested tickets must be between 1 and 20");
        }

        long adultTickets = Arrays.stream(ticketTypeRequests)
                .filter(ticketTypeRequest -> ticketTypeRequest.getTicketType().equals(TicketTypeRequest.Type.ADULT))
                .count();

        if (adultTickets == 0) {
            throw new InvalidPurchaseException("At least one adult ticket must be purchased");
        }
    }

    private void validateTicketIndividualRequestCount(TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        long negativeTicketCount = Arrays.stream(ticketTypeRequests)
                .map(TicketTypeRequest::getNoOfTickets)
                .filter(ticketNum -> ticketNum <= 0)
                .count();

        if (negativeTicketCount > 0) {
            throw new InvalidPurchaseException("Each ticket request must have a request count >= 1");
        }
    }

    private void validateNullTicketTypeRequests(TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        if (ticketTypeRequests == null || Arrays.stream(ticketTypeRequests).anyMatch(Objects::isNull)) {
            throw new InvalidPurchaseException("Ticket requests must not be null");
        }
    }

    private void validateTicketRequestType(TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        long nullTicketType = Arrays.stream(ticketTypeRequests)
                .filter(ticketTypeRequest -> ticketTypeRequest.getTicketType() == null)
                .count();

        if (nullTicketType > 0) {
            throw new InvalidPurchaseException("Each ticket request must have a ticket request type");
        }
    }

}
