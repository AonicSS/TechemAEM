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
class IconNumberTileTest
{

	private IconNumberTile iconNumberTile;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/icon-number-tile";

	private static final String NUMBER = "number";
	private static final String TITLE = "title";
	private static final String BODY = "body";

	private static final String EXPECTED_NUMBER = "Number for Icon Number Tile";
	private static final String EXPECTED_TITLE = "Title for Icon Number Tile";
	private static final String EXPECTED_BODY = "Text for Icon Number Tile";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "iconNumberTile",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				NUMBER, EXPECTED_NUMBER,
				TITLE, EXPECTED_TITLE,
				BODY, EXPECTED_BODY);

		iconNumberTile = resource.adaptTo(IconNumberTile.class);
	}

	@Test
	void testIconNumberTileNumber() throws Exception
	{
		assertNotNull(iconNumberTile);
		assertEquals(EXPECTED_NUMBER, iconNumberTile.getNumber());
	}

	@Test
	void testIconNumberTileTitle() throws Exception
	{
		assertNotNull(iconNumberTile);
		assertEquals(EXPECTED_TITLE, iconNumberTile.getTitle());
	}

	@Test
	void testIconNumberTileBody() throws Exception
	{
		assertNotNull(iconNumberTile);
		assertEquals(EXPECTED_BODY, iconNumberTile.getBody());
	}
}
