package pl.edu.pjatk.mas.mp02.model;

import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;
import pl.edu.pjatk.mas.mp02.model.association.Association;

@Association(targetType = Ticket.class)
public class Seat extends AssociatedObject {
    private final int seatNumber;
    private final boolean window;

    public Seat(int seatNumber, boolean window) {
        super();
        this.seatNumber = seatNumber;
        this.window = window;
    }
}
