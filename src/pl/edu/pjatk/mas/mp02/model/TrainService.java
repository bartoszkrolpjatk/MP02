package pl.edu.pjatk.mas.mp02.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.association.Payload;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class TrainService implements Payload {
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;
}
