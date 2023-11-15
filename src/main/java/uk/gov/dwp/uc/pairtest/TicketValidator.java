package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public interface TicketValidator {

    void validateTicketRequest(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException;
}
