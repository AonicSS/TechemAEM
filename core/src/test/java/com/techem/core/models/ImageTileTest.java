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
class ImageTileTest {

	private ImageTile imageTile;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/image-tile";

	private static final String HEADLINE = "headline";
	private static final String BODY = "body";
	private static final String LINK_URL_2 = "linkURL2";
	private static final String FILE_REFERENCE_2 = "fileReference2";


	private static final String EXPECTED_HEADLINE = "Headline for Image Tile";
	private static final String EXPECTED_BODY = "Text for Image Tile";
	private static final String EXPECTED_LINK_URL_2 = "www.techem.com";
	private static final String EXPECTED_FILE_REFERENCE_2 = "/content/dam/core-components-examples/library/sample-assets/mountain-range.jpg";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception {

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "imageTile",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE,
				BODY, EXPECTED_BODY,
				LINK_URL_2, EXPECTED_LINK_URL_2,
				FILE_REFERENCE_2, EXPECTED_FILE_REFERENCE_2);

		imageTile = resource.adaptTo(ImageTile.class);
	}

	@Test
	void testImageTileHeadline() throws Exception {
		assertNotNull(imageTile);
		assertEquals(EXPECTED_HEADLINE, imageTile.getHeadline());
	}

	@Test
	void testImageTileBody() throws Exception {
		assertNotNull(imageTile);
		assertEquals(EXPECTED_BODY, imageTile.getBody());
	}

	@Test
	void testImageTileLinkURL2() throws Exception {
		assertNotNull(imageTile);
		assertEquals(EXPECTED_LINK_URL_2, imageTile.getLinkURL2());
	}

	@Test
	void testImageTileFileReference2() throws Exception
	{
		assertNotNull(imageTile);
		assertEquals(EXPECTED_FILE_REFERENCE_2, imageTile.getFileReference2());
	}
}
