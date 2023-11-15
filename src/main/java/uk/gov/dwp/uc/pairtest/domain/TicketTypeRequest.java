package uk.gov.dwp.uc.pairtest.domain;

/**
 * Immutable Object
 */

public class TicketTypeRequest {

    private int noOfTickets;
    private Type type;

    public TicketTypeRequest(Type type, int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    /**
     * Calculates the total ticket cost, based on the type of ticket and number.
     * @return The cost of the ticket.
     */
    public int calculateTicketCost() {
        return type.ticketCost * noOfTickets;
    }

    public enum Type {
        ADULT(20), CHILD(10) , INFANT(0);

        private final int ticketCost;

        Type(int ticketCost) {
            this.ticketCost = ticketCost;
        }
    }

}
