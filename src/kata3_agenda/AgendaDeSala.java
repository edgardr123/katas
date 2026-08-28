package kata3_agenda;

public class AgendaDeSala {

    public static final int MAX_RESERVAS = 20;

    private Reserva[] reservas = new Reserva[MAX_RESERVAS];
    private int total = 0;
    private String salaId;

    public AgendaDeSala(String salaId) {
        this.salaId = salaId;
    }

    public boolean reservar(Reserva reserva) {
        if (reserva == null || !reserva.esValida()) {
            return false;
        }
        if (total >= MAX_RESERVAS) {
            return false;
        }

        for (int i = 0; i < total; i++) {
            if (reservas[i].getId().equals(reserva.getId())) {
                return false;
            }

            if (reserva.getFin() > reservas[i].getInicio() && reserva.getInicio() < reservas[i].getFin()) {
                return false;
            }
        }

        reservas[total] = reserva;
        total++;
        return true;
    }

    public boolean cancelar(String reservaId) {
        if (reservaId == null) return false;
        
        for (int i = 0; i < total; i++) {
            if (reservas[i].getId().equals(reservaId)) {

                for (int j = i; j < total - 1; j++) {
                    reservas[j] = reservas[j + 1];
                }
                total--;
                reservas[total] = null; 
                return true;
            }
        }
        return false;
    }

    public Reserva[] getReservas() {
        Reserva[] copia = new Reserva[total];
        for (int i = 0; i < total; i++) {
            copia[i] = reservas[i];
        }
        return copia;
    }
    public int getTotal() {
        return total;
    }

}
