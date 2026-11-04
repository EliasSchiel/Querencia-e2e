# language: es
@smoke @movil
Característica: Uso desde el celular
  Como persona que entra desde el celular (la mayoría de las visitas)
  Quiero que Querencia se adapte a la pantalla
  Para poder navegar sin hacer zoom ni desplazarme hacia los costados

  Escenario: El menú se abre con el botón de hamburguesa
    Dado que abro la página de inicio
    Entonces el botón del menú está visible
    Y el enlace "nav-adoptar" está oculto
    Cuando abro el menú
    Entonces el enlace "nav-adoptar" está visible

  Esquema del escenario: Las páginas no se desbordan hacia los costados
    Cuando abro la dirección "<ruta>"
    Entonces la página no tiene desplazamiento horizontal

    Ejemplos:
      | ruta               |
      | /                  |
      | /Publicaciones     |
      | /Donar             |
      | /Cuenta/Login      |
      | /Home/Terminos     |

  Escenario: La ficha de una mascota se ve completa en el celular
    Dado que abro el listado de mascotas
    Cuando abro la primera publicación del listado
    Entonces la página no tiene desplazamiento horizontal
