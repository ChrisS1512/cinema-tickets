package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.List;

/**
 * Interface for Ticket validation.
 */
public interface TicketValidator {

    void validateTicketRequest(Long accountId, List<TicketTypeRequest> ticketTypeRequests) throws InvalidPurchaseException;
}
