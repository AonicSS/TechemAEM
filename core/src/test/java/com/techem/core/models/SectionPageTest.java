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
class SectionPageTest
{

	private SectionPage sectionPage;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/service-item";

	private static final String STAGE_HEADLINE = "stageHeadline";
	private static final String STAGE_TEXT = "stageText";
	private static final String IMAGE = "image";

	private static final String EXPECTED_STAGE_HEADLINE = "Hello from service  item";
	private static final String EXPECTED_STAGE_TEXT = "www.techem.de";
	private static final String EXPECTED_IMAGE = "path/to/image";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "serviceItem",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				STAGE_HEADLINE, EXPECTED_STAGE_HEADLINE,
				STAGE_TEXT, EXPECTED_STAGE_TEXT,
				IMAGE, EXPECTED_IMAGE);

		sectionPage = resource.adaptTo(SectionPage.class);
	}

	@Test
	void testStageHeadline() throws Exception
	{
		assertNotNull(sectionPage);
		assertEquals(EXPECTED_STAGE_HEADLINE, sectionPage.getStageHeadline());
	}

	@Test
	void testStageText() throws Exception
	{
		assertNotNull(sectionPage);
		assertEquals(EXPECTED_STAGE_TEXT, sectionPage.getStageText());
	}

	@Test
	void testImage() throws Exception
	{
		assertNotNull(sectionPage);
		assertEquals(EXPECTED_IMAGE, sectionPage.getImage());
	}

}
