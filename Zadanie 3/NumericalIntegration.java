
/**
 * Interfejs caĹkowania numerycznego funkcji jednej zmiennej.
 */
public interface NumericalIntegration {
	/**
	 * Ustawienie funkcji do scaĹkowania numerycznego
	 * 
	 * @param f funkcja
	 */
	void setFunction(Function f);

	/**
	 * Obliczenia caĹki w zadanym zakresie liczb (range). Zakres naleĹźy podzieliÄ na
	 * wskazanÄ liczbÄ przedziaĹow (subintervals).
	 * 
	 * @param range        zakres liczb dla jakich wyznaczana jest caĹka
	 * @param subintervals liczba podprzedziaĹĂłw
	 * @return wynik caĹkowania numerycznego
	 */
	double integrate(Range range, int subintervals);
}