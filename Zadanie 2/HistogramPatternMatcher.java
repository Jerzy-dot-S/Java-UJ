import java.util.*;

public class HistogramPatternMatcher extends AbstractHistogramPatternMatcher {
    private final Map<Integer, Integer> dataMap = new TreeMap<>(); //Tworze mape o nazwie dataMap

    @Override
    public void data(int value) {
        dataMap.put(value, dataMap.getOrDefault(value, 0) + 1); //Do mapy wstawiam value
    }

    @Override
    public Map<Integer, Integer> histogram() {
        if (dataMap.isEmpty()) {
            return new TreeMap<>(); //Jak nie mam żadnych wartości to zwracam nową pustą mape
        }
        return new TreeMap<>(dataMap); //Jak są to kopia
    }

    @Override
    public Set<Integer> match(List<Integer> pattern) {
        Set<Integer> result = new TreeSet<>(); //Nowa lista dla zwracanych wartości

        if (pattern == null || pattern.size() < 2) {
            return result; //Jeśli nie ma pattern albo mniejszy od 2 to zwróć pustą liste
        }

        if (dataMap.isEmpty()) {
            return result; //Jeśli nie ma wartości w datamap to też zwróć pustą liste
        }

        int minKey = ((TreeMap<Integer, Integer>) dataMap).firstKey(); //Ustawienie wskaźników na pierwszy i ostatni element
        int maxKey = ((TreeMap<Integer, Integer>) dataMap).lastKey();
        int patternlength = pattern.size();

        boolean patternAllOnes = true;
        for (int i = 0; i < pattern.size(); i++) {
            if (pattern.get(i) != 1) { 
                patternAllOnes = false;  // sprawdzam czy pattern to same jedynki 
                break; 
            }
        }

        for (int k = minKey; k <= maxKey; k++) {
            if (k + patternlength - 1 > maxKey) {  //Jeśli sprawdzam pattern który przekracza odległość obecnego       
                break;                             //od ostatniego klucza to przerywam pętle
            }

            int baseCount = dataMap.getOrDefault(k, 0);
            boolean patternmatch = true;
            if (baseCount == 0) {  //Jak mamy 0 w dataMap to sprawdzam czy cały pattern to 1
                if (!patternAllOnes) { 
                    patternmatch = false; 
                } else {
                    for (int i = 0; i < patternlength; i++) {
                        int key = k + i;
                        int current = dataMap.getOrDefault(key, 0); //Jak jest 1 to sprawdzam dla następnych liczb czy są 0
                        if (current != 0) {                                      // bo jak zaczynam od 0 to pattern (1, 1, ... 1) działa tylko
                            patternmatch = false;                                // dla 0
                            break; 
                        }
                    }
                }
            } else { //Jeśli obecna wartość nie jest 0
                for (int i = 0; i < patternlength; i++) {
                    int key = k + i;
                    int current = dataMap.getOrDefault(key, 0); //Pętla sprawdza dla każdej wartości czy 
                    long expected = (long) baseCount * (long) pattern.get(i); //Zgadza się z pattern poprzez mnożenie
                    if (current != expected) {                                //obecnej wartości z pattern i porównywanie
                        patternmatch = false;                                           //z następną wartością
                        break; 
                    }
                }
            }
            if (patternmatch) {
                result.add(k); //Dodaje do result
            }
        }
        return result; //Zwracam wynik
    }

}