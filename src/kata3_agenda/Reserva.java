package kata3_agenda;

public class Reserva {
    private final String id;
    private final String solicitante;
    private final int inicio;
    private final int fin;

    public Reserva(String id, String solicitante, int inicio, int fin) {
        this.id = id;
        this.solicitante = solicitante;
        this.inicio = inicio;
        this.fin = fin;
    }

    public String getId() {
        return id;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public int getInicio() {
        return inicio;
    }

    public int getFin() {
        return fin;
    }

    public boolean esValida() {
        if (id == null || id.trim().isEmpty()) return false;
        if (solicitante == null || solicitante.trim().isEmpty()) return false;
        if (inicio < 0 || fin > 1440) return false;
        if (inicio >= fin) return false;
        return true;
    }
}