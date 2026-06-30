import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractHistogramPatternMatcher {
	/**
	 * Metoda umoĹźliwia uĹźytkownikowi wprowadzanie danych.
	 * 
	 * @param value wprowadzana przez uĹźytkownika liczba
	 */
	abstract public void data(int value);

	/**
	 * Metoda zwraca histogram. Histogram nie moĹźe zawieraÄ pozycji zawierajÄcych
	 * zero zliczeĹ. JeĹli wywoĹanie metody nastÄpi przed prowadzeniem danych metoda
	 * zwraca pustÄ mapÄ (mapÄ o rozmiarze 0). Metoda nigdy nie zwraca NULL.
	 * 
	 * @return mapa reprezentujÄca histogram. Klucz to liczba, wartoĹÄ wskazywana
	 *         kluczem, to liczba wystÄpieĹ danej liczby we wprowadzanych danych.
	 */
	abstract public Map<Integer, Integer> histogram();

	/**
	 * Metoda zwraca zbiĂłr kluczy z histogramu. Do zbioru wprawdzane sÄ te klucze,
	 * od ktĂłrych zaczyna siÄ sekwencja zliczeĹ, ktĂłra pasuje do wskazanego wzorca.
	 * Wzorzec naleĹźy rozumieÄ na zasadzie proporcji pomiÄdzy kolejnymi zliczeniami.
	 * Wzorzec: [2,1,2] pasowaÄ bÄdzie np. do sekwencji zliczeĹ 10:5:10 czy 4:2:4,
	 * jednoczeĹnie wzorzec ten nie pasuje np. do 11:5:10. Brak odpowiedzi metoda
	 * sygnalizuje zwracajÄc zbiĂłr pusty. Metoda nigdy nie zwraca NULL.
	 * 
	 * @param pattern wzorzec kolejnych zliczeĹ w histogramie
	 * @return zbiĂłr liczby rozpoczynajÄcych sekwencjÄ zliczeĹ
	 */
	abstract public Set<Integer> match(List<Integer> pattern);

}