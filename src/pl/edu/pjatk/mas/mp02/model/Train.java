package pl.edu.pjatk.mas.mp02.model;

import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;
import pl.edu.pjatk.mas.mp02.model.association.Association;
import pl.edu.pjatk.mas.mp02.model.association.Qualifier;
import pl.edu.pjatk.mas.mp02.model.carriage.Carriage;

@RequiredArgsConstructor
@Association(target = Carriage.class, min = 1, qualifier = @Qualifier(fieldName = "carriageNumber", type = Integer.class))
@Association(target = RailwayLine.class, payload = TrainService.class)
public class Train extends AssociatedObject {
    private final String identifier;
    private final String operator;
    private final String category;
}
