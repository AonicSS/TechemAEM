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
class HomeStageModelTest
{

	private HomeStageModel homeStageModel;

	private static final String CONTENT_TEST_RESOURCE = "/content/mypage";
	private static final String RESOURCE_TYPE = "sling:resourceType";
	private static final String COMPONENT_RESOURCE_TYPE = "techem/components/home-stage";

	private static final String TRANSITION_SPEED = "transitionSpeed";

	private static final String EXPECTED_TRANSITION_SPEED = "10000";

	private Page page;
	private Resource resource;

	@BeforeEach
	public void setup(AemContext context) throws Exception
	{

		page = context.create().page(CONTENT_TEST_RESOURCE);
		resource = context.create().resource(page, "homeStageModel",
				RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
				TRANSITION_SPEED, EXPECTED_TRANSITION_SPEED);

		homeStageModel = resource.adaptTo(HomeStageModel.class);
	}

	@Test
	void testHomeStageModelTransitionSpeed() throws Exception
	{
		assertNotNull(homeStageModel);
		assertEquals(EXPECTED_TRANSITION_SPEED, homeStageModel.getTransitionSpeed());
	}

}
