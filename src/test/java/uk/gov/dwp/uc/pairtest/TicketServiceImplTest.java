package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @Mock
    private SeatReservationService seatReservationService;

    @Mock
    private TicketPaymentService ticketPaymentService;

    @Mock
    private TicketValidator ticketValidator;

    private TicketService ticketService;

    private static final String TEST_EXCEPTION_MESSAGE = "Test Message";

    @BeforeEach
    public void setup() {
        ticketService = new TicketServiceImpl(seatReservationService, ticketPaymentService, ticketValidator);
    }

    @Test
    public void testExceptionIsThrownWhenTicketValidationFails() {
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 4);

        doThrow(new InvalidPurchaseException(TEST_EXCEPTION_MESSAGE)).when(ticketValidator)
                .validateTicketRequest(1L, List.of(ticketTypeRequest));

        InvalidPurchaseException invalidPurchaseException = assertThrows(InvalidPurchaseException.class, () ->
                ticketService.purchaseTickets(1L, ticketTypeRequest), "No exception thrown");

        assertEquals(TEST_EXCEPTION_MESSAGE, invalidPurchaseException.getMessage(),
                "Invalid exception message displayed");
    }

    @Test
    public void testPaymentServiceIsCalledWithCorrectValues() {
        TicketTypeRequest[] ticketTypeRequests = {
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 4),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 8)
        };

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);

        doNothing().when(ticketPaymentService).makePayment(eq(2L), argumentCaptor.capture());

        ticketService.purchaseTickets(2L, ticketTypeRequests);

        assertEquals(100, argumentCaptor.getValue(), "Invalid payment amount supplied");
    }

    @Test
    public void testSeatReservationServiceIsCalledWithCorrectValues() {
        TicketTypeRequest[] ticketTypeRequests = {
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 4),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 8)
        };

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);

        doNothing().when(seatReservationService).reserveSeat(eq(4L), argumentCaptor.capture());

        ticketService.purchaseTickets(4L, ticketTypeRequests);

        assertEquals(6, argumentCaptor.getValue(), "Invalid seat reservation supplied");
    }
}
