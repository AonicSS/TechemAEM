package com.techem.core.models;

import com.google.common.collect.ImmutableMap;
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
public class NavigationDetailsTest {

    private static final String IMAGE_PATH = "/content/dam/core-components-examples/library/sample-assets/mountain-range.jpg";
    private static final String PAGE_DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur adipiscing elit";
    private static final String NAV_TITLE = "Monitoring";
    private static final String PARENT_PATH = "/content/components";

    private AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private NavigationDetails navigationDetails;

    @BeforeEach
    public void setUp() throws Exception {

        final Resource resource = context.create().resource("/content/components/navigation-details", ImmutableMap.<String, Object>builder()
                .put("image", IMAGE_PATH)
                .put("hideImageInHeader", Boolean.FALSE)
                .put("pageDescription", PAGE_DESCRIPTION)
                .put("navTitle", NAV_TITLE)
                .put("pageTitle", NAV_TITLE)
                .build());

        navigationDetails = resource.adaptTo(NavigationDetails.class);
    }

    @Test
    public void testGetters() {
        assertEquals(IMAGE_PATH, navigationDetails.getPageImagePath());
        assertEquals(Boolean.FALSE, navigationDetails.isHideImageInHeader());
        assertEquals(PAGE_DESCRIPTION, navigationDetails.getPageDescription());
        assertEquals(NAV_TITLE, navigationDetails.getPageTitle());
        assertEquals(NAV_TITLE, navigationDetails.getNavTitle());
        assertEquals(PARENT_PATH, navigationDetails.getPagePath());
    }
}
