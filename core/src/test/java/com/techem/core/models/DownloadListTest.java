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
class DownloadListTest
{

	private DownloadList downloadList;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/download-list";

	private static final String HEADLINE = "headline";
	private static final String DESCRIPTION = "description";

	private static final String EXPECTED_HEADLINE = "Headline for Download List";
	private static final String EXPECTED_DESCRIPTION = "5 Ergebnisse";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "downloadList",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE,
				DESCRIPTION, EXPECTED_DESCRIPTION);

		downloadList = resource.adaptTo(DownloadList.class);
	}

	@Test
	void testDownloadListHeadline() throws Exception
	{
		assertNotNull(downloadList);
		assertEquals(EXPECTED_HEADLINE, downloadList.getHeadline());
	}

	@Test
	void testDownloadListDescription() throws Exception
	{
		assertNotNull(downloadList);
		assertEquals(EXPECTED_DESCRIPTION, downloadList.getDescription());
	}
}
