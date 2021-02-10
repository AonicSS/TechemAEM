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
class ContactTileTest
{

	private ContactTile contactTile;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/contact-tile";

	private static final String HEADLINE = "headline";
	private static final String SUBLINE = "subline";
	private static final String PHONE = "phone";
	private static final String EMAIL = "email";

	private static final String EXPECTED_HEADLINE = "Hello from contact tile";
	private static final String EXPECTED_SUBLINE = "Some text for subline part";
	private static final String EXPECTED_PHONE = "+49061965222324";
	private static final String EXPECTED_EMAIL = "robert.woggon@techem.de";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "contactTile",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				HEADLINE, EXPECTED_HEADLINE,
				SUBLINE, EXPECTED_SUBLINE,
				PHONE, EXPECTED_PHONE,
				EMAIL, EXPECTED_EMAIL);

		contactTile = resource.adaptTo(ContactTile.class);
	}

	@Test
	void testContactTileHeadline() throws Exception
	{
		assertNotNull(contactTile);
		assertEquals(EXPECTED_HEADLINE, contactTile.getHeadline());
	}

	@Test
	void testContactTileSubline() throws Exception
	{
		assertNotNull(contactTile);
		assertEquals(EXPECTED_SUBLINE, contactTile.getSubline());
	}

	@Test
	void testContactTilePhone() throws Exception
	{
		assertNotNull(contactTile);
		assertEquals(EXPECTED_PHONE, contactTile.getPhone());
	}

	@Test
	void testContactTileEmail() throws Exception
	{
		assertNotNull(contactTile);
		assertEquals(EXPECTED_EMAIL, contactTile.getEmail());
	}

}
