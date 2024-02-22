import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue {
    private final Queue<Comanda> coada = new LinkedList<>();
    public synchronized Comanda get() {
        while (coada.isEmpty() && Main.producersDone.get() < Main.producers) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException("Eroare producator");
            }
        }
        var rez = coada.poll();
        //this.notifyAll();
        return rez;
    }

    public synchronized void add(Comanda comanda) {
        /*while(coada.size() >= 10) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException("Eroare producator");
            }
        }*/
        coada.add(comanda);
        this.notify();
    }

    public synchronized void notifica() {
        this.notifyAll();
    }

    public synchronized int size() {
        return coada.size();
    }

    public synchronized boolean isEmpty() {
        return coada.isEmpty();
    }
}
