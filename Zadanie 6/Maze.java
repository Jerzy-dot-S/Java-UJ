import java.util.List;
import java.util.Set;

/**
 * Interfejs labiryntu.
 */
public interface Maze {

	/**
	 * Ustawia wysokoĹÄ planszy w polach.
	 * 
	 * @param rows wysokoĹÄ planszy
	 */

	void rows(int rows);

	/**
	 * Ustawia szerokoĹÄ planszy w polach.
	 * 
	 * @param cols szerokoĹÄ planszy
	 */
	void cols(int cols);

	/**
	 * Metoda przekazuje zbiĂłr zajÄtch pĂłl planszy.
	 * 
	 * @param squares zbiĂłr zajÄtych pĂłl
	 */
	void occupiedSquare(Set<Square> squares);

	/**
	 * Zwracana lista zawiera najwiÄkszy wiersz, do ktĂłrego udaĹo siÄ dotrzeÄ
	 * wypeĹniajÄc puste pola planszy zaczynajÄc od kolejnych pĂłl wiersza zerowego.
	 * 
	 * @return lista najwyĹźszych wierszy osiÄgniÄtych w trakcie wypeĹniania planszy.
	 */
	List<Integer> howFar();

	/**
	 * Zwracana lista zawiera pole powierzchni wypeĹnionego obszaru, gdy wypeĹnianie
	 * rozpoczÄto od kolejnych pĂłl wiersza zerowego. Pole powierzchni wyraĹźone jest
	 * w liczbie zajÄtych pĂłl.
	 * 
	 * @return lista pĂłl powierzchni osiÄgniÄtych w trakcie wypeĹniania planszy.
	 */
	List<Integer> area();

	/**
	 * Metoda zwraca zbiĂłr tych pĂłl, do ktĂłrych nigdy nie udaĹo siÄ dotrzeÄ w
	 * procesie wypeĹniania planszy.
	 * 
	 * @return zbiĂłr pĂłl, do ktĂłrych nie moĹźna dotrzeÄ rozpoczynajÄc wypeĹnianie
	 *         planszy od kolejnych pĂłl wiersza zero.
	 * 
	 */
	Set<Square> unreachableSquares();

}