package com.techem.core.models;

import com.adobe.xfa.Bool;
import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AemContextExtension.class)
class CarouselImageGalleryTest
{

	private CarouselImageGallery carouselImageGallery;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/carousel-image-gallery";

	private static final String LOOP = "loop";

	private static final Boolean EXPECTED_LOOP = true;

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "carouselImageGallery",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				LOOP, EXPECTED_LOOP);

		carouselImageGallery = resource.adaptTo(CarouselImageGallery.class);
	}

	@Test
	void testCaroselImageGalleryLoop() throws Exception
	{
		assertNotNull(carouselImageGallery);
		assertTrue(carouselImageGallery.getLoop());
	}

}
