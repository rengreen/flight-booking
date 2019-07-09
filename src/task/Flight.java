package task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Flight {

    //id
    private String id;
    //miejsce wylotu i symbol (np. Wiedeń VIE)
    private String departureAirport;
    // miejsce przylotu i symbol
    private String arrivalAirport;
    // data wylotu
    private String departureDate;
    //data powrotu
    private String returnDate;
    // długość lotu (w godzinach)
    private double flightDuration;
    // liczba pasażerów
    private int numberOfPassengers;
    // cena w zł
    // (bazowa, niewidoczna dla klienta. Klient może widzieć cenę biletu po przeliczeniu przez reguły z punktu 2)
    private BigDecimal basicPrice;

    private final int NUMBER_OF_PLACES=100;

    public Flight() {
    }

    public Flight(String departureAirport, String arrivalAirport, String departureDate, String returnDate,
                  double flightDuration, int numberOfPassengers, BigDecimal basicPrice) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.flightDuration = flightDuration;
        this.numberOfPassengers = numberOfPassengers;
        this.basicPrice = basicPrice;
    }


    public Flight(String id, String departureAirport, String arrivalAirport, String departureDate, String returnDate,
                  double flightDuration, int numberOfPassengers, BigDecimal basicPrice) {
        this.id = id;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.flightDuration = flightDuration;
        this.numberOfPassengers = numberOfPassengers;
        this.basicPrice = basicPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirportCode(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String arrivalDate) {
        this.returnDate = returnDate;
    }

    public double getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(double flightDuration) {
        this.flightDuration = flightDuration;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public BigDecimal getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(BigDecimal basicPrice) {
        this.basicPrice = basicPrice;
    }


    public BigDecimal getFinalFlightPrice() {
        BigDecimal finalPrice=this.basicPrice;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.parse(this.departureDate, formatter);

        // 1) 110% gdy lot wypada w niedzielę
        if (localDate.getDayOfWeek().getValue()==7){
            finalPrice=finalPrice.multiply(new BigDecimal("1.1"));
        }
        // 2) 90% ceny bazowej gdy lot jest za ponad 30 dni, 120% ceny bazowej gdy wylot jest za mniej niż 7 dni
        int timeToDeparture = localDate.getDayOfYear()- LocalDate.now().getDayOfYear();
        if (timeToDeparture>30){
            finalPrice=finalPrice.multiply(new BigDecimal("0.9"));
        }
        if (timeToDeparture<7){
            finalPrice=finalPrice.multiply(new BigDecimal("1.2"));
        }

        // 3) 110% ceny bazowej gdy wolnych miejsc jest poniżej 50, 125% gdy wolnych miejsc jest poniżej 20
        int freeSeats = NUMBER_OF_PLACES - this.numberOfPassengers;
        if (freeSeats < 50 && freeSeats >= 20){
            finalPrice=finalPrice.multiply(new BigDecimal("1.1"));
        } else if (freeSeats < 20){
            finalPrice=finalPrice.multiply(new BigDecimal("1.25"));
        }

        return finalPrice;
    }

    @Override
    public String toString() {


        return "Lot z " + departureAirport +
                " do " + arrivalAirport +
                ", wylot dnia: " + departureDate +
                ", powrót dnia: " + returnDate +
                ", czas lotu: " + flightDuration +
                " h, wolne miejsca: " + (NUMBER_OF_PLACES - this.numberOfPassengers) +
                ", cena: " + getFinalFlightPrice().setScale(2, RoundingMode.HALF_UP);
    }
}
