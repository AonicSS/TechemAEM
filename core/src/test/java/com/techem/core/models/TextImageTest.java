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
class TextImageTest
{

	private TextImage textImage;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/text-image";

	private static final String HEADLINE = "headline";
	private static final String TEXT_POSITION = "textPosition";
	private static final String TEXT = "text";

	private static final String EXPECTED_HEADLINE = "Hello";
	private static final String EXPECTED_TEXT_POSITION = "left";
	private static final String EXPECTED_TEXT = "some long long text";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "textImage",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE,
				TEXT_POSITION, EXPECTED_TEXT_POSITION,
				TEXT, EXPECTED_TEXT);

		textImage = resource.adaptTo(TextImage.class);
	}

	@Test
	void testTextPosition() throws Exception
	{
		assertNotNull(textImage);
		assertEquals(EXPECTED_TEXT_POSITION, textImage.getTextPosition());
	}

	@Test
	void testSText() throws Exception
	{
		assertNotNull(textImage);
		assertEquals(EXPECTED_TEXT, textImage.getText());
	}

	@Test
	void testHeadline() throws Exception
	{
		assertNotNull(textImage);
		assertEquals(EXPECTED_HEADLINE, textImage.getHeadline());
	}

}
