# Kata 3 — Individual: `AgendaDeSala`

**Se entrega:** código + pruebas.
**Se califica el razonamiento igual que el código.**
**Tiempo:** 60 min.

## Enunciado

Administrar las reservas de una sala durante un día. El tiempo se mide en **minutos desde la medianoche** (0 = 00:00, 1440 = 24:00). Una reserva se acepta solo si no se traslapa con ninguna existente.

**Regla clave:** los intervalos son `[inicio, fin)`. Una reserva de 600 a 660 y otra de 660 a 720 **no** se traslapan: se tocan en un extremo y eso está permitido.

## Contrato obligatorio

```java
public class Reserva {
    public Reserva(String id, String solicitante, int inicio, int fin) { }
    public String  getId()          { }
    public String  getSolicitante() { }
    public int     getInicio()      { }
    public int     getFin()         { }
    public boolean esValida()       { }   // id y solicitante no vacios,
                                          // 0 <= inicio < fin <= 1440
}

public class AgendaDeSala {

    public static final int MAX_RESERVAS = 20;

    public AgendaDeSala(String salaId) { }

    public boolean reservar(Reserva reserva) { }  // false si se traslapa o no cabe
    public boolean cancelar(String reservaId) { } // false si no existe

    public Reserva[] getReservas() { }            // copia, solo el tramo usado
    public int       getTotal()    { }
}
```

Internamente:

```java
private Reserva[] reservas = new Reserva[MAX_RESERVAS];
private int total = 0;
```

Al cancelar, recorre los elementos siguientes una posición a la izquierda, baja `total` y **pon en `null` la última posición que quedó libre**. Explica en un comentario qué pasaría si no la pusieras en `null`.

## Entregable A — Complejidad

Completa la tabla contando **cuántos elementos recorres en el peor caso**, con `n` reservas guardadas:

| Operación | Recorridos en el peor caso | Explicación |
|---|---|---|
| `reservar` | | |
| `cancelar` | | |
| `getReservas` | | |
| Insertar n reservas, una por una | | |

Responde en comentarios dentro de tu código:

1. **Escribe el predicado de traslape.** Es más corto de lo que crees. Pista: enumera los dos casos en que dos reservas **no** se traslapan y niégalos.
2. Con 20 reservas al día, ¿importa que `reservar` recorra toda la lista? ¿Y si fueran 100 000?
3. Si mantuvieras el arreglo **ordenado por hora de inicio**, ¿podrías detener la búsqueda antes de llegar al final? ¿Cuánto ahorrarías en el peor caso? ¿Vale la pena el trabajo extra de mantenerlo ordenado?
4. `cancelar` busca por `id` y `reservar` busca por horario. ¿Qué tendrías que hacer para que las dos fueran rápidas al mismo tiempo, y qué podría salir mal?

## Entregable B — Casos límite

Cada uno con su línea de `Prueba.igual`:

| # | Caso | Esperado |
|---|---|---|
| 1 | Una reserva contiene completamente a otra | Rechazada |
| 2 | Traslape parcial por la izquierda | Rechazada |
| 3 | Traslape parcial por la derecha | Rechazada |
| 4 | El `fin` de una es igual al `inicio` de la otra | **Aceptada** |
| 5 | Reserva idéntica a una existente | Rechazada |
| 6 | `inicio == fin` (dura 0 minutos) | `esValida()` es `false`, se rechaza |
| 7 | `fin < inicio` | `esValida()` es `false`, se rechaza |
| 8 | `inicio = -10` o `fin = 2000` | `esValida()` es `false`, se rechaza |
| 9 | Cancelar un `id` que no existe | `false` |
| 10 | Reservar, cancelar y volver a reservar el mismo horario | Aceptada las dos veces |
| 11 | Llenar las 20 y pedir la 21 | `false` |
| 12 | Después de una reserva rechazada, `getTotal()` | El mismo número que antes |
| 13 | Cancelar la del medio y revisar el arreglo | Sin huecos, `getTotal()` bajó en 1 |

El caso 4 es el que más gente reprueba. Si usas `<=` en tu predicado en vez de `<`, va a fallar.

## Entregable C — `MainAgenda.java`

Un `main` que ejecute los 13 casos con `Prueba.igual(...)` y termine con `Prueba.resumen()`.
