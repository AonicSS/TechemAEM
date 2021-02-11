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
class ButtonTest
{

	private Button button;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/button";

	private static final String OPEN_NEW_TAB = "openNewTab";

	private static final Boolean EXPECTED_OPEN_NEW_TAB = true;

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "button",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				OPEN_NEW_TAB, EXPECTED_OPEN_NEW_TAB);

		button = resource.adaptTo(Button.class);
	}

	@Test
	void testButtonOpenNewTab() throws Exception
	{
		assertNotNull(button);
		assertEquals(EXPECTED_OPEN_NEW_TAB, button.getOpenNewTab());
	}

}
