package kata3_agenda;

/**
 * Kata 3 - Pruebas de referencia. Los 13 casos limite del enunciado.
 *
 * Los horarios estan en minutos desde la medianoche.
 *   600 = 10:00    660 = 11:00    720 = 12:00    780 = 13:00
 *
 * Igual que en la Kata 1: cada reserva rechazada se prueba con DOS lineas,
 * una para el false y otra para verificar que getTotal() no se movio.
 */
public class MainAgenda {

    public static void main(String[] args) {

/*
*        // ------------------------------------------------------------------
*        // Caso 1: una reserva contiene completamente a otra -> rechazada
*        // ------------------------------------------------------------------
*        AgendaDeSala a1 = new AgendaDeSala("Sala A");
*        Prueba.igual("0a la agenda nace vacia", 0, a1.getTotal());
*        Prueba.igual("0b primera reserva 600-660", true,
*                a1.reservar(new Reserva("R1", "Ana", 600, 660)));
*        Prueba.igual("0c la agenda tiene una", 1, a1.getTotal());
*
*        Prueba.igual("1a la nueva 570-700 contiene a la existente", false,
*                a1.reservar(new Reserva("R2", "Beto", 570, 700)));
*        Prueba.igual("1b el total NO cambio", 1, a1.getTotal());
*        Prueba.igual("1c la nueva 610-650 esta contenida en la existente", false,
*                a1.reservar(new Reserva("R3", "Carla", 610, 650)));
*        Prueba.igual("1d el total NO cambio", 1, a1.getTotal());
*
*        // ------------------------------------------------------------------
*        // Caso 2: traslape parcial por la izquierda -> rechazada
*        //   existente        [600======660)
*        //   nueva      [570=====610)
*        // ------------------------------------------------------------------
*        Prueba.igual("2a traslape parcial por la izquierda", false,
*                a1.reservar(new Reserva("R4", "Dora", 570, 610)));
*        Prueba.igual("2b el total NO cambio", 1, a1.getTotal());
*        Prueba.igual("2c traslape de UN SOLO minuto por la izquierda", false,
*                a1.reservar(new Reserva("R5", "Eva", 540, 601)));
*        Prueba.igual("2d el total NO cambio", 1, a1.getTotal());
*
*        // ------------------------------------------------------------------
*        // Caso 3: traslape parcial por la derecha -> rechazada
*        //   existente  [600======660)
*        //   nueva            [650=====700)
*        // ------------------------------------------------------------------
*        Prueba.igual("3a traslape parcial por la derecha", false,
*                a1.reservar(new Reserva("R6", "Hugo", 650, 700)));
*        Prueba.igual("3b el total NO cambio", 1, a1.getTotal());
*        Prueba.igual("3c traslape de UN SOLO minuto por la derecha", false,
*                a1.reservar(new Reserva("R7", "Ines", 659, 700)));
*        Prueba.igual("3d el total NO cambio", 1, a1.getTotal());
*
*        // ------------------------------------------------------------------
*        // Caso 4: EL QUE MAS GENTE REPRUEBA.
*        // El fin de una es igual al inicio de la otra -> ACEPTADA.
*        // Los intervalos son [inicio, fin): 600-660 ocupa hasta el 659.
*        //   existente  [600======660)
*        //   nueva                 [660======720)   se tocan, no se traslapan
*        // ------------------------------------------------------------------
*        Prueba.igual("4a pegada por la derecha: 660-720 SE ACEPTA", true,
*                a1.reservar(new Reserva("R8", "Jose", 660, 720)));
*        Prueba.igual("4b el total subio a 2", 2, a1.getTotal());
*        Prueba.igual("4c pegada por la izquierda: 540-600 SE ACEPTA", true,
*                a1.reservar(new Reserva("R9", "Luz", 540, 600)));
*        Prueba.igual("4d el total subio a 3", 3, a1.getTotal());
*
*        // ------------------------------------------------------------------
*        // Caso 5: reserva identica a una existente -> rechazada
*        // (mismo horario, otro id y otro solicitante: lo que choca es el
*        //  horario, no el nombre)
*        // ------------------------------------------------------------------
*        Prueba.igual("5a horario identico al de una existente", false,
*                a1.reservar(new Reserva("R10", "Mario", 600, 660)));
*        Prueba.igual("5b el total NO cambio", 3, a1.getTotal());
*
*        // ------------------------------------------------------------------
*        // Caso 6: inicio == fin (dura 0 minutos) -> invalida y rechazada
*        // ------------------------------------------------------------------
*        Reserva vacia = new Reserva("R11", "Nora", 900, 900);
*        Prueba.igual("6a una reserva de 0 minutos es invalida", false, vacia.esValida());
*        Prueba.igual("6b y la agenda la rechaza", false, a1.reservar(vacia));
*        Prueba.igual("6c el total NO cambio", 3, a1.getTotal());
*
*        // ------------------------------------------------------------------
*        // Caso 7: fin < inicio -> invalida y rechazada
*        // ------------------------------------------------------------------
*        Reserva alReves = new Reserva("R12", "Omar", 700, 600);
*        Prueba.igual("7a una reserva con fin < inicio es invalida", false, alReves.esValida());
*        Prueba.igual("7b y la agenda la rechaza", false, a1.reservar(alReves));
*        Prueba.igual("7c el total NO cambio", 3, a1.getTotal());
*
*        // ------------------------------------------------------------------
*        // Caso 8: fuera del dia -> invalida y rechazada
*        // ------------------------------------------------------------------
*        Reserva antesDelDia = new Reserva("R13", "Pati", -10, 100);
*        Prueba.igual("8a inicio negativo es invalido", false, antesDelDia.esValida());
*        Prueba.igual("8b y la agenda la rechaza", false, a1.reservar(antesDelDia));
*
*        Reserva despuesDelDia = new Reserva("R14", "Raul", 1400, 2000);
*        Prueba.igual("8c fin mayor a 1440 es invalido", false, despuesDelDia.esValida());
*        Prueba.igual("8d y la agenda la rechaza", false, a1.reservar(despuesDelDia));
*        Prueba.igual("8e el total NO cambio", 3, a1.getTotal());
*
*        // Las orillas exactas SI son validas: 0 y 1440 estan dentro del dia.
*        Prueba.igual("8f la orilla 0-1 es valida", true,
*                new Reserva("R15", "Sara", 0, 1).esValida());
*        Prueba.igual("8g la orilla 1439-1440 es valida", true,
*                new Reserva("R16", "Tere", 1439, 1440).esValida());
*        Prueba.igual("8h id vacio la vuelve invalida", false,
*                new Reserva("   ", "Ulises", 100, 200).esValida());
*        Prueba.igual("8i solicitante null la vuelve invalida", false,
*                new Reserva("R17", null, 100, 200).esValida());
*
*        // ------------------------------------------------------------------
*        // Caso 9: cancelar un id que no existe -> false
*        // ------------------------------------------------------------------
*        Prueba.igual("9a cancelar un id inexistente", false, a1.cancelar("NO-EXISTE"));
*        Prueba.igual("9b el total NO cambio", 3, a1.getTotal());
*        Prueba.igual("9c cancelar null", false, a1.cancelar(null));
*        Prueba.igual("9d el total NO cambio", 3, a1.getTotal());
*        // R2 nunca entro a la agenda (se rechazo en el caso 1). Cancelarla
*        // tiene que fallar: lo que se rechaza no queda guardado en ningun lado.
*        Prueba.igual("9e cancelar una reserva que fue rechazada", false, a1.cancelar("R2"));
*        Prueba.igual("9f el total NO cambio", 3, a1.getTotal());
*
*        // ------------------------------------------------------------------
*        // Caso 10: reservar, cancelar y volver a reservar el mismo horario
*        // ------------------------------------------------------------------
*        AgendaDeSala a2 = new AgendaDeSala("Sala B");
*        Prueba.igual("10a primera vez", true,
*                a2.reservar(new Reserva("X1", "Ana", 600, 660)));
*        Prueba.igual("10b el horario ya esta ocupado", false,
*                a2.reservar(new Reserva("X2", "Beto", 600, 660)));
*        Prueba.igual("10c se cancela", true, a2.cancelar("X1"));
*        Prueba.igual("10d la agenda quedo vacia", 0, a2.getTotal());
*        Prueba.igual("10e el mismo horario se acepta otra vez", true,
*                a2.reservar(new Reserva("X2", "Beto", 600, 660)));
*        Prueba.igual("10f la agenda tiene una", 1, a2.getTotal());
*        Prueba.igual("10g y es la nueva, no la vieja", "Beto",
*                a2.getReservas()[0].getSolicitante());
*
*        // ------------------------------------------------------------------
*        // Caso 11: llenar las 20 y pedir la 21 -> false
*        // Bloques de 30 minutos pegados uno tras otro: [0,30) [30,60) ...
*        // Ninguno se traslapa con el anterior, justo por la regla del caso 4.
*        // ------------------------------------------------------------------
*        AgendaDeSala a3 = new AgendaDeSala("Sala C");
*        for (int i = 0; i < AgendaDeSala.MAX_RESERVAS; i++) {
*            a3.reservar(new Reserva("F" + i, "Persona" + i, i * 30, (i + 1) * 30));
*        }
*        Prueba.igual("11a la agenda quedo llena", 20, a3.getTotal());
*        Reserva laVeintiuna = new Reserva("F20", "Sobrante", 600, 630);
*        Prueba.igual("11b la 21 es valida por si misma", true, laVeintiuna.esValida());
*        Prueba.igual("11c pero la agenda ya no tiene lugar", false, a3.reservar(laVeintiuna));
*        Prueba.igual("11d el total NO cambio", 20, a3.getTotal());
*        // Y despues de hacer lugar, la misma reserva entra.
*        Prueba.igual("11e se libera un lugar", true, a3.cancelar("F0"));
*        Prueba.igual("11f ahora si cabe", true, a3.reservar(laVeintiuna));
*        Prueba.igual("11g la agenda volvio a estar llena", 20, a3.getTotal());
*
*        // ------------------------------------------------------------------
*        // Caso 12: despues de una reserva rechazada, getTotal() no se movio.
*        // Este es el caso que caza la version que guarda primero y valida
*        // despues. Se prueba tambien que el arreglo siga limpio.
*        // ------------------------------------------------------------------
*        AgendaDeSala a4 = new AgendaDeSala("Sala D");
*        a4.reservar(new Reserva("Y1", "Ana", 600, 660));
*        int antes = a4.getTotal();
*        Prueba.igual("12a reserva rechazada por traslape", false,
*                a4.reservar(new Reserva("Y2", "Beto", 630, 690)));
*        Prueba.igual("12b el total es el mismo de antes", antes, a4.getTotal());
*        Prueba.igual("12c el arreglo tampoco crecio", antes, a4.getReservas().length);
*        Prueba.igual("12d la rechazada no se puede cancelar porque no entro", false,
*                a4.cancelar("Y2"));
*        Prueba.igual("12e la que si entro sigue ahi", "Y1", a4.getReservas()[0].getId());
*
*        // ------------------------------------------------------------------
*        // Caso 13: cancelar la del medio -> sin huecos y total baja en 1
*        // ------------------------------------------------------------------
*        AgendaDeSala a5 = new AgendaDeSala("Sala E");
*        a5.reservar(new Reserva("M1", "Ana", 600, 660));
*        a5.reservar(new Reserva("M2", "Beto", 660, 720));
*        a5.reservar(new Reserva("M3", "Carla", 720, 780));
*        Prueba.igual("13a hay tres reservas", 3, a5.getTotal());
*
*        Prueba.igual("13b se cancela la del medio", true, a5.cancelar("M2"));
*        Prueba.igual("13c el total bajo en 1", 2, a5.getTotal());
*
*        Reserva[] quedan = a5.getReservas();
*        Prueba.igual("13d el arreglo devuelto mide exactamente el total", 2, quedan.length);
*        Prueba.igual("13e no quedo hueco: la posicion 0 es M1", "M1", quedan[0].getId());
*        Prueba.igual("13f no quedo hueco: la posicion 1 es M3", "M3", quedan[1].getId());
*        Prueba.igual("13g ninguna posicion quedo en null", false,
*                quedan[0] == null || quedan[1] == null);
*        // La prueba de que el recorrido funciono de verdad: el horario que
*        // quedo libre se puede volver a reservar, y los otros dos no.
*        Prueba.igual("13h el hueco de horario si se libero", true,
*                a5.reservar(new Reserva("M4", "Dora", 660, 720)));
*        Prueba.igual("13i M3 sigue defendiendo su horario", false,
*                a5.reservar(new Reserva("M5", "Eva", 720, 780)));
*        Prueba.igual("13j el total NO cambio", 3, a5.getTotal());
*
*        // Cancelar la primera y la ultima tambien tiene que funcionar.
*        Prueba.igual("13k cancelar la primera", true, a5.cancelar("M1"));
*        Prueba.igual("13l la que era segunda paso a ser primera", "M3",
*                a5.getReservas()[0].getId());
*        Prueba.igual("13m cancelar la ultima", true, a5.cancelar("M4"));
*        Prueba.igual("13n queda una", 1, a5.getTotal());
*
*        // ------------------------------------------------------------------
*        // Extra: id repetido. Esta prueba salio de la revision (REVISION.md,
*        // fila A1) y se escribio ANTES de corregir AgendaDeSala. Con la
*        // version anterior, 15a devolvia true y 15c devolvia 1: cancelar
*        // borraba una de las dos y nadie podia saber cual.
*        // ------------------------------------------------------------------
*        AgendaDeSala a7 = new AgendaDeSala("Sala G");
*        Prueba.igual("15a primera con id D1", true,
*                a7.reservar(new Reserva("D1", "Ana", 600, 660)));
*        Prueba.igual("15b otra con el MISMO id y horario libre se rechaza", false,
*                a7.reservar(new Reserva("D1", "Beto", 660, 720)));
*        Prueba.igual("15c el total NO cambio", 1, a7.getTotal());
*        Prueba.igual("15d con otro id el mismo horario si entra", true,
*                a7.reservar(new Reserva("D2", "Beto", 660, 720)));
*        Prueba.igual("15e ahora cancelar D1 es una orden sin ambiguedad", true,
*                a7.cancelar("D1"));
*        Prueba.igual("15f y quedo exactamente la otra", "D2", a7.getReservas()[0].getId());
*        Prueba.igual("15g tras cancelar, el id se puede volver a usar", true,
*                a7.reservar(new Reserva("D1", "Carla", 600, 660)));
*
*        // ------------------------------------------------------------------
*        // Extra: getReservas() devuelve una copia, no el arreglo interno
*        // ------------------------------------------------------------------
*        AgendaDeSala a6 = new AgendaDeSala("Sala F");
*        a6.reservar(new Reserva("Z1", "Ana", 600, 660));
*        Reserva[] copia = a6.getReservas();
*        copia[0] = null;
*        Prueba.igual("14a la agenda no se entero del sabotaje", 1, a6.getTotal());
*        Prueba.igual("14b la reserva sigue ahi", "Z1", a6.getReservas()[0].getId());
*        Prueba.igual("14c cada llamada devuelve un arreglo distinto", false,
*                a6.getReservas() == a6.getReservas());
*
*        Prueba.resumen();
*/
    }
}
