package kata2b_notificador;

public class Prueba {

    public static int fallos = 0;

    public static void igual(String caso, Object esperado, Object obtenido) {
        boolean ok = String.valueOf(esperado).equals(String.valueOf(obtenido));
        if (!ok) {
            fallos = fallos + 1;
        }
        System.out.println((ok ? "OK    " : "FALLA ") + caso
                + "   esperado=" + esperado + "   obtenido=" + obtenido);
    }

    public static void resumen() {
        if (fallos == 0) {
            System.out.println("\nTODAS LAS PRUEBAS PASARON");
        } else {
            System.out.println("\nPRUEBAS FALLIDAS: " + fallos);
        }
    }
}
