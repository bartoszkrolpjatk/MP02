package pl.edu.pjatk.mas.mp02;

import pl.edu.pjatk.mas.mp02.model.Seat;
import pl.edu.pjatk.mas.mp02.model.Ticket;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationAlreadyExistsException;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws AssociationAlreadyExistsException {
        Seat seat = new Seat(1, true);
        Ticket ticket = new Ticket(null, 200, LocalDateTime.now(), "Michał", "Tomaszewski");
        ticket.link(seat);

        ticket.printAssociations();
        seat.printAssociations();
    }
}
//todo: wsparcie asocjacji kwalifikowanej
//todo: wsparcie kompozycji
//todo: oddzielna klasa @Qualified? AbstractProcessor?
//todo: wsparcie klasy asocjacji
//todo: wsparcie dla liczności asocjacji
//todo: bardziej rozbudowany system cen biletów na podstawie klasy wagonu
//todo: dodać pole description do asocjacji?
//todo: usuwanie asocjacji
