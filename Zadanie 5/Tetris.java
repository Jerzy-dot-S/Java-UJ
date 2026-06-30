import java.util.List;

/**
 * Interfejs tetrisa
 */
public interface Tetris {

	/**
	 * Ustawia gĹÄbokoĹÄ studni w kratkach. Studnia ma wiersze od 1 do rows.
	 * 
	 * @param rows gĹÄbokoĹÄ studni
	 */

	void rows(int rows);

	/**
	 * Ustawia szerokoĹÄ studni w kratkach. Studnia ma kolumny od 0 do cols-1.
	 * 
	 * @param cols szerokoĹÄ studni
	 */
	void cols(int cols);

	/**
	 * Zrzut klocka bez optymalizacji. Klocek w przekazanym ksztaĹcie opuszczany
	 * jest na dno studni, aĹź do oparcia siÄ o dno i/lub elementy innych klockĂłw.
	 * 
	 * @param block zrzucany klocek.
	 */
	void drop(Block block);

	/**
	 * Zrzut z optymalizacjÄ poĹoĹźenia klocka. Zasady optymalizacji przedstawia opis
	 * zadnia.
	 * 
	 * @param block zrzucany klocek.
	 */
	void optimalDrop(Block block);

	/**
	 * Wynik pracy. Lista zawiera pozycjÄ najwyĹźszej zajÄtej kratki dla kolejnych
	 * kolumn. Stan poczÄtkowy to lista zawierajÄce same zera.
	 * 
	 * @return najwyĹźsza, zajeta kratka dla kolejnych kolumn.
	 */
	List<Integer> state();
}