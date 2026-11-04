# language: es
@smoke @seguridad
Característica: Seguridad y acceso
  Como responsable de Querencia
  Quiero que las páginas privadas pidan iniciar sesión
  Para proteger los datos de las personas y las asociaciones

  Esquema del escenario: Las páginas privadas piden iniciar sesión
    Cuando abro la dirección "<ruta>"
    Entonces me lleva a la página para ingresar
    Y después de ingresar volvería a "<ruta>"

    Ejemplos:
      | ruta                 |
      | /Admin               |
      | /Mascotas            |
      | /Publicaciones/Mias  |
      | /Favoritos           |
      | /Adopciones          |
      | /Cuenta/Perfil       |

  Escenario: robots.txt le pide a los buscadores no indexar la administración
    Cuando abro la dirección "/robots.txt"
    Entonces el contenido incluye "Disallow: /Admin/"
    Y el contenido incluye "sitemap.xml"

  Escenario: sitemap.xml lista las páginas públicas
    Cuando abro la dirección "/sitemap.xml"
    Entonces el contenido incluye "/Publicaciones"
    Y el contenido incluye "/Home/Privacidad"

  @https
  Escenario: El sitio publicado redirige de http a https
    Cuando abro el sitio con http
    Entonces la dirección final empieza con "https://"
