# language: es
@smoke @busqueda
Característica: Búsqueda de mascotas
  Como persona que quiere adoptar o ayudar
  Quiero filtrar las publicaciones
  Para encontrar rápido a la mascota que busco

  Antecedentes:
    Dado que abro el listado de mascotas

  Escenario: El listado muestra publicaciones
    Entonces veo al menos 1 publicación en el listado

  Esquema del escenario: Filtrar por tipo muestra solo publicaciones de ese tipo
    Cuando filtro por el tipo "<tipo>"
    Entonces todas las publicaciones del listado son del tipo "<tipo>"

    Ejemplos:
      | tipo        |
      | Adopcion    |
      | PrimerHogar |
      | Transito    |
      | PedidoAyuda |

  Escenario: Buscar un nombre que no existe muestra el aviso sin resultados
    Cuando busco "zz-ninguna-mascota-se-llama-asi"
    Entonces veo el aviso de que no hay resultados

  Escenario: Abrir una publicación muestra su ficha completa
    Cuando abro la primera publicación del listado
    Entonces la ficha muestra el mismo nombre que la tarjeta
    Y la ficha muestra la ubicación y los datos de la mascota
