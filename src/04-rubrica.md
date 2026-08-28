# Rúbrica y anexo

## Cómo se califica

| Criterio | Peso | Qué se mide |
|---|---|---|
| El objeto se protege solo | 30 % | Ninguna operación rechazada deja rastro. Se comprueba con las pruebas del alumno |
| Diseño | 25 % | Kata 2: agregar un ajuste nuevo sin abrir `Cobrador` |
| Revisión con Claude Code | 30 % | Calidad del argumento en `REVISION.md`, no cuántos errores encontró Claude |
| Complejidad y casos límite (Kata 3) | 15 % | Predicado correcto, tabla completa, decisión justificada con números |

**Reglas duras:**

- Aceptar un cambio sugerido por Claude sin escribir por qué → 0 en el criterio de revisión.
- `REVISION.md` sin al menos una fila de cada categoría (A, B, C) → incompleto.
- Un `Main` que solo prueba el camino feliz no cuenta para el criterio de protección. Cada operación rechazada necesita **dos** líneas: una que verifica el `false` y otra que verifica que el estado no cambió.
- Si Claude Code editó archivos del alumno, la entrega se revisa como si fuera de Claude.

## Cómo evaluar `REVISION.md` (para el instructor)

Lo que se busca no es que el alumno le gane a Claude. Se busca que **sepa por qué está de acuerdo**.

- Un alumno que acepta una corrección y explica bien qué razonamiento le faltó puntúa **igual o más** que uno que rechaza una sugerencia.
- La categoría **B** (fuera de alcance) es la más valiosa del ejercicio: Claude va a proponer excepciones, `ArrayList` y `record`, y el alumno tiene que escribir qué problema resuelve cada uno. Eso es un adelanto del temario ganado con contexto real.
- Señal de alarma: `REVISION.md` con puras filas A y ninguna B. Significa que copió el diagnóstico sin leerlo.

---

## Anexo — Código sembrado con fallas (para practicar la fase 3)

Si Claude Code no encuentra nada interesante en el código de un alumno, dale este archivo para que lo revise. Compila, se ve normal y tiene cinco fallas reales.

```java
public class TarjetaPrepago {

    private String id;
    private String titular;
    private double saldo;
    private boolean bloqueada;
    private String[] historial = new String[50];
    private int totalMovimientos = 0;

    public TarjetaPrepago(String id, String titular, double saldoInicial) {
        this.id = id;
        this.titular = titular;
        this.saldo = saldoInicial;
    }

    public void setSaldo(double saldo) { this.saldo = saldo; }
    public double getSaldo() { return saldo; }
    public String[] getHistorial() { return historial; }

    public boolean recargar(double monto) {
        saldo = saldo + monto;
        historial[totalMovimientos] = "RECARGA " + monto;
        totalMovimientos = totalMovimientos + 1;
        if (saldo > 5000) {
            return false;
        }
        return true;
    }

    public boolean cobrar(double monto) {
        if (saldo >= monto) {
            saldo = saldo - monto;
            historial[totalMovimientos] = "COBRO " + monto;
            totalMovimientos = totalMovimientos + 1;
            return true;
        }
        return false;
    }
}
```

Las cinco fallas (no revelarlas antes de que las busquen):

1. `double` para dinero. Se demuestra con 30 recargas de `0.10`: no dan `3.00`.
2. `recargar` modifica el saldo y el historial **antes** de revisar el tope, y devuelve `false` con el objeto ya corrupto. Rompe la regla "si devuelve `false`, nada cambió".
3. `getHistorial()` devuelve el arreglo interno: cualquiera puede escribir en él desde fuera.
4. `setSaldo` público. Toda la encapsulación de la clase se vuelve decorativa.
5. Nunca revisa si el historial se llenó ni si la tarjeta está bloqueada. Con 51 movimientos truena.

Cada falla debe demostrarse con una línea de `Prueba.igual` que falle. Una opinión no cuenta.
