package pl.edu.pjatk.mas.mp02.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.edu.pjatk.mas.mp02.model.association.Payload;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class StationStop implements Payload {
    private final LocalDateTime time;
    private final Integer stationNumber;
}
