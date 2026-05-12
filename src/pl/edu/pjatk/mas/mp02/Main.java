package pl.edu.pjatk.mas.mp02;

import pl.edu.pjatk.mas.mp02.model.Seat;
import pl.edu.pjatk.mas.mp02.model.Station;
import pl.edu.pjatk.mas.mp02.model.Ticket;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationAlreadyExistsException;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationDoesNotExistException;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationMultiplicityException;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws AssociationAlreadyExistsException, AssociationDoesNotExistException, AssociationMultiplicityException {

        if (true) {
            Seat seat = new Seat(1, true);
            Ticket ticket = new Ticket(null, 200, LocalDateTime.now(), "Michał", "Tomaszewski");
            ticket.link(seat);

            ticket.printAssociations();
            seat.printAssociations();

            seat.unlink(ticket);
            ticket.printAssociations();
        }

        if (true) {
            Ticket ticket = new Ticket(null, 200, LocalDateTime.now(), "Michał", "Tomaszewski");
            Station station = new Station("Warszawa Zachodnia");
            ticket.link(station, Ticket.START_STATION_ID);
            ticket.link(station, Ticket.STOP_STATION_ID);

            ticket.unlink(station, Ticket.START_STATION_ID);
            ticket.printAssociations();
            station.printAssociations();
            ticket.unlink(station, Ticket.STOP_STATION_ID);
            ticket.printAssociations();
            station.printAssociations();
        }

        if (true) {
            Ticket wschodniaZachodnia = new Ticket(null, 200, LocalDateTime.now(), "Michał", "Tomaszewski");
            Station wschodnia = new Station("Wwa Wschodnia");
            Station zachodnia = new Station("Wwa Zachodnia");
            wschodniaZachodnia.link(wschodnia, Ticket.START_STATION_ID);
            wschodniaZachodnia.link(zachodnia, Ticket.STOP_STATION_ID);

            Ticket wschodniaWlochy = new Ticket(null, 200, LocalDateTime.now(), "Michał", "Tomaszewski");
            Station wlochy = new Station("Wwa Włochy");
            wschodniaWlochy.link(wlochy, Ticket.START_STATION_ID);
            wschodniaWlochy.link(wlochy, Ticket.STOP_STATION_ID);

            wschodnia.printAssociations();
        }
    }
}
//todo: wsparcie asocjacji kwalifikowanej
//todo: wsparcie kompozycji
//todo: oddzielna klasa @Qualified? AbstractProcessor?
//todo: wsparcie klasy asocjacji
//todo: bardziej rozbudowany system cen biletów na podstawie klasy wagonu
//todo: dodać pole description do asocjacji?
//todo: pobranie listy asocjacji
