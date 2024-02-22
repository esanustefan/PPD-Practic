import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Supervisor extends Thread{
    private final Map<Comanda,Status> registru;
    public static boolean stop = false;
    private Set<Comanda> comenziFinalizate = new HashSet<>();
    private int inAsteptare = 0;
    private int inProcesare = 0;
    private int finalizate = 0;

    public Supervisor(Map<Comanda,Status> registru) {
        this.registru = registru;
    }

    @Override
    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("raportSupervisor.txt", true))) {
            while (true) {
                if (System.currentTimeMillis() - Main.startTime >= Main.maxTime) {
                    stop = true;
                }
                inAsteptare = 0;
                inProcesare = 0;
                finalizate = 0;
                synchronized (registru) {
                    for (Map.Entry<Comanda, Status> entry : registru.entrySet()) {
                        switch (entry.getValue()) {
                            case IN_ASTEPTARE:
                                inAsteptare++;
                                break;
                            case IN_PROCESARE:
                                inProcesare++;
                                break;
                            case FINALIZATA:
                                comenziFinalizate.add(entry.getKey());
                                finalizate++;
                                break;
                        }
                    }

                    writer.write("\nComenzi finalizate:\n");
                    for (Comanda c : comenziFinalizate) {
                        if (c != null) {
                            writer.write(c.toString() + "\n");
                        }
                    }
                    writer.write("Numarul comenzilor in asteptare: " + inAsteptare + "\n");
                    writer.write("Numarul comenzilor in procesare: " + inProcesare + "\n");
                    writer.write("Numarul comenzilor finalizate: " + finalizate + "\n");
                    writer.write("Statistici la ora: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS")) + "\n");
                    writer.write("----------------------------------------------------------------------\n");
                    writer.flush(); // Asigură-te că datele sunt scrise în fișier

                    if (Main.echipeDone.get() == Main.echipe) {
                        break;
                    }
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Setează flag-ul de întrerupere
                    break; // Ieși din buclă în caz de întrerupere
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
