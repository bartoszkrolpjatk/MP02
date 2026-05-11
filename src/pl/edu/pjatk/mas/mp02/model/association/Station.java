package pl.edu.pjatk.mas.mp02.model.association;

import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.Ticket;

@Association(targetType = Ticket.class, identifier = Station.ARRIVING_TICKETS_IDENTIFIER, targetIdentifier = Ticket.START_STATION_IDENTIFIER)
@Association(targetType = Ticket.class, identifier = Station.DEPARTING_TICKETS_IDENTIFIER, targetIdentifier = Ticket.STOP_STATION_IDENTIFIER)
@RequiredArgsConstructor
public class Station extends AssociatedObject {
    public static final String DEPARTING_TICKETS_IDENTIFIER = "departingTickets";
    public static final String ARRIVING_TICKETS_IDENTIFIER = "arrivingTickets";

    private final String name;
}
