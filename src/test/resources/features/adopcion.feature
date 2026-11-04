# language: es
@regresion @adopcion
Característica: Flujo completo de adopción
  Como persona que da una mascota en adopción
  Quiero publicarla, recibir solicitudes y elegir a la familia
  Para que encuentre su querencia

  Escenario: Publicar, aprobar, postularse, elegir y registrar la entrega
    Dado que "Ana" se registró como usuaria nueva
    Y Ana cargó una mascota llamada "Tito"
    Cuando Ana publica la mascota para adopción
    Entonces la publicación queda "Pendiente" de revisión

    Cuando el administrador aprueba la publicación
    Entonces la mascota aparece en el listado público

    Cuando "Bruno" se registra y se postula para adoptarla
    Y Ana elige a Bruno entre las solicitudes
    Entonces la publicación queda "EnProceso"

    Cuando Ana registra la entrega
    Entonces la adopción queda registrada
    Y la publicación queda "Adoptada"
