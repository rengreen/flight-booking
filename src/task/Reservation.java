package task;

import java.math.BigDecimal;

public class Reservation {
    private String reservationId;
    private Flight flight;
    private boolean isBusinessClass;
    private String passengerType;
    private boolean isMealReserved;
    private boolean isPriorityBoarding;

    public Reservation() {
    }

    public Reservation(Flight flight, boolean isBusinessClass, String passengerType,
                       boolean isMealReserved, boolean isPriorityBoarding) {
        this.flight = flight;
        this.isBusinessClass = isBusinessClass;
        this.passengerType = passengerType;
        this.isMealReserved = isMealReserved;
        this.isPriorityBoarding = isPriorityBoarding;

        int randomNumber=(int)(Math.random() * 1000000);
        String departure=this.flight.getDepartureAirport();
        String arrival=this.flight.getArrivalAirport();
        this.reservationId
                = departure.substring(departure.length() - 3)
                + arrival.substring(arrival.length() - 3)
                + randomNumber;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId() {

    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public boolean isBusinessClass() {
        return isBusinessClass;
    }

    public void setBusinessClass(boolean businessClass) {
        this.isBusinessClass = businessClass;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public boolean isMealReserved() {
        return isMealReserved;
    }

    public void setMealReserved(boolean mealReserved) {
        this.isMealReserved = mealReserved;
    }

    public boolean isPriorityBoarding() {
        return isPriorityBoarding;
    }

    public void setPriorityBoarding(boolean priorityBoarding) {
        isPriorityBoarding = priorityBoarding;
    }

    public BigDecimal getFinalReservationPrice(){
        BigDecimal finalReservationPrice=this.flight.getFinalFlightPrice().add(new BigDecimal("170.0"));
        if (this.isBusinessClass) {
            finalReservationPrice=finalReservationPrice.multiply(new BigDecimal("1.65"));
        }
        if (this.isMealReserved) {
            finalReservationPrice=finalReservationPrice.multiply(new BigDecimal("1.65"));
        }
        if (this.isPriorityBoarding && this.isBusinessClass) {
            finalReservationPrice=finalReservationPrice.add(new BigDecimal("25.0"));
        }

        if (this.isPriorityBoarding && !this.isBusinessClass) {
            finalReservationPrice=finalReservationPrice.add(new BigDecimal("15.0"));
        }

        return finalReservationPrice;
    }
}
