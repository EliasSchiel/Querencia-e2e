# Querencia E2E

[![Smoke diario en producción](https://github.com/EliasSchiel/Querencia-e2e/actions/workflows/smoke-diario.yml/badge.svg)](https://eliasschiel.github.io/Querencia-e2e/)
[![Reporte Allure](https://img.shields.io/badge/reporte-Allure-orange)](https://eliasschiel.github.io/Querencia-e2e/)
[![Ejecuciones](https://img.shields.io/badge/ejecuciones-GitHub%20Actions-2E4A3B)](https://github.com/EliasSchiel/Querencia-e2e/actions/workflows/smoke-diario.yml)

Pruebas end-to-end de **[Querencia](https://proyecto-querencia.runasp.net)**, una plataforma de adopción responsable de mascotas
(ASP.NET Core MVC + SQL Server). La suite prueba la aplicación desde afuera, como lo haría una persona con el navegador.

**Stack:** Java 17 · Maven · Selenium WebDriver 4 · Cucumber 7 (Gherkin en español) · JUnit 5 · Page Object Model · Allure · GitHub Actions.

## Dos modos con la misma suite

Los escenarios se separan con etiquetas de Cucumber:

| Etiqueta | Contra qué corre | Qué hace |
| --- | --- | --- |
| `@smoke` | Producción | **Solo lectura.** Carga de páginas, navegación, filtros, fichas, seguridad (páginas privadas, robots.txt, sitemap, https), versión celular y login. Corre todos los días en GitHub Actions. |
| `@regresion` | Entorno privado de pruebas | **Flujos completos que crean datos:** registro, roles, adopción de punta a punta, límite de colectas con rendición de comprobantes y denuncias. |

Por seguridad, los escenarios `@regresion` **no se ejecutan contra producción** aunque se olviden las etiquetas
(ver `hooks/Hooks.java`): crean cuentas, mascotas y denuncias.

> **Sobre el código de Querencia:** este repositorio contiene solo las pruebas. El código de la aplicación es privado,
> así que los escenarios `@regresion` los ejecuta el autor en su entorno de pruebas antes de cada publicación.
> Los escenarios `@smoke` sí se pueden correr desde cualquier lugar, porque solo leen el sitio público.

## Cobertura

| Feature | Escenarios | Qué valida |
| --- | --- | --- |
| `navegacion.feature` | 7 | Portada, menú, páginas legales, página 404 amigable |
| `busqueda.feature` | 7 | Listado, filtro por cada tipo de publicación, búsqueda sin resultados, ficha |
| `seguridad.feature` | 9 | 6 páginas privadas piden login con ReturnUrl, robots.txt, sitemap.xml, redirección http → https |
| `responsive.feature` | 7 | Menú hamburguesa y que ninguna página se desborde en un celular de 390 px |
| `login.feature` | 6 | Formulario, credenciales incorrectas, cuenta de prueba, menú según el rol (admin, usuario, asociación) |
| `registro.feature` | 4 | Validaciones (correo, contraseñas distintas, contraseña corta) y alta exitosa |
| `adopcion.feature` | 1 | Publicar → aprobar (admin) → postularse → elegir → registrar la entrega |
| `colectas.feature` | 1 | Regla de negocio: máximo 3 colectas sin rendir, rendición con comprobante y aprobación del admin |
| `denuncias.feature` | 1 | Denunciar, no poder repetir, revisión del administrador |

**Bugs que encontró la suite** (y ya se corrigieron en Querencia):
- El botón flotante del asistente tapaba los enlaces «Términos» y «Privacidad» del pie: no se podía hacer clic.
- En el celular, la ficha de una mascota se desbordaba 12 px hacia los costados (gutter de Bootstrap más ancho que el padding del contenedor).

## Cómo correr el smoke

Requisitos: Java 17, Maven y Google Chrome (Selenium Manager descarga el driver solo).

```bash
# Solo lectura contra el sitio publicado: no crea ni modifica nada
mvn test -Dbaseurl=https://proyecto-querencia.runasp.net -Dcucumber.filter.tags=@smoke

# Lo mismo con el navegador oculto
mvn test -Dbaseurl=https://proyecto-querencia.runasp.net -Dcucumber.filter.tags=@smoke -Dheadless=true
```

Opciones: `-Dbrowser=chrome|edge|firefox`, `-Dheadless=true`, `-Dbaseurl=...`.

### Reportes

- **Cucumber HTML:** `target/cucumber-report.html` (se abre con el navegador).
- **Allure:** `allure serve target/allure-results` (requiere [Allure](https://allurereport.org/docs/install/)), o el [reporte publicado en GitHub Pages](https://eliasschiel.github.io/Querencia-e2e/).
- Si un paso falla, se adjunta una **captura de pantalla** y la URL al reporte.

## Integración continua

`.github/workflows/smoke-diario.yml` corre `@smoke` contra producción **todos los días a las 9:00** (hora de Argentina),
en cada push a `main` y a mano con *Run workflow*. Genera el reporte de Allure **con historial** (tendencia de las
corridas anteriores) y lo publica en GitHub Pages. Si falla un escenario, el run queda en rojo y GitHub avisa por mail.

**Secrets** (Settings → Secrets and variables → Actions):

| Secret | Para qué |
| --- | --- |
| `QUERENCIA_TEST_EMAIL` | Correo de una cuenta común creada para las pruebas |
| `QUERENCIA_TEST_PASSWORD` | Su contraseña |

Sin estos secrets el escenario de la cuenta de prueba se marca como omitido (no falla).

## Estructura

```
src/main/java/pages/          Page Objects (BasePage con esperas explícitas y selectores data-testid)
src/test/java/steps/          Definiciones de pasos de Cucumber
src/test/java/hooks/          Driver por escenario, capturas al fallar, protección de @regresion
src/test/java/utils/          Configuración, DriverManager (ThreadLocal), datos únicos (CUIT válido), sesión
src/test/java/runners/        Runner de JUnit Platform Suite
src/test/resources/features/  Escenarios en Gherkin (español)
```

**Decisiones:**
- **Selectores `data-testid`** agregados en Querencia para las pruebas: no se rompen si cambia un texto o un estilo.
- **Esperas explícitas** (`WebDriverWait`), nunca `Thread.sleep`.
- **Datos únicos por ejecución** (correos, nombres, CUIT con dígito verificador válido): la regresión se puede repetir sin limpiar la base.
- **Cambio de usuario por cookies** dentro de un escenario (dueña, administrador, adoptante), más rápido y estable que usar el menú.
