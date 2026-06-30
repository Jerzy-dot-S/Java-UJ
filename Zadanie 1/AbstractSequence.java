/**
 * Klasa abstrakcyjna prezentujÄca metody obsĹugi ciÄgu liczb.
 */
public abstract class AbstractSequence {
	/**
	 * Ustawienie danych, dla ktĂłrych wykonywane bÄdÄ kolejne operacje. Ponowne
	 * wykonanie metody powoduje zmianÄ obsĹugiwanego zestawu danych. Dane zapisane
	 * sÄ w postaci (kolejne pozycje tablicy):
	 * 
	 * <pre>
	 * dana delta1 delta2 ... deltaN dana delta1 delta2 ... deltaN itd.
	 * </pre>
	 * 
	 * W powyĹźszy sposĂłb zakodowany jest pewien ciÄg liczb. Idea kodowania polega na
	 * tym, Ĺźe oryginalny ciÄg liczb dzielony jest na fragmenty (podciÄgi) o
	 * rozmiarze N+1. Pierwsza liczba w podciÄgu podawana jest wprost. Kolejne N
	 * liczb to rĂłĹźnice pomiedzy kolejnymi liczbami ciÄgu. I tak:
	 * 
	 * <pre>
	 * indeks   tablica wejĹciowa    odkodowany ciÄg
	 * 0           data[0]             data[0]
	 * 1           data[1]             data[0]+data[1]
	 * 2           data[2]             data[0]+data[1]+data[2]				
	 * .           .
	 * .           .
	 * N           data[N]             data[0]+data[1]+data[2]+...+data[N]
	 * N+1         data[N+1]           data[N+1]
	 * N+2         data[N+2]           data[N+1]+data[N+2]
	 * N+2         data[N+3]           data[N+1]+data[N+2]+data[N+3]
	 * itd.
	 * </pre>
	 * 
	 * Nie ma gwarancji, Ĺźe wszystkie podciÄgi sÄ uĹźywane w caĹoĹci. Ostatni moĹźe
	 * zawieraÄ mniej niĹź N delt. Czyli, rozmiar tablicy data nie musi byÄ caĹkowitÄ
	 * wielokrotnoĹciÄ (deltaSegmentSize+1).
	 * 
	 * @param data             tablica zawierajÄca dane
	 * @param deltaSegmentSize rozmiar segmentu zawierajÄcego delty
	 *                         (deltaSegmentSize = N). PodciÄg ma efektywnie dĹugoĹÄ
	 *                         deltaSegmentSize+1
	 * 
	 */
	abstract public void sequence(int[] data, int deltaSegmentSize);

	/**
	 * Metoda zwraca odkodowany ciÄg.
	 * 
	 * @return odkodowany ciÄg
	 */
	abstract public int[] decode();

	/**
	 * Metoda koduje otrzymany metodÄ sequence ciÄg dostosowujÄc wynik do podanej
	 * dĹugoĹci segmentu zawierajÄcego delta.
	 * 
	 * @param deltaSegmentSize
	 * @return ciÄg zakodowany z podanym rozmiarem segmentu zawierajÄcego delty.
	 */
	abstract public int[] encode(int deltaSegmentSize);

	/**
	 * Metoda sprawdza, czy podany ciÄg liczb jest rĂłwnowaĹźny temu, ktĂłry wczeĹniej
	 * otrzymano za pomocÄ sequence. Metoda uwzglÄdnia zawartoĹÄ ciÄgu liczb jak i
	 * jego dĹugoĹÄ.
	 * 
	 * @param data             dane nowego ciÄgu liczb
	 * @param deltaSegmentSize rozmiar segmentu delt
	 * @return prawda - odkodowane ciÄgi liczb sÄ rĂłwnie, false - odkodowane ciÄgi
	 *         liczb do siebie nie pasujÄ.
	 */
	abstract public boolean equals(int[] data, int deltaSegmentSize);
}