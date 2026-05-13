package pl.edu.pjatk.mas.mp02.model.carriage;

import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.Seat;
import pl.edu.pjatk.mas.mp02.model.association.Association;

@RequiredArgsConstructor
@Association(targetType = Seat.class, min = 1)
public class Carriage {
    private final CarriageType carriageType;
    private final CarriageClass carriageClass;
}
