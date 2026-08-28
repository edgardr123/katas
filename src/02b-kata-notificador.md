# Kata 2B — POO II express: `Notificador`

**Tema:** composición e interfaces.
**Idea central:** cuando el comportamiento varía, se **combinan objetos pequeños**; no se hacen clases cada vez más grandes ni cadenas de `if`.
**Tiempo:** 25 min, **guiada en vivo por los pollitos**.

## Enunciado

Mandar un mismo aviso a varios destinos a la vez: correo, notificación push, SMS. Cada destino tiene sus propias razones para aceptar o rechazar un mensaje, y deben poder combinarse libremente sin que quien manda el aviso sepa nada de ellas.

## Contrato obligatorio

```java
public interface Canal {
    boolean enviar(String mensaje);
}

public class Notificador {
    public Notificador(Canal[] canales) { }
    public int notificar(String mensaje) { }   // cuantos canales aceptaron
    public int getTotalCanales()         { }
}
```

`notificar` hace exactamente esto: le pasa el mismo mensaje a cada canal, en orden, y cuenta cuántos devolvieron `true`.

**Contrato de `Canal.enviar`:** `true` = el canal aceptó el mensaje. `false` = lo rechazó y **no guardó nada**; el canal quedó exactamente como estaba. Es la regla 1 de [`00-reglas-del-curso.md`](00-reglas-del-curso.md).

## Clases a entregar

| Clase | Implementa | Comportamiento |
|---|---|---|
| `CanalMemoria` | `Canal` | **Se entrega ya escrita** (abajo). Guarda lo que recibe |
| `CanalConLongitudMaxima` | `Canal` | Rechaza mensajes de más de N caracteres |
| `CanalConLimite` | `Canal` | Acepta los primeros N mensajes y luego rechaza |

## Restricciones

- `Notificador` **no puede nombrar ninguna de esas clases**. Nada de `instanceof`, nada de `switch` por tipo. Solo conoce el `interface Canal`.
- Prohibido `extends`. Solo `implements`.
- **Prueba de fuego del diseño:** el instructor va a pedir en clase un canal nuevo. Si para agregarlo hay que tocar `Notificador`, el diseño está mal.

## `CanalMemoria` — cópiala tal cual

Es el instrumento de medición de la kata, no el ejercicio. Léela por `getEnviados()`.

```java
public class CanalMemoria implements Canal {

    public static final int MAX_MENSAJES = 20;

    private String nombre;
    private String[] enviados = new String[MAX_MENSAJES];
    private int total = 0;

    public CanalMemoria(String nombre) {
        if (nombre == null) {
            this.nombre = "";
        } else {
            this.nombre = nombre.trim();
        }
    }

    public String getNombre() { return nombre; }
    public int    getTotal()  { return total;  }

    public boolean enviar(String mensaje) {
        if (total >= MAX_MENSAJES) {
            return false;                    // sin espacio: se rechaza completa
        }
        enviados[total] = mensaje;
        total = total + 1;
        return true;
    }

    public String[] getEnviados() {          // COPIA, y solo el tramo usado
        String[] copia = new String[total];
        for (int i = 0; i < total; i++) {
            copia[i] = enviados[i];
        }
        return copia;
    }
}
```

**Por qué no un `CanalConsola` con `System.out.println`:** porque `println` no devuelve nada que `Prueba.igual` pueda comparar. Un canal que imprime se ve bonito en la demo y no se puede probar — para saber si funcionó habría que leer la pantalla con los ojos. Éste se acuerda de lo que recibió, y entonces la prueba es una línea.

## Decisiones que debes tomar y documentar (no hay una sola respuesta correcta)

**1. Si un canal rechaza, ¿siguen los demás?**

- **Siguen todos:** un notificador es un reparto, no una transacción. Si el correo está caído, eso no es razón para dejar sin aviso a quien lo espera por otro lado.
- **Corta al primer fallo:** tendría sentido si los canales fueran pasos de un proceso, no destinos paralelos.

Si cortas, el resultado pasa a depender del **orden del arreglo** — y ese orden no significa nada para nadie. Poner el canal frágil en la posición 0 apagaría todo el sistema. Elige, escribe por qué, y **verifica ese caso exacto** con `Prueba.igual`.

**2. `notificar(null)` o `notificar("")`: ¿quién lo filtra?**

- **El `Notificador`:** la regla se pone una vez, en el único lugar por el que pasan todos.
- **Cada canal:** el día que alguien escriba el canal número siete, se le va a olvidar.

Si lo filtra el `Notificador`, decide además si lo filtra **antes** de llamar a nadie (los canales ni se enteran del intento) o después.

**3. ¿Por qué `notificar` devuelve `int` y no `boolean`,** si la regla 1 del curso dice que los métodos que pueden fallar devuelven `boolean`?

Escríbelo en un comentario. Pista: con tres canales de los que dos aceptaron, ¿qué diría un `boolean`?

## Casos límite obligatorios

| # | Caso | Qué revela |
|---|---|---|
| 1 | `new Canal[0]` y `null` | Devuelve 0, no truena |
| 2 | Dos canales que aceptan | Devuelve 2 y **los dos** tienen el mensaje |
| 3 | Un canal rechaza **en medio** | Los que venían **después sí** recibieron |
| 4 | `notificar(null)` y `notificar("   ")` | Devuelve 0 y ningún canal recibió nada |
| 5 | `CanalConLongitudMaxima(5)` con `"holas"` y `"holaaa"` | `true` y `false` |
| 6 | Escribir en el arreglo de `getEnviados()` | El canal no se entera |

## Preguntas para `DISENO.md`

1. Dibuja qué objetos existen en memoria cuando creas un `Notificador` con 3 canales. ¿Cuántos objetos hay en total?
2. Si en vez del `interface` usaras un `if` gigante dentro de `Notificador` con todos los tipos de canal, ¿cuántas líneas tendrías que tocar para agregar el quinto canal? ¿Y con el `interface`?
3. Un `Canal` mal escrito podría devolver `true` sin haber guardado nada. ¿Quién debería evitarlo: cada canal o el `Notificador`? Escribe tu regla como comentario en el `interface Canal`.
