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
class DoubleTextImageTest
{

	private DoubleTextImage doubleTextImage;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/double-text-image";

	private static final String HEADLINE = "headline";

	private static final String EXPECTED_HEADLINE = "Headline for Double Text Image Container";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "double",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE);

		doubleTextImage = resource.adaptTo(DoubleTextImage.class);
	}

	@Test
	void testDoubleTextImageHeadline() throws Exception
	{
		assertNotNull(doubleTextImage);
		assertEquals(EXPECTED_HEADLINE, doubleTextImage.getHeadline());
	}

}
