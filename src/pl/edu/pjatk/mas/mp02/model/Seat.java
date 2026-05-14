package pl.edu.pjatk.mas.mp02.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;
import pl.edu.pjatk.mas.mp02.model.association.Association;
import pl.edu.pjatk.mas.mp02.model.carriage.Carriage;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Association(target = Ticket.class)
@Association(target = Carriage.class, isComposition = true, min = 1, max = 1)
public class Seat extends AssociatedObject {
    private final int seatNumber;
    private final boolean window;
}
