import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class MultithreadedIntegaration implements ParallelIntegaration {

    private Function<Double, Double> function;
    private int threads;
    private double result;

    @Override
    public void setFunction(Function<Double, Double> function) {
        this.function = function;
    }

    @Override
    public void setThreadsNumber(int threads) {
        this.threads = threads;
    }

    @Override
    public void calc(Range range, int subintervals) {
        final double a = range.min();
        final double width = (range.max() - a) / subintervals;

        final AtomicInteger index = new AtomicInteger(0);
        final int batchSize = Math.max(1, subintervals / (threads * 200));

        Thread[] workers = new Thread[threads];
        double[] partial = new double[threads];

        for (int t = 0; t < threads; t++) {
            final int id = t;

            workers[t] = new Thread(() -> {
                double local = 0.0;

                while (true) {
                    int start = index.getAndAdd(batchSize);
                    if (start >= subintervals) break;

                    int end = start + batchSize;
                    if (end > subintervals) end = subintervals;

                    double x = a + (start + 0.5) * width;

                    for (int i = start; i < end; i++) {
                        local += function.apply(x);
                        x += width;
                    }
                }

                partial[id] = local;
            });

            workers[t].start();
        }

        for (Thread w : workers) {
            try {
                w.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }

        double sum = 0.0;
        for (double p : partial) sum += p;

        result = sum * width;
    }

    @Override
    public double getResult() {
        return result;
    }
}
