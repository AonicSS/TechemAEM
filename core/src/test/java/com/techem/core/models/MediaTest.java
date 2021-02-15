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
class MediaTest
{

	private Media media;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/footer";

	private static final String TITLE = "title";
	private static final String ALT_TEXT = "altText";
	private static final String ICON = "icon";
	private static final String LINK = "link";
	private static final String OPEN_NEW_TAB = "openNewTab";


	private static final String EXPECTED_TITLE = "Title of Media";
	private static final String EXPECTED_ALT_TEXT = "AltText of Media";
	private static final String EXPECTED_ICON = "Icon";
	private static final String EXPECTED_LINK = "www.techem.com";
	private static final Boolean EXPECTED_OPEN_NEW_TAB = true;

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "media",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				TITLE, EXPECTED_TITLE,
				ALT_TEXT, EXPECTED_ALT_TEXT,
				ICON, EXPECTED_ICON,
				LINK, EXPECTED_LINK,
				OPEN_NEW_TAB, EXPECTED_OPEN_NEW_TAB);

		media = resource.adaptTo(Media.class);
	}

	@Test
	void testMediaTitle() throws Exception
	{
		assertNotNull(media);
		assertEquals(EXPECTED_TITLE, media.getTitle());
	}

	@Test
	void testMediaAltText() throws Exception
	{
		assertNotNull(media);
		assertEquals(EXPECTED_ALT_TEXT, media.getAltText());
	}

	@Test
	void testMediaIcon() throws Exception
	{
		assertNotNull(media);
		assertEquals(EXPECTED_ICON, media.getIcon());
	}

	@Test
	void testMediaLink() throws Exception
	{
		assertNotNull(media);
		assertEquals(EXPECTED_LINK, media.getLink());
	}

	@Test
	void testMediaOpenNewTab() throws Exception
	{
		assertNotNull(media);
		assertEquals(EXPECTED_OPEN_NEW_TAB, media.getOpenNewTab());
	}
}
