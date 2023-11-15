package uk.gov.dwp.uc.pairtest.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketTypeRequestTest {

    @Test
    public void testCalculateTicketCostAdult() {
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 4);

        assertEquals(80, ticketTypeRequest.calculateTicketCost(), "Invalid adult ticket cost");
    }

    @Test
    public void testCalculateTicketCostChild() {
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 5);

        assertEquals(50, ticketTypeRequest.calculateTicketCost(), "Invalid child ticket cost");
    }

    @Test
    public void testCalculateTicketCostInfant() {
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 8);

        assertEquals(0, ticketTypeRequest.calculateTicketCost(), "Invalid infant ticket cost");
    }
}
