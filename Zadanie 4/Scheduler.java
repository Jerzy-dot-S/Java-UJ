import java.util.List;
import java.util.Set;

/**
 * NarzÄdzie do wyszukiwania rozkĹadu programĂłw telewizyjnych
 */
public interface Scheduler {
	/**
	 * Metoda pozwala wprowadziÄ informacjÄ o programie telewizyjnym i czasie jego
	 * trwania. Program zajmuje pewien przedziaĹ czasowy. Programy dla jednego
	 * nadawcy nie nakĹadajÄ siÄ na siebie.
	 * 
	 * @param program informacja o pojedynczym programie telewizyjnym
	 */
	public void addSlot(Slot program);

	/**
	 * NarzÄdzie optymalizacji. PrzeglÄda zarejestrowane programy telewizyjne i
	 * proponuje moĹźliwe scenariusze ich oglÄdania. KaĹźdy scenariusz (lista slotĂłw)
	 * pozwala na oglÄdniÄcie programĂłw o podanych nazwach tak, Ĺźe nie nakĹadajÄ siÄ
	 * one na siebie. PoniewaĹź moĹźe istnieÄ wiÄcej niĹź jedno rozwiÄzanie, metoda
	 * zwraca zbiĂłr poprawnych scenariuszy. W przypadku braku moĹźliwoĹci rozwiÄzania
	 * problemu, metoda zwraca pusty zbiĂłr.
	 * 
	 * @param programs zbiĂłr programĂłw, ktĂłre chcemy oglÄdnÄÄ w dowolnej kolejnoĹci
	 * @return zbiĂłr scenariuszy pozwalajÄcych na oglÄdniÄcie wszystkich programĂłw w
	 *         caĹoĹci.
	 */
	public Set<List<Slot>> match(Set<String> programs);
}