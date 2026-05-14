package pl.edu.pjatk.mas.mp02;

import pl.edu.pjatk.mas.mp02.model.RailwayLine;
import pl.edu.pjatk.mas.mp02.model.Seat;
import pl.edu.pjatk.mas.mp02.model.Station;
import pl.edu.pjatk.mas.mp02.model.StationStop;
import pl.edu.pjatk.mas.mp02.model.Ticket;
import pl.edu.pjatk.mas.mp02.model.Train;
import pl.edu.pjatk.mas.mp02.model.TrainService;
import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;
import pl.edu.pjatk.mas.mp02.model.association.Association;
import pl.edu.pjatk.mas.mp02.model.association.Payload;
import pl.edu.pjatk.mas.mp02.model.association.Qualifier;
import pl.edu.pjatk.mas.mp02.model.association.exception.declaration.IncorrectCompositionUsageException;
import pl.edu.pjatk.mas.mp02.model.association.exception.declaration.IncorrectMultiplicitiesAnnotationDeclarationException;
import pl.edu.pjatk.mas.mp02.model.association.exception.declaration.IncorrectPayloadParamDeclaration;
import pl.edu.pjatk.mas.mp02.model.association.exception.declaration.IncorrectQualifierDeclaration;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationAlreadyExistsException;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationIsNotQualifiedException;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationMultiplicityException;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationsDoNotExistException;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.PayloadOperationException;
import pl.edu.pjatk.mas.mp02.model.carriage.Carriage;
import pl.edu.pjatk.mas.mp02.model.carriage.CarriageClass;
import pl.edu.pjatk.mas.mp02.model.carriage.CarriageType;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // =========================================================================
        // TEST 1: SYMULACJA PEŁNEGO SYSTEMU (Rozszerzony graf)
        // =========================================================================
        {
            System.out.println(">>> TEST 1: SYMULACJA PEŁNEGO SYSTEMU <<<");
            System.out.println("Cel: Utworzenie poprawnego, rozbudowanego grafu dla wszystkich asocjacji w systemie.");
            try {
                Train pendolino = new Train("EIP123", "PKP Intercity", "EIP");

                Carriage carriage1 = new Carriage(1, CarriageType.STANDARD, CarriageClass.FIRST);
                Carriage carriage2 = new Carriage(2, CarriageType.STANDARD, CarriageClass.SECOND);

                Seat seat12 = new Seat(12, true);
                Seat seat14 = new Seat(14, false);

                Ticket ticketJan = new Ticket(0.0, 150.0, LocalDateTime.now(), "Jan", "Kowalski");
                Ticket ticketAnna = new Ticket(51.0, 75.0, LocalDateTime.now(), "Anna", "Nowak");

                Station wwaCentralna = new Station("Warszawa Centralna");
                Station krakowGlowny = new Station("Kraków Główny");
                Station gdanskGlowny = new Station("Gdańsk Główny");

                RailwayLine line8 = new RailwayLine(8);

                // 2. Inicjalizacja klas asocjacji (Payloads)
                TrainService pendolinoService = new TrainService(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3));
                StationStop wwaStop = new StationStop(LocalDateTime.now().plusHours(1), 1);
                StationStop krkStop = new StationStop(LocalDateTime.now().plusHours(3), 2);
                StationStop gdaStop = new StationStop(LocalDateTime.now().plusHours(5), 3);

                System.out.println("\n[OPERACJA] Łączenie rozbudowanego grafu...");

                // Łączenie Pociągu z Wagonami (Kwalifikatory zadziałają automatycznie dla numerów wagonów)
                pendolino.link(carriage1);
                pendolino.link(carriage2);

                // Łączenie Wagonów z Miejscami (Kompozycje)
                carriage1.link(seat12);
                carriage2.link(seat14);

                // Łączenie Biletów z Miejscami i Stacjami
                ticketJan.link(seat12);
                ticketJan.link(wwaCentralna, Ticket.START_STATION_ID);
                ticketJan.link(krakowGlowny, Ticket.STOP_STATION_ID);

                ticketAnna.link(seat14);
                ticketAnna.link(wwaCentralna, Ticket.START_STATION_ID);
                ticketAnna.link(gdanskGlowny, Ticket.STOP_STATION_ID);

                // Łączenie Pociągu z Linią (Payload)
                pendolino.link(line8, pendolinoService);

                // Łączenie Linii ze Stacjami (Payload, min = 2)
                line8.link(wwaCentralna, wwaStop);
                line8.link(krakowGlowny, krkStop);
                line8.link(gdanskGlowny, gdaStop);

                System.out.println("[SUKCES] Rozbudowany graf zbudowany bez rzucania wyjątków.");

                System.out.println("\n[STAN KOŃCOWY] Zrzut mapy asocjacji dla Biletu Jana:");
                ticketJan.printAssociations();

                System.out.println("\n[STAN KOŃCOWY] Zrzut mapy asocjacji dla Linii Kolejowej (nr 8):");
                line8.printAssociations();

                System.out.println("\n[STAN KOŃCOWY] Zrzut mapy asocjacji dla Pociągu (Pendolino):");
                pendolino.printAssociations();

            } catch (Exception e) {
                System.err.println("[AWARIA TESTU 1] Nieoczekiwany wyjątek: " + e.getClass().getSimpleName() + " | Wiadomość: " + e.getMessage());
            }
            System.out.println("---------------------------------------------------------\n");
        }

        // =========================================================================
        // TEST 2: ASOCJACJE KWALIFIKOWANE (BŁĘDY I ZABEZPIECZENIA)
        // =========================================================================
        {
            System.out.println(">>> TEST 2: ASOCJACJE KWALIFIKOWANE <<<");
            System.out.println("Cel: Próba naruszenia zasad asocjacji kwalifikowanych (duplikaty kluczy, brak adnotacji, błędy definicji).");
            try {
                Train pendolino = new Train("EIP123", "PKP Intercity", "EIP");
                Carriage carriageNum7 = new Carriage(7, CarriageType.STANDARD, CarriageClass.SECOND);
                Carriage anotherCarriageNum7 = new Carriage(7, CarriageType.BAR, CarriageClass.FIRST);
                Seat seat1 = new Seat(1, true);

                System.out.println("\n[OPERACJE BŁĘDNE - DZIAŁANIE PROGRAMU]");

                // ---------------------------------------------------------
                // Scenariusz A: Szukanie po kwalifikatorze w relacji, która go nie ma
                // ---------------------------------------------------------
                try {
                    System.out.print("1. Szukanie po kwalifikatorze dla relacji bez @Qualifier (Carriage -> Seat)... ");
                    carriageNum7.link(seat1); // Ta asocjacja istnieje, ale nie jest kwalifikowana
                    carriageNum7.findByQualifier(Seat.class, 1);
                    System.err.println("[BŁĄD] Wyszukiwanie powinno rzucić wyjątek!");
                } catch (AssociationIsNotQualifiedException e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

                // ---------------------------------------------------------
                // Scenariusz B: Kolizja kwalifikatorów (dwa wagony o tym samym numerze)
                // ---------------------------------------------------------
                try {
                    System.out.print("2. Dodanie dwóch obiektów o tym samym kluczu kwalifikatora do Pociągu... ");
                    pendolino.link(carriageNum7);
                    pendolino.link(anotherCarriageNum7); // Ten sam numer wagonu = ten sam klucz w mapie!
                    System.err.println("[BŁĄD] Drugie połączenie powinno rzucić wyjątek kolizji klucza!");
                } catch (AssociationAlreadyExistsException e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

                System.out.println("\n[OPERACJE BŁĘDNE - DEKLARACJA ADNOTACJI]");

                // ---------------------------------------------------------
                // Scenariusz C: Kwalifikator wskazuje na nieistniejące pole
                // ---------------------------------------------------------
                @Association(target = Train.class, qualifier = @Qualifier(fieldName = "poleKtoreNieIstnieje", type = String.class))
                class MissingQualifierFieldClass extends AssociatedObject {
                }

                try {
                    System.out.print("3. Skanowanie kwalifikatora z nieistniejącym polem... ");
                    new MissingQualifierFieldClass().link(new Train("TLK99", "PKP", "TLK"));
                    System.err.println("[BŁĄD] Refleksja powinna zablokować to połączenie!");
                } catch (IncorrectQualifierDeclaration e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

                // ---------------------------------------------------------
                // Scenariusz D: Kwalifikator ma zły typ (w Train "identifier" to String, a tu wpisujemy Integer)
                // ---------------------------------------------------------
                @Association(target = Train.class, qualifier = @Qualifier(fieldName = "identifier", type = Integer.class))
                class WrongQualifierTypeClass extends AssociatedObject {
                }

                try {
                    System.out.print("4. Skanowanie kwalifikatora z niezgodnym typem zmiennej... ");
                    new WrongQualifierTypeClass().link(new Train("TLK99", "PKP", "TLK"));
                    System.err.println("[BŁĄD] Refleksja powinna wyłapać niezgodność typów!");
                } catch (IncorrectQualifierDeclaration e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

            } catch (Exception e) {
                System.err.println("[AWARIA TESTU 2] Nieoczekiwany wyjątek: " + e.getClass().getSimpleName() + " | Wiadomość: " + e.getMessage());
            }
            System.out.println("---------------------------------------------------------\n");
        }

        // =========================================================================
        // TEST 3: WALIDACJA LIMITÓW MULTIPLICITY (MIN / MAX) ORAZ DUPLIKATÓW
        // =========================================================================
        {
            System.out.println(">>> TEST 3: LIMITÓW MULTIPLICITY I DUPLIKATÓW <<<");
            System.out.println("Cel: Wywołanie wyjątków przekroczenia limitów max/min, duplikatów oraz błędów w deklaracjach.");
            try {
                Carriage carriage1 = new Carriage(1, CarriageType.STANDARD, CarriageClass.SECOND);
                Train intercity = new Train("IC100", "PKP Intercity", "IC");
                Train tlk = new Train("TLK200", "PKP Intercity", "TLK");

                Seat seatA = new Seat(10, true);
                Ticket ticketJan = new Ticket(0.0, 50.0, LocalDateTime.now(), "Jan", "Kowalski");

                System.out.println("\n[OPERACJE BŁĘDNE - DZIAŁANIE PROGRAMU]");

                // ---------------------------------------------------------
                // Scenariusz A: Przekroczenie limitu MAX (max = 1)
                // ---------------------------------------------------------
                carriage1.link(intercity); // Przypinamy wagon do pierwszego pociągu (sukces)
                try {
                    System.out.print("1. Próba przypięcia tego samego wagonu do drugiego pociągu (przekroczenie max=1)... ");
                    carriage1.link(tlk);
                    System.err.println("[BŁĄD] Połączenie nie powinno się udać!");
                } catch (AssociationMultiplicityException e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

                // ---------------------------------------------------------
                // Scenariusz B: Duplikat istniejącego powiązania
                // ---------------------------------------------------------
                try {
                    System.out.print("2. Próba ponownego zlinkowania tych samych obiektów (duplikat)... ");
                    intercity.link(carriage1); // Ten sam pociąg i ten sam wagon
                    System.err.println("[BŁĄD] Połączenie nie powinno się udać!");
                } catch (AssociationAlreadyExistsException e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

                // ---------------------------------------------------------
                // Scenariusz C: Zejście poniżej limitu MIN podczas odpinania
                // ---------------------------------------------------------
                ticketJan.link(seatA); // Bilet musi mieć min=1 miejsce w modelu
                try {
                    System.out.print("3. Próba odpięcia miejsca od biletu (naruszenie min=1)... ");
                    ticketJan.unlink(seatA);
                    System.err.println("[BŁĄD] Odpięcie nie powinno się udać!");
                } catch (AssociationMultiplicityException e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

                System.out.println("\n[OPERACJE BŁĘDNE - DEKLARACJA ADNOTACJI]");

                // ---------------------------------------------------------
                // Scenariusz D: Nielogiczne limity (min > max)
                // ---------------------------------------------------------
                @Association(target = Station.class, min = 5, max = 2)
                class BadMultiplicityClass extends AssociatedObject {
                }

                try {
                    System.out.print("4. Skanowanie klasy z zadeklarowanym min > max... ");
                    new BadMultiplicityClass().link(new Station("Testowa Stacja"));
                    System.err.println("[BŁĄD] Refleksja powinna zablokować to powiązanie przy pierwszym skanowaniu!");
                } catch (IncorrectMultiplicitiesAnnotationDeclarationException e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

                System.out.println("\n[OPERACJE POPRAWNE - DZIAŁANIE PROGRAMU]");

                // ---------------------------------------------------------
                // Scenariusz E: Prawidłowe odpinanie wagonów
                // ---------------------------------------------------------
                System.out.println("4. Testowanie poprawnego odpinania wagonów (bez naruszania limitów)... ");
                Carriage carriage2 = new Carriage(2, CarriageType.RESTAURANT, CarriageClass.FIRST);
                Carriage carriage3 = new Carriage(3, CarriageType.STANDARD, CarriageClass.SECOND);

                // Dopinammy dodatkowe wagony
                System.out.println("   -> Podpinanie wagonów nr 2 i 3 do pociągu IC100...");
                intercity.link(carriage2);
                intercity.link(carriage3);

                System.out.println("\n[STAN: Pociąg z 3 wagonami]");
                intercity.printAssociations();

                // Odpinamy carriage2 i carriage3.
                // Wagon ma domyślnie min=0 dla pociągu (może istnieć sam).
                // Pociąg ma min=1 dla wagonów, ale zostawiamy mu carriage1, więc to też jest legalne!
                System.out.println("   -> Odpinanie wagonów nr 2 i 3...");
                intercity.unlink(carriage2);
                carriage3.unlink(intercity); // Test odpięcia z drugiej strony

                System.out.println("\n  -> [SUKCES] Odpięto wagony 2 i 3. Pociąg (z zachowanym wagonem 1) i luźne wagony istnieją bezpiecznie w pamięci.");

                System.out.println("\n[STAN: Pociąg po odpięciu - pozostał tylko 1 wagon]");
                intercity.printAssociations();

                System.out.println("\n[STAN: Wagon nr 1 - nadal przypięty do pociągu]");
                carriage1.printAssociations();

                System.out.println("\n[STAN: Wagon nr 2 - odpięty, pusta mapa]");
                carriage2.printAssociations();

                System.out.println("\n[STAN: Wagon nr 3 - odpięty, pusta mapa]");
                carriage3.printAssociations();

            } catch (Exception e) {
                System.err.println("[AWARIA TESTU 3] Nieoczekiwany wyjątek: " + e.getClass().getSimpleName() + " | Wiadomość: " + e.getMessage());
            }
            System.out.println("---------------------------------------------------------\n");
        }

        // =========================================================================
        // TEST 4: KOMPOZYCJA (isComposition)
        // =========================================================================
        {
            System.out.println(">>> TEST 4: KOMPOZYCJA (isComposition = true) <<<");
            System.out.println("Cel: Weryfikacja restrykcyjnych reguł cyklu życia kompozycji (min=1, max=1) oraz błędów definicji.");
            try {
                Carriage carriageFirstClass = new Carriage(1, CarriageType.COMPARTMENT, CarriageClass.FIRST);
                Carriage carriageSecondClass = new Carriage(2, CarriageType.STANDARD, CarriageClass.SECOND);
                Seat seatWindow = new Seat(42, true);

                System.out.println("\n[OPERACJE POPRAWNE - DZIAŁANIE PROGRAMU]");
                System.out.println("1. Przypięcie Miejsca do Wagonu (tworzenie kompozycji)...");
                seatWindow.link(carriageFirstClass);
                System.out.println("  -> [SUKCES] Miejsce pomyślnie stało się częścią Wagonu nr 1.");

                System.out.println("\n[STAN: Miejsce w Wagonie nr 1]");
                seatWindow.printAssociations();

                System.out.println("\n[OPERACJE BŁĘDNE - DZIAŁANIE PROGRAMU]");

                // ---------------------------------------------------------
                // Scenariusz A: Próba osierocenia kompozycji
                // ---------------------------------------------------------
                try {
                    System.out.print("2. Próba odpięcia Miejsca od Wagonu (niszczenie powiązania bez niszczenia obiektu)... ");
                    seatWindow.unlink(carriageFirstClass);
                    System.err.println("[BŁĄD] Odpięcie nie powinno się udać!");
                } catch (AssociationMultiplicityException e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

                // ---------------------------------------------------------
                // Scenariusz B: Próba przepięcia (relink) do innej całości
                // ---------------------------------------------------------
                try {
                    System.out.print("3. Próba przypięcia tego samego Miejsca do Wagonu nr 2... ");
                    seatWindow.link(carriageSecondClass);
                    System.err.println("[BŁĄD] Połączenie nie powinno się udać!");
                } catch (AssociationMultiplicityException e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

                System.out.println("\n[STAN: Nienaruszona kompozycja]");
                System.out.println("Miejsce powinno pozostać bezpiecznie przypięte tylko i wyłącznie do Wagonu 1:");
                seatWindow.printAssociations();

                System.out.println("\n[OPERACJE BŁĘDNE - DEKLARACJA ADNOTACJI]");

                // ---------------------------------------------------------
                // Scenariusz C: Źle zdefiniowane limity dla kompozycji
                // ---------------------------------------------------------
                @Association(target = Ticket.class, isComposition = true, min = 0, max = 2)
                class BadCompositionClass extends AssociatedObject {
                }

                try {
                    System.out.print("4. Skanowanie klasy z isComposition=true, ale zepsutymi limitami (min=0, max=2 zamiast 1)... ");
                    new BadCompositionClass().link(new Ticket(0.0, 0.0, LocalDateTime.now(), "A", "B"));
                    System.err.println("[BŁĄD] Refleksja powinna natychmiast zablokować to powiązanie!");
                } catch (IncorrectCompositionUsageException e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

            } catch (Exception e) {
                System.err.println("[AWARIA TESTU 4] Nieoczekiwany wyjątek: " + e.getClass().getSimpleName() + " | Wiadomość: " + e.getMessage());
            }
            System.out.println("---------------------------------------------------------\n");
        }

        // =========================================================================
        // TEST 5: KLASY ASOCJACJI (PAYLOAD) - DZIAŁANIE I WALIDACJA
        // =========================================================================
        {
            System.out.println(">>> TEST 5: KLASY ASOCJACJI <<<");
            System.out.println("Cel: Weryfikacja operacji na klasach asocjacji, bezpieczeństwa typów oraz błędów symetrii deklaracji.");
            try {
                Train expressTrain = new Train("EIP-PREMIUM", "PKP Intercity", "EIP");
                RailwayLine mainLine = new RailwayLine(4);
                RailwayLine branchLine = new RailwayLine(22);

                TrainService morningService = new TrainService(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(5));
                StationStop invalidAssociationClassObj = new StationStop(LocalDateTime.now().plusHours(1), 1);

                System.out.println("\n[OPERACJE POPRAWNE - DZIAŁANIE PROGRAMU]");
                System.out.println("1. Prawidłowe zapięcie asocjacji z jednoczesnym przekazaniem klasy asocjacji (Pociąg <-> mainLine)...");
                expressTrain.link(mainLine, morningService);
                System.out.println("  -> [SUKCES] Utworzono asocjację zawierającą obiekt klasy asocjacji.");

                System.out.println("2. Prawidłowe zapięcie asocjacji BEZ instancji klasy asocjacji (Pociąg <-> branchLine)...");
                expressTrain.link(branchLine);
                System.out.println("  -> [SUKCES] Utworzono asocjację (pusty zbiór w AssociationEntry).");

                System.out.println("\n[STAN: Pociąg po dodaniu obu asocjacji (jedna z klasą asocjacji, druga bez)]");
                expressTrain.printAssociations();

                System.out.println("\n[OPERACJE BŁĘDNE - DZIAŁANIE PROGRAMU]");

                // ---------------------------------------------------------
                // Scenariusz A: Przekazanie błędnego typu klasy asocjacji przy łączeniu
                // ---------------------------------------------------------
                try {
                    System.out.print("3. Próba utworzenia asocjacji z użyciem obiektu StationStop (zamiast TrainService)... ");
                    RailwayLine errorLine = new RailwayLine(99);
                    expressTrain.link(errorLine, invalidAssociationClassObj);
                    System.err.println("[BŁĄD] Utworzenie asocjacji nie powinno się udać!");
                } catch (PayloadOperationException e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

                // ---------------------------------------------------------
                // Scenariusz B: Próba usunięcia instancji klasy asocjacji z nieistniejącej relacji
                // ---------------------------------------------------------
                try {
                    System.out.print("4. Próba usunięcia obiektu klasy asocjacji z Linii, z którą Pociąg nie ma relacji... ");
                    RailwayLine notLinkedLine = new RailwayLine(100);
                    expressTrain.removePayload(notLinkedLine, morningService);
                    System.err.println("[BŁĄD] Operacja nie powinna się udać!");
                } catch (PayloadOperationException e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

                System.out.println("\n[OPERACJE BŁĘDNE - DEKLARACJA ADNOTACJI]");

                // ---------------------------------------------------------
                // Scenariusz C: Klasa asocjacji wskazująca na samą siebie
                // ---------------------------------------------------------
                @Association(target = Carriage.class, payload = SelfAssociationClass.class)
                class SelfAssociationClass extends AssociatedObject implements Payload {
                }

                try {
                    System.out.print("5. Skanowanie klasy asocjacji, która odwołuje się do samej siebie... ");
                    new SelfAssociationClass().link(new Carriage(1, CarriageType.STANDARD, CarriageClass.SECOND), new SelfAssociationClass());
                    System.err.println("[BŁĄD] Refleksja powinna zablokować to powiązanie!");
                } catch (IncorrectPayloadParamDeclaration e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

                // ---------------------------------------------------------
                // Scenariusz D: Asymetria w definicji klasy asocjacji (Brak lustrzanego odbicia)
                // ---------------------------------------------------------
                @Association(target = Carriage.class, payload = StationStop.class)
                class AsymmetricAssociationClass extends AssociatedObject {
                }

                try {
                    System.out.print("6. Skanowanie jednostronnie zadeklarowanej klasy asocjacji (asymetria)... ");
                    new AsymmetricAssociationClass().link(new Carriage(1, CarriageType.STANDARD, CarriageClass.SECOND), new StationStop(LocalDateTime.now(), 1));
                    System.err.println("[BŁĄD] Refleksja powinna zablokować to powiązanie ze względu na brak lustrzanej deklaracji w Carriage!");
                } catch (IncorrectPayloadParamDeclaration e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

            } catch (Exception e) {
                System.err.println("[AWARIA TESTU 5] Nieoczekiwany wyjątek: " + e.getClass().getSimpleName() + " | Wiadomość: " + e.getMessage());
            }
            System.out.println("---------------------------------------------------------\n");
        }

        // =========================================================================
        // TEST 6: ODCZYTY, IZOLACJA ID ORAZ PUSTE RELACJE
        // =========================================================================
        {
            System.out.println(">>> TEST 6: ODCZYTY, IZOLACJA ID ORAZ PUSTE RELACJE <<<");
            System.out.println("Cel: Sprawdzenie izolacji wielu asocjacji między tymi samymi klasami (ID) oraz zachowania przy braku powiązań.");
            try {
                Ticket ticket = new Ticket(0.0, 120.0, LocalDateTime.now(), "Piotr", "Zalewski");
                Station startStation = new Station("Warszawa Wschodnia");
                Station stopStation = new Station("Gdynia Główna");

                Train train = new Train("TLK-MORZE", "PKP", "TLK");
                Carriage carriage = new Carriage(10, CarriageType.STANDARD, CarriageClass.SECOND);

                System.out.println("\n[OPERACJE POPRAWNE - IZOLACJA ID]");

                System.out.println("1. Podpinanie stacji początkowej i końcowej do biletu...");
                ticket.link(startStation, Ticket.START_STATION_ID);
                ticket.link(stopStation, Ticket.STOP_STATION_ID);

                // Pobieranie wyizolowanych list
                List<Station> startStations = ticket.getLinks(Station.class, Ticket.START_STATION_ID);
                List<Station> stopStations = ticket.getLinks(Station.class, Ticket.STOP_STATION_ID);

                System.out.println("   -> [SUKCES] Stacja początkowa pobrana przez getLinks: " + startStations.get(0).getName());
                System.out.println("   -> [SUKCES] Stacja końcowa pobrana przez getLinks: " + stopStations.get(0).getName());

                if (!startStations.contains(stopStation) && !stopStations.contains(startStation)) {
                    System.out.println("   -> [SUKCES] Zbiory są odseparowane w pamięci na podstawie parametru id!");
                }

                // ---------------------------------------------------------
                // Scenariusz A: getLinks na pustej relacji
                // ---------------------------------------------------------
                System.out.print("2. Próba pobrania listy wagonów z pociągu (getLinks), zanim jakikolwiek został przypięty... ");
                var emptyList = train.getLinks(Carriage.class);

                if (emptyList.isEmpty()) {
                    System.out.printf("\n  -> [SUKCES] Operacja poprawnie zwróciła pustą listę: %s zamiast rzucać wyjątkiem!\n", emptyList);
                } else {
                    System.err.println("\n  -> [BŁĄD] Oczekiwano pustej listy, ale zwrócono elementy!");
                }
                System.out.println("\n[OPERACJE BŁĘDNE - (BRAK ZAINICJOWANYCH MAP)]");
                // ---------------------------------------------------------
                // Scenariusz B: unlink na niezainicjowanej relacji
                // ---------------------------------------------------------
                try {
                    System.out.print("3. Próba odpięcia wagonu (unlink), który nigdy nie został przypięty... ");
                    train.unlink(carriage);
                    System.err.println("[BŁĄD] Operacja powinna rzucić wyjątek o braku powiązań!");
                } catch (AssociationsDoNotExistException e) {
                    System.out.println("\n  -> [SUKCES] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

                // ---------------------------------------------------------
                // Scenariusz C: getPayloads na niezainicjowanej relacji
                // ---------------------------------------------------------
                RailwayLine emptyLine = new RailwayLine(123);
                System.out.print("4. Próba odczytu klas asocjacji (getPayloads) między nowym Pociągiem a nową Linią... ");
                try {
                    var emptySet = train.getPayloads(emptyLine);
                    System.out.printf("\n  -> [SUKCES] Operacja nie powinna rzucić wyjątku o braku powiązań! Zwrócona wartość: %s (pusty set)%n", emptySet);
                } catch (PayloadOperationException e) {
                    System.err.println("\n  -> [BŁĄD] Złapano: " + e.getClass().getSimpleName() + "\n  -> Wiadomość: " + e.getMessage());
                }

            } catch (Exception e) {
                System.err.println("[AWARIA TESTU 6] Nieoczekiwany wyjątek: " + e.getClass().getSimpleName() + " | Wiadomość: " + e.getMessage());
            }
            System.out.println("---------------------------------------------------------\n");
        }

        // =========================================================================
        // TEST 7: WIELE INSTANCJI KLAS ASOCJACJI ({BAG}) ORAZ SYMETRIA USUWANIA
        // =========================================================================
        {
            System.out.println(">>> TEST 7: WIELE KLAS ASOCJACJI I SYMETRIA USUWANIA <<<");
            System.out.println("Cel: Sprawdzenie dodawania/usuwania wielu ładunków w jednej relacji oraz test całkowitego niszczenia krawędzi.");
            try {
                Train pendolino = new Train("EIP-KRAKOW", "PKP Intercity", "EIP");
                RailwayLine mainLine = new RailwayLine(4);

                TrainService morningService = new TrainService(LocalDateTime.now().plusHours(8), LocalDateTime.now().plusHours(11));
                TrainService noonService = new TrainService(LocalDateTime.now().plusHours(12), LocalDateTime.now().plusHours(15));
                TrainService eveningService = new TrainService(LocalDateTime.now().plusHours(18), LocalDateTime.now().plusHours(21));

                System.out.println("\n[OPERACJE POPRAWNE - ZBIORY ŁADUNKÓW]");

                System.out.println("1. Tworzenie asocjacji z pierwszym kursem (morningService)...");
                pendolino.link(mainLine, morningService);

                System.out.println("2. Dodawanie kolejnych kursów (noonService, eveningService) do TEJ SAMEJ asocjacji...");
                pendolino.addPayload(mainLine, noonService);
                pendolino.addPayload(mainLine, eveningService); // Kolejny ładunek na tym samym moście!

                System.out.println("  -> [SUKCES] Dodano 3 różne kursy do jednej relacji.");

                System.out.println("\n[STAN: Pociąg z trzema kursami na linii nr 4]");
                pendolino.printAssociations();

                System.out.println("\n[OPERACJE POPRAWNE - USUWANIE POJEDYNCZYCH ŁADUNKÓW]");

                System.out.println("3. Usuwanie kursu południowego (noonService)...");
                pendolino.removePayload(mainLine, noonService);

                System.out.println("  -> [SUKCES] Usunięto pojedynczy ładunek.");

                System.out.println("\n[STAN: Pociąg po usunięciu jednego z kursów (powinny zostać 2)]");
                pendolino.printAssociations();

                System.out.println("\n[OPERACJE POPRAWNE - SYMETRIA ODPIĘCIA (UNLINK)]");

                System.out.println("4. Całkowite zniszczenie asocjacji (odpięcie Pociągu od Linii)...");
                // Odpinamy od strony Pociągu. Framework powinien automatycznie wyczyścić mapę również w obiekcie mainLine!
                pendolino.unlink(mainLine);
                System.out.println("  -> [SUKCES] Wywołano metodę unlink.");

                System.out.println("5. Weryfikacja symetrii odpięcia z obu stron (oczekiwanie pustych zbiorów)...");

                var trainPayloads = pendolino.getPayloads(mainLine);
                if (trainPayloads.isEmpty()) {
                    System.out.println("  -> [SUKCES] getPayloads() dla Pociągu zwróciło pusty zbiór. (Strona Pociągu jest czysta)");
                } else {
                    System.err.println("[BŁĄD] Pociąg nadal widzi Linię! Zbiór nie jest pusty.");
                }

                var linePayloads = mainLine.getPayloads(pendolino);
                if (linePayloads.isEmpty()) {
                    System.out.println("  -> [SUKCES] getPayloads() dla Linii zwróciło pusty zbiór. (Strona Linii jest czysta - symetria zachowana!)");
                } else {
                    System.err.println("[BŁĄD] Linia nadal widzi Pociąg! Zbiór nie jest pusty.");
                }

            } catch (Exception e) {
                System.err.println("[AWARIA TESTU 7] Nieoczekiwany wyjątek: " + e.getClass().getSimpleName() + " | Wiadomość: " + e.getMessage());
            }
            System.out.println("---------------------------------------------------------\n");
        }
    }
}
//todo: relink (pamiętać, że nie można przepiąć kompozycji)
//todo: refactor walidacji w cache holder
//todo: bag constraint na klasie asocjacji - aktualnie każda klasa asocjacji jest bag
//todo: metoda removePayload może usunąć ostatni payload z Setu, wtedy mamy asocjację bez żadnego payloadu - dodać walidację