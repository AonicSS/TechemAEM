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
class DownloadTest
{

	private Download download;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/download";

	private static final String TEXT = "text";
	private static final String ICON_FORMAT = "iconFormat";

	private static final String EXPECTED_TEXT = "Text for Download";
	private static final String EXPECTED_ICON_FORMAT = "pdf";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "download",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				TEXT, EXPECTED_TEXT,
				ICON_FORMAT, EXPECTED_ICON_FORMAT);

		download = resource.adaptTo(Download.class);
	}

	@Test
	void testDownloadText() throws Exception
	{
		assertNotNull(download);
		assertEquals(EXPECTED_TEXT, download.getText());
	}

	@Test
	void testDownloadIconFormat() throws Exception
	{
		assertNotNull(download);
		assertEquals(EXPECTED_ICON_FORMAT, download.getIconFormat());
	}
}
