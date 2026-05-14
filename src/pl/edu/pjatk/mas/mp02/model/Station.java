package pl.edu.pjatk.mas.mp02.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;
import pl.edu.pjatk.mas.mp02.model.association.Association;

@Data
@EqualsAndHashCode(callSuper = false)
@Association(target = Ticket.class, id = Ticket.START_STATION_ID)
@Association(target = Ticket.class, id = Ticket.STOP_STATION_ID)
@RequiredArgsConstructor
public class Station extends AssociatedObject {
    private final String name;
}
