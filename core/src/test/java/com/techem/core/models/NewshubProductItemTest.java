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
class NewshubProductItemTest {

	private NewshubProductItem newshubProductItem;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";

	private static final String PRODUCT_TITLE = "productTitle";
	private static final String PRODUCT_BODY = "productBody";
	private static final String PRODUCT_LINK = "productLink";
	private static final String PRODUCT_FILE_REFERENCE = "productFileReference";


	private static final String EXPECTED_PRODUCT_TITLE = "Headline";
	private static final String EXPECTED_PRODUCT_BODY = "Text";
	private static final String EXPECTED_PRODUCT_LINK = "www.techem.com/de/de";
	private static final String EXPECTED_PRODUCT_FILE_REFERENCE= "/content/dam/core-components-examples/library/sample-assets/mountain-range.jpg";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception {

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "newshubProductItem",
				PRODUCT_TITLE, EXPECTED_PRODUCT_TITLE,
				PRODUCT_BODY, EXPECTED_PRODUCT_BODY,
				PRODUCT_LINK, EXPECTED_PRODUCT_LINK,
				PRODUCT_FILE_REFERENCE, EXPECTED_PRODUCT_FILE_REFERENCE);

		newshubProductItem = resource.adaptTo(NewshubProductItem.class);
	}

	@Test
	void testProductTitle() throws Exception {
		assertNotNull(newshubProductItem);
		assertEquals(EXPECTED_PRODUCT_TITLE, newshubProductItem.getProductTitle());
	}

	@Test
	void testProductBody() throws Exception {
		assertNotNull(newshubProductItem);
		assertEquals(EXPECTED_PRODUCT_BODY, newshubProductItem.getProductBody());
	}

	@Test
	void testProductLink() throws Exception {
		assertNotNull(newshubProductItem);
		assertEquals(EXPECTED_PRODUCT_LINK, newshubProductItem.getProductLink());
	}

	@Test
	void testProductFileReference() throws Exception
	{
		assertNotNull(newshubProductItem);
		assertEquals(EXPECTED_PRODUCT_FILE_REFERENCE, newshubProductItem.getProductFileReference());
	}
}