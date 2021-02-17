package com.techem.core.models;

import com.day.cq.wcm.api.Page;
import com.google.common.collect.ImmutableMap;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AemContextExtension.class)
public class TextStageTest {

    private static final String HEADLINE = "Test";
    private static final String CATEGORY = "category";
    private static final String DATE = "2020-01-01T14:55:59.809+02:00";
    private static final String TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit";
    private static final String RESOURCE_PATH = "/content/techem/us/en/Monitoring/jcr:content";
    private static final String PAGE_PATH = "/content/techem/us/en/Monitoring";

    private AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private MockSlingHttpServletRequest request;

    private TextStage textStage;

    @BeforeEach
    public void setUp() throws Exception {

        final Page page = context.create().page(PAGE_PATH);
        final Resource resource = context.create().resource(RESOURCE_PATH,
                ImmutableMap.<String, Object>builder()
                        .put("headline", HEADLINE)
                        .put("category", CATEGORY)
                        .put("date", DATE)
                        .put("text", TEXT)
                        .put("cq:lastModified", DATE)
                        .build());

        context.currentPage(page);
        context.currentResource(resource);

        request = context.request();
        request.setResource(context.currentResource());

        textStage = request.adaptTo(TextStage.class);
    }

    @Test
    public void testGetters() {
      final String expected_date = "01.01.2020";
      final Stage stage = textStage.getStage();

      assertNotNull(stage);
      assertNotNull(stage.getDateObject());
      assertNotNull(stage.getLastModifiedObject());

      assertEquals(HEADLINE, stage.getHeadline());
      assertEquals(CATEGORY, stage.getCategory());
      assertEquals(expected_date, stage.getDate());
      assertEquals(TEXT, stage.getText());
    }
}
