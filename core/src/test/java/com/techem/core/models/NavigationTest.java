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
import java.util.Map;

import static com.techem.core.models.Navigation.LEFT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AemContextExtension.class)
public class NavigationTest {

    private static final String BUTTON_LINK = "/content/techem/us/en/Test";
    private static final String NAVIGATION_ROOT = "/content/techem/us/en";
    private static final String BUTTON_LABEL = "Button";
    private static final String LOGO_LINK = "/content/techem/us/en/Newsroom";
    private static final String IMAGE_PATH = "/content/dam/core-components-examples/library/sample-assets/lava-rock-formation.jpg";
    private static final String BACK_BUTTON = "back";
    private static final String TITLE = "Monitoring";
    private static final String PAGE_DESCRIPTION = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";


    private AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private Navigation navigation;

    @BeforeEach
    public void setUp() throws Exception {
       final Resource resource =  context.load().json("/navigation.json",
               "/content/techem/us/en");

       context.currentResource(resource);
       final Resource resourceNavigation = context.create().resource("/content/components/navigation", ImmutableMap.<String, Object>builder()
                .put("buttonLink", BUTTON_LINK)
                .put("navigationRoot", NAVIGATION_ROOT)
                .put("label", BUTTON_LABEL)
                .put("logoLink", LOGO_LINK)
                .put("backButtonText", BACK_BUTTON)
                .put("fileReference", IMAGE_PATH)
                .build());

       navigation = resourceNavigation.adaptTo(Navigation.class);
    }

    @Test
    public void testGetLabel() {
        assertEquals(BUTTON_LABEL, navigation.getLabel());
    }

    @Test
    public void testGetButtonLink() {
        assertEquals(BUTTON_LINK, navigation.getButtonLink());
    }

    @Test
    public void testGetLogoLink() {
        assertEquals(LOGO_LINK, navigation.getLogoLink());
    }

    @Test
    public void testGetLogoImage() {
        assertEquals(IMAGE_PATH, navigation.getLogoImage());
    }

    @Test
    public void testGetBackButtonTexte() {
        assertEquals(BACK_BUTTON, navigation.getBackButtonText());
    }

    @Test
    public void testGetNavigationItems() {
        final Map<Header, Map<String, List<NavigationDetails>>> mapOfMap = navigation.getNavigationItems();
        final Header header = mapOfMap.keySet().stream().findFirst().get();
        final Map<String, List<NavigationDetails>> map = mapOfMap.get(header);
        final List<NavigationDetails> navigationDetailsList = map.get(LEFT);
        final NavigationDetails navigationDetails = navigationDetailsList.get(0);

        assertNotNull(mapOfMap);
        assertEquals(1, mapOfMap.size());
        assertEquals(TITLE, header.getNavTitle());
        assertEquals(TITLE, header.getPageTitle());

        assertEquals(IMAGE_PATH, navigationDetails.getPageImagePath());
        assertEquals(PAGE_DESCRIPTION, navigationDetails.getPageDescription());
        assertEquals(TITLE, navigationDetails.getPageTitle());
        assertEquals(TITLE, navigationDetails.getNavTitle());
    }
}
