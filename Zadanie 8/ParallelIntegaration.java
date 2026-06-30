import java.util.function.Function;

public interface ParallelIntegaration {
	/**
	 * Metoda ustawia funkcjÄ, ktĂłrej caĹkÄ trzeba policzyÄ.
	 * 
	 * @param function funkcja do scaĹkowania
	 */
	public void setFunction(Function<Double, Double> function);

	/**
	 * Metoda ustawia liczbÄ wÄtkĂłw jakÄ wolno uĹźyÄ do rĂłwnolegĹego liczenia caĹki.
	 * 
	 * @param threads liczba wÄtkĂłw
	 */
	public void setThreadsNumber(int threads);

	/**
	 * Metoda zleca wykonanie rachunku. CaĹka wyznaczana jest metodÄ prostokÄtĂłw.
	 * LiczbÄ podprzedziaĹĂłw przekazuje ta metoda. Liczba podprzedziaĹĂłw bÄdzie
	 * wiÄksza od liczby wÄtkĂłw.
	 * 
	 * @param range        przedziaĹ, w jakim caĹkÄ naleĹźy policzyÄ
	 * @param subintervals liczba przedziaĹĂłw
	 */
	public void calc(Range range, int subintervals);

	/**
	 * Metoda zwraca wynik rachunku.
	 * 
	 * @return wynik rachunku
	 */
	public double getResult();
}