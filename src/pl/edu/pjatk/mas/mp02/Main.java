package pl.edu.pjatk.mas.mp02;

import pl.edu.pjatk.mas.mp02.model.Seat;
import pl.edu.pjatk.mas.mp02.model.Station;
import pl.edu.pjatk.mas.mp02.model.Ticket;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationAlreadyExistsException;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationMultiplicityException;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationsDoNotExistException;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws AssociationAlreadyExistsException, AssociationMultiplicityException, AssociationsDoNotExistException {

        if (false) {
            Seat seat = new Seat(1, true);
            Ticket ticket1 = new Ticket(null, 200, LocalDateTime.now(), "Michał", "Tomaszewski");
            Ticket ticket2 = new Ticket(null, 200, LocalDateTime.now().minusDays(1), "Michał", "Tomaszewski");
            ticket1.link(seat);
            seat.link(ticket2);

            ticket1.printAssociations();
            System.out.println(seat.getLinks(Ticket.class));

        }

        if (false) {
            Ticket ticket = new Ticket(null, 200, LocalDateTime.now(), "Michał", "Tomaszewski");
            Station station = new Station("Warszawa Zachodnia");
            ticket.link(station, Ticket.START_STATION_ID);
            ticket.link(station, Ticket.STOP_STATION_ID);
        }

        if (false) {
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

        if (true) {
            Ticket ticket1 = new Ticket(0.5, 100, LocalDateTime.now(), "Bartosz", "Król");
            Ticket ticket2 = new Ticket(0.5, 100, LocalDateTime.now(), "Mariusz", "Pudzianowski");
            Station station1 = new Station("Wwa Centralna");
            Station station2 = new Station("Wwa Zachodnia");

            ticket1.link(station1, Ticket.START_STATION_ID);
            ticket1.link(station2, Ticket.STOP_STATION_ID);
            ticket2.link(station1, Ticket.START_STATION_ID);
            ticket2.link(station2, Ticket.STOP_STATION_ID);

            System.out.println(ticket1.getLinks(Station.class, Ticket.START_STATION_ID));
            System.out.println(ticket1.getLinks(Station.class, Ticket.STOP_STATION_ID));

            System.out.println(station1.getLinks(Ticket.class, Ticket.START_STATION_ID));
        }
    }
}
//todo: wsparcie asocjacji kwalifikowanej
//todo: wsparcie kompozycji
//todo: oddzielna klasa @Qualified? AbstractProcessor?
//todo: wsparcie klasy asocjacji
//todo: bardziej rozbudowany system cen biletów na podstawie klasy wagonu
//todo: dodać pole description do asocjacji?
//todo: relink
