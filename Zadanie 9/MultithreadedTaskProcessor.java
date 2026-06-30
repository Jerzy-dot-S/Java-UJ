import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MultithreadedTaskProcessor implements TaskProcessor {

    private int maxThreads;

    @Override
    public void threadsLimit(int limit) {
        this.maxThreads = limit;
    }

    @Override
    public void set(List<Task> tasks,
                    ThreadsFactory factory,
                    ResultConsumer consumer) {

        final int taskCount = tasks.size();

        final AtomicInteger startIndex = new AtomicInteger(0);
        final AtomicInteger saveIndex = new AtomicInteger(0);

        final int[] values = new int[taskCount];
        final boolean[] finished = new boolean[taskCount];

        final Object monitor = new Object();
        List<Runnable> jobList = new ArrayList<>();

        for (int i = 0; i < maxThreads; i++) {
            jobList.add(() -> {
                int index;

                while ((index = startIndex.getAndIncrement()) < taskCount) {

                    int value = tasks.get(index).result();

                    synchronized (monitor) {
                        values[index] = value;
                        finished[index] = true;

                        while (saveIndex.get() < taskCount
                                && finished[saveIndex.get()]) {

                            int pos = saveIndex.getAndIncrement();
                            consumer.save(tasks.get(pos).id(), values[pos]);
                        }

                        monitor.notifyAll();
                    }
                }
            });
        }

        List<Thread> threadPool = factory.createThreads(jobList);

        for (Thread t : threadPool) {
            t.start();
        }

        synchronized (monitor) {
            while (saveIndex.get() < taskCount) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
