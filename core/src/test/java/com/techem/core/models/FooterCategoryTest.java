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
class FooterCategoryTest
{

	private FooterCategory footerCategory;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/footer";

	private static final String CATEGORY_TITLE = "categoryTitle";
	private static final String CATEGORY_LINK = "categoryLink";

	private static final String EXPECTED_CATEGORY_TITLE = "Category Title for Footer Category";
	private static final String EXPECTED_CATEGORY_LINK = "www.techem.de";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "double",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				CATEGORY_TITLE, EXPECTED_CATEGORY_TITLE,
                CATEGORY_LINK, EXPECTED_CATEGORY_LINK);

		footerCategory = resource.adaptTo(FooterCategory.class);
	}
    @Test
    void testFooterCategoryTitle() throws Exception
    {
        assertNotNull(footerCategory);
        assertEquals(EXPECTED_CATEGORY_TITLE, footerCategory.getCategoryTitle());
    }

	@Test
	void testFooterCategoryLink() throws Exception
	{
		assertNotNull(footerCategory);
		assertEquals(EXPECTED_CATEGORY_LINK, footerCategory.getCategoryLink());
	}

}
