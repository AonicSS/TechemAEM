package com.techem.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(AemContextExtension.class)
public class SocialMediaTest {

    private static final String ICON = "svg-icon-Twitter";
    private static final String ALT_TEXT = "Twitter";
    private static final String TITLE = "Twitter";
    private static final String CATEGORY_TITLE = "Social Media Title";

    private AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private SocialMedia socialMedia;

    @BeforeEach
    public void setUp() throws Exception {
        context.load().json("/social-media.json", "/content/components/social-media");
        Resource socialMediaResource =  context.currentResource("/content/components/social-media");

        socialMedia = socialMediaResource.adaptTo(SocialMedia.class);
    }

    @Test
    public void testGetSocialMediaList() {
        final List<Media> mediaList = socialMedia.getSocialMediaList();
        final Media media = mediaList.get(1);

        assertEquals(2, mediaList.size());
        assertEquals(TITLE, media.getTitle());
        assertEquals(ALT_TEXT, media.getAltText());
        assertEquals(ICON, media.getIcon());
    }

    @Test
    public void testGetCategoryTitle() {
        assertEquals(CATEGORY_TITLE, socialMedia.getCategoryTitle());
    }
}
