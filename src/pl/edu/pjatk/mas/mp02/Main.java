package pl.edu.pjatk.mas.mp02;

import pl.edu.pjatk.mas.mp02.model.RailwayLine;
import pl.edu.pjatk.mas.mp02.model.Seat;
import pl.edu.pjatk.mas.mp02.model.Station;
import pl.edu.pjatk.mas.mp02.model.Ticket;
import pl.edu.pjatk.mas.mp02.model.Train;
import pl.edu.pjatk.mas.mp02.model.TrainService;
import pl.edu.pjatk.mas.mp02.model.association.Payload;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationAlreadyExistsException;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationException;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationMultiplicityException;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.PayloadOperationException;
import pl.edu.pjatk.mas.mp02.model.carriage.Carriage;
import pl.edu.pjatk.mas.mp02.model.carriage.CarriageClass;
import pl.edu.pjatk.mas.mp02.model.carriage.CarriageType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws AssociationException {

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

        if (false) {
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

        if (false) {
            Train train = new Train("EIC8021", "Intercity", "EIC");
            Carriage carriage1 = new Carriage(15, CarriageType.STANDARD, CarriageClass.SECOND);
            Carriage carriage2 = new Carriage(16, CarriageType.STANDARD, CarriageClass.SECOND);
            carriage1.link(train);
            carriage2.link(train);

            train.printAssociations();
            carriage1.printAssociations();

            var foundCarriage = (Carriage) train.findByQualifier(Carriage.class, 15).get();
            System.out.println(foundCarriage);
        }

        if (false) {
            Train ic = new Train("IC1023", "Intercity", "IC");
            Train eic = new Train("EIC4121", "Intercity", "EIC");
            Carriage carriage1 = new Carriage(15, CarriageType.STANDARD, CarriageClass.SECOND);
            Carriage carriage2 = new Carriage(15, CarriageType.STANDARD, CarriageClass.SECOND);
            Carriage carriage3 = new Carriage(16, CarriageType.RESTAURANT, CarriageClass.SECOND);

            ic.link(carriage1);
            try {
                carriage2.link(ic);
            } catch (AssociationAlreadyExistsException e) {
                System.err.println("Carriage2 has the same identifier as carriage1");
            }
            try {
                carriage1.link(eic);
            } catch (AssociationMultiplicityException e) {
                System.err.println("Carriage1 is already linked to ic");
            }
            ic.link(carriage3);
            ic.printAssociations();

            ic.unlink(carriage1);
            ic.printAssociations();
        }

        if (false) {
            Train eic = new Train("EIC4121", "Intercity", "EIC");
            Carriage carriage = new Carriage(15, CarriageType.STANDARD, CarriageClass.SECOND);
            eic.link(carriage);

            RailwayLine railwayLine = new RailwayLine(19);
            TrainService trainService1 = new TrainService(LocalDateTime.now(), LocalDateTime.now().plusHours(3));
            TrainService trainService2 = new TrainService(LocalDateTime.now(), LocalDateTime.now().plusHours(4));

            eic.link(railwayLine, trainService1);
            try {
                eic.addPayload(railwayLine, trainService1);
            } catch (PayloadOperationException e) {
                System.err.println("trainService1 is already linked to eic and railwayLine");
            }
            eic.addPayload(railwayLine, trainService2);

            eic.printAssociations();
            railwayLine.printAssociations();

            eic.unlink(railwayLine);
            eic.printAssociations();
        }

        if (false) {
            Train eic = new Train("EIC4121", "Intercity", "EIC");
            RailwayLine railwayLine = new RailwayLine(19);
            TrainService trainService1 = new TrainService(LocalDateTime.now(), LocalDateTime.now().plusHours(3));

            eic.link(railwayLine, trainService1);

            eic.printAssociations();

            eic.removePayload(railwayLine, trainService1);

            eic.printAssociations();
        }

        if (false) {
            Train eic = new Train("EIC4121", "Intercity", "EIC");
            RailwayLine railwayLine10 = new RailwayLine(10);
            RailwayLine railwayLine11 = new RailwayLine(11);
            eic.link(railwayLine10);
            eic.link(railwayLine11);

            List<RailwayLine> links = eic.getLinks(RailwayLine.class);
            System.out.println(links);
        }

        if (true) {
            System.out.println("--- TESTOWANIE METODY getPayloads ---");

            // Przygotowanie danych testowych
            Train eic = new Train("EIC4121", "Intercity", "EIC");
            RailwayLine railwayLine = new RailwayLine(19);
            RailwayLine emptyLine = new RailwayLine(42);
            Carriage carriage = new Carriage(15, CarriageType.STANDARD, CarriageClass.SECOND);

            TrainService morningService = new TrainService(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(4));
            TrainService eveningService = new TrainService(LocalDateTime.now().plusHours(10), LocalDateTime.now().plusHours(13));

            try {
                // Konfiguracja początkowych powiązań
                eic.link(railwayLine, morningService);
                eic.addPayload(railwayLine, eveningService);
                eic.link(carriage);
            } catch (AssociationException e) {
                System.err.println("Błąd podczas konfiguracji testu: " + e.getMessage());
            }

            // ---------------------------------------------------------
            // SCENARIUSZ 1: Poprawne pobranie payloadów (Sukces)
            // ---------------------------------------------------------
            System.out.println("\n1. Test poprawnego pobrania payloadów (Train -> RailwayLine):");
            try {
                // Wnioskowanie typów (Type Inference) zadziała tutaj automatycznie!
                Set<TrainService> services = eic.getPayloads(railwayLine);
                System.out.println("Sukces! Pobrano kursy (" + services.size() + "):");
                services.forEach(System.out::println);
            } catch (AssociationException e) {
                System.err.println("Niespodziewany błąd: " + e.getMessage());
            }

            // ---------------------------------------------------------
            // SCENARIUSZ 2: Walidacja - Brak powiązania w mapie
            // ---------------------------------------------------------
            System.out.println("\n2. Test walidacji - Brak powiązania z linią:");
            try {
                Set<TrainService> emptyServices = eic.getPayloads(emptyLine);
                System.out.println("Pobrano kursy: " + emptyServices);
            } catch (AssociationException e) {
                // Oczekujemy wypisania błędu AssociationsDoNotExistException
                System.err.println("Złapano oczekiwany wyjątek: " + e.getMessage());
            }

            // ---------------------------------------------------------
            // SCENARIUSZ 3: Walidacja - Asocjacja bez deklaracji payload
            // ---------------------------------------------------------
            System.out.println("\n3. Test walidacji - Brak wsparcia dla payload (Train -> Carriage):");
            try {
                Set<Payload> carriagePayloads = eic.getPayloads(carriage);
                System.out.println("Pobrano: " + carriagePayloads);
            } catch (AssociationException e) {
                // Oczekujemy wypisania błędu PayloadOperationException
                System.err.println("Złapano oczekiwany wyjątek: " + e.getMessage());
            }

            System.out.println("-------------------------------------");
        }
    }
}
//todo: relink (nie można przepiąć kompozycji)
//todo: refactor walidacji w cache holder
//todo: bag constraint na klasie asocjacji
//todo: metoda removePayload może usunąć ostatni payload z Setu, wtedy mamy asocjację bez żadnego payloadu - dodać walidację