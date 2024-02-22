public class Consumer extends Thread{

    private final BlockingQueue queue;
    public Consumer(BlockingQueue queue){
        this.queue = queue;
    }

    @Override
    public void run(){
        while(true)
        {
            Comanda comanda;
            synchronized (queue) {
                comanda = queue.get();
                if (comanda != null) {
                    synchronized (Main.registru) {
                        Main.registru.put(comanda, Status.IN_ASTEPTARE);
                    }
                    if (comanda.getTipMancare().equals(TipMancare.SUSHI)) {
                        synchronized (Main.queue1) {
                            Main.queue1.add(comanda);
                        }
                    } else if (comanda.getTipMancare().equals(TipMancare.PIZZA)) {
                        synchronized (Main.queue2) {
                            Main.queue2.add(comanda);
                        }
                    } else if (comanda.getTipMancare().equals(TipMancare.PASTE)) {
                        synchronized (Main.queue3) {
                            Main.queue3.add(comanda);
                        }
                    }
                } else {
                    break;
                }
            }
        }
        Main.consumersDone.incrementAndGet();
    }
}
