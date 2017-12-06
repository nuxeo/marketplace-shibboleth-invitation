package org.nuxeo.ecm.platform.shibboleth.pages;

import org.nuxeo.functionaltests.Locator;
import org.nuxeo.functionaltests.forms.Select2WidgetElement;
import org.nuxeo.functionaltests.pages.DocumentBasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @since 9.10
 */
public class InvitationTabSubPage extends DocumentBasePage {

    @FindBy(id = "workspace_requests_create:nxl_user_invitation_info:nxw_user_request_email")
    protected WebElement emailField;

    @FindBy(id = "workspace_requests_create:nxl_user_invitation_info:rights_permission_select")
    protected WebElement permissionField;

    @FindBy(id = "s2id_workspace_requests_create:nxl_user_invitation_info:nxw_user_request_group_select2")
    protected WebElement groupField;

    @FindBy(xpath = "//input[@type='submit' and @value='Invite User']")
    protected WebElement inviteButton;


    public InvitationTabSubPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedDocumentBasePage invite(String email, String permission, String group) {
        emailField.sendKeys(email);
        selectItemInDropDownMenu(permissionField, permission);
        Select2WidgetElement groups = new Select2WidgetElement(driver, groupField, true);
        groups.selectValue(group);
        Locator.waitUntilEnabledAndClick(inviteButton);
        return asPage(ExtendedDocumentBasePage.class);
    }

}
