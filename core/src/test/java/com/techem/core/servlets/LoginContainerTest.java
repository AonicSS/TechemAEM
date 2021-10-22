package com.techem.core.servlets;

import com.day.cq.wcm.api.Page;
import com.techem.core.models.LoginContainer;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AemContextExtension.class)
class LoginContainerTest
{

    private LoginContainer loginContainer;

    private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
    private static final String RESOURCE_TYPE = "sling:resourceType";
    private static final String COMPONENT_RESOURCE_TYPE = "techem/components/login/login-container";

    private static final String LINK_URL= "linkURL";
    private static final String EXPECTED_LINK_URL = "/content/de";

    private Page page;
    private Resource resource;

    @BeforeEach
    public void setup(AemContext context) throws Exception {

        page = context.create().page(CONTENT_TEST_RESOURCE);
        resource = context.create().resource(page, "loginContainer",
                RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
                LINK_URL, EXPECTED_LINK_URL);

        loginContainer = resource.adaptTo(LoginContainer.class);
    }

    @Test
    void testImageTileLinkURL2() throws Exception {
        assertNotNull(loginContainer);
        assertEquals(EXPECTED_LINK_URL, loginContainer.getLinkURL());
    }

}