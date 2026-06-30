import java.util.Set;

/**
 * Opis pojedynczego klocka
 */
public interface Block {
	/**
	 * Kratka odniesienia. Metoda nigdy nie zwraca null. Kratka odniesienia zawsze
	 * istnieje.
	 * 
	 * @return pozycja kratki odniesienia
	 */
	Position base();

	/**
	 * Dodatkowe kratki wchodzÄce w skĹad klocka. Liczba dodatkowych kratek moĹźe byÄ
	 * rĂłwna zero - w takim przypadku metoda zwraca zbiĂłr pusty, a ksztaĹt skĹada
	 * siÄ wyĹÄcznie z jednej kratki, ktĂłra jest jednoczeĹnie kratkÄ odniesienia.
	 * 
	 * @return zbiĂłr dodatkowych kratek
	 */
	Set<Vector> squares();
}