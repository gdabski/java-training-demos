package gdabski.training.datetime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public final class DateTimeTypesUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final PGSimpleDataSource dataSource = new PGSimpleDataSource();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper.enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);

        dataSource.setServerNames(new String[]{""});
        dataSource.setDatabaseName("");
        dataSource.setUser("");
        dataSource.setPassword("");
        dataSource.setPortNumbers(new int[]{});
    }

    private DateTimeTypesUtils() {}

    @SuppressWarnings("deprecation")
    public static List<String> getEventsOnDateInYear1969(Date date) {
        date.setYear(69);
        return getEventsOnDate(date);
    }

    private static List<String> getEventsOnDate(@SuppressWarnings("unused") Date date) {
        return List.of("lądowanie na księżycu");
    }

    public static final class Person {

        private final Date birthdate;

        public Person(Date birthdate) {
            this.birthdate = birthdate;
        }

        public Date getBirthdate() {
            return birthdate;
        }

    }

    public static String serializeToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T deserializeFromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
