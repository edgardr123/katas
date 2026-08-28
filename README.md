# Katas de POO en Java

Material de un taller introductorio de programación orientada a objetos en Java.
Tres katas con enunciado, contrato obligatorio, pruebas de referencia y rúbrica.

La idea que las atraviesa: **encapsular no es poner `private` y generar getters y
setters, es impedir que un objeto llegue a un estado imposible**. Toda operación
rechazada tiene que dejar el objeto exactamente como estaba.

## Antes de empezar

Lee [`src/00-reglas-del-curso.md`](src/00-reglas-del-curso.md). Define qué está
permitido y qué no en este punto del temario, y las dos convenciones que
sustituyen a las excepciones:

1. Los métodos que pueden fallar devuelven `boolean`. `false` = se rechazó y
   **nada cambió**.
2. Si un constructor no puede rechazar datos malos, no los recibe: se marca el
   objeto como inválido (`esValida()`) o se normaliza el dato.

No se usan todavía excepciones, colecciones, genéricos, herencia, `record` ni
`equals` propio. Solo arreglos de tamaño fijo. Las restricciones son el
ejercicio, no un descuido.

## Las katas

| # | Kata | Tema | Tiempo | Enunciado | Código |
|---|---|---|---|---|---|
| 1 | `TarjetaPrepago` | Clases, encapsulación, constructores | 60 min | [`01-kata-tarjeta.md`](src/01-kata-tarjeta.md) | [`kata1_tarjeta/`](src/kata1_tarjeta) |
| 2B | `Notificador` | Composición e interfaces | 25 min, guiada | [`02b-kata-notificador.md`](src/02b-kata-notificador.md) | [`kata2b_notificador/`](src/kata2b_notificador) |
| 3 | `AgendaDeSala` | Casos límite y análisis escrito | 60 min | [`03-kata-agenda.md`](src/03-kata-agenda.md) | [`kata3_agenda/`](src/kata3_agenda) |

Cómo se califica: [`04-rubrica.md`](src/04-rubrica.md). Incluye un anexo con
código sembrado con cinco fallas reales para practicar la fase de revisión.

## Estado del código

- **Kata 1** está resuelta y sirve de ejemplo completo de implementación.
- **Katas 2B y 3** traen los esqueletos de las clases vacíos —los rellena quien
  hace la kata— y las pruebas de referencia (`MainNotificador`, `MainAgenda`) ya
  escritas. Empieza por ahí: el `Main` te dice qué tiene que cumplir tu código.

Cada paquete incluye su propia copia de `Prueba.java`, un `assertEquals` hecho a
mano de 20 líneas. Es la misma idea de JUnit, sin la herramienta.

## Compilar y ejecutar

Requiere un JDK 17 o superior (probado con Temurin 21). Desde la raíz del repo:

```bash
javac -d out src/kata1_tarjeta/*.java
java  -cp out kata1_tarjeta.MainTarjeta
```

Igual para las otras dos, cambiando el paquete:

```bash
javac -d out src/kata2b_notificador/*.java && java -cp out kata2b_notificador.MainNotificador
javac -d out src/kata3_agenda/*.java       && java -cp out kata3_agenda.MainAgenda
```

Cada `Main` imprime una línea por caso y un resumen final. El objetivo es
`TODAS LAS PRUEBAS PASARON`.

## Protocolo de 4 fases

| Fase | Qué haces | Entregable |
|---|---|---|
| 0. Diseño en papel | Sin computadora, sin IA. Contrato, reglas y 3 casos límite | `DISENO.md` |
| 1. Código | Implementas tú, sin IA | `.java` |
| 2. Pruebas | Tu `Main` con `Prueba.igual(...)`, hasta que pase | `Main*.java` |
| 3. Revisión con IA | La IA revisa y **tú decides** qué aceptar | `REVISION.md` |

**No abras la IA hasta terminar la fase 2.** Si la abres antes, la fase 3 no
enseña nada.

En la fase 3 cada observación se clasifica en una de tres categorías: **A** es un
defecto real (se corrige, escribiendo primero la prueba que lo demuestra), **B**
es correcto pero fuera de alcance (usa algo que aún no vemos: hay que escribir
qué problema resuelve esa herramienta) y **C** es una sugerencia que rechazas
con un argumento concreto. Hace falta al menos una fila de cada una.

Lo que se evalúa en esa fase no es cuántos errores encontró la IA, sino la
calidad del argumento con el que aceptas o rechazas cada uno.
