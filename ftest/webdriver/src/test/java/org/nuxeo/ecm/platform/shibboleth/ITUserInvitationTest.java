/*
 * (C) Copyright 2017 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Funsho David
 */

package org.nuxeo.ecm.platform.shibboleth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.nuxeo.functionaltests.Constants.ADMINISTRATOR;
import static org.nuxeo.functionaltests.Constants.WORKSPACES_PATH;
import static org.nuxeo.functionaltests.Constants.WORKSPACE_TYPE;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nuxeo.ecm.platform.shibboleth.pages.ExtendedDocumentBasePage;
import org.nuxeo.ecm.platform.shibboleth.pages.InvitationTabSubPage;
import org.nuxeo.ecm.platform.shibboleth.pages.InvitationValidationPage;
import org.nuxeo.functionaltests.Locator;
import org.nuxeo.functionaltests.RestHelper;
import org.nuxeo.functionaltests.pages.DocumentBasePage;
import org.nuxeo.functionaltests.shibboleth.ShibbolethTest;
import org.openqa.selenium.By;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @since 9.10
 */
public class ITUserInvitationTest extends ShibbolethTest {

    protected static final String TEST_WORKSPACE = "TestWorkspace";

    protected SimpleSmtpServer mailServer;

    @Before
    public void before() {
        mailServer = SimpleSmtpServer.start(25000);
        RestHelper.createDocument(WORKSPACES_PATH, WORKSPACE_TYPE, TEST_WORKSPACE);
    }

    @After
    public void after() {
        RestHelper.cleanup();
        mailServer.stop();
    }

    @Test
    public void testUserInvitation() {
        loginAsShibbolethUser(NUXEO_URL + "/", ADMINISTRATOR, ADMINISTRATOR);
        Locator.findElementWaitUntilEnabledAndClick(By.linkText("WORKSPACE"));

        asPage(DocumentBasePage.class).goToWorkspaces()
                                      .goToDocumentWorkspaces()
                                      .getContentTab()
                                      .goToDocument(TEST_WORKSPACE);

        InvitationTabSubPage invitationPage = asPage(ExtendedDocumentBasePage.class).getInvitationTab();
        invitationPage.invite("nuxeotest@nuxeo.com", "Edit", "members");

        asPage(ExtendedDocumentBasePage.class).getWaitingInvitationTab();

        Locator.findElementWaitUntilEnabledAndClick(
                By.xpath("//form[@id='local_user_requests_view']//table//tr/td[11]/div/a"));
        Locator.waitForTextPresent(
                By.xpath(
                        "//span[@id='local_user_requests_view:local_user_requests_view_repeat:0:nxl_user_requests_listing_layout:nxw_listing_lifecycle']"),
                "Approved");
        logoutSimply();

        assertEquals(1, mailServer.getReceivedEmailSize());
        String message = ((SmtpMessage) mailServer.getReceivedEmail().next()).getBody();

        Pattern p = Pattern.compile("href=\"([^\"]*)\"", Pattern.DOTALL);
        Matcher m = p.matcher(message);
        assertTrue(m.find());
        String validateInvitationURL = m.group(1);

        InvitationValidationPage page = get(validateInvitationURL, InvitationValidationPage.class);

        page.chooseShibbolethAuthentication().login(SHIB_TEST_USER, SHIB_TEST_PASSWORD);
        Locator.findElementWaitUntilEnabledAndClick(By.linkText("WORKSPACE"));
        DocumentBasePage docPage = asPage(DocumentBasePage.class).goToWorkspaces()
                                                                 .goToDocumentWorkspaces()
                                                                 .getContentTab()
                                                                 .goToDocument(TEST_WORKSPACE);
        assertTrue(docPage.hasEditTab());

        logoutSimply();
    }

}
