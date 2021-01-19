package gdabski.training.datetime;

import gdabski.training.datetime.DateTimeTypesUtils.*;
import gdabski.training.datetime.improved.ImprovedBean;
import gdabski.training.datetime.standard.Bean;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.threeten.extra.Interval;
import org.threeten.extra.LocalDateRange;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static gdabski.training.datetime.DateTimeTypesUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * === JAVA 11 DATETIME TYPES ===
 */
class DateTimeTypesTest {

    /**
     * POJĘCIA 1:
     * <ul>
     * <li>moment w czasie (instant, instant in time, moment, timestamp) np. {@code 1608317121239} - punkt
     * na (linearnej) osi czasu</li>
     * </ul>
     */
    @Test
    void demonstrateUnixTimestamp() {
        System.out.println(System.currentTimeMillis());
    }

    @Test
    void demonstrateUnixTimestampIsANumber() {
        long unixTimestamp = System.currentTimeMillis();

        System.out.println(unixTimestamp + 5 * 60 * 1000);
        System.out.println(unixTimestamp + TimeUnit.MINUTES.toMillis(5)); // z java.util.concurrent
    }

    /**
     * POJĘCIA 2:
     * <ul>
     * <li>dzień (day, date) np. {@code 2020-12-18}</li>
     * <li>czas (time) np. {@code 15:32:11.345}</li>
     * <li>strefa czasowa (timezone)  np. {@code Z}, {@code +02:00}</li>
     * <li>data (date, datetime)  np. {@code 2020-12-18T15:32.345}, {@code 2020-12-18T15:32.345+02:00}</li>
     * </ul>
     */
    @Test
    void demonstrateJavaUtilDate() {
        Date date = new Date();
        System.out.println(date);
    }

    @Test
    void demonstrateJavaUtilDateContainsMillis() {
        Date date = new Date();
        System.out.println(date);
        System.out.println(new SimpleDateFormat("EEE MMM dd HH:mm:ss.SSS zzz yyyy").format(date)); // SDF not thread-safe!
    }

    /**
     * {@link Date} to opakowanie na jednego {@code long}a, zob. {@link Date#fastTime}.
     */
    @Test
    void demonstrateJavaUtilDateRepresentsAnInstantInTime() {
        Date date = new Date();
        System.out.println(date);
        long unixTimestamp = date.getTime();
        System.out.println(unixTimestamp);
        System.out.println(new Date(unixTimestamp));
    }

