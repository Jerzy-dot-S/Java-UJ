import java.util.Map;

/**
 * Interfejs pomocnika oceniania
 */
public interface GradesHelper {

	/**
	 * WyjÄtek zgĹaszany, gdy przedziaĹy dla dwĂłch ocen na siebie zachodzÄ.
	 */
	public class RangeConflictException extends Exception {
		private static final long serialVersionUID = -8152212610934429384L;
	}

	/**
	 * WyjÄtek zgĹaszany, gdy w pliku z zasadami oceny ta sama ocena wystÄpuje
	 * wiÄcej niĹź jeden raz i jednoczeĹnie dane przedziaĹĂłw sÄ rĂłĹźne.
	 * 
	 */
	public class MarkConflictException extends Exception {
		private static final long serialVersionUID = 6771406201150967367L;

		private final String mark;

		/**
		 * Konstruktor ustawiajÄcy pole mark.
		 * 
		 * @param mark ocena, dla ktĂłrej wykryto konflikt
		 */
		public MarkConflictException(String mark) {
			this.mark = mark;
		}

		public String getMark() {
			return mark;
		}
	}

	/**
	 * Ocena studenta o podanych danych nie byĹa moĹźliwa.
	 */
	public class AssessmentImpossible extends Exception {
		private static final long serialVersionUID = 1174503033731861293L;
		private final String firstName;
		private final String lastName;

		/**
		 * Konstruktor ustawiajÄcy dane studenta, ktĂłrego nie udaĹo siÄ oceniÄ.
		 * 
		 * @param firstName imiÄ studenta
		 * @param lastName  nazwisko studenta
		 */
		public AssessmentImpossible(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}
	}

	/**
	 * Metoda powoduje wczytanie z pliku listy studentĂłw. Plik zawiera 3 kolumny
	 * rozdzielone Ĺrednikiem. Format wiersza jest nastÄpujÄcy:
	 * 
	 * <pre>
	 * IDStudenta;ImiÄStudenta;NazwiskoStudenta;
	 * </pre>
	 * 
	 * Koniec linii oznaczony jest Ĺrednikiem. Wiersze niezgodne z powyĹźszym
	 * formatem naleĹźy odrzuciÄ (zignorowaÄ). IDStudenta jest liczbÄ caĹkowitÄ.
	 * ImiÄStudenta i NazwiskoStudenta to ciÄgi znakĂłw.
	 * 
	 * @param file nazwa pliku do wczytania
	 */
	void loadStudents(String file);

	/**
	 * Metoda wczytuje zasady oceniania. Plik zawiera trzy kolumny rozdzielone
	 * Ĺrednikami. Pierwsza kolumna to ciÄg znakĂłw, dwie pozostaĹe to liczby.
	 * Separatorem dziesietym jest kropka. Koniec linii oznaczony jest Ĺrednikiem.
	 * Wiersze niezawierajÄce danych naleĹźy ignorowaÄ. Format wiersza jest
	 * nastÄpujÄcy:
	 * 
	 * <pre>
	 * ocena;min;max;
	 * </pre>
	 * 
	 * Idea: ocena obowiÄzuje w przedziale od min punktĂłw wĹÄcznie do max punktĂłw
	 * wĹÄcznie.
	 * 
	 * @param file nazwa pliku z danymi
	 * 
	 * @throws RangeConflictException wyjÄtek zgĹaszany w przypadku kolizji
	 *                                przedziaĹow
	 * @throws MarkConflictException  kolizja - ta sama ocena pojawia siÄ z innym
	 *                                przedziaĹem min/max.
	 */
	void loadScoring(String file) throws RangeConflictException, MarkConflictException;

	/**
	 * Metoda ocenia pracÄ studenta. Dane dla jednego studenta to jeden wiersz. Dane
	 * rozdzielajÄ Ĺredniki. Separator dziesietny to kropka. Linie niezawierajÄce
	 * danych naleĹźy ignorowaÄ.
	 * 
	 * Format wiersza jest nastÄpujÄcy:
	 * 
	 * <pre>
	 * ImieStudenta;NazwiskoStudenta;nota1;nota2;....;notaN;
	 * </pre>
	 * 
	 * Metoda wczytuje dane ze wskazanego pliku. Na podstawie wczeĹniej zdobytych
	 * informacji ustala IDStudenta. W przypadku braku moĹźliwoĹci wyznaczenia
	 * IDStudenta naleĹźy zgĹosiÄ wyjÄtek. <br>
	 * NastÄpnie, ze wszystkich not studenta naleĹźy wyliczyÄ ĹredniÄ. Liczba
	 * zdobytych not moĹźe byÄ rĂłĹźna dla rĂłĹźnych studentĂłw. ĹredniÄ naleĹźy dopasowaÄ
	 * do zasad oceniania. W przypadku, gdy Ĺrednia nie pasuje do Ĺźadnego przedziaĹu
	 * min/max naleĹźy zgĹosiÄ wyjÄtek. <br>
	 * Wynikiem metody jest mapa zawierajÄca oceny (nie Ĺrednie!) studentĂłw
	 * identyfikowanych za pomocÄ identyfikatora liczbowego.
	 * 
	 * @param data plik z danymi (dane studenta i otrzymane noty)
	 * @return mapa, ktĂłrej kluczem jest IDStudenta. WartoĹÄ to ocena wyznaczona na
	 *         postawie Ĺredniej i zasad oceniania.
	 * @throws AssessmentImpossible wyjÄtek zgĹaszany w przypadku braku moĹźliwoĹci
	 *                              oceny studenta.
	 */
	Map<Integer, String> generateGrades(String data) throws AssessmentImpossible;
}