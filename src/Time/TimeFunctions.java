

package Time;

import Appointments.Appointment;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static Appointments.AppointmentFunctions.getAllAppointments;
/**
 * This contains the functions related to converting time.
 */
public class TimeFunctions {
    static ZoneId utc = ZoneId.of("UTC");
    static ZoneId est = ZoneId.of("US/Eastern");
    static ZoneId sys = ZoneId.of(String.valueOf(ZoneId.systemDefault()));

    public static Timestamp sysToUTC (Timestamp ts) {
        LocalDateTime utcDateTime = ts.toLocalDateTime();
        Timestamp utcTime = Timestamp.valueOf(utcDateTime.atZone(est).withZoneSameInstant(sys).toLocalDateTime());
        return utcTime;
    }
    public static Timestamp sysToEST (Timestamp ts) {
        LocalDateTime estDateTime = ts.toLocalDateTime();
        Timestamp estTime = Timestamp.valueOf(estDateTime.atZone(sys).withZoneSameInstant(est).toLocalDateTime());
        return estTime;
    }
    public static Timestamp utcToSYS (Timestamp ts) {
        LocalDateTime sysDateTime = ts.toLocalDateTime();
        Timestamp sysTime = Timestamp.valueOf(sysDateTime.atZone(utc).withZoneSameInstant(est).toLocalDateTime());
        return sysTime;
    }

    public static Timestamp estToUTC (Timestamp ts) {
        LocalDateTime utcDateTime = ts.toLocalDateTime();
        Timestamp utcTime = Timestamp.valueOf(utcDateTime.atZone(est).withZoneSameInstant(utc).toLocalDateTime());
        return utcTime;
    }
    public static Timestamp estToSYS (Timestamp ts) {
        LocalDateTime sysDateTime = ts.toLocalDateTime();
        Timestamp sysTime = Timestamp.valueOf(sysDateTime.atZone(est).withZoneSameInstant(sys).toLocalDateTime());
        return sysTime;
    }
    public static Timestamp utcToEST (Timestamp ts) {
        LocalDateTime estDateTime = ts.toLocalDateTime();
        Timestamp estTime = Timestamp.valueOf(estDateTime.atZone(utc).withZoneSameInstant(est).toLocalDateTime());
        return estTime;
    }

    public static LocalDateTime openTime(LocalDate localDate) {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM:dd:yy");

        LocalTime lt = LocalTime.parse("08:00", timeFormat);

        LocalDateTime ldt = LocalDateTime.of(localDate, lt);

        Timestamp ts = estToSYS(Timestamp.valueOf(ldt));

        LocalDateTime ldt2 = ts.toLocalDateTime();

        return ldt2;
    }
    public static LocalDateTime closeTime(LocalDate localDate) {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM:dd:yy");

        LocalTime lt = LocalTime.parse("22:00", timeFormat);

        LocalDateTime ldt = LocalDateTime.of(localDate, lt);

        Timestamp ts = estToSYS(Timestamp.valueOf(ldt));

        LocalDateTime ldt2 = ts.toLocalDateTime();

        return ldt2;
    }
}
