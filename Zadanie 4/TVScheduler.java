import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TVScheduler implements Scheduler {

    private Map<String, List<Slot>> slotsByProgram = new LinkedHashMap<>();

    @Override
    public void addSlot(Slot program) {
        List<Slot> list = slotsByProgram.computeIfAbsent(program.program(), k -> new ArrayList<>()); 
        if (!list.contains(program)) { //Pobiera liste slotów dla programów jeśli jej nie ma to tworzy nową i wkłada do mapy i potem
            list.add(program);          //Jeśli lista nie ma takiego porgramu to doadje do listy
        }
    }

    @Override
    public Set<List<Slot>> match(Set<String> programs) {
        Set<List<Slot>> result = new HashSet<>();
        if (programs == null) return result; //Jak puste to zwracamy pusty result 

        if (programs.isEmpty()) {
            result.add(new ArrayList<>()); //Jak nie mamy żadnych programów które chcemy obejrzeć to zwracamy pustą liste bo nie ma żadnych możliwości obejrzenia programu 
            return result;
        }//

        Map<String, List<Slot>> available = new LinkedHashMap<>();
        for (String p : programs) { //Pęlta for each dla każdego programu
            List<Slot> list = slotsByProgram.get(p); //Pobieram wszystkie godziny emisji
            if (list == null || list.isEmpty()) { //Jak null lub puste to nie da sie obejrzeć wszytkich programów więc zwracam pusty zbiór
                return result;
            }
            List<Slot> copy = new ArrayList<>(list); //kopia
            copy.sort((a, b) -> Integer.compare(start(a), start(b))); //Sortuje po czasie rozpoczęcia
            available.put(p, copy); //Wstawiam do mapy posortowane
        }

        List<String> ordered = new ArrayList<>(available.keySet()); 
        ordered.sort((a, b) -> {
            int sa = available.get(a).size(); 
            int sb = available.get(b).size();
            if (sa != sb) return Integer.compare(sa, sb); //Porównuje na zasadzie ilości dostępnych emisji
            return a.compareTo(b);
        });

        backtrack(ordered, available, 0, new ArrayList<>(), result);
        return result;
}

    private static int start(Slot s) {
        return s.atH() * 60 + s.atM();
    }

    private static int end(Slot s) {
        return start(s) + s.duration();
    }

    private static boolean overlaps(Slot a, Slot b) {
        return start(a) < end(b) && start(b) < end(a);
    }

    //Dodatkowa metoda do sprawdzania wszystkich możliwości obejrzenia wybranych programów
    private void backtrack(List<String> ordered, Map<String, List<Slot>> available, int idx, List<Slot> current, Set<List<Slot>> result) {
        if (idx == ordered.size()) {
            result.add(new ArrayList<>(current)); //Jeśli mamy już sprawdzone wszystkie programy to dodajemy kopie do result i zwracamy
            return;
        }

        String program = ordered.get(idx);
        for (Slot candidate : available.get(program)) {
            boolean ok = true;
            for (Slot chosen : current) {
                if (overlaps(candidate, chosen)) {
                    ok = false; //Sprawdzamy czy programy się nakładają
                    break;
                }
            }
            if (!ok) continue;

            current.add(candidate); //Jak nie nakłada się to dodaje do current
            backtrack(ordered, available, idx + 1, current, result); //Rekurencja
            current.remove(current.size() - 1);  //Usuwam ostatni element
        }
    }
}