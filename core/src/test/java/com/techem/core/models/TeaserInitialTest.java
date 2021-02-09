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
class TeaserInitialTest
{

	private TeaserInitial teaserInitial;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/teaser-initial";

	private static final String CONTAINER_HEADLINE = "containerHeadline";
	private static final String CONTAINER_SUBTITLE = "containerSubtitle";

	private static final String EXPECTED_CONTAINER_HEADLINE = "Hello from teaser initial";
	private static final String EXPECTED_CONTAINER_SUBTITLE = "some long long long long  subtitle";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "teaserInitial",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				CONTAINER_HEADLINE, EXPECTED_CONTAINER_HEADLINE,
				CONTAINER_SUBTITLE, EXPECTED_CONTAINER_SUBTITLE);

		teaserInitial = resource.adaptTo(TeaserInitial.class);
	}

	@Test
	void testContainerSubtitle() throws Exception
	{
		assertNotNull(teaserInitial);
		assertEquals(EXPECTED_CONTAINER_SUBTITLE, teaserInitial.getContainerSubtitle());
	}

	@Test
	void testContainerHeadline() throws Exception
	{
		assertNotNull(teaserInitial);
		assertEquals(EXPECTED_CONTAINER_HEADLINE, teaserInitial.getContainerHeadline());
	}

}
