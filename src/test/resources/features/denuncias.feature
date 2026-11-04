# language: es
@regresion @denuncias
Característica: Denuncias de publicaciones
  Como integrante de la comunidad
  Quiero avisar cuando una publicación parece una venta o una estafa
  Para que el equipo de Querencia la revise

  Escenario: Denunciar una publicación, no poder repetirlo y que el administrador la revise
    Dado que "Carla" se registró como usuaria nueva
    Y abro la primera publicación de adopción del listado
    Cuando denuncio la publicación por "Venta encubierta o cobro por el animal" con el detalle "Me pidieron dinero por la adopción."
    Entonces veo el aviso de confirmación "Gracias por avisar"
    Y la ficha indica que ya la denuncié

    Cuando el administrador abre las denuncias
    Entonces ve la denuncia de esa publicación con el motivo "Venta encubierta o cobro por el animal"
    Cuando el administrador descarta las denuncias de esa publicación
    Entonces la publicación sigue visible en el listado
