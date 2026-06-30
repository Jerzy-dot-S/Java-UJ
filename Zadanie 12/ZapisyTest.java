import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ZapisyTest {
    static final String DB_FILE = "test_baza.db";
    Zapisy zapisy;

    @BeforeEach
    void setUp() throws Exception {
        Files.deleteIfExists(Path.of(DB_FILE));
        zapisy = new Zapisy();
        zapisy.plikBazy(DB_FILE);
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE)) {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE Studenci (student_id INTEGER PRIMARY KEY, imie TEXT, nazwisko TEXT)");
            st.executeUpdate("CREATE TABLE Kursy (kurs_id INTEGER PRIMARY KEY, nazwa_kursu TEXT)");
            st.executeUpdate("CREATE TABLE Zapisy (student_id INTEGER, kurs_id INTEGER)");
            st.executeUpdate("INSERT INTO Studenci VALUES (1, 'Jan', 'Kowalski'), (2, 'Anna', 'Nowak')");
            st.executeUpdate("INSERT INTO Kursy VALUES (1, 'Matematyka'), (2, 'Fizyka')");
            st.executeUpdate("INSERT INTO Zapisy VALUES (1, 1), (1, 2), (2, 1)");
        }
    }

    @AfterEach
    void tearDown() {
        zapisy.close();
        try { Files.deleteIfExists(Path.of(DB_FILE)); } catch (Exception ignored) {}
    }

    @Test
    void testWybraneKursy() {
        Student jan = new Student("Jan", "Kowalski");
        List<String> kursy = zapisy.wybraneKursy(jan);
        assertTrue(kursy.contains("Matematyka"));
        assertTrue(kursy.contains("Fizyka"));
        assertEquals(2, kursy.size());
    }

    @Test
    void testIluStudentow() {
        assertEquals(2, zapisy.iluStudentow("Matematyka"));
        assertEquals(1, zapisy.iluStudentow("Fizyka"));
        assertEquals(0, zapisy.iluStudentow("Biologia"));
    }

    @Test
    void testKtoWybral() {
        List<Student> studenci = zapisy.ktoWybral("Matematyka");
        assertTrue(studenci.contains(new Student("Jan", "Kowalski")));
        assertTrue(studenci.contains(new Student("Anna", "Nowak")));
        assertEquals(2, studenci.size());
    }
}
