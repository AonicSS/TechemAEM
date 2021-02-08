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
class ServiceItemTest
{

	private ServiceItem serviceItem;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/service-item";

	private static final String PRODUCT_TITLE = "productTitle";
	private static final String LINK_URL = "linkURL";
	private static final String DESCRIPTION = "description";

	private static final String EXPECTED_PRODUCT_TITLE = "Hello from service  item";
	private static final String EXPECTED_LINK_URL = "www.techem.de";
	private static final String EXPECTED_DESCRIPTION = "some long long  long description for the service item";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "serviceItem",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				PRODUCT_TITLE, EXPECTED_PRODUCT_TITLE,
				LINK_URL, EXPECTED_LINK_URL,
				DESCRIPTION, EXPECTED_DESCRIPTION);

		serviceItem = resource.adaptTo(ServiceItem.class);
	}

	@Test
	void testServiceItemTitle() throws Exception
	{
		assertNotNull(serviceItem);
		assertEquals(EXPECTED_PRODUCT_TITLE, serviceItem.getProductTitle());
	}

	@Test
	void testServiceItemLinkUrl() throws Exception
	{
		assertNotNull(serviceItem);
		assertEquals(EXPECTED_LINK_URL, serviceItem.getLinkURL());
	}

	@Test
	void testServiceItemDescription() throws Exception
	{
		assertNotNull(serviceItem);
		assertEquals(EXPECTED_DESCRIPTION, serviceItem.getDescription());
	}

}
