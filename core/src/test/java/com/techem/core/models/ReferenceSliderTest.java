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
class ReferenceSliderTest
{

	private ReferenceSlider referenceSlider;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/reference-slider";

	private static final String HEADLINE = "headline";
	private static final String TITLE = "title";
	private static final String SOURCE_AUTHOR = "sourceAuthor";
	private static final String SOURCE_DETAILS = "sourceDetails";
	private static final String DESCRIPTION = "description";

	private static final String EXPECTED_HEADLINE = "Headline for Reference Slider";
	private static final String EXPECTED_TITLE = "Title for Reference Slider";
	private static final String EXPECTED_SOURCE_AUTHOR = "John Doe";
	private static final String EXPECTED_SOURCE_DETAILS = "private investigator";
	private static final String EXPECTED_DESCRIPTION = "Some description for the Reference Slider";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "referenceSlider",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE,
				TITLE, EXPECTED_TITLE,
				SOURCE_AUTHOR, EXPECTED_SOURCE_AUTHOR,
				SOURCE_DETAILS, EXPECTED_SOURCE_DETAILS,
				DESCRIPTION, EXPECTED_DESCRIPTION);

		referenceSlider = resource.adaptTo(ReferenceSlider.class);
	}

	@Test
	void testReferenceSliderHeadline() throws Exception
	{
		assertNotNull(referenceSlider);
		assertEquals(EXPECTED_HEADLINE, referenceSlider.getHeadline());
	}

	@Test
	void testReferenceSliderTitle() throws Exception
	{
		assertNotNull(referenceSlider);
		assertEquals(EXPECTED_TITLE, referenceSlider.getTitle());
	}

	@Test
	void testReferenceSliderSourceAuthor() throws Exception
	{
		assertNotNull(referenceSlider);
		assertEquals(EXPECTED_SOURCE_AUTHOR, referenceSlider.getSourceAuthor());
	}

	@Test
	void testReferenceSliderSourceDetails() throws Exception
	{
		assertNotNull(referenceSlider);
		assertEquals(EXPECTED_SOURCE_DETAILS, referenceSlider.getSourceDetails());
	}

	@Test
	void testReferenceSliderDescription() throws Exception
	{
		assertNotNull(referenceSlider);
		assertEquals(EXPECTED_DESCRIPTION, referenceSlider.getDescription());
	}
}
