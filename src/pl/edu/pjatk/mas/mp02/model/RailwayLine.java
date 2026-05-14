package pl.edu.pjatk.mas.mp02.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;
import pl.edu.pjatk.mas.mp02.model.association.Association;

@EqualsAndHashCode(callSuper = false)
@Data
@RequiredArgsConstructor
@Association(target = Train.class, payload = TrainService.class)
@Association(target = Station.class, payload = StationStop.class, min = 2)
public class RailwayLine extends AssociatedObject {
    private final Integer line;
}
