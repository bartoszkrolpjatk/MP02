package pl.edu.pjatk.mas.mp02.model;

import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;
import pl.edu.pjatk.mas.mp02.model.association.Association;

import java.time.LocalDateTime;

@Association(targetType = Seat.class)
public class Ticket extends AssociatedObject {
    private final Double discount;
    private final double basePrice;
    private final LocalDateTime purchaseDate;
    private final String passengerFirstName;
    private final String passengerLastName;

    public Ticket(Double discount, double basePrice, LocalDateTime purchaseDate, String passengerFirstName, String passengerLastName) {
        super();
        this.discount = discount;
        this.basePrice = basePrice;
        this.purchaseDate = purchaseDate;
        this.passengerFirstName = passengerFirstName;
        this.passengerLastName = passengerLastName;
    }
}
