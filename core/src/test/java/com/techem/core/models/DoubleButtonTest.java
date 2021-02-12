package com.techem.core.models;

import com.google.common.collect.ImmutableMap;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AemContextExtension.class)
public class DoubleButtonTest {

    private static final String BUTTON_LABEL = "Test Button";
    private static final String LINK = "/content/en/techem/home";
    private static final String ACCESSIBILITY_LABEL = "accessibility";

    private AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private DoubleButton doubleButton;

    @BeforeEach
    public void setUp() throws Exception {

       final Resource resource = context.create().resource("/content/components/double-button", ImmutableMap.<String, Object>builder()
                .put("primaryButtonLabel", BUTTON_LABEL)
                .put("primaryLink", LINK)
                .put("secondaryButtonLabel", BUTTON_LABEL)
                .put("secondaryLink", LINK)
                .put("showSecondaryButton", Boolean.TRUE)
                .put("accessibilityLabel", ACCESSIBILITY_LABEL)
                .put("openNewTabPrimary", Boolean.TRUE)
                .put("openNewTabSecondary", Boolean.TRUE)
                .build());

        doubleButton = resource.adaptTo(DoubleButton.class);
    }

    @Test
    public void testGetters() {
        assertNotNull(doubleButton);
        assertEquals(BUTTON_LABEL, doubleButton.getPrimaryButtonLabel());
        assertEquals(LINK, doubleButton.getPrimaryLink());
        assertEquals(BUTTON_LABEL, doubleButton.getSecondaryButtonLabel());
        assertEquals(LINK, doubleButton.getSecondaryLink());
        assertEquals(Boolean.TRUE, doubleButton.isShowSecondaryButton());
        assertEquals(ACCESSIBILITY_LABEL, doubleButton.getAccessibilityLabel());
        assertEquals(Boolean.TRUE, doubleButton.getOpenNewTabPrimary());
        assertEquals(Boolean.TRUE, doubleButton.getOpenNewTabSecondary());

    }
}
