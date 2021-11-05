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
class NewsBannerTest {

    private NewsBanner newsBanner;

    private static final String CONTENT_TEST_RESOURCE = "/content/components/news-banner";
    private static final String RESOURCE_TYPE = "sling:resourceType";
    private static final String COMPONENT_RESOURCE_TYPE = "techem/components/news-banner";

    private static final String HEADLINE = "articleTitle";
    private static final String SUBTITLE = "articleSubTitle";
    private static final String IMAGE_REFERENCE = "articleImg";
    private static final String ARTICLE_PATH = "articlePath";

    private static final String EXPECTED_HEADLINE = "Lorem ipsum dolor sit amet";
    private static final String EXPECTED_SUBTITLE = "Test";
    private static final String EXPECTED_IMAGE_REFERENCE = "/content/dam/core-components-examples/library/sample-assets/mountain-range.jpg";
    private static final String EXPECTED_ARTICLE_PATH = "/content/components/news-banner/jcr:content/newsBanner/articlePath";

    private Page page;
    private Resource resource;

    @BeforeEach
    public void setup(AemContext context) throws Exception
    {
        page = context.create().page(CONTENT_TEST_RESOURCE);
        resource = context.create().resource(page, "newsBanner",
                RESOURCE_TYPE, COMPONENT_RESOURCE_TYPE,
                HEADLINE, EXPECTED_HEADLINE,
                SUBTITLE, EXPECTED_SUBTITLE,
                IMAGE_REFERENCE, EXPECTED_IMAGE_REFERENCE,
                ARTICLE_PATH, EXPECTED_ARTICLE_PATH);

        newsBanner = resource.adaptTo(NewsBanner.class);
    }

    @Test
    void testGetArticleImage() throws Exception{
        assertNotNull(newsBanner);
        assertEquals(EXPECTED_IMAGE_REFERENCE, newsBanner.getArticleImage());
    }

    @Test
    void testGetArticleTitle() {
        assertNotNull(newsBanner);
        assertEquals(EXPECTED_HEADLINE, newsBanner.getArticleTitle());
    }

    @Test
    void testGetArticleSubTitle() {
        assertNotNull(newsBanner);
        assertEquals(EXPECTED_SUBTITLE, newsBanner.getArticleSubTitle());
    }

    @Test
    void testGetArticlePath() {
        assertNotNull(newsBanner);
        assertEquals(EXPECTED_ARTICLE_PATH, newsBanner.getArticlePath());
    }

}