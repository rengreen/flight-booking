package task;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

class ConsoleController {
    private Scanner scanner = new Scanner(System.in);

    private List<Flight> flights = Arrays.asList(
            new Flight("1", "Warsaw Chopin Airport WAW", "Gdańsk Lech Wałęsa GDA",
                    "01.06.2019", "06.06.2019", 1,
                    91, new BigDecimal("617.0")),
            new Flight("2", "Warsaw Chopin Airport WAW", "Amsterdam Airport Schiphol AMS",
                    "16.06.2019", "21.06.2019", 2,
                    92, new BigDecimal("2152.0")),
            new Flight("3", "Warsaw Chopin Airport WAW", "Milan Malpensa International Airport MXP",
                    "11.06.2019", "15.06.2019", 2.25,
                    49, new BigDecimal("1670.0")),
            new Flight("4", "Warsaw Chopin Airport WAW", "Rome Leonardo Da Vinci–Fiumicino Airport FCO",
                    "29.05.2019", "04.06.2019", 2.5,
                    95, new BigDecimal("1158.0")),
            new Flight("5", "Modlin Airport WMI", "Paris-Charles de Gaulle Airport CDG",
                    "31.05.2019", "04.06.2019", 2.5,
                    85, new BigDecimal("2661.0")),
            new Flight("6", "Modlin Airport WMI", "Berlin-Tegel Airport TXL",
                    "24.07.2019", "29.07.2019", 1.25,
                    99, new BigDecimal("678.0")),
            new Flight("7", "Modlin Airport WMI", "Hamburg Airport HAM",
                    "20.07.2019", "27.07.2019", 1.5,
                    53, new BigDecimal("3072.0")),
            new Flight("8", "Gdańsk Lech Wałęsa GDA", "Athens Eleftherios Venizelos International Airport ATH",
                    "16.07.2019", "21.07.2019", 2.5,
                    42, new BigDecimal("2228.0")),
            new Flight("9", "Gdańsk Lech Wałęsa GDA", "Amsterdam Airport Schiphol AMS",
                    "14.06.2019", "20.06.2019", 2,
                    88, new BigDecimal("2849.0")),
            new Flight("10", "Gdańsk Lech Wałęsa GDA", "London Luton Airport LTN",
                    "21.06.2019", "28.06.2019", 2.25,
                    86, new BigDecimal("662.0"))
    );


