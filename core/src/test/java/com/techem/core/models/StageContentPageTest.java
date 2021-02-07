package com.techem.core.models;

import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AemContextExtension.class)
class StageContentPageTest
{

	private StageContentPage stageContentPage;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/teaser-initial";

	private static final String HEADLINE = "headline";
	private static final String TEXT = "text";

	private static final String EXPECTED_HEADLINE = "Hello from stage container page";
	private static final String EXPECTED_TEXT = "some long long long long  subtitle";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "stageContentPage",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE,
				TEXT, EXPECTED_TEXT);

		stageContentPage = resource.adaptTo(StageContentPage.class);
	}

	@Test
	void testText() throws Exception
	{
		assertNotNull(stageContentPage);
		assertEquals(EXPECTED_TEXT, stageContentPage.getText());
	}

	@Test
	void testHeadline() throws Exception
	{
		assertNotNull(stageContentPage);
		Assertions.assertEquals(EXPECTED_HEADLINE, stageContentPage.getHeadline());
	}

}
