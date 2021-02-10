package com.techem.core.models;

import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AemContextExtension.class)
class FooterLinkTest
{

	private FooterLink footerLink;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/footer";

	private static final String LINK_TEXT = "linkText";
	private static final String LINK_URL = "linkUrl";
	private static final String OPEN_NEW_TAB = "openNewTab";

	private static final String EXPECTED_LINK_TEXT = "Text for Footer Link";
	private static final String EXPECTED_LINK_URL = "www.techem.de";
	private static final Boolean EXPECTED_OPEN_NEW_TAB = true;

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "footerLink",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				LINK_TEXT, EXPECTED_LINK_TEXT,
				LINK_URL, EXPECTED_LINK_URL,
				OPEN_NEW_TAB, EXPECTED_OPEN_NEW_TAB);

		footerLink = resource.adaptTo(FooterLink.class);
	}
    @Test
    void testFooterLinkText() throws Exception
    {
        assertNotNull(footerLink);
        assertEquals(EXPECTED_LINK_TEXT, footerLink.getLinkText());
    }

	@Test
	void testFooterLinkUrl() throws Exception
	{
		assertNotNull(footerLink);
		assertEquals(EXPECTED_LINK_URL, footerLink.getLinkUrl());
	}

	@Test
	void testFooterLinkOpenNewTab() throws Exception
	{
		assertNotNull(footerLink);
		assertEquals(EXPECTED_OPEN_NEW_TAB, footerLink.getOpenNewTab());
	}

}
