package org.nuxeo.ecm.platform.shibboleth.pages;

import org.nuxeo.functionaltests.pages.DocumentBasePage;
import org.nuxeo.functionaltests.pages.tabs.ManageTabSubPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @since 9.10
 */
public class ExtendedDocumentBasePage extends DocumentBasePage {

    @FindBy(xpath = "//a[contains(@id,'nxw_UserRegistrationRequestsCreate')]/span")
    protected WebElement invitationTabLink;

    @FindBy(xpath = "//a[contains(@id,'nxw_UserRegistrationRequestsListingLocal')]/span")
    protected WebElement waitingInvitationTabLink;

    public ExtendedDocumentBasePage(WebDriver driver) {
        super(driver);
    }

    public InvitationTabSubPage getInvitationTab() {
        getManageTab().clickOnDocumentTabLink(invitationTabLink);
        return asPage(InvitationTabSubPage.class);
    }

    public InvitationTabSubPage getWaitingInvitationTab() {
        getManageTab().clickOnDocumentTabLink(waitingInvitationTabLink);
        return asPage(InvitationTabSubPage.class);
    }

}
