package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InicioPage extends BasePage {

    public InicioPage(WebDriver driver) {
        super(driver);
    }

    public boolean portadaVisible() {
        return isDisplayed(testId("portada"));
    }

    public int mascotasRecientes() {
        return todos(By.cssSelector("[data-testid='seccion-recientes'] [data-testid='tarjeta-publicacion']")).size();
    }
}
