package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(id = "Email")
    private WebElement emailInput;

    @FindBy(id = "Password")
    private WebElement passwordInput;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(By.id("Email")) && isDisplayed(By.id("Password")) && isDisplayed(testId("btn-ingresar"));
    }

    public void ingresar(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        click(testId("btn-ingresar"));
    }
}
