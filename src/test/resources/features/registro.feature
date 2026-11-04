# language: es
@regresion @registro
Característica: Registro de usuarios
  Como persona nueva en Querencia
  Quiero crear una cuenta
  Para poder adoptar o dar en adopción

  Antecedentes:
    Dado que abro la página para crear una cuenta

  Esquema del escenario: El formulario valida los datos antes de crear la cuenta
    Cuando completo el registro con el correo "<correo>", la contraseña "<clave>" y la confirmación "<confirmacion>"
    Y envío el registro
    Entonces veo el error "<error>"

    Ejemplos:
      | correo                | clave    | confirmacion | error                                  |
      | no-es-un-correo       | Clave123 | Clave123     | Ese correo no tiene un formato válido. |
      | nuevo@querencia.test  | Clave123 | Otra1234     | Las contraseñas no coinciden.          |
      | nuevo@querencia.test  | ab1      | ab1          | al menos 6 caracteres                  |

  Escenario: Con datos válidos se crea la cuenta y queda la sesión iniciada
    Cuando completo el registro con un correo nuevo y la contraseña "Clave123"
    Y envío el registro
    Entonces tengo la sesión iniciada