    /**
     * Dygresja: ustawianie z kodu domyślnej stefy czasowej jest w ogóle ryzykowne.
     */
    @Test
    void demonstrateJavaUtilDateIsJvmTimezoneSensitive() {
        Date date = new Date();
        System.out.println(date);

        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC)); // TimeZone.getTimeZone("UTC")
        System.out.println(date);
    }

    @Test
    void demonstrateJavaUtilDateIsMutableAndNotThreadSafe() {
        Person person = new Person(new Date());
        System.out.println(person.getBirthdate());

        List<String> events = getEventsOnDateInYear1969(person.getBirthdate());
        System.out.println(events);

        System.out.println(person.getBirthdate());
    }

    /**
     * {@link Calendar} służy do poruszaniu się po kalendarzu.
     */
    @Test
    void demonstrateCalendar() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        System.out.println(date);
        System.out.println();
        System.out.println("ERA: " + calendar.get(Calendar.ERA));
        System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
        System.out.println("MONTH: " + calendar.get(Calendar.MONTH)); // uwaga!
        System.out.println("WEEK_OF_YEAR: " + calendar.get(Calendar.WEEK_OF_YEAR));
        System.out.println("WEEK_OF_MONTH: " + calendar.get(Calendar.WEEK_OF_MONTH));
        System.out.println("DATE: " + calendar.get(Calendar.DATE));
        System.out.println("DAY_OF_MONTH: " + calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("DAY_OF_YEAR: " + calendar.get(Calendar.DAY_OF_YEAR));
        System.out.println("DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK)); // uwaga!
        System.out.println("DAY_OF_WEEK_IN_MONTH: " + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        System.out.println("AM_PM: " + calendar.get(Calendar.AM_PM));
        System.out.println("HOUR: " + calendar.get(Calendar.HOUR));
        System.out.println("HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
        System.out.println("MINUTE: " + calendar.get(Calendar.MINUTE));
        System.out.println("SECOND: " + calendar.get(Calendar.SECOND));
        System.out.println("MILLISECOND: " + calendar.get(Calendar.MILLISECOND));
        System.out.println("FIRST DAY OF WEEK: " + calendar.getFirstDayOfWeek());
    }

    @Test
    void demonstrateCalendarIsLocaleSensitive() {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTime(new Date());

        System.out.println("ERA: " + calendar.get(Calendar.ERA));
        System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
        System.out.println("MONTH: " + calendar.get(Calendar.MONTH)); // uwaga!
        System.out.println("WEEK_OF_YEAR: " + calendar.get(Calendar.WEEK_OF_YEAR)); // zależy od Locale
        System.out.println("WEEK_OF_MONTH: " + calendar.get(Calendar.WEEK_OF_MONTH)); // zależy od Locale
        System.out.println("DATE: " + calendar.get(Calendar.DATE));
        System.out.println("DAY_OF_MONTH: " + calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("DAY_OF_YEAR: " + calendar.get(Calendar.DAY_OF_YEAR));
        System.out.println("DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK)); // uwaga!
        System.out.println("DAY_OF_WEEK_IN_MONTH: " + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        System.out.println("AM_PM: " + calendar.get(Calendar.AM_PM));
        System.out.println("HOUR: " + calendar.get(Calendar.HOUR));
        System.out.println("HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
        System.out.println("MINUTE: " + calendar.get(Calendar.MINUTE));
        System.out.println("SECOND: " + calendar.get(Calendar.SECOND));
        System.out.println("MILLISECOND: " + calendar.get(Calendar.MILLISECOND));
        System.out.println("FIRST DAY OF WEEK: " + calendar.getFirstDayOfWeek());
        System.out.println("TIMEZONE: " + calendar.getTimeZone());
        System.out.println("ZONE OFFSET: " + calendar.get(Calendar.ZONE_OFFSET));

        System.out.printf("%n===========%n%n");

        calendar = Calendar.getInstance(new Locale("ja", "JP", "JP"));

        System.out.println("ERA: " + calendar.get(Calendar.ERA));
        System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
        System.out.println("MONTH: " + calendar.get(Calendar.MONTH)); // uwaga!
        System.out.println("WEEK_OF_YEAR: " + calendar.get(Calendar.WEEK_OF_YEAR)); // zależy od Locale
        System.out.println("WEEK_OF_MONTH: " + calendar.get(Calendar.WEEK_OF_MONTH)); // zależy od Locale
        System.out.println("DATE: " + calendar.get(Calendar.DATE));
        System.out.println("DAY_OF_MONTH: " + calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("DAY_OF_YEAR: " + calendar.get(Calendar.DAY_OF_YEAR));
        System.out.println("DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK)); // uwaga!
        System.out.println("DAY_OF_WEEK_IN_MONTH: " + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        System.out.println("AM_PM: " + calendar.get(Calendar.AM_PM));
        System.out.println("HOUR: " + calendar.get(Calendar.HOUR));
        System.out.println("HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
        System.out.println("MINUTE: " + calendar.get(Calendar.MINUTE));
        System.out.println("SECOND: " + calendar.get(Calendar.SECOND));
        System.out.println("MILLISECOND: " + calendar.get(Calendar.MILLISECOND));
        System.out.println("FIRST DAY OF WEEK: " + calendar.getFirstDayOfWeek());
        System.out.println("TIMEZONE: " + calendar.getTimeZone());
        System.out.println("ZONE OFFSET: " + calendar.get(Calendar.ZONE_OFFSET));
    }

    @Test
    void demonstrateCalendarIsMutableAndHasUnintuitiveParameters() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, 2);
        calendar.set(Calendar.DAY_OF_WEEK, 3);
        calendar.set(Calendar.HOUR, 7);
        calendar.add(Calendar.MINUTE, 10);
        calendar.add(Calendar.YEAR, -5);
        System.out.println(calendar.getTime());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.add(Calendar.MINUTE, 10);
        calendar.add(Calendar.YEAR, -5);
        System.out.println(calendar.getTime());
    }

    /**
     * Odpowiedzią na braki w bibliotece standardowej był projekt Joda Time. NA jego podstawie
     * od Javy 8 nowe API {@code java.time} (JSR-310).
     * <br><br>
     * {@link Instant} jest opakowaniem na {@code long}a + {@code int}a.
     */
    @Test
    void demonstrateInstant() {
        Instant instant = Instant.now();
        System.out.println(instant);
        System.out.println(instant.getEpochSecond()); // większy zasięg
        System.out.println(instant.getNano());
    }

    @Test
    void demonstrateInstantToUnixTimestampConversion() {
        long unixTimestamp = System.currentTimeMillis();
        Instant instant = Instant.ofEpochMilli(unixTimestamp);
        long copy = instant.toEpochMilli();
        assertEquals(unixTimestamp, copy);
    }

    @Test
    void demonstrateInstantToDateConversion() {
        Date date = new Date();
        Instant instant = date.toInstant();
        Date copy = Date.from(instant);
        assertEquals(date, copy);
    }

    /**
     * {@link Date} nie zawiera żadnej informacji o strefie czasowej, ale mimo to od niej
     * zależy. {@link Instant} nie zawiera i nie zależy od strefy czasowej.
     */
    @Test
    void demonstrateInstantIsInsensitiveToLocaleAndTimezone() {
        long unixTimestamp = System.currentTimeMillis();
        Instant instant = Instant.ofEpochMilli(unixTimestamp);
        System.out.println(instant);

        System.out.printf("%n===========%n%n");
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.ofHours(-9)));
        System.out.println(instant);
        System.out.println(Instant.ofEpochMilli(unixTimestamp));

        System.out.printf("%n===========%n%n");
        Locale.setDefault(new Locale("ja", "JP", "JP"));
        System.out.println(instant);
        System.out.println(Instant.ofEpochMilli(unixTimestamp));
    }

    @Test
    void demonstrateInstantStringRepresentation() {
        Instant instant = Instant.now();
        String asString = instant.toString();
        Instant copy = Instant.parse(asString);

        assertEquals(instant, copy);

        System.out.println(Instant.parse("2020-10-09T22:11:33Z"));
    }

    /**
     * {@link Instant} jak wszystkie typy {@code java.time} jest też niemutowalny.
     */
    @Test
    void demonstrateInstantMethods() {
        Instant instant = Instant.now();
        System.out.println(instant);
        System.out.println(instant.plusSeconds(10));
        System.out.println(instant.minus(300, ChronoUnit.NANOS));
        System.out.println(instant.plus(2, ChronoUnit.DAYS));
        System.out.println(instant.truncatedTo(ChronoUnit.SECONDS));
        System.out.println(instant.truncatedTo(ChronoUnit.HOURS));
        System.out.println(instant.with(ChronoField.MILLI_OF_SECOND, 333));

        // ale...
        System.out.println(instant.plus(3, ChronoUnit.MONTHS));
    }

    /**
     * Kalendarz gregoriański wg ISO 8601. Pełna data z offsetem i strefą czasową. Identyfikuje
     * konkretny moment w czasie.
     */
    @Test
    void demonstrateZonedDateTime() {
        Instant instant = Instant.now();
        ZonedDateTime zonedDateTimeAtWarsaw = instant.atZone(ZoneId.of("Europe/Warsaw"));
        System.out.println(zonedDateTimeAtWarsaw);
        System.out.println(formatDate(zonedDateTimeAtWarsaw));
        ZonedDateTime zonedDateTimeAtPlusOne = instant.atZone(ZoneOffset.ofHours(1));
        System.out.println(zonedDateTimeAtPlusOne);
        System.out.println(formatDate(zonedDateTimeAtPlusOne));
        ZonedDateTime zonedDateTimeAtChicago = instant.atZone(ZoneId.of("America/Chicago"));
        System.out.println(zonedDateTimeAtChicago);
        System.out.println(formatDate(zonedDateTimeAtChicago));
        ZonedDateTime zonedDateTimeAtMinusSix = instant.atZone(ZoneOffset.of("-06:00"));
        System.out.println(zonedDateTimeAtMinusSix);
        System.out.println(formatDate(zonedDateTimeAtMinusSix));
    }

    @Test
    void demonstrateZonedDateTimeRepresentsAnInstantInTime() {
        Instant instant = Instant.now();
        System.out.println(instant);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Europe/Warsaw"));
        System.out.println(zonedDateTime);
        Instant copy = zonedDateTime.toInstant();
        System.out.println(copy);

        assertEquals(instant, copy);
    }

    @Test
    void demonstrateZonedDateTimeMethods() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Europe/Warsaw"));
        System.out.println(zonedDateTime);
        System.out.println(zonedDateTime.plusSeconds(10));
        System.out.println(zonedDateTime.minus(300, ChronoUnit.NANOS));
        System.out.println(zonedDateTime.plus(2, ChronoUnit.DAYS));
        System.out.println(zonedDateTime.truncatedTo(ChronoUnit.SECONDS));
        System.out.println(zonedDateTime.truncatedTo(ChronoUnit.HOURS));
        System.out.println(zonedDateTime.with(ChronoField.MILLI_OF_SECOND, 333));

        // ale także ...
        System.out.printf("%n===========%n%n");
        System.out.println(zonedDateTime.plus(3, ChronoUnit.MONTHS));
        System.out.println(zonedDateTime.minusYears(20));

        System.out.printf("%n===========%n%n");
        System.out.println(zonedDateTime.getDayOfWeek());
        System.out.println(zonedDateTime.getYear());
        System.out.println(zonedDateTime.getMonth());
        System.out.println(zonedDateTime.getDayOfMonth());
        System.out.println(zonedDateTime.getHour());
        System.out.println(zonedDateTime.getMinute());
        System.out.println(zonedDateTime.getSecond());
        System.out.println(zonedDateTime.getNano());
    }

    @Test
    void demonstrateZonedDateTimeWrapsLocalDateTimeOffsetAndZone() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Europe/Warsaw"));
        System.out.println(zonedDateTime);
        System.out.println(zonedDateTime.toLocalDateTime()); // LocalDateTime
        System.out.println(zonedDateTime.getOffset()); // ZoneOffset prawie nadmiarowe
        System.out.println(zonedDateTime.getZone()); // ZoneId
    }

    /**
     * Pełna data bez offsetu ani stefy czasowej. <b>Nie</b> identyfikuje konkretnego momentu w czasie.
     */
    @Test
    void demonstrateLocalDateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Europe/Warsaw"));
        System.out.println(zonedDateTime);
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        System.out.println(localDateTime);
        ZonedDateTime copy = localDateTime.atZone(ZoneId.of("Europe/Warsaw"));
        System.out.println(copy);

        assertEquals(zonedDateTime, copy);
    }

    @Test
    void demonstrateLocalDateTimeWrapsLocalDateAndLocalTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2020, 12, 29, 10, 0, 0);
        System.out.println(localDateTime);
        System.out.println(localDateTime.toLocalDate()); // LocalDate
        System.out.println(localDateTime.toLocalTime()); // LocalTime
    }

    @Test
    void demonstrateLocalDateTimeDoesNotRepresentInstantInTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2020, 12, 29, 10, 0, 0);
        System.out.println(localDateTime);

        Instant instant = localDateTime.toInstant(ZoneOffset.ofHours(-6)); // konieczny offset!
        System.out.println(instant);
    }

    /**
     * Ten sam zestaw operacji co {@link ZonedDateTime}.
     */
    @Test
    void demonstrateLocalDateTimeMethods() {
        LocalDateTime localDateTime = LocalDateTime.of(2020, 12, 29, 10, 0, 0);

        System.out.println(localDateTime);
        System.out.println(localDateTime.plusSeconds(10));
        System.out.println(localDateTime.minus(300, ChronoUnit.NANOS));
        System.out.println(localDateTime.plus(2, ChronoUnit.DAYS));
        System.out.println(localDateTime.truncatedTo(ChronoUnit.SECONDS));
        System.out.println(localDateTime.truncatedTo(ChronoUnit.HOURS));
        System.out.println(localDateTime.with(ChronoField.MILLI_OF_SECOND, 333));

        System.out.printf("%n===========%n%n");
        System.out.println(localDateTime.plus(3, ChronoUnit.MONTHS));
        System.out.println(localDateTime.minusYears(20));

        System.out.printf("%n===========%n%n");
        System.out.println(localDateTime.getDayOfWeek());
        System.out.println(localDateTime.getYear());
        System.out.println(localDateTime.getMonth());
        System.out.println(localDateTime.getDayOfMonth());
        System.out.println(localDateTime.getHour());
        System.out.println(localDateTime.getMinute());
        System.out.println(localDateTime.getSecond());
        System.out.println(localDateTime.getNano());
    }

    /**
     * Jak kartka z kalendarza: identyfikuje dzień i rok, nie zawiera czasu.
     */
    @Test
    void demonstrateLocalDate() {
        LocalDate localDate = LocalDate.of(2020, Month.DECEMBER, 29);
        System.out.println(localDate);
        System.out.println(localDate.plusDays(5));
        System.out.println(localDate.minusYears(20));
        System.out.println(localDate.getMonth());
        System.out.println(localDate.atTime(20, 0, 0)); // LocalDateTime

        // ale...
        System.out.println(localDate.plus(5, ChronoUnit.HOURS));
    }

    @Test
    void demonstrateLocalDateStartAndEnd() {
        LocalDate localDate = LocalDate.now(ZoneId.of("Europe/Warsaw"));
        System.out.println(localDate.atStartOfDay()); // LocalDateTime
        System.out.println(localDate.atStartOfDay(ZoneId.of("Europe/Warsaw"))); // ZonedDateTime
        // dlaczego trzeba było podać strefę czasową po raz drugi? ;)

        // localDate.atEndOfDay() nie istnieje!
        // 2020-12-29T23:59:59.999 ?
        // 2020-12-29T23:59:59.999999 ?
        // 2020-12-29T23:59:59.999999999 ?
    }

    /**
     * Ilość czasu.
     */
    @Test
    void demonstrateTemporalAmount() {
        Duration fifteenSeconds = Duration.ofSeconds(15);
        System.out.println(fifteenSeconds);
        Period fiveYears = Period.ofYears(5);
        System.out.println(fiveYears);

        System.out.printf("%n===========%n%n");
        System.out.println(Duration.ofHours(1).plusMinutes(30));
        System.out.println(Duration.between(Instant.now(), LocalDate.of(2021, Month.JULY, 23).atTime(19, 0).atZone(ZoneId.of("Asia/Tokyo"))));
        System.out.println(Period.between(LocalDate.now(ZoneId.of("Europe/Warsaw")), LocalDate.of(2021, Month.JULY, 23)));

        System.out.printf("%n===========%n%n");
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/Warsaw"));
        System.out.println(localDateTime);
        System.out.println(localDateTime.plus(fiveYears).plus(fifteenSeconds));

        System.out.printf("%n===========%n%n");
        ZonedDateTime zonedDateTime = LocalDateTime.of(2021, Month.MARCH, 27, 12, 0).atZone(ZoneId.of("Europe/Warsaw"));
        System.out.println(zonedDateTime);
        System.out.println(zonedDateTime.plus(Duration.ofDays(1)));
        System.out.println(zonedDateTime.plus(Period.ofDays(1)));
    }

    /**
     * Przedziały czasu z konkretnym początkiem i końcem: z three-ten extras
     */
    @Test
    void demonstrateIntervalAndLocalDateRange() {
        LocalDate validFrom = LocalDate.of(2021, Month.JANUARY, 6);
        LocalDate validTo = LocalDate.of(2021, Month.JULY, 23);
        Instant someInstant = LocalDate.of(2021, Month.MARCH, 15).atTime(15, 12).atZone(ZoneId.of("Europe/Warsaw")).toInstant();

        Instant validFromInstant = validFrom.atStartOfDay(ZoneId.of("Europe/Warsaw")).toInstant();
        Instant validToInstant = validTo.atStartOfDay(ZoneId.of("Europe/Warsaw")).plusDays(1).toInstant();
        // Instant validToInstant = validFrom.atStartOfDay(ZoneId.of("Europe/Warsaw")).plusDays(1).minusNanos(1).toInstant(); nie próbuj tego!
        System.out.println(!someInstant.isBefore(validFromInstant) && someInstant.isBefore(validToInstant)); // bez użycia Interval

        Interval interval = Interval.of(validFromInstant, validToInstant);
        System.out.println(interval);
        System.out.println(interval.contains(someInstant));

        System.out.printf("%n===========%n%n");
        // LocalDateRange localDateRange = LocalDateRange.of(validFrom, validTo); // end exclusive
        LocalDateRange localDateRange = LocalDateRange.ofClosed(validFrom, validTo); // end inclusive
        System.out.println(localDateRange);
        System.out.println(localDateRange.contains(LocalDate.of(2021, Month.MARCH, 15)));
        System.out.println(localDateRange.contains(LocalDate.of(2021, Month.JULY, 23)));
        System.out.println(localDateRange.contains(LocalDate.of(2021, Month.JULY, 24)));
    }

    @Test
    void demonstrateOtherJsr310Types() {
        LocalTime localTime = LocalTime.of(15, 23, 59, 999847123);
        System.out.println(localTime); // LocalTime
        System.out.println(localTime.atOffset(ZoneOffset.ofHours(2))); // OffsetTime - offset, nie strefa czasowa
        System.out.println(YearMonth.of(2020, Month.DECEMBER)); // YearMonth - jak ważność karty płatniczej
        System.out.println(MonthDay.of(Month.NOVEMBER, 11)); // MonthDay - jak corocznie obchodzone urodziny
    }

    /**
     * Jak {@link ZonedDateTime} z {@link ZoneOffset}em, niezależny od bazy stref czasowych w JVM-ie. Wydaje się,
     * że jedynym celem istnienia osobnego typu {@link OffsetDateTime} jest umożliwienie autorom API wymuszenia
     * użycia obiektu ze stałym offsetem. Do zastosowań specjalistycznych.
     */
    @Test
    void demonstrateOffsetDateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2020, Month.DECEMBER, 29, 16, 15);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Europe/Warsaw"));
        System.out.println(zonedDateTime);
        ZonedDateTime zonedDateTimeWithOffset = localDateTime.atZone(ZoneOffset.ofHours(1)); // == zonedDateTime.withFixedOffsetZone()
        System.out.println(zonedDateTimeWithOffset);

        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.ofHours(1)); // javadoc / == zonedDateTime.toOffsetDateTime()
        System.out.println(offsetDateTime);
        System.out.println(offsetDateTime.toInstant());
    }

    @Test
    void demonstrateZonedDateTimeCornerCasesAtOffsetChanges() {
        System.out.println(ZonedDateTime.of(LocalDateTime.parse("2021-03-28T02:30:00"), ZoneId.of("Europe/Warsaw")));
        System.out.println(ZonedDateTime.of(LocalDateTime.parse("2021-10-31T02:30:00"), ZoneId.of("Europe/Warsaw")));
        System.out.println(ZonedDateTime.of(LocalDateTime.parse("2021-10-31T02:30:00"), ZoneId.of("Europe/Warsaw")).withLaterOffsetAtOverlap());
    }

    /**
     * <ul>
     *     <li> jeśli zapis zawiera strefę czasową, a nie zawiera offsetu, odczyt może być niejednoznaczny, p. wyżej </li>
     *     <li> jeśli w czasie odczytu dostępne będą inne reguły zmiany czasu niż w czasie zapisu, odczytany offset
     *     może ulec dostosowaniu do nowych reguł, a całość wskazywać na inny punkt w czasie niż przy zapisie </li>
     * </ul>
     */
    @Test
    void demonstrateZonedDateTimeCornerCasesWhenSerializing() {
        ZonedDateTime zonedDateTime = LocalDate.of(2038, Month.DECEMBER, 29).atStartOfDay(ZoneId.of("Europe/Warsaw"));
        System.out.println(zonedDateTime);

        System.out.printf("%n===========%n%n");
        String serialized = serializeToJson(zonedDateTime);
        System.out.println(serialized);
        ZonedDateTime deserialized = deserializeFromJson(serialized, ZonedDateTime.class);
        System.out.println(deserialized);

        assertEquals(zonedDateTime.toInstant(), deserialized.toInstant());

        System.out.printf("%n===========%n%n");
        String serializedShifted = serialized.replace("+01:00", "+02:00");
        System.out.println(serializedShifted);
        ZonedDateTime deserializedShifted = deserializeFromJson(serializedShifted, ZonedDateTime.class);
        System.out.println(deserializedShifted);

        assertNotEquals(zonedDateTime.toInstant(), deserializedShifted.toInstant());
    }

    /**
     * Tu akurat reprezentacja stringowa dobrze oddaje znaczenie typów - w przeciwieństwie do
     * ich wewnętrznej implementacji. Typy odpowiadają typom daty/czasu ze specyfikacji SQL-a:
     * {@code TIMESTAMP}, {@code DATE}, {@code TIME}.
     */
    @Test
    void demonstrateJavaSqlTypes() {
        long unixTimestamp = System.currentTimeMillis();
        System.out.println(new Timestamp(unixTimestamp)); // jak LocalDateTime
        System.out.println(new java.sql.Date(unixTimestamp)); // jak LocalDate
        System.out.println(new Time(unixTimestamp)); // jak LocalTime
    }

    /**
     * Wszystkie trzy typy dziedziczą po {@link Date}, co jest wielką pomyłką i źródłem problemów
     * (zob. javadoc {@link Timestamp}).
     */
    @Test
    void demonstrateJavaSqlTypesExtendJavaUtilDate() {
        long unixTimestamp = System.currentTimeMillis();
        Date javaUtilDate = new Date(unixTimestamp);
        Date javaSqlTimestamp = new Timestamp(unixTimestamp); // nie rób tego!
        Date javaSqlDate = new java.sql.Date(unixTimestamp); // nie rób tego!
        Date javaSqlTime = new Time(unixTimestamp); // nie rób tego!

        // zwłaszcza źle: Date date = resultSet.getDate("aaa");
        // uwaga na debugger!

        assertEquals(javaUtilDate, javaSqlTimestamp);
        assertNotEquals(javaSqlTimestamp, javaUtilDate); // niesymetryczny equals()!
        assertEquals(javaUtilDate, javaSqlDate);
        assertEquals(javaSqlDate, javaUtilDate); // co to ma znaczyć?
        assertEquals(javaUtilDate, javaSqlTime);
        assertEquals(javaSqlTime, javaUtilDate); // co to ma znaczyć?
        assertEquals(javaSqlDate, javaSqlTimestamp);
        assertNotEquals(javaSqlTimestamp, javaSqlDate); // niesymetryczny equals()!
        assertEquals(javaSqlTime, javaSqlTimestamp);
        assertNotEquals(javaSqlTimestamp, javaSqlTime); // niesymetryczny equals()!
        assertEquals(javaSqlDate, javaSqlTime);
        assertEquals(javaSqlTime, javaSqlDate); // co to ma znaczyć?
    }

    /**
     * Zob.: <a href=https://www.postgresql.org/docs/10/datatype-datetime.html>dokumentacja typów dat i czasu w Postgresie</a>.
     * W bazie danych <b>nie jest</b> przechowywana żadna informacja o strefie czasowej! Przechowywany jest
     * wyłącznie dzień i czas. Słowa "with time zone" w nazwie kolumny należy rozumieć jako "with time zone
     * conversion".
     */
    @Test
    void demonstrateJavaSqlTimestampBehaviorWhenReadFromDatabase() throws SQLException {
        Timestamp javaSqlTimestamp = new Timestamp(System.currentTimeMillis());
        System.out.format("original %s %s%n", javaSqlTimestamp, javaSqlTimestamp.getTime());

        try (Connection connection = getConnection()) {
            createTemporaryTable(connection);

            try (PreparedStatement statement = connection.prepareStatement("UPDATE datetime SET timestamp = ?, timestamptz = ?")) {
                statement.setTimestamp(1, javaSqlTimestamp);
                statement.setTimestamp(2, javaSqlTimestamp);
                statement.executeUpdate();

            }

            System.out.printf("%n===========%n%n");
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT timestamp, timestamptz FROM datetime")) {
                resultSet.next();
                Timestamp retrievedTimestamp = resultSet.getTimestamp("timestamp");
                System.out.format("from timestamp %s %s%n", retrievedTimestamp, retrievedTimestamp.getTime());
                Timestamp retrievedTimestamptz = resultSet.getTimestamp("timestamptz");
                System.out.format("from timestamptz %s %s%n", retrievedTimestamptz, retrievedTimestamptz.getTime());

                assertEquals(javaSqlTimestamp, retrievedTimestamp);
                assertEquals(javaSqlTimestamp.getTime(), retrievedTimestamp.getTime());
                assertEquals(javaSqlTimestamp, retrievedTimestamptz);
                assertEquals(javaSqlTimestamp.getTime(), retrievedTimestamptz.getTime());
            }

            TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.ofHours(-6)));

            System.out.printf("%n===========%n%n");
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT timestamp, timestamptz FROM datetime")) {
                resultSet.next();
                Timestamp retrievedTimestamp = resultSet.getTimestamp("timestamp");
                System.out.format("from timestamp %s %s%n", retrievedTimestamp, retrievedTimestamp.getTime());
                Timestamp retrievedTimestamptz = resultSet.getTimestamp("timestamptz");
                System.out.format("from timestamptz %s %s%n", retrievedTimestamptz, retrievedTimestamptz.getTime());

                assertNotEquals(javaSqlTimestamp, retrievedTimestamp);
                assertNotEquals(javaSqlTimestamp.getTime(), retrievedTimestamp.getTime());
                assertEquals(javaSqlTimestamp, retrievedTimestamptz);
                assertEquals(javaSqlTimestamp.getTime(), retrievedTimestamptz.getTime());
            }
        }
    }

    /**
     * W {@link java.sql.Date} przy odczycie zerowane są wszystkie elementy czasu! Podobnie zachowuje
     * się też {@link Time}.
     */
    @Test
    void demonstrateJavaSqlDateBehaviorWhenReadFromDatabase() throws SQLException {
        java.sql.Date javaSqlDate = new java.sql.Date(System.currentTimeMillis());
        System.out.format("original %s %s%n", javaSqlDate, javaSqlDate.getTime());

        try (Connection connection = getConnection()) {
            createTemporaryTable(connection);

            try (PreparedStatement statement = connection.prepareStatement("UPDATE datetime SET date = ?")) {
                statement.setDate(1, javaSqlDate);
                statement.executeUpdate();
            }

            System.out.printf("%n===========%n%n");
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT date FROM datetime")) {
                resultSet.next();
                java.sql.Date retrieved = resultSet.getDate("date");
                System.out.format("retrieved %s %s%n", retrieved, retrieved.getTime());

                assertNotEquals(javaSqlDate, retrieved);
                assertNotEquals(javaSqlDate.getTime(), retrieved.getTime());
            }
        }
    }

    /**
     * Typów {@code java.sql.*} nawet w przeszłości nie powinno było się używać poza kontekstem
     * zapisu i odczytu z bazy danych. Obecnie można nie korzystać z nich wcale, wykorzystując
     * bezpośrednio typy JSR-310. W razie konieczności użycia typów {@code java.sql.*} należy
     * tworzyć ich obiekty w ostatniej chwili i konwertować na typy JSR-310 od razu po otrzymaniu.
     */
    @Test
    void demonstrateJdbcWithJsr310Types() throws SQLException {
        LocalDateTime localDateTime = LocalDateTime.of(2020, Month.DECEMBER, 30, 14, 25, 31);
        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.of("+04:00"));
        LocalDate localDate = localDateTime.toLocalDate();

        System.out.println(localDateTime);

        try (Connection connection = getConnection()) {
            createTemporaryTable(connection);

            try (PreparedStatement statement = connection.prepareStatement("UPDATE datetime SET " +
                    "timestamp = ?, timestamptz = ?, date = ?")) {
                statement.setObject(1, localDateTime);
                statement.setObject(2, offsetDateTime);
                statement.setObject(3, localDate);
                statement.executeUpdate();
            }

            System.out.printf("%n===========%n%n");
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT timestamp, timestamptz, date FROM datetime")) {
                resultSet.next();
                System.out.println(resultSet.getObject("timestamp", LocalDateTime.class));
                System.out.println(resultSet.getTimestamp("timestamp").toLocalDateTime()); // bezpieczne, jeśli nie zmieni się strefa
                System.out.println(resultSet.getTimestamp("timestamp").toInstant()); // uwaga, domyślna strefa czasowa
                System.out.println(resultSet.getObject("timestamptz", OffsetDateTime.class)); // zawsze w UTC :o
                System.out.println(resultSet.getObject("date", LocalDate.class));
                System.out.println(resultSet.getDate("date").toLocalDate()); // bezpieczne, jeśli nie zmieni się strefa
            }

            TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.ofHours(-6)));

            System.out.printf("%n===========%n%n");
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT timestamp, timestamptz, date FROM datetime")) {
                resultSet.next();
                System.out.println(resultSet.getObject("timestamp", LocalDateTime.class));
                System.out.println(resultSet.getTimestamp("timestamp").toLocalDateTime()); // bezpieczne, jeśli nie zmieni się strefa
                System.out.println(resultSet.getTimestamp("timestamp").toInstant()); // uwaga, domyślna strefa czasowa
                System.out.println(resultSet.getObject("timestamptz", OffsetDateTime.class)); // zawsze w UTC :o
                System.out.println(resultSet.getObject("date", LocalDate.class));
                System.out.println(resultSet.getDate("date").toLocalDate()); // bezpieczne, jeśli nie zmieni się strefa
            }
        }
    }

    /**
     * Opakowuje typy {@code xs:date}, {@code xs:time}, {@code xs:dateTime} i pokrewne zdefiniowane
     * w <a href=https://www.w3.org/TR/xmlschema-2/#dateTime>standardzie XML-a</a> i obsługuje konwersje
     * na ich leksykalną (stringową) reprezentację, która jest oparta i zbliżona do ISO 8601.
     * <br><br>
     * Ten typ domyślnie użyty jest klasach generowanych narzędziem XJC.
     */
    @Test
    void demonstrateXmlGregorianCalendar() {
        System.out.println(getXmlDatatypeFactory().newXMLGregorianCalendar("2020-12-29"));
        System.out.println(getXmlDatatypeFactory().newXMLGregorianCalendar("20:00:21.123456789"));
        System.out.println(getXmlDatatypeFactory().newXMLGregorianCalendar("2020-12-29T20:00:21.123456789"));

        XMLGregorianCalendar calendar = getXmlDatatypeFactory().newXMLGregorianCalendar("2020-12-29T20:00:21.123456789+02:00");
        System.out.println(calendar);
        System.out.printf("%n===========%n%n");
        System.out.println("YEAR: " + calendar.getEonAndYear());
        System.out.println("MONTH: " + calendar.getMonth()); // 1-12
        System.out.println("DAY: " + calendar.getDay());
        System.out.println("HOUR: " + calendar.getHour());
        System.out.println("MINUTE: " + calendar.getMinute());
        System.out.println("SECOND: " + calendar.getSecond());
        System.out.println("FRACTIONAL_SECOND: " + calendar.getFractionalSecond());
        System.out.println("TIMEZONE: " + calendar.getTimezone());
    }

    /**
     * Pod względem API {@link XMLGregorianCalendar} zbliżony jest do {@link Calendar}.
     * Nie zależy jednak od {@link Locale}.
     */
    @Test
    void demonstrateXmlGregorianCalendarIsMutable() {
        XMLGregorianCalendar calendar = getXmlDatatypeFactory().newXMLGregorianCalendar("2020-12-29T20:00:21.123456789+02:00");
        System.out.println(calendar);

        calendar.setMonth(10);
        calendar.setTime(0, 11, 33);
        calendar.setFractionalSecond(null);
        calendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
        System.out.println(calendar);
    }

    /**
     * Tak jak typy {@code java.sql.*}, {@link XMLGregorianCalendar} jest sprofilowany pod konkretny
     * standard komunikacji (wymiany danych) i nie powinien być używany poza tym kontekstem. Nie zapewnia
     * silnego typowania różnych typów XML Schema i ma ograniczone metody biznesowe.
     */
    @Test
    @Disabled("zob. TimeUtils i TimeUtilsTest")
    void demonstrateXmlGregorianCalendarConversions() {
        // zob. TimeUtils i TimeUtilsTest
    }

    /**
     * Najlepszym rozwiązaniem jest jednak zastąpienie typu {@link XMLGregorianCalendar} typami JSR-310
     * bezpośrednio w generowanych klasach.
     */
    @Test
    void demonstrateXmlSerializationWithJsr310Types() {
        Bean bean = new Bean();
        bean.setDate(getXmlDatatypeFactory().newXMLGregorianCalendar("2020-12-29"));
        bean.setTime(getXmlDatatypeFactory().newXMLGregorianCalendar("20:00:21.123456789"));
        bean.setDatetime(getXmlDatatypeFactory().newXMLGregorianCalendar("2020-12-29T20:00:21.123456789+02:00"));
        System.out.println(bean);

        String xml = serializeToXml(bean);
        System.out.println(xml);

        ImprovedBean copy = deserializeFromXml(xml, ImprovedBean.class);
        System.out.println(copy);

        xml = serializeToXml(copy);
        System.out.println(xml);

        Bean finalCopy = deserializeFromXml(xml, Bean.class);
        System.out.println(finalCopy);

        assertEquals(bean, finalCopy);
    }

}
