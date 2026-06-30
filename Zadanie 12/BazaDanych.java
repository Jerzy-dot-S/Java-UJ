import java.util.List;

/**
 * Prosty interfejs dostÄpu do bazy zapisĂłw na kursy.
 */
public interface BazaDanych {

	/**
	 * ĹcieĹźka do pliku z baza danych SQLite. Uwaga: to tylko informacja o samym
	 * pliku.
	 * 
	 * @param plik zawierajacy baze danych
	 */
	void plikBazy(String plik);

	/**
	 * Lista kursĂłw (moĹźe byÄ pusta) wybranych przez studenta.
	 * 
	 * @param student dane studenta
	 * @return lista wybranych kursĂłw
	 */
	List<String> wybraneKursy(Student student);

	/**
	 * Liczba studentĂłw, ktĂłrzy wybrali kurs o podanej nazwie.
	 * 
	 * @param nazwaKursu nazwa kursu
	 * @return liczba studentĂłw zapisanych na kurs
	 */
	int iluStudentow(String nazwaKursu);

	/**
	 * Lista studntĂłw, ktĂłrzy wybrali kurs o podanej nazwie. Lista moĹźe byÄ pusta.
	 * 
	 * @param nazwaKursu nazwa kursu
	 * @return lista zapisanych na kurs studentĂłw.
	 */
	List<Student> ktoWybral(String nazwaKursu);

	/**
	 * Koniec pracy z baza danych.
	 */
	void close();
}