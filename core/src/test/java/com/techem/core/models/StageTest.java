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
class StageTest
{

	private Stage stage;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/stage";

	private static final String HEADLINE = "headline";
	private static final String CATEGORY = "category";
	private static final String DATE = "dateObject";
	private static final String TEXT = "text";

	private static final String EXPECTED_HEADLINE = "Hello";
	private static final String EXPECTED_CATEGORY = "category of item";
	private static final String EXPECTED_DATE = "01.01.2020";
	private static final String EXPECTED_TEXT = "some long long text";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "stage",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE,
				CATEGORY, EXPECTED_CATEGORY,
				DATE, EXPECTED_DATE,
				TEXT, EXPECTED_TEXT);

		stage = resource.adaptTo(Stage.class);
	}

	@Test
	void testStageCategory() throws Exception
	{
		assertNotNull(stage);
		assertEquals(EXPECTED_CATEGORY, stage.getCategory());
	}

	@Test
	void testStageText() throws Exception
	{
		assertNotNull(stage);
		assertEquals(EXPECTED_TEXT, stage.getText());
	}

	@Test
	void testStageHeadline() throws Exception
	{
		assertNotNull(stage);
		assertEquals(EXPECTED_HEADLINE, stage.getHeadline());
	}

}
