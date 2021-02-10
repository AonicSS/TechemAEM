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
class GeneralContainerTest
{

	private GeneralContainer generalContainer;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/general-container";

	private static final String HEADLINE = "headline";
	private static final String SUBTITLE = "subtitle";
	private static final String BACKGROUND = "background";

	private static final String EXPECTED_HEADLINE = "Headline for General Container";
	private static final String EXPECTED_SUBTITLE = "Subtitle for General container";
	private static final String EXPECTED_BACKGROUND = "gray";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "generalContainer",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE,
				SUBTITLE, EXPECTED_SUBTITLE,
				BACKGROUND, EXPECTED_BACKGROUND);

		generalContainer = resource.adaptTo(GeneralContainer.class);
	}

	@Test
	void testGeneralContainerHeadline() throws Exception
	{
		assertNotNull(generalContainer);
		assertEquals(EXPECTED_HEADLINE, generalContainer.getHeadline());
	}

	@Test
	void testGeneralContainerSubtitle() throws Exception
	{
		assertNotNull(generalContainer);
		assertEquals(EXPECTED_SUBTITLE, generalContainer.getSubtitle());
	}

	@Test
	void testGeneralContainerBackground() throws Exception
	{
		assertNotNull(generalContainer);
		assertEquals(EXPECTED_BACKGROUND, generalContainer.getBackground());
	}
}
