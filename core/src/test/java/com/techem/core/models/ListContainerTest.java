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
class ListContainerTest
{

	private ListContainer listContainer;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/list-container";

	private static final String HEADLINE = "headline";
	private static final String BACKGROUND = "bgColor";

	private static final String EXPECTED_HEADLINE = "Headline for List Container";
	private static final String EXPECTED_BACKGROUND = "gray";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "listContainer",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE,
				BACKGROUND, EXPECTED_BACKGROUND);

		listContainer = resource.adaptTo(ListContainer.class);
	}

	@Test
	void testListContainerHeadline() throws Exception
	{
		assertNotNull(listContainer);
		assertEquals(EXPECTED_HEADLINE, listContainer.getHeadline());
	}

	@Test
	void testListContainerBackground() throws Exception
	{
		assertNotNull(listContainer);
		assertEquals(EXPECTED_BACKGROUND, listContainer.getBgColor());
	}
}
