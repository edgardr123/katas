# Kata 1 — POO I: `TarjetaPrepago`

**Tema:** clases, objetos, encapsulación y constructores.
**Idea central:** encapsular NO es "poner `private` y generar getters y setters". Es **impedir que el objeto llegue a un estado imposible**.
**Tiempo:** 60 min. Lee antes `00-reglas-del-curso.md`.

## Enunciado

Una tarjeta de prepago de transporte. Guarda saldo en centavos, anota cada movimiento en un historial y puede bloquearse.

## Contrato obligatorio (respeta los nombres)

```java
public class TarjetaPrepago {

    public static final long TOPE_CENTAVOS = 500000;   // $5,000.00
    public static final int  MAX_MOVIMIENTOS = 50;

    public TarjetaPrepago(String id, String titular) { }

    public String  getId()          { }
    public String  getTitular()     { }
    public long    getSaldo()       { }
    public boolean esValida()       { }
    public boolean estaBloqueada()  { }

    public boolean recargar(long centavos)                { }
    public boolean cobrar(long centavos, String concepto) { }
    public void    bloquear()                             { }

    public String[] getHistorial()        { }  // copia, solo los movimientos reales
    public int      getTotalMovimientos() { }
}
```

El historial se guarda internamente así:

```java
private String[] historial = new String[MAX_MOVIMIENTOS];
private int totalMovimientos = 0;
```

Formato de cada línea: `"RECARGA 1000"`, `"COBRO 850 metro"`.

## Reglas que el objeto debe cumplir SIEMPRE

1. La tarjeta **nace con saldo 0**. No hay saldo inicial en el constructor.
2. `0 <= getSaldo() <= TOPE_CENTAVOS`, en todo momento.
3. Si `id` o `titular` son `null` o quedan vacíos tras `trim()`, la tarjeta es **inválida** y **todas** sus operaciones devuelven `false`.
4. Una tarjeta bloqueada rechaza `recargar` y `cobrar`. `bloquear()` se puede llamar dos veces sin problema.
5. `recargar` y `cobrar` exigen `centavos > 0`.
6. **Si una operación devuelve `false`, ni el saldo ni el historial cambiaron.** Ni un poquito.
7. Ninguna recarga es parcial: si pasarse del tope, se rechaza completa.
8. Si el historial ya está lleno, la operación se rechaza y el saldo **no** se mueve.

## Restricciones

- Prohibido `double` y `float`.
- Prohibidos los setters públicos y los campos públicos.
- Prohibido devolver el arreglo interno del historial.

## Casos límite obligatorios (uno por línea de `Prueba.igual`)

| # | Caso | Esperado |
|---|---|---|
| 1 | Recarga que deja el saldo exactamente en el tope | `true` |
| 2 | Recarga de 1 centavo por encima del tope | `false`, y el saldo sigue igual |
| 3 | Cobro que deja el saldo en 0 | `true` |
| 4 | Cobro de exactamente saldo + 1 | `false`, y el saldo sigue igual |
| 5 | `recargar(0)` y `recargar(-100)` | `false` |
| 6 | Tarjeta creada con `titular = "   "` | `esValida()` es `false` y `recargar` devuelve `false` |
| 7 | Escribir en el arreglo que devolvió `getHistorial()` | El objeto no se entera; `getTotalMovimientos()` no cambia |
| 8 | Llenar el historial y hacer un cobro más | `false`, y el saldo sigue igual |
| 9 | `bloquear()` dos veces y luego `cobrar` | No truena; `cobrar` devuelve `false` |

El caso 7 se prueba así:

```java
String[] copia = t.getHistorial();
copia[0] = "HACKEADO";
Prueba.igual("historial protegido", "RECARGA 1000", t.getHistorial()[0]);
```

## Preguntas para `DISENO.md` (antes de programar)

1. ¿En qué orden validas dentro de `cobrar`? Escribe la lista de comprobaciones **antes** de tocar el saldo.
2. Si `getHistorial()` devolviera el arreglo interno, escribe las 3 líneas de código que un tercero usaría para corromper la tarjeta.
3. ¿Por qué la tarjeta nace en 0 en vez de recibir un saldo inicial en el constructor? (Pista: regla 2 de `00-reglas-del-curso.md`.)
