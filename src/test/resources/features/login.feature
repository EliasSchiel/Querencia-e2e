# language: es
@login
Característica: Inicio de sesión
  Como usuario registrado
  Quiero ingresar con mi correo y contraseña
  Para publicar, postularme y seguir mis adopciones

  Antecedentes:
    Dado que abro la página para ingresar

  @smoke
  Escenario: El formulario de ingreso muestra sus campos
    Entonces el formulario de ingreso se muestra correctamente

  @smoke
  Escenario: Con credenciales incorrectas se muestra un error
    Cuando ingreso con el correo "nadie.e2e@querencia.test" y la contraseña "incorrecta123"
    Entonces veo el error "El correo o la contraseña no son correctos."
    Y no tengo la sesión iniciada

  @smoke @cuenta-prueba
  Escenario: La cuenta de prueba inicia y cierra sesión
    Cuando ingreso con la cuenta de prueba
    Entonces tengo la sesión iniciada
    Cuando cierro la sesión
    Entonces no tengo la sesión iniciada

  @regresion
  Esquema del escenario: Cada rol ve su menú
    Cuando ingreso con el correo "<correo>" y la contraseña "<clave>"
    Entonces tengo la sesión iniciada
    Y el menú de administración <visibilidad>

    Ejemplos:
      | correo                | clave      | visibilidad      |
      | admin@querencia.com   | Admin123   | está visible     |
      | maria@querencia.com   | Maria123   | no está visible  |
      | refugio@querencia.com | Refugio123 | no está visible  |