    void action() throws IOException, InterruptedException {
        int numberOfReservations;
        System.out.println("Witaj w Systemie Rezerwacji Biletów Lotniczych \"Who says sky is the limit?\".\n");

        //Selection of flight
        String filterOption = "w"; //All flights
        Flight selectedFlight = getFlightFromUser(flights, filterOption);

        System.out.println("Twój wybór:");
        System.out.println(selectedFlight.toString());
        String flightId = prepareReservationId(selectedFlight);

        System.out.println("Podaj ile miejsc w samolocie chcesz zarezerwować?");
        numberOfReservations = getUserNumberOfReservations();

        int sumOfPassengers = selectedFlight.getNumberOfPassengers() + numberOfReservations;

        if (sumOfPassengers > 100) {
            System.out.println("Niestety nie ma już tylu wolnych miejsc.");
            System.out.println("Czy chcesz lecieć jako steward/stewardesa? Obniży to cenę biletu o 20%.");
            boolean stewardAnswer=getYesNoAnswer();
            if (!stewardAnswer){
                System.out.println("Czy chcesz lecieć na skrzydle samolotu? Obniży to cenę biletu o 50%.");
                boolean wingAnswer=getYesNoAnswer();
                if (!wingAnswer){
                    System.out.println("Czy chcesz zmienić liczbę rezerwowanych miejsc?");
                    boolean changeAnswer=getYesNoAnswer();
                    if (changeAnswer) {
                        System.out.println("Wolne miejsca: "+(100-selectedFlight.getNumberOfPassengers()));
                        System.out.println("Podaj ile miejsc w samolocie chcesz zarezerwować?");
                        numberOfReservations = getUserNumberOfReservations();
                    } else {
                        System.out.println("Do widzenia. Polecamy się na przyszłość.");
                        Thread.sleep(2000);
                        System.exit(0);
                    }
                }
            }
        }

        if (numberOfReservations == 1) {
            System.out.println("Podaj dodatkowe informacje:");
        } else {
            System.out.println("Podaj dodatkowe informacje o pasażerach");
        }

        String filePath = "bilet-rezerwacja-" + flightId + ".txt";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 1; i <= numberOfReservations; i++) {
            System.out.println("Pasażer nr " + 1);
            Reservation reservation = new Reservation();
            reservation.setFlight(selectedFlight);

            boolean isBusinessClass = checkIfBusinessClass();
            reservation.setBusinessClass(isBusinessClass);

            String passengerType = getPassengerTypeFromUser();
            reservation.setPassengerType(passengerType);

            boolean mealReserved = checkIfMealReserved();
            reservation.setMealReserved(mealReserved);

            boolean priorityBoarding = checkIfPriorityBoarding(isBusinessClass);
            reservation.setPriorityBoarding(priorityBoarding);


            try {
                fileWriter.write("\n\nbilet: " + String.valueOf(i) + " z " + String.valueOf(numberOfReservations));
                fileWriter.write("\n\nIdentyfikator rezerwacji: " + flightId);
                fileWriter.write("\nMiejsce wylotu: " + selectedFlight.getDepartureAirport());
                fileWriter.write("\nMiejsce przylotu: " + selectedFlight.getArrivalAirport());
                fileWriter.write("\nData wylotu: " + selectedFlight.getDepartureDate());
                fileWriter.write("\nData powrotu: " + selectedFlight.getReturnDate());
                fileWriter.write("\nCena: "
                        + reservation.getFinalReservationPrice().setScale(2, RoundingMode.HALF_UP)
                        + " PLN");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (fileWriter != null) {
            if (numberOfReservations == 1) {
                System.out.println("Bilet został zapisany do pliku '" + filePath + "' w katalogu głównym projektu.");
            } else {
                System.out.println("Bilety zostały zapisane do pliku '" + filePath + "' w katalogu głównym projektu.");
            }
            fileWriter.close();
        }

    }

    private Flight getFlightFromUser(List<Flight> flights, String filterOption) {
        Set<String> validFlightAnswers = new HashSet<>();
        validFlightAnswers.add("f");
        validFlightAnswers.add("q");

        switch (filterOption) {
            case "s": //DeparturePlace
                System.out.println("Podaj nazwę lub kod lotniska wylotu: ");
                String selectedDeparturePlace = scanner.nextLine();
                System.out.println("LISTA DOSTĘPNYCH LOTÓW:");

                flights.stream()
                        .filter(flight -> flight.getDepartureAirport().contains(selectedDeparturePlace))
                        .map(flight -> flight.getId() + ": " + flight)
                        .forEach(System.out::println);
                flights.stream()
                        .filter(flight -> flight.getDepartureAirport().contains(selectedDeparturePlace))
                        .map(Flight::getId)
                        .forEach(validFlightAnswers::add);
                break;
            case "d": //DepartureDate
                System.out.println("Podaj datę wylotu [dd.mm.rrr]: ");
                String selectedDepartureDate = scanner.nextLine();
                System.out.println("LISTA DOSTĘPNYCH LOTÓW:");
                flights.stream()
                        .filter(flight -> flight.getDepartureDate().equals(selectedDepartureDate))
                        .map(flight -> flight.getId() + ": " + flight)
                        .forEach(System.out::println);
                flights.stream()
                        .filter(flight -> flight.getDepartureDate().equals(selectedDepartureDate))
                        .map(Flight::getId)
                        .forEach(validFlightAnswers::add);
                break;
            case "c": //MaximumPrice
                System.out.println("Podaj maksymalną cenę lotu: ");
                BigDecimal selectedMaximumPrice = new BigDecimal(scanner.nextLine());
                System.out.println("LISTA DOSTĘPNYCH LOTÓW:");
                flights.stream()
                        .filter(flight -> flight.getBasicPrice().compareTo(selectedMaximumPrice) < 0)
                        .map(flight -> flight.getId() + ": " + flight)
                        .forEach(System.out::println);
                flights.stream()
                        .filter(flight -> flight.getBasicPrice().compareTo(selectedMaximumPrice) < 0)
                        .map(Flight::getId)
                        .forEach(validFlightAnswers::add);
                break;
            case "w": //All
                System.out.println("LISTA DOSTĘPNYCH LOTÓW:");
                flights.stream()
                        .map(flight -> flight.getId() + ": " + flight)
                        .forEach(System.out::println);
                flights.stream()
                        .map(Flight::getId)
                        .forEach(validFlightAnswers::add);
                break;
            case "q":
                System.exit(0);
            default:
                throw new IllegalStateException("Unexpected value: " + filterOption);
        }

        System.out.println("\nLEGENDA");
        System.out.println("liczba: wybierz numer lotu");
        System.out.println("f: filtruj listę lotów");
        System.out.println("q: wyjście");

        String answer = getUserAnswer(validFlightAnswers);

        if (answer.equalsIgnoreCase("f")) {
            return selectFilteredFlight();
        } else if (answer.equalsIgnoreCase("q")) {
            System.exit(0);
            return null;
        } else {
            int flightId = Integer.parseInt(answer);
            return flights.get(flightId - 1);
        }
    }

    private String getUserAnswer(Set<String> validAnswers) {
        boolean isValidAnswer = false;
        String answer = "";
        while (!isValidAnswer) {
            System.out.print("\nWybierz opcję:");
            answer = scanner.nextLine();
            if (validAnswers.contains(answer)) {
                isValidAnswer = true;
            }
        }
        return answer;
    }

    private int getUserNumberOfReservations() {
        Set<String> validAnswers = new HashSet<>();
        for (int i = 1; i <= 10; i++) {
            validAnswers.add("" + i);
        }
        boolean isValidAnswer = false;
        String answer = "";
        while (!isValidAnswer) {
            System.out.println("Podaj liczbę (1-10):");
            answer = scanner.nextLine();
            if (validAnswers.contains(answer)) {
                isValidAnswer = true;
            }
        }
        return Integer.parseInt(answer);
    }

    private Flight selectFilteredFlight() {
        System.out.println("OPCJE FILTROWANIA:");
        System.out.println("s: wybór lotniska wylotu");
        System.out.println("d: wybór daty wylotu");
        System.out.println("c: ograniczenie ceny");
        System.out.println("w: wszystkie loty bez filtrowania");
        System.out.println("q: wyjście");

        Set<String> validAnswers = new HashSet<>();
        validAnswers.add("s");
        validAnswers.add("d");
        validAnswers.add("c");
        validAnswers.add("w");
        validAnswers.add("q");

        String filterOption = getUserAnswer(validAnswers);

        return getFlightFromUser(flights, filterOption);
    }

    private boolean checkIfBusinessClass() {
        System.out.println("Wybierz klasę lotu");
        System.out.println("b: klasa biznesowa");
        System.out.println("e: klasa ekonomiczna");

        Set<String> validAnswers = new HashSet<>();
        validAnswers.add("b");
        validAnswers.add("e");

        String answer = getUserAnswer(validAnswers);

        return answer.equalsIgnoreCase("b");
    }

    private String getPassengerTypeFromUser() {
        System.out.println("Określ wiek pasażera");
        System.out.println("o: osoba dorosła (powyżej 18 lat)");
        System.out.println("m: młodzież (od 12 do 18 lat)");
        System.out.println("d: dziecko (od 2 do 12 lat)");
        System.out.println("n: niemowlę (od 0 do 2 lat");

        Set<String> validAnswers = new HashSet<>();
        validAnswers.add("o");
        validAnswers.add("m");
        validAnswers.add("d");
        validAnswers.add("n");

        String answer = getUserAnswer(validAnswers);

        switch (answer) {
            case "o":
                return "18+";
            case "m":
                return "12-18";
            case "d":
                return "2-12";
            case "n":
                return "0-2";
            default:
                return null;
        }
    }

    private boolean checkIfMealReserved() {
        System.out.println("Czy chcesz zarezerwować posiłek na pokładzie?");
        System.out.println("Opłata - 40 zł za posiłek");
        return getYesNoAnswer();
    }

    private boolean checkIfPriorityBoarding(boolean isBusinessClass) {
        System.out.println("Czy chcesz pierwszeństwo przy wsiadaniu do samolotu?");
        if (isBusinessClass) {
            System.out.println("Opłata - 25 zł");
        } else {
            System.out.println("Opłata - 15 zł");
        }
        return getYesNoAnswer();
    }

    private boolean getYesNoAnswer() {
        System.out.println("t: tak");
        System.out.println("n: nie");

        Set<String> validAnswers = new HashSet<>();
        validAnswers.add("t");
        validAnswers.add("n");

        String answer = getUserAnswer(validAnswers);
        return answer.equalsIgnoreCase("t");
    }

    String prepareReservationId(Flight selectedFlight) {
        int randomNumber = (int) (Math.random() * 1000000);
        String departure = selectedFlight.getDepartureAirport();
        String arrival = selectedFlight.getArrivalAirport();
        return departure.substring(departure.length() - 3)
                + arrival.substring(arrival.length() - 3)
                + randomNumber;

    }
}
