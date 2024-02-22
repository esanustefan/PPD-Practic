import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static final BlockingQueue queue = new BlockingQueue();
    public static final BlockingQueue queue1 = new BlockingQueue();
    public static final BlockingQueue queue2 = new BlockingQueue();
    public static final BlockingQueue queue3 = new BlockingQueue();
    public static final Map<Comanda, Status> registru = new HashMap<>();

    public static int producers = 5;
    public static int consumers = 2;
    public static int echipe = 3;
    public static AtomicInteger producersDone = new AtomicInteger(0);
    public static AtomicInteger consumersDone = new AtomicInteger(0);
    public static AtomicInteger echipeDone = new AtomicInteger(0);

    public static long startTime;
    public static long maxTime = 7000;



    public static void main(String[] args) {
        startTime = System.currentTimeMillis();
        Producer[] produceri = new Producer[producers];
        for(int i = 0; i < producers; i++)
        {
            produceri[i] = new Producer(queue);
            produceri[i].start();
        }
        Consumer[] specialisti = new Consumer[consumers];
        for(int i = 0; i < consumers; i++)
        {
            specialisti[i] = new Consumer(queue);
            specialisti[i].start();
        }

        Echipa echipa1 = new Echipa (1, queue1);
        Echipa echipa2 = new Echipa (2, queue2);
        Echipa echipa3 = new Echipa (3, queue3);
        echipa1.start();
        echipa2.start();
        echipa3.start();

        Supervisor supervisor = new Supervisor(registru);
        supervisor.start();

        for (int i = 0; i < producers; i++)
        {
            try {
                produceri[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        queue.notifica();

        for (int i = 0; i < consumers; i++)
        {
            try {
                specialisti[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try{
            echipa1.join();
            echipa2.join();
            echipa3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            supervisor.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}