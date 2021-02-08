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
class TabsIconNumberTest
{

	private TabsIconNumber tabsIconNumber;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/teaser-initial";

	private static final String HEADLINE = "headline";
	private static final String SUBTITLE = "subtitle";

	private static final String EXPECTED_HEADLINE = "Hello from tabs icon number";
	private static final String EXPECTED_SUBTITLE = "some long long long long  subtitle";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "tabsIconNumber",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE,
				SUBTITLE, EXPECTED_SUBTITLE);

		tabsIconNumber = resource.adaptTo(TabsIconNumber.class);
	}

	@Test
	void testContainerSubtitle() throws Exception
	{
		assertNotNull(tabsIconNumber);
		assertEquals(EXPECTED_SUBTITLE, tabsIconNumber.getSubtitle());
	}

	@Test
	void testContainerHeadline() throws Exception
	{
		assertNotNull(tabsIconNumber);
		assertEquals(EXPECTED_HEADLINE, tabsIconNumber.getHeadline());
	}

}
