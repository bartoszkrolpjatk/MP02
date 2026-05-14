package pl.edu.pjatk.mas.mp02.model.carriage;

import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.Seat;
import pl.edu.pjatk.mas.mp02.model.Train;
import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;
import pl.edu.pjatk.mas.mp02.model.association.Association;

@RequiredArgsConstructor
@Association(target = Seat.class, min = 1)
@Association(target = Train.class, max = 1)
public class Carriage extends AssociatedObject {
    private final Integer carriageNumber;
    private final CarriageType carriageType;
    private final CarriageClass carriageClass;
}
