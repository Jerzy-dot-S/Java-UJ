public class PerformanceTest {

    public static void main(String[] args) {
        int subintervals = 300_000_000;
        Range range = new SimpleRange(-1.0, 3.0);

        // SEKWENCYJNIE (1 wątek)
        MultithreadedIntegaration seq = new MultithreadedIntegaration();
        seq.setFunction(new EvilFunction());
        seq.setThreadsNumber(1);

        long t1 = System.nanoTime();
        seq.calc(range, subintervals);
        long t2 = System.nanoTime();

        double seqTime = (t2 - t1) / 1e9;
        System.out.println("1 wątek: " + seqTime + " s");

        // RÓWNOLEGLE
        int threads = Runtime.getRuntime().availableProcessors()/4;

        MultithreadedIntegaration par = new MultithreadedIntegaration();
        par.setFunction(new EvilFunction());
        par.setThreadsNumber(threads);

        long t3 = System.nanoTime();
        par.calc(range, subintervals);
        long t4 = System.nanoTime();

        double parTime = (t4 - t3) / 1e9;
        System.out.println(threads + " wątków: " + parTime + " s");

        double efficiency = seqTime / (parTime * threads);
        System.out.println("Efektywność E = " + efficiency);
    }
}