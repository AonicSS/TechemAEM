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

@ExtendWith(AemContextExtension.class)
class GeneralTabsContainerTest
{

	private GeneralTabsContainer generalTabsContainer;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/general-tabs-container";

	private static final String HEADLINE = "headline";
	private static final String SUBTITLE = "subtitle";

	private static final String EXPECTED_HEADLINE = "Headline for General Container";
	private static final String EXPECTED_SUBTITLE = "Subtitle for General container";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "generalTabsContainer",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE,
				SUBTITLE, EXPECTED_SUBTITLE);

		generalTabsContainer = resource.adaptTo(GeneralTabsContainer.class);
	}

	@Test
	void testGeneralTabsContainerHeadline() throws Exception
	{
		assertNotNull(generalTabsContainer);
		assertEquals(EXPECTED_HEADLINE, generalTabsContainer.getHeadline());
	}

	@Test
	void testGeneralTabsContainerSubtitle() throws Exception
	{
		assertNotNull(generalTabsContainer);
		assertEquals(EXPECTED_SUBTITLE, generalTabsContainer.getSubtitle());
	}
}
