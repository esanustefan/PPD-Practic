public class Echipa extends Thread{
    private final int idEchipa;
    private BlockingQueue queue;

    public Echipa(int idEchipa, BlockingQueue queue){
        this.idEchipa = idEchipa;
        this.queue = queue;
    }

    @Override
    public void run(){
        while(!queue.isEmpty() || Main.consumersDone.get() < Main.consumers)
        {
            synchronized (queue)
            {
                Comanda comanda = queue.get();
                Main.registru.put(comanda, Status.IN_PROCESARE);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Main.registru.put(comanda, Status.FINALIZATA);
            }
        }
        Main.echipeDone.incrementAndGet();
    }

}
