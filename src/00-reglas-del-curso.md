# Reglas del taller (leer antes de cualquier kata)

## Qué SÍ puedes usar

- Clases, constructores, campos `private`, métodos.
- `interface` (solo en la Kata 2).
- Tipos: `int`, `long`, `boolean`, `String`.
- **Arreglos de tamaño fijo**: `String[]`, `Reserva[]`, `Ajuste[]`.
- `System.out.println` y la clase `Prueba` de este documento.

## Qué NO puedes usar todavía

| Prohibido | Por qué | Cuándo lo verás |
|---|---|---|
| `throw`, `try`, `catch` | Aún no vemos excepciones | Módulo de excepciones |
| `ArrayList`, `HashMap`, `List` | Aún no vemos colecciones | Módulo de colecciones |
| `<T>` (genéricos) | Aún no vemos genéricos | Módulo de colecciones |
| `equals` / `hashCode` propios | Aún no vemos comparación de objetos | Módulo de colecciones |
| `extends` (herencia) | Aún no vemos herencia | Módulo de herencia |
| `record` | Atajo que te quitaría el ejercicio | — |
| `double` / `float` para dinero | Pierde centavos | Nunca. Usa `long`. |

> Usar `String.equals(...)` sí está permitido: es un método que ya existe. Lo prohibido es *escribir* tu propio `equals`.

## Convenciones del curso (sustituyen a las excepciones)

Como todavía no podemos lanzar excepciones, usamos dos reglas fijas:

**Regla 1 — Los métodos que pueden fallar devuelven `boolean`.**
`true` = la operación se realizó. `false` = se rechazó y **nada cambió**.

```java
if (!tarjeta.recargar(1000)) {
    System.out.println("No se pudo recargar");
}
```

**Regla 2 — Si un constructor no puede rechazar datos malos, no los recibe.**
Un constructor no devuelve nada, así que no puede avisar de un error. Dos salidas:

- **Marcar el objeto como inválido**: guarda un `boolean valido` y expón `esValida()`. Un objeto inválido rechaza todas sus operaciones. Se usa cuando el dato malo es grave (Kata 1 y 3).
- **Normalizar**: convertir el dato malo en uno seguro (minutos negativos → 0). Se usa cuando el dato malo tiene una interpretación razonable (Kata 2).

Decidir cuál aplicar es parte del ejercicio. El dinero **no** se normaliza en silencio; una duración sí.

---

## La clase `Prueba` (cópiala tal cual, se usa en las 3 katas)

Es un `assertEquals` hecho a mano. Cuando veas JUnit más adelante, será exactamente esta idea con más herramientas.

```java
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
```

Uso:

```java
public class MainTarjeta {
    public static void main(String[] args) {
        TarjetaPrepago t = new TarjetaPrepago("A1", "Ana");

        Prueba.igual("saldo inicial en cero", 0L, t.getSaldo());
        Prueba.igual("recarga valida", true, t.recargar(1000));
        Prueba.igual("saldo tras recarga", 1000L, t.getSaldo());
        Prueba.igual("recarga negativa se rechaza", false, t.recargar(-50));
        Prueba.igual("saldo no cambio", 1000L, t.getSaldo());   // <-- esta es la importante

        Prueba.resumen();
    }
}
```

**La regla más importante de las pruebas de este taller:** después de cada operación rechazada, verifica que el estado **no cambió**. Comprobar que devolvió `false` no basta.

---

## Protocolo de 4 fases

| Fase | Qué haces | Tiempo | Entregable |
|---|---|---|---|
| 0. Diseño en papel | Sin computadora, sin IA. Escribe el contrato, las reglas y 3 casos límite | 10 min | `DISENO.md` |
| 1. Código | Implementas tú, sin IA | 35 min | `.java` |
| 2. Pruebas | Escribes tu `Main` con `Prueba.igual(...)` y lo haces pasar | 15 min | `Main*.java` |
| 3. Revisión con Claude Code | Claude revisa, **tú decides** | 20 min | `REVISION.md` |

**No abras Claude Code hasta terminar la fase 2.** Si lo abres antes, la fase 3 no te enseña nada.

### Fase 3: cómo usar Claude Code

Pégale este prompt exacto:

```
Revisa el codigo de esta carpeta. NO edites ningun archivo, solo dime que encontraste.

Restricciones del curso, respetalas en tus sugerencias:
sin excepciones, sin ArrayList ni colecciones, sin genericos, sin herencia,
sin record, sin equals propio. Solo arreglos de tamano fijo.

Para cada problema dime: (1) que linea, (2) que caso concreto lo rompe,
(3) por que esta mal. No me des el codigo corregido.
```

Luego llena `REVISION.md`. Cada observación de Claude entra en **una** de estas tres columnas:

| # | Qué dijo Claude | Categoría | Mi decisión y por qué |
|---|---|---|---|

Categorías:

- **A — Defecto real.** Tiene razón, lo corrijo. Escribe primero la línea de `Prueba.igual` que lo demuestra, y *luego* arregla.
- **B — Fuera de alcance.** Es correcto, pero usa algo que aún no vemos (excepciones, `ArrayList`, `record`). Escribe **qué problema resuelve** esa herramienta. Esto es un adelanto del temario, no un error tuyo.
- **C — Lo rechazo.** No estoy de acuerdo. Explica con un caso concreto o con números por qué.

**Requisitos de entrega:** mínimo una fila de cada categoría. Una fila de categoría A sin su línea de prueba no cuenta.
