package task;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class FlightTest extends Flight{
    private static final double DELTA = 1e-15;
    private Flight flight;
    //1) 110% gdy lot wypada w niedzielę
    // 2) 90% ceny bazowej gdy lot jest za ponad 30 dni, 120% ceny bazowej gdy wylot jest za mniej niż 7 dni
    // 3) 110% ceny bazowej gdy wolnych miejsc jest poniżej 50, 125% gdy wolnych miejsc jest poniżej 20

    //ad 1) 23.06.2019 niedziela
    @Test
    public void getFinalFlightPrice_FlightOnSundayShouldGive110() {
        {
            flight = new Flight("1", "Warsaw Chopin Airport WAW", "Gdańsk Lech Wałęsa GDA",
                    "23.06.2019", "25.06.2019", 1,
                    20, new BigDecimal("100.0"));
        }
        assertEquals(110.0, flight.getFinalFlightPrice().doubleValue(),DELTA);
    }

    //ad 1) 24.06.2019 poniedziałek
    @Test
    public void getFinalFlightPrice_FlightOnMondayShouldGive100() {
        {
            flight = new Flight("1", "Warsaw Chopin Airport WAW", "Gdańsk Lech Wałęsa GDA",
                    "24.06.2019", "25.06.2019", 1,
                    20, new BigDecimal("100.0"));
        }
        assertEquals(100.0, flight.getFinalFlightPrice().doubleValue(),DELTA);
    }

    //ad 2) za 35 dni
    @Test
    public void getFinalFlightPrice_FlightIn35DaysShouldGive90() {
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String depDay= LocalDateTime.now().plusDays(35).format(formatter);
            flight = new Flight("1", "Warsaw Chopin Airport WAW", "Gdańsk Lech Wałęsa GDA",
                    depDay, "25.06.2019", 1,
                    20, new BigDecimal("100.0"));
        }
        assertEquals(90.0, flight.getFinalFlightPrice().doubleValue(),DELTA);
    }

    //ad 2) za 20 dni
    @Test
    public void getFinalFlightPrice_FlightIn21DaysShouldGive100() {
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String depDay= LocalDateTime.now().plusDays(21).format(formatter);
            flight = new Flight("1", "Warsaw Chopin Airport WAW", "Gdańsk Lech Wałęsa GDA",
                    depDay, "25.06.2019", 1,
                    20, new BigDecimal("100.0"));
        }
        assertEquals(100.0, flight.getFinalFlightPrice().doubleValue(),DELTA);
    }

    //ad 2) za 4 dni
    @Test
    public void getFinalFlightPrice_FlightIn4DaysShouldGive120() {
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String depDay= LocalDateTime.now().plusDays(4).format(formatter);
            flight = new Flight("1", "Warsaw Chopin Airport WAW", "Gdańsk Lech Wałęsa GDA",
                    depDay, "25.06.2019", 1,
                    20, new BigDecimal("100.0"));
        }
        assertEquals(120.0, flight.getFinalFlightPrice().doubleValue(),DELTA);
    }

    //ad 3) wolnych miejsc jest powyżej 50
    @Test
    public void getFinalFlightPrice_FlightWith80FreeSeatsShouldGive100() {
        {
            flight = new Flight("1", "Warsaw Chopin Airport WAW", "Gdańsk Lech Wałęsa GDA",
                    "24.06.2019", "25.06.2019", 1,
                    20, new BigDecimal("100.0"));
        }
        assertEquals(100.0, flight.getFinalFlightPrice().doubleValue(),DELTA);
    }

    //ad 3) wolnych miejsc jest poniżej 50
    @Test
    public void getFinalFlightPrice_FlightWith49FreeSeatsShouldGive110() {
        {
            flight = new Flight("1", "Warsaw Chopin Airport WAW", "Gdańsk Lech Wałęsa GDA",
                    "24.06.2019", "25.06.2019", 1,
                    51, new BigDecimal("100.0"));
        }
        assertEquals(110.0, flight.getFinalFlightPrice().doubleValue(),DELTA);
    }

    //ad 3) wolnych miejsc jest poniżej 20
    @Test
    public void getFinalFlightPrice_FlightIWith10FreeSeatsShouldGive125() {
        {
            flight = new Flight("1", "Warsaw Chopin Airport WAW", "Gdańsk Lech Wałęsa GDA",
                    "24.06.2019", "25.06.2019", 1,
                    90, new BigDecimal("100.0"));
        }
        assertEquals(125.0, flight.getFinalFlightPrice().doubleValue(),DELTA);
    }

    //mix: lot w niedzielę, za mniej niż 7 dni, wolnych miejsc jest poniżej 20
    //100 * 1.1 * 1.2 * 1.25= 165
    @Test
    public void getFinalFlightPrice_FlightOnSundayInUnder7DaysWith10FreeSeatsShouldGive125() {
        {
            flight = new Flight("1", "Warsaw Chopin Airport WAW", "Gdańsk Lech Wałęsa GDA",
                    "02.06.2019", "25.06.2019", 1,
                    90, new BigDecimal("100.0"));
        }
        assertEquals(165.0, flight.getFinalFlightPrice().doubleValue(),DELTA);
    }
}
