package pl.edu.pjatk.mas.mp02.model;

import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;
import pl.edu.pjatk.mas.mp02.model.association.Association;
import pl.edu.pjatk.mas.mp02.model.association.Station;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Association(targetType = Seat.class)
@Association(targetType = Station.class, identifier = Ticket.START_STATION_IDENTIFIER, targetIdentifier = Station.DEPARTING_TICKETS_IDENTIFIER)
@Association(targetType = Station.class, identifier = Ticket.STOP_STATION_IDENTIFIER, targetIdentifier = Station.ARRIVING_TICKETS_IDENTIFIER)
public class Ticket extends AssociatedObject {
    public static final String START_STATION_IDENTIFIER = "startStation";
    public static final String STOP_STATION_IDENTIFIER = "stopStation";

    private final Double discount;
    private final double basePrice;
    private final LocalDateTime purchaseDate;
    private final String passengerFirstName;
    private final String passengerLastName;
}
