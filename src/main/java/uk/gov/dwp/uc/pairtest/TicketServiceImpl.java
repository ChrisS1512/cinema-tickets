package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.List;
import java.util.stream.Stream;

public class TicketServiceImpl implements TicketService {

    private SeatReservationService seatReservationService;
    private TicketPaymentService ticketPaymentService;
    private TicketValidator ticketValidator;

    TicketServiceImpl(SeatReservationService seatReservationService,
                             TicketPaymentService ticketPaymentService,
                             TicketValidator ticketValidator) {
        this.seatReservationService = seatReservationService;
        this.ticketPaymentService = ticketPaymentService;
        this.ticketValidator = ticketValidator;
    }

    /**
     * Should only have private methods other than the one below.
     */
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        ticketValidator.validateTicketRequest(accountId, List.of(ticketTypeRequests));

        int totalAmountToPay = Stream.of(ticketTypeRequests)
                .mapToInt(TicketTypeRequest::calculateTicketCost)
                .sum();

        int totalAllocatedSeats = Stream.of(ticketTypeRequests)
                .filter(ticketTypeRequest -> !ticketTypeRequest.getTicketType().equals(TicketTypeRequest.Type.INFANT))
                .mapToInt(TicketTypeRequest::getNoOfTickets)
                .sum();

        ticketPaymentService.makePayment(accountId, totalAmountToPay);
        seatReservationService.reserveSeat(accountId, totalAllocatedSeats);
    }

}
