package kata1_tarjeta;

/**
 * Kata 1 - Pruebas de referencia.
 *
 * Regla dura de la rubrica: cada operacion rechazada necesita DOS lineas.
 * Una que verifica que devolvio false, y otra que verifica que el estado
 * no cambio. La segunda es la que realmente prueba algo: un metodo puede
 * devolver false y haber corrompido el objeto de todas formas.
 */
public class MainTarjeta {

    public static void main(String[] args) {

        // ------------------------------------------------------------------
        // Estado inicial
        // ------------------------------------------------------------------
        TarjetaPrepago t = new TarjetaPrepago("A1", "Ana");
        Prueba.igual("0a la tarjeta nace con saldo cero", 0L, t.getSaldo());
        Prueba.igual("0b la tarjeta nace sin movimientos", 0, t.getTotalMovimientos());
        Prueba.igual("0c la tarjeta nace valida", true, t.esValida());
        Prueba.igual("0d la tarjeta nace desbloqueada", false, t.estaBloqueada());

        // ------------------------------------------------------------------
        // Caso 1: recarga que deja el saldo exactamente en el tope -> true
        // ------------------------------------------------------------------
        Prueba.igual("1a recarga hasta el tope exacto", true,
                t.recargar(TarjetaPrepago.TOPE_CENTAVOS));
        Prueba.igual("1b el saldo quedo en el tope", 500000L, t.getSaldo());
        Prueba.igual("1c se anoto el movimiento", 1, t.getTotalMovimientos());

        // ------------------------------------------------------------------
        // Caso 2: un centavo por encima del tope -> false y nada cambio
        // ------------------------------------------------------------------
        Prueba.igual("2a un centavo sobre el tope se rechaza", false, t.recargar(1));
        Prueba.igual("2b el saldo NO cambio", 500000L, t.getSaldo());
        Prueba.igual("2c el historial NO crecio", 1, t.getTotalMovimientos());

        // ------------------------------------------------------------------
        // Caso 3: cobro que deja el saldo en 0 -> true
        // ------------------------------------------------------------------
        Prueba.igual("3a cobro que deja el saldo en cero", true, t.cobrar(500000, "liquidacion"));
        Prueba.igual("3b el saldo quedo en cero", 0L, t.getSaldo());
        Prueba.igual("3c formato del movimiento", "COBRO 500000 liquidacion", t.getHistorial()[1]);

        // ------------------------------------------------------------------
        // Caso 4: cobro de exactamente saldo + 1 -> false y nada cambio
        // ------------------------------------------------------------------
        TarjetaPrepago t2 = new TarjetaPrepago("A2", "Beto");
        t2.recargar(1000);
        Prueba.igual("4a cobro de saldo+1 se rechaza", false, t2.cobrar(1001, "metro"));
        Prueba.igual("4b el saldo NO cambio", 1000L, t2.getSaldo());
        Prueba.igual("4c el historial NO crecio", 1, t2.getTotalMovimientos());
        Prueba.igual("4d cobro de exactamente el saldo si pasa", true, t2.cobrar(1000, "metro"));
        Prueba.igual("4e el saldo quedo en cero", 0L, t2.getSaldo());

        // ------------------------------------------------------------------
        // Caso 5: recargar(0) y recargar(-100) -> false
        // ------------------------------------------------------------------
        TarjetaPrepago t3 = new TarjetaPrepago("A3", "Carla");
        t3.recargar(1000);
        Prueba.igual("5a recargar(0) se rechaza", false, t3.recargar(0));
        Prueba.igual("5b recargar(-100) se rechaza", false, t3.recargar(-100));
        Prueba.igual("5c cobrar(0) se rechaza", false, t3.cobrar(0, "metro"));
        Prueba.igual("5d cobrar(-100) se rechaza", false, t3.cobrar(-100, "metro"));
        Prueba.igual("5e el saldo NO cambio", 1000L, t3.getSaldo());
        Prueba.igual("5f el historial NO crecio", 1, t3.getTotalMovimientos());

        // ------------------------------------------------------------------
        // Caso 6: titular en blanco -> invalida y todo se rechaza
        // ------------------------------------------------------------------
        TarjetaPrepago t4 = new TarjetaPrepago("A4", "   ");
        Prueba.igual("6a titular en blanco: tarjeta invalida", false, t4.esValida());
        Prueba.igual("6b recargar en tarjeta invalida", false, t4.recargar(1000));
        Prueba.igual("6c el saldo sigue en cero", 0L, t4.getSaldo());
        Prueba.igual("6d cobrar en tarjeta invalida", false, t4.cobrar(1, "metro"));
        Prueba.igual("6e el historial sigue vacio", 0, t4.getTotalMovimientos());

        TarjetaPrepago t5 = new TarjetaPrepago(null, "Dora");
        Prueba.igual("6f id null: tarjeta invalida", false, t5.esValida());
        Prueba.igual("6g recargar en tarjeta invalida", false, t5.recargar(1000));
        Prueba.igual("6h el saldo sigue en cero", 0L, t5.getSaldo());

        TarjetaPrepago t6 = new TarjetaPrepago("  A6  ", "  Eva  ");
        Prueba.igual("6i los espacios de sobra se recortan", "A6", t6.getId());
        Prueba.igual("6j el titular tambien se recorta", "Eva", t6.getTitular());
        Prueba.igual("6k con contenido real la tarjeta es valida", true, t6.esValida());

        // ------------------------------------------------------------------
        // Caso 7: escribir en el arreglo que devolvio getHistorial()
        // ------------------------------------------------------------------
        TarjetaPrepago t7 = new TarjetaPrepago("A7", "Hugo");
        t7.recargar(1000);
        String[] copia = t7.getHistorial();
        copia[0] = "HACKEADO";
        Prueba.igual("7a historial protegido", "RECARGA 1000", t7.getHistorial()[0]);
        Prueba.igual("7b el total de movimientos no cambio", 1, t7.getTotalMovimientos());
        Prueba.igual("7c getHistorial devuelve solo el tramo usado, no los 50", 1,
                t7.getHistorial().length);
        Prueba.igual("7d cada llamada devuelve un arreglo distinto", false,
                t7.getHistorial() == t7.getHistorial());

        // ------------------------------------------------------------------
        // Caso 8: historial lleno -> se rechaza y el saldo no se mueve
        // ------------------------------------------------------------------
        TarjetaPrepago t8 = new TarjetaPrepago("A8", "Ines");
        for (int i = 0; i < TarjetaPrepago.MAX_MOVIMIENTOS; i++) {
            t8.recargar(100);
        }
        Prueba.igual("8a el historial quedo lleno", 50, t8.getTotalMovimientos());
        Prueba.igual("8b saldo tras 50 recargas de 100", 5000L, t8.getSaldo());
        Prueba.igual("8c cobro con el historial lleno se rechaza", false, t8.cobrar(100, "metro"));
        Prueba.igual("8d el saldo NO cambio", 5000L, t8.getSaldo());
        Prueba.igual("8e el total de movimientos NO cambio", 50, t8.getTotalMovimientos());
        Prueba.igual("8f recarga con el historial lleno tambien se rechaza", false, t8.recargar(100));
        Prueba.igual("8g el saldo NO cambio", 5000L, t8.getSaldo());

        // ------------------------------------------------------------------
        // Caso 9: bloquear() dos veces y luego cobrar
        // ------------------------------------------------------------------
        TarjetaPrepago t9 = new TarjetaPrepago("A9", "Jose");
        t9.recargar(1000);
        t9.bloquear();
        t9.bloquear();
        Prueba.igual("9a bloquear dos veces no truena", true, t9.estaBloqueada());
        Prueba.igual("9b cobrar en tarjeta bloqueada", false, t9.cobrar(100, "metro"));
        Prueba.igual("9c el saldo NO cambio", 1000L, t9.getSaldo());
        Prueba.igual("9d recargar en tarjeta bloqueada", false, t9.recargar(100));
        Prueba.igual("9e el saldo NO cambio", 1000L, t9.getSaldo());
        Prueba.igual("9f el historial NO crecio", 1, t9.getTotalMovimientos());

        // ------------------------------------------------------------------
        // Extras: cosas que el enunciado no lista pero rompen el objeto
        // ------------------------------------------------------------------
        TarjetaPrepago t10 = new TarjetaPrepago("A10", "Luz");
        t10.recargar(1000);
        Prueba.igual("10a cobro con concepto null se rechaza", false, t10.cobrar(100, null));
        Prueba.igual("10b el saldo NO cambio", 1000L, t10.getSaldo());
        Prueba.igual("10c cobro con concepto en blanco se rechaza", false, t10.cobrar(100, "   "));
        Prueba.igual("10d el historial NO crecio", 1, t10.getTotalMovimientos());

        // El desbordamiento: si recargar preguntara (saldo + centavos > TOPE),
        // con un long enorme la suma se pasa de largo, da negativo, y la
        // comparacion contra el tope pasaria por error.
        TarjetaPrepago t11 = new TarjetaPrepago("A11", "Mario");
        Prueba.igual("11a recarga absurda se rechaza sin desbordarse", false,
                t11.recargar(9223372036854775807L));
        Prueba.igual("11b el saldo sigue en cero", 0L, t11.getSaldo());

        Prueba.resumen();
    }
}
