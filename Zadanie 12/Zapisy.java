import java.sql.*;
import java.util.*;

public class Zapisy implements BazaDanych {

    private Connection conn;

    @Override
    public void plikBazy(String plik) {
        try {
            String url = "jdbc:sqlite:" + plik;
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException("Nie można otworzyć bazy: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> wybraneKursy(Student student) {
        List<String> wynik = new ArrayList<>();

        String sql = """
            SELECT k.nazwa_kursu
            FROM Studenci s
            JOIN Zapisy z ON s.student_id = z.student_id
            JOIN Kursy k ON z.kurs_id = k.kurs_id
            WHERE s.imie = ? AND s.nazwisko = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.imie());
            ps.setString(2, student.nazwisko());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    wynik.add(rs.getString("nazwa_kursu"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Błąd wybraneKursy: " + e.getMessage(), e);
        }

        return wynik;
    }

    @Override
    public int iluStudentow(String nazwaKursu) {
        String sql = """
            SELECT COUNT(*) AS cnt
            FROM Zapisy z
            JOIN Kursy k ON z.kurs_id = k.kurs_id
            WHERE k.nazwa_kursu = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nazwaKursu);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Błąd iluStudentow: " + e.getMessage(), e);
        }

        return 0;
    }

    @Override
    public List<Student> ktoWybral(String nazwaKursu) {
        List<Student> wynik = new ArrayList<>();

        String sql = """
            SELECT s.imie, s.nazwisko
            FROM Studenci s
            JOIN Zapisy z ON s.student_id = z.student_id
            JOIN Kursy k ON z.kurs_id = k.kurs_id
            WHERE k.nazwa_kursu = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nazwaKursu);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String imie = rs.getString("imie");
                    String nazwisko = rs.getString("nazwisko");
                    wynik.add(new Student(imie, nazwisko));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Błąd ktoWybral: " + e.getMessage(), e);
        }

        return wynik;
    }

    @Override
    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }
}