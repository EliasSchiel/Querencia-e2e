# language: es
@regresion @colectas
Característica: Colectas con rendición obligatoria
  Como equipo de Querencia
  Quiero que cada asociación tenga como máximo 3 colectas sin comprobante aprobado
  Para cuidar la confianza de quienes donan

  Antecedentes:
    Dado una asociación nueva verificada por el administrador

  Escenario: La cuarta colecta sin rendir se bloquea hasta aprobar un comprobante
    Dado que la asociación abrió 3 colectas
    Cuando intenta abrir una cuarta colecta
    Entonces ve el aviso "Ya tenés 3 colectas sin rendir"

    Cuando la asociación informa que recaudó 40000 en la primera colecta y la cierra
    Y sube el comprobante de la primera colecta
    Entonces la primera colecta queda con el comprobante en revisión
    Y todavía no puede abrir una cuarta colecta

    Cuando el administrador aprueba el comprobante de la primera colecta
    Entonces la ficha de la primera colecta muestra la rendición verificada
    Y la asociación ya puede abrir otra colecta
