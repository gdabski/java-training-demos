package gdabski.training.datetime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.postgresql.ds.PGSimpleDataSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static java.lang.String.join;

public final class DateTimeTypesUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper.enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);
    }

    private static final PGSimpleDataSource dataSource = new PGSimpleDataSource();

    static {
        dataSource.setServerNames(new String[]{""});
        dataSource.setDatabaseName("");
        dataSource.setUser("");
        dataSource.setPassword("");
        dataSource.setPortNumbers(new int[]{5432});
    }

    private static final DatatypeFactory xmlDatatypeFactory;

    static {
        try {
            xmlDatatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static final JAXBContext jaxbContext;

    static {
        try {
             jaxbContext = JAXBContext.newInstance(join(":",
                     "gdabski.training.datetime.standard",
                     "gdabski.training.datetime.improved"));
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
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

    public static String serializeToXml(Object object) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(object, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T deserializeFromXml(String xml, Class<T> clazz) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setEventHandler(new DefaultValidationEventHandler());
            JAXBElement<T> jaxbElement = unmarshaller.unmarshal(new StreamSource(new StringReader(xml)), clazz);
            return jaxbElement.getValue();
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void createTemporaryTable(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("CREATE TEMPORARY TABLE datetime (" +
                "timestamp timestamp, timestamptz timestamptz, date date)")) {
            statement.execute();
        }
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO datetime " +
                "VALUES (null, null, null)")) {
            statement.execute();
        }
    }

    public static DatatypeFactory getXmlDatatypeFactory() {
        return xmlDatatypeFactory;
    }

}
