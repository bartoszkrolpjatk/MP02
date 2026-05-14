package pl.edu.pjatk.mas.mp02.model;

import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;
import pl.edu.pjatk.mas.mp02.model.association.Association;

@RequiredArgsConstructor
@Association(target = Train.class, payload = TrainService.class)
public class RailwayLine extends AssociatedObject {
    private final Integer line;
}
