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
class FullBleedTest
{

	private FullBleed fullBleed;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/full-bleed";

	private static final String HEADLINE = "headline";
	private static final String BODY = "body";
	private static final String HTML_ID = "htmlId";

	private static final String EXPECTED_HEADLINE = "Headline for Full Bleed";
	private static final String EXPECTED_BODY = "Body for Full Bleed";
	private static final String EXPECTED_HTML_ID = "ID for Full Bleed";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "fullBleed",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE,
				BODY, EXPECTED_BODY,
				HTML_ID, EXPECTED_HTML_ID);

		fullBleed = resource.adaptTo(FullBleed.class);
	}

	@Test
	void testFullBleedHeadline() throws Exception
	{
		assertNotNull(fullBleed);
		assertEquals(EXPECTED_HEADLINE, fullBleed.getHeadline());
	}

	@Test
	void testFullBleedBody() throws Exception
	{
		assertNotNull(fullBleed);
		assertEquals(EXPECTED_BODY, fullBleed.getBody());
	}

	@Test
	void testFullBleedId() throws Exception
	{
		assertNotNull(fullBleed);
		assertEquals(EXPECTED_HTML_ID, fullBleed.getHtmlId());
	}
}
