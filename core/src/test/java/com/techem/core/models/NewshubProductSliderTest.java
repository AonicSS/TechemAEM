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
class NewshubProductSliderTest {

	private NewshubProductSlider newshubProductSlider;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/newshub-product-slider";

	private static final String HEADLINE = "headline";
	private static final String ALIGNMENT = "alignment";

	private static final String EXPECTED_HEADLINE = "Headline";
	private static final String EXPECTED_ALIGNMENT = "center";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception {

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "newshubProductSlider",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE,
				ALIGNMENT, EXPECTED_ALIGNMENT);

		newshubProductSlider = resource.adaptTo(NewshubProductSlider.class);
	}

	@Test
	void testHeadline() throws Exception {
		assertNotNull(newshubProductSlider);
		assertEquals(EXPECTED_HEADLINE, newshubProductSlider.getHeadline());
	}
	@Test
	void testAlignment() throws Exception {
		assertNotNull(newshubProductSlider);
		assertEquals(EXPECTED_ALIGNMENT, newshubProductSlider.getAlignment());
	}
}