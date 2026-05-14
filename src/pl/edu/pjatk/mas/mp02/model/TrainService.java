package pl.edu.pjatk.mas.mp02.model;

import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.association.Association;
import pl.edu.pjatk.mas.mp02.model.association.Payload;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TrainService implements Payload {
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;
}
