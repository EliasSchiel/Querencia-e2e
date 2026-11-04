package runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

/**
 * Runner de la suite. Ejecuta todas las features de src/test/resources/features.
 *
 * Qué escenarios corren se elige con etiquetas al ejecutar:
 *   mvn test -Dcucumber.filter.tags=@smoke        (solo lectura, apto para producción)
 *   mvn test -Dcucumber.filter.tags=@regresion    (flujos completos, contra Querencia local)
 *
 * Los reportes (pretty, HTML, JSON y Allure) se configuran en el pom.xml.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "steps,hooks")
public class CucumberTest {
}
