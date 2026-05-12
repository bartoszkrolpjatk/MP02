package pl.edu.pjatk.mas.mp02.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;
import pl.edu.pjatk.mas.mp02.model.association.Association;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Association(targetType = Seat.class, min = 1, max = 1)
@Association(targetType = Station.class, id = Ticket.START_STATION_ID, min = 1, max = 1)
@Association(targetType = Station.class, id = Ticket.STOP_STATION_ID, min = 1, max = 1)
@RequiredArgsConstructor
public class Ticket extends AssociatedObject {
    public static final String START_STATION_ID = "startStation";
    public static final String STOP_STATION_ID = "stopStation";

    private final Double discount;
    private final double basePrice;
    private final LocalDateTime purchaseDate;
    private final String passengerFirstName;
    private final String passengerLastName;
}
