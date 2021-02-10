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
class HomeStageItemTest
{

	private HomeStageItem homeStageItem;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/home-stage-item";

	private static final String HEADLINE = "headline";
	private static final String LINK_URL_2 = "linkURL2";

	private static final String EXPECTED_HEADLINE = "Hello from home stage item";
	private static final String EXPECTED_LINK_URL_2 = "www.techem.de";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "serviceItem",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE,
				LINK_URL_2, EXPECTED_LINK_URL_2);

		homeStageItem = resource.adaptTo(HomeStageItem.class);
	}

	@Test
	void testHomeStageItemHeadline() throws Exception
	{
		assertNotNull(homeStageItem);
		assertEquals(EXPECTED_HEADLINE, homeStageItem.getHeadline());
	}

	@Test
	void testHomeStageItemLinkURL2() throws Exception
	{
		assertNotNull(homeStageItem);
		assertEquals(EXPECTED_LINK_URL_2, homeStageItem.getLinkURL2());
	}

}
