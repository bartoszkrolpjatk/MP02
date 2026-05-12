package pl.edu.pjatk.mas.mp02.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;
import pl.edu.pjatk.mas.mp02.model.association.Association;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Association(targetType = Ticket.class)
public class Seat extends AssociatedObject {
    private final int seatNumber;
    private final boolean window;
}
