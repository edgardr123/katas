package kata1_tarjeta;

/**
 * Kata 1 - Solucion de referencia.
 *
 * La idea que se esta demostrando: encapsular no es "private + getters y setters".
 * Es que NO EXISTA NINGUNA SECUENCIA DE LLAMADAS que deje a este objeto en un
 * estado imposible. Las tres tecnicas que lo logran aqui son:
 *
 *   1. Validar TODO antes de tocar un solo campo (metodos recargar y cobrar).
 *   2. No tener setters ni campos publicos.
 *   3. No entregar nunca el arreglo interno (getHistorial devuelve una copia).
 */
public class TarjetaPrepago {

    public static final long TOPE_CENTAVOS = 500000;   // $5,000.00
    public static final int  MAX_MOVIMIENTOS = 50;

    private String id;
    private String titular;
    private long saldo;
    private boolean bloqueada;
    private boolean valida;

    private String[] historial = new String[MAX_MOVIMIENTOS];
    private int totalMovimientos = 0;

    /**
     * Un constructor no devuelve nada, asi que no puede avisar de un error.
     * Regla 2 del curso: marcamos el objeto como invalido y que todas sus
     * operaciones se rechacen. No normalizamos: un titular vacio no tiene
     * ninguna interpretacion razonable, a diferencia de unos minutos negativos.
     *
     * La tarjeta nace en saldo 0 a proposito. Si el constructor recibiera un
     * saldo inicial, tendria que poder rechazarlo (negativo, o arriba del tope)
     * y no puede. Se elimina el problema quitando el parametro.
     */
    public TarjetaPrepago(String id, String titular) {
        this.valida = !esTextoVacio(id) && !esTextoVacio(titular);
        if (this.valida) {
            this.id = id.trim();
            this.titular = titular.trim();
        } else {
            this.id = "";
            this.titular = "";
        }
        this.saldo = 0;
        this.bloqueada = false;
    }

    public String getId() {
        return id;
    }

    public String getTitular() {
        return titular;
    }

    public long getSaldo() {
        return saldo;
    }

    public boolean esValida() {
        return valida;
    }

    public boolean estaBloqueada() {
        return bloqueada;
    }

    /**
     * Orden de validacion: primero lo que descalifica al objeto entero
     * (invalida, bloqueada), luego lo que descalifica al argumento
     * (centavos <= 0), luego lo que descalifica al resultado
     * (no cabe en el historial, se pasa del tope).
     *
     * La linea que separa el "puede fallar" del "ya no puede fallar" esta
     * marcada abajo. Despues de ella no hay un solo return false. Eso es lo
     * que garantiza la regla 6: si devuelve false, nada cambio.
     */
    public boolean recargar(long centavos) {
        if (!valida) {
            return false;
        }
        if (bloqueada) {
            return false;
        }
        if (centavos <= 0) {
            return false;
        }
        if (historialLleno()) {
            return false;
        }
        // Se escribe asi, y no como (saldo + centavos > TOPE_CENTAVOS), porque
        // saldo + centavos puede desbordarse si alguien pasa un long enorme.
        // TOPE_CENTAVOS - saldo nunca se desborda: los dos estan acotados.
        // Ademas la recarga nunca es parcial (regla 7): o entra completa o se rechaza.
        if (centavos > TOPE_CENTAVOS - saldo) {
            return false;
        }

        // ---- a partir de aqui ya nada puede fallar ----
        saldo = saldo + centavos;
        anotar("RECARGA " + centavos);
        return true;
    }

    /**
     * Se rechaza un concepto vacio o null. No lo pide el enunciado, pero sin
     * esta linea el historial guardaria "COBRO 850 null", que es un dato
     * corrupto dentro del objeto. Ver DISENO.md, decision 4.
     */
    public boolean cobrar(long centavos, String concepto) {
        if (!valida) {
            return false;
        }
        if (bloqueada) {
            return false;
        }
        if (centavos <= 0) {
            return false;
        }
        if (esTextoVacio(concepto)) {
            return false;
        }
        if (historialLleno()) {
            return false;
        }
        if (centavos > saldo) {
            return false;
        }

        // ---- a partir de aqui ya nada puede fallar ----
        saldo = saldo - centavos;
        anotar("COBRO " + centavos + " " + concepto.trim());
        return true;
    }

    /**
     * Devuelve void, asi que no es una de las "operaciones que devuelven
     * boolean" de la regla 1. Es idempotente: llamarla dos veces deja la
     * tarjeta en el mismo estado que llamarla una vez.
     */
    public void bloquear() {
        bloqueada = true;
    }

    /**
     * Copia, por dos razones distintas:
     *   - Es una copia y no el arreglo interno, para que nadie de fuera pueda
     *     escribir en el historial de la tarjeta (caso limite 7).
     *   - Tiene largo totalMovimientos y no MAX_MOVIMIENTOS, para no entregar
     *     45 posiciones en null que quien llame tendria que aprender a ignorar.
     */
    public String[] getHistorial() {
        String[] copia = new String[totalMovimientos];
        for (int i = 0; i < totalMovimientos; i++) {
            copia[i] = historial[i];
        }
        return copia;
    }

    public int getTotalMovimientos() {
        return totalMovimientos;
    }

    private void anotar(String linea) {
        historial[totalMovimientos] = linea;
        totalMovimientos = totalMovimientos + 1;
    }

    private boolean historialLleno() {
        return totalMovimientos >= MAX_MOVIMIENTOS;
    }

    private boolean esTextoVacio(String texto) {
        return texto == null || texto.trim().length() == 0;
    }
}
