package org.nuxeo.ecm.platform.shibboleth.pages;

import org.nuxeo.functionaltests.pages.AbstractPage;
import org.nuxeo.functionaltests.pages.shibboleth.ShibbolethLoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @since 9.10
 */
public class InvitationValidationPage extends AbstractPage {

    @FindBy(name = "submitShibbo")
    protected WebElement button;

    public InvitationValidationPage(WebDriver driver) {
        super(driver);
    }

    public ShibbolethLoginPage chooseShibbolethAuthentication() {
        button.submit();
        return asPage(ShibbolethLoginPage.class);
    }
}
