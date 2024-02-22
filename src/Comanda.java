public class Comanda {
    private int idComanda;
    private TipMancare tipMancare;

    public Comanda(int idComanda, TipMancare tipMancare) {
        this.idComanda = idComanda;
        this.tipMancare = tipMancare;
    }

    public int getIdComanda() {
        return idComanda;
    }

    public TipMancare getTipMancare() {
        return tipMancare;
    }


    public String toString() {
        return "ID Comanda: " + idComanda + ", Tip Mancare: " + tipMancare;
    }
}
