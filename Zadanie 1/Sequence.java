    import java.util.*;
public class Sequence extends AbstractSequence {
    private int[] encodedData;
    private int storedDeltaSegmentSize;
    private int[] decodedCache;

    @Override
    public void sequence(int[] data, int deltaSegmentSize) {
        if (data == null) {
            this.encodedData = new int[0]; //Jeśli metoda sequence dostała null to pusta tablica
        } else {
            this.encodedData = Arrays.copyOf(data, data.length); //Jeśli mamy dane to tworze tablice jako kopie na której będziemy działać
        }
        this.storedDeltaSegmentSize = deltaSegmentSize; 
        this.decodedCache = null; 
    }

    @Override
    public int[] decode() {
        if (this.encodedData == null) {
            return new int[0]; //Jeśli data jest null tworzymy pustą tablice
        }
        if (this.decodedCache != null) {
            return Arrays.copyOf(this.decodedCache, this.decodedCache.length); //Jeśli mamy już zapisaną zdekodowaną tablice to ją zwracamy
        }

        List<Integer> out = new ArrayList<>();
        int i = 0;
        int n = this.encodedData.length;
        int seg = this.storedDeltaSegmentSize;

        while (i < n) {  //Pętla działa tak że ustawiamy pierwszy element a potem dodajemy 
            int base = this.encodedData[i]; //delta razy następne elementy a potem przechodzimy 
            out.add(base);                  //do następnych elementów i znowu dodajemy delta razy
            int sum = base; 
            for (int d = 1; d <= seg && i + d < n; d++) { 
                sum += this.encodedData[i + d]; 
                out.add(sum); 
            }
            i += seg + 1; 
        }

        this.decodedCache = new int[out.size()]; //Tworze nową tablice o liczbie elementów out
        for (int k = 0; k < out.size(); k++) { //Pętla po długość size
            this.decodedCache[k] = out.get(k); // Wrzucam wartości out do decoded cache
        }

        return Arrays.copyOf(this.decodedCache, this.decodedCache.length); //Zwracam kopie decodedcache
    }

    @Override
    public int[] encode(int deltaSegmentSize) {
        int[] decoded = decode();
        if (decoded.length == 0) {
            return new int[0]; //Jeśli decoded length jest 0 to zwracamy pustą tablice
        }

        List<Integer> out = new ArrayList<>();
        int i = 0;
        int n = decoded.length;
        int seg = deltaSegmentSize;
        while (i < n) {
            out.add(decoded[i]);
            for (int d = 1; d <= seg && i + d < n; d++) {
                out.add(decoded[i + d] - decoded[i + d - 1]); //Pętla działa odwortnie jak ta w decoded czyli zamiast dodawać co 
            }                                                 //iteracje następny wyraz to odejmujemy delta następnych wyrazów 
            i += seg + 1;                                     //od poprzednich i potem zapisujemy do out
        }

        int[] res = new int[out.size()];
        for (int k = 0; k < out.size(); k++) { //Tworze nową tablice do której zapisuje tą zdekodowaną
            res[k] = out.get(k);
        }
        return res;
    }

    @Override
    public boolean equals(int[] data, int deltaSegmentSize) {
        int[] decodedThis = decode();
        List<Integer> out = new ArrayList<>();

        if (data != null) { 
            int i = 0;
            int n = data.length;
            int seg = deltaSegmentSize;
            while (i < n) {
                int base = data[i];
                out.add(base);
                int sum = base;
                for (int d = 1; d <= seg && i + d < n; d++) { //Robi to samo co pętla w decode pod warunkiem że data nie jest null
                    sum += data[i + d];
                    out.add(sum);
                }
                i += seg + 1;
            }
        }

        int[] decodedOther = new int[out.size()]; //przepisuje z out do tablicy decodedother
        for (int k = 0; k < out.size(); k++) {
            decodedOther[k] = out.get(k);
        }

        return Arrays.equals(decodedThis, decodedOther); //Porównuje dwie tablice i zwraca true or false
    }

}
