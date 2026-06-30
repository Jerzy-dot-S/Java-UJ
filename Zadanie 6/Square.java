import java.util.Set;

/**
 * Pole na planszy do gry. Pole opisuje para liczb caĹkowitych: kolumna i
 * wiersz.
 * 
 * @param col kolumna
 * @param row wiersz
 */

public record Square(int col, int row) {

	/**
	 * ZbiĂłr wszystkich potencjalnych sÄsiadĂłw. Metoda nie zna rozmiarĂłw planszy.
	 * Zawsze zwraca zbiĂłr czterech moĹźliwych sÄsiadĂłw.
	 * 
	 * @return zbiĂłr potencjalnych sÄsiadĂłw danego pola.
	 */
	public Set<Square> neighbours() {
		return Set.of(new Square(col + 1, row), new Square(col - 1, row), new Square(col, row + 1),
				new Square(col, row - 1));
	}
}