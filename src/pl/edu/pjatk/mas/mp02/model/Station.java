package pl.edu.pjatk.mas.mp02.model;

import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;
import pl.edu.pjatk.mas.mp02.model.association.Association;

import java.util.UUID;

@Association(targetType = Ticket.class, id = Ticket.START_STATION_IDENTIFIER)
@Association(targetType = Ticket.class, id = Ticket.STOP_STATION_IDENTIFIER)
@RequiredArgsConstructor
public class Station extends AssociatedObject {
    private final String name;
}
