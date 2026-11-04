# language: es
@smoke @navegacion
Característica: Navegación pública
  Como visitante de Querencia
  Quiero recorrer las secciones principales sin iniciar sesión
  Para conocer a las mascotas y a las asociaciones

  Escenario: La página de inicio carga con la portada y las mascotas recientes
    Dado que abro la página de inicio
    Entonces veo la portada de Querencia
    Y el título de la pestaña contiene "Querencia"

  Esquema del escenario: El menú principal lleva a cada sección
    Dado que abro la página de inicio
    Cuando entro al enlace "<enlace>" del menú
    Entonces la dirección contiene "<ruta>"
    Y el título principal contiene "<titulo>"

    Ejemplos:
      | enlace           | ruta           | titulo                      |
      | nav-adoptar      | /Publicaciones | Mascotas que buscan hogar   |
      | nav-asociaciones | /Asociaciones  | Asociaciones                |
      | nav-dona         | /Donar         | Doná                        |

  Esquema del escenario: Las páginas legales están disponibles desde el pie
    Dado que abro la página de inicio
    Cuando entro al enlace "<enlace>" del pie
    Entonces el título principal contiene "<titulo>"

    Ejemplos:
      | enlace         | titulo                   |
      | pie-terminos   | Términos y condiciones   |
      | pie-privacidad | Política de privacidad   |

  Escenario: Una dirección inexistente muestra la página de error de Querencia
    Cuando abro la dirección "/Publicaciones/Detalle/999999"
    Entonces veo el aviso "Esta página no existe"
