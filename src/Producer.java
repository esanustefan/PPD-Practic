import java.util.Random;

public class Producer extends Thread{

    private final BlockingQueue queue;

    public Producer(BlockingQueue queue){
        this.queue = queue;
    }

    private static final Random random = new Random();
    int nrComenzi = 50;
    @Override
    public void run(){
        for (int i = 0; i < nrComenzi; i++)
        {
            if(!Supervisor.stop) {
                TipMancare tipMancare = TipMancare.values()[random.nextInt(TipMancare.values().length)];
                Comanda comanda = new Comanda(i + 1, tipMancare);
                synchronized (queue) {
                    queue.add(comanda);
                }
                try {
                    Thread.sleep(180);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                while(Main.producers > Main.producersDone.get()) {
                    Main.producersDone.incrementAndGet();
                }
                return;
            }
        }
        Main.producersDone.incrementAndGet();
    }
}
