package kata2b_notificador;

public class MainNotificador {

    public static void main(String[] args) {
/*
*        // ------------------------------------------------------------------
*        // Caso 1: sin canales. No truena y devuelve 0.
*        // 0 canales que aceptaron es la respuesta honesta: no le llego a
*        // nadie. Por eso notificar devuelve int y no boolean.
*        // ------------------------------------------------------------------
*        Notificador vacio = new Notificador(new Canal[0]);
*        Prueba.igual("1a sin canales devuelve 0", 0, vacio.notificar("hola"));
*        Prueba.igual("1b sin canales getTotalCanales es 0", 0, vacio.getTotalCanales());
*
*        Notificador nulo = new Notificador(null);
*        Prueba.igual("1c canales null se trata como arreglo vacio", 0, nulo.notificar("hola"));
*        Prueba.igual("1d y tampoco truena al preguntar el total", 0, nulo.getTotalCanales());
*
*        // ------------------------------------------------------------------
*        // Caso 2: dos canales que aceptan. Devuelve 2 y LOS DOS tienen el
*        // mensaje. Contar no basta: hay que ir a preguntarle a cada destino.
*        // ------------------------------------------------------------------
*        CanalMemoria correo = new CanalMemoria("correo");
*        CanalMemoria push = new CanalMemoria("push");
*        Notificador dos = new Notificador(new Canal[] { correo, push });
*
*        Prueba.igual("2a dos canales aceptaron", 2, dos.notificar("se cayo el servidor"));
*        Prueba.igual("2b el primero lo recibio", 1, correo.getTotal());
*        Prueba.igual("2c y es el mensaje correcto", "se cayo el servidor", correo.getEnviados()[0]);
*        Prueba.igual("2d el segundo tambien", 1, push.getTotal());
*        Prueba.igual("2e y es el mismo mensaje", "se cayo el servidor", push.getEnviados()[0]);
*        Prueba.igual("2f getTotalCanales dice cuantos hay", 2, dos.getTotalCanales());
*
*        // ------------------------------------------------------------------
*        // Caso 3: UN CANAL RECHAZA EN MEDIO. Este es el que mas gente
*        // reprueba. Si haces return en cuanto uno falla, el canal que venia
*        // DESPUES se queda sin mensaje y la prueba 3c lo caza.
*        //
*        // Orden: [ acepta , caido , acepta ]
*        // ------------------------------------------------------------------
*        CanalMemoria antes = new CanalMemoria("antes");
*        CanalConLimite caido = new CanalConLimite("caido", 0);   // rechaza todo
*        CanalMemoria despues = new CanalMemoria("despues");
*        Notificador conFalla = new Notificador(new Canal[] { antes, caido, despues });
*
*        Prueba.igual("3a dos de tres aceptaron", 2, conFalla.notificar("aviso"));
*        Prueba.igual("3b el de antes del fallo recibio", 1, antes.getTotal());
*        Prueba.igual("3c EL DE DESPUES DEL FALLO TAMBIEN RECIBIO", 1, despues.getTotal());
*        Prueba.igual("3d el canal caido no guardo nada", 0, caido.getRecibidos());
*
*        // El mismo reparto con el canal caido en la posicion 0. El resultado
*        // no cambia, y eso es justo lo que se buscaba: que el orden del
*        // arreglo no decida quien se queda sin aviso.
*        CanalMemoria uno = new CanalMemoria("uno");
*        CanalMemoria otro = new CanalMemoria("otro");
*        Notificador fallaPrimero = new Notificador(
*                new Canal[] { new CanalConLimite("caido", 0), uno, otro });
*        Prueba.igual("3e con el caido al principio da lo mismo", 2, fallaPrimero.notificar("aviso"));
*        Prueba.igual("3f los dos buenos recibieron", 2, uno.getTotal() + otro.getTotal());
*
*        // ------------------------------------------------------------------
*        // Caso 4: mensaje vacio. El Notificador lo filtra ANTES de llamar a
*        // nadie: devuelve 0 y ningun canal se entera de que hubo un intento.
*        // ------------------------------------------------------------------
*        CanalMemoria testigo = new CanalMemoria("testigo");
*        Notificador conTestigo = new Notificador(new Canal[] { testigo });
*
*        Prueba.igual("4a mensaje null devuelve 0", 0, conTestigo.notificar(null));
*        Prueba.igual("4b y el canal no recibio nada", 0, testigo.getTotal());
*        Prueba.igual("4c mensaje vacio devuelve 0", 0, conTestigo.notificar(""));
*        Prueba.igual("4d solo espacios tambien devuelve 0", 0, conTestigo.notificar("   "));
*        Prueba.igual("4e despues de tres intentos, el canal sigue vacio", 0, testigo.getTotal());
*        Prueba.igual("4f un mensaje de verdad si pasa", 1, conTestigo.notificar("x"));
*
*        // ------------------------------------------------------------------
*        // Caso 5: el canal con longitud maxima. La regla vive en el canal,
*        // no en el Notificador.
*        // ------------------------------------------------------------------
*        CanalConLongitudMaxima sms = new CanalConLongitudMaxima("sms", 5);
*        Prueba.igual("5a un mensaje corto pasa", true, sms.enviar("hola"));
*        Prueba.igual("5b exactamente el maximo pasa", true, sms.enviar("holas"));
*        Prueba.igual("5c un caracter mas se rechaza", false, sms.enviar("holaaa"));
*        Prueba.igual("5d llevo la cuenta de los que acepto", 2, sms.getAceptados());
*        Prueba.igual("5e y de los que rechazo", 1, sms.getRechazados());
*
*        // Un limite absurdo marca el canal como invalido y entonces RECHAZA
*        // todo. Se falla del lado que hace ruido, no del que deja pasar.
*        CanalConLongitudMaxima dedazo = new CanalConLongitudMaxima("sms", 0);
*        Prueba.igual("5f un maximo de 0 es invalido", false, dedazo.esValido());
*        Prueba.igual("5g y un canal invalido no acepta nada", false, dedazo.enviar("hola"));
*        Prueba.igual("5h un canal bien construido si es valido", true, sms.esValido());
*
*        // Y visto desde el Notificador, que no sabe nada de longitudes:
*        Notificador mixto = new Notificador(new Canal[] {
*                new CanalMemoria("correo"),
*                new CanalConLongitudMaxima("sms", 5)
*        });
*        Prueba.igual("5i mensaje largo: solo el correo lo acepta", 1,
*                mixto.notificar("este mensaje no cabe en un sms"));
*        Prueba.igual("5j mensaje corto: los dos lo aceptan", 2, mixto.notificar("hola"));
*
*        // ------------------------------------------------------------------
*        // Caso 6: getEnviados() devuelve una copia. La leccion de la Kata 1,
*        // aplicada al objeto del que dependen todas las pruebas de arriba.
*        // ------------------------------------------------------------------
*        CanalMemoria protegido = new CanalMemoria("protegido");
*        protegido.enviar("uno");
*        protegido.enviar("dos");
*
*        String[] copia = protegido.getEnviados();
*        copia[0] = "HACKEADO";
*        Prueba.igual("6a el canal no se entero de la travesura", "uno",
*                protegido.getEnviados()[0]);
*        Prueba.igual("6b ni le movieron el total", 2, protegido.getTotal());
*        Prueba.igual("6c la copia trae solo el tramo usado, no los 20", 2, copia.length);
*
*        // ------------------------------------------------------------------
*        // Caso 7: LA PRUEBA DE FUEGO. El canal sorpresa.
*        // Estas lineas se escribieron despues de terminar la kata y no
*        // obligaron a tocar Notificador.java ni una sola vez.
*        //
*        // Un CanalConLimite(2) acepta los dos primeros mensajes y rechaza
*        // del tercero en adelante. El Notificador no sabe que existe un
*        // canal que responde distinto cada vez, y no le hace falta saberlo.
*        // ------------------------------------------------------------------
*        CanalMemoria siempre = new CanalMemoria("siempre");
*        CanalConLimite limitado = new CanalConLimite("limitado", 2);
*        Notificador conSorpresa = new Notificador(new Canal[] { siempre, limitado });
*
*        Prueba.igual("7a primer mensaje: los dos aceptan", 2, conSorpresa.notificar("uno"));
*        Prueba.igual("7b segundo mensaje: los dos aceptan", 2, conSorpresa.notificar("dos"));
*        Prueba.igual("7c tercer mensaje: el limitado ya se lleno", 1, conSorpresa.notificar("tres"));
*        Prueba.igual("7d cuarto mensaje: sigue rechazando", 1, conSorpresa.notificar("cuatro"));
*        Prueba.igual("7e el canal sin limite recibio los cuatro", 4, siempre.getTotal());
*        Prueba.igual("7f el limitado se quedo en dos", 2, limitado.getRecibidos());
*
*        CanalConLimite negativo = new CanalConLimite("raro", -5);
*        Prueba.igual("7g un maximo negativo se normaliza a 0", 0, negativo.getMaximo());
*        Prueba.igual("7h y entonces no acepta nada", false, negativo.enviar("hola"));
*
*        // ------------------------------------------------------------------
*        // Caso 8: el Notificador copio el arreglo que recibio.
*        // Sin la copia en el constructor, esta prueba fallaria: el de afuera
*        // le habria cambiado los canales a un Notificador ya construido.
*        // ------------------------------------------------------------------
*        CanalMemoria original = new CanalMemoria("original");
*        Canal[] misCanales = { original };
*        Notificador n = new Notificador(misCanales);
*
*        Prueba.igual("8a antes de la travesura", 1, n.notificar("antes"));
*        misCanales[0] = new CanalConLimite("impostor", 0);
*        Prueba.igual("8b el Notificador no se entero del cambio", 1, n.notificar("despues"));
*        Prueba.igual("8c y el canal original recibio los dos", 2, original.getTotal());
*
*        // ------------------------------------------------------------------
*        // Caso 9: un hueco null en el arreglo de canales.
*        // Sin el if de la linea del bucle, este caso tumba la notificacion
*        // completa y los canales de despues se quedan sin mensaje.
*        // ------------------------------------------------------------------
*        CanalMemoria a = new CanalMemoria("a");
*        CanalMemoria b = new CanalMemoria("b");
*        Notificador conHueco = new Notificador(new Canal[] { a, null, b });
*
*        Prueba.igual("9a el hueco se salta, no truena", 2, conHueco.notificar("hola"));
*        Prueba.igual("9b el canal de despues del hueco recibio", 1, b.getTotal());
*        Prueba.igual("9c getTotalCanales cuenta el hueco", 3, conHueco.getTotalCanales());
*
*        // ------------------------------------------------------------------
*        // Caso 10: el CanalMemoria lleno rechaza, y no se pisa nada.
*        // Misma regla del historial lleno de la Kata 1.
*        // ------------------------------------------------------------------
*        CanalMemoria lleno = new CanalMemoria("lleno");
*        for (int i = 0; i < CanalMemoria.MAX_MENSAJES; i++) {
*            lleno.enviar("m" + i);
*        }
*        Prueba.igual("10a se lleno con 20", 20, lleno.getTotal());
*        Prueba.igual("10b el 21 se rechaza", false, lleno.enviar("uno mas"));
*        Prueba.igual("10c y el total no se movio", 20, lleno.getTotal());
*        Prueba.igual("10d el primero sigue siendo el primero", "m0", lleno.getEnviados()[0]);
*
*        Prueba.resumen();
*/
    }
}
