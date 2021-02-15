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
class HeadlineTest
{

	private Headline headline;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/headline";

	private static final String HEADLINE = "headline";
	private static final String TEXT = "text";
	private static final String TYPES = "types";


	private static final String EXPECTED_HEADLINE = "Headline for the component";
	private static final String EXPECTED_TEXT = "Extra text for Headline";
	private static final String EXPECTED_TYPES = "H2";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "headline",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE,
				TEXT, EXPECTED_TEXT,
				TYPES, EXPECTED_TYPES);

		headline = resource.adaptTo(Headline.class);
	}

	@Test
	void testHeadlineHeadline() throws Exception
	{
		assertNotNull(headline);
		assertEquals(EXPECTED_HEADLINE, headline.getHeadline());
	}

	@Test
	void testHeadlineText() throws Exception
	{
		assertNotNull(headline);
		assertEquals(EXPECTED_TEXT, headline.getText());
	}

	@Test
	void testHeadlineTypes() throws Exception
	{
		assertNotNull(headline);
		assertEquals(EXPECTED_TYPES, headline.getTypes());
	}
}
