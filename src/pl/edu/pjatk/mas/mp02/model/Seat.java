package pl.edu.pjatk.mas.mp02.model;

import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;
import pl.edu.pjatk.mas.mp02.model.association.Association;

@RequiredArgsConstructor
@Association(targetType = Ticket.class)
public class Seat extends AssociatedObject {
    private final int seatNumber;
    private final boolean window;
}
