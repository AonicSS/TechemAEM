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

@ExtendWith(AemContextExtension.class)
public class FocusTeaserTest {

    private static final String TEXT = "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit</p>\n";
    private static final String IMAGE_REFERENCE = "/content/dam/core-components-examples/library/sample-assets/lava-rock-formation.jpg";
    private static final String BUTTON_LABEL = "Zum Service";
    private static final String PREFERENCE_LIST = "bulleted";
    private static final String ALIGNMENT = "right";

    private AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private FocusTeaser focusTeaser;

    @BeforeEach
    public void setUp() throws Exception {

       final Resource resource = context.create().resource("/content/components/focus-teaser", ImmutableMap.<String, Object>builder()
                .put("text", TEXT)
                .put("fileReference",IMAGE_REFERENCE)
                .put("alignnment", ALIGNMENT)
                .put("preferenceList", PREFERENCE_LIST)
                .build());

       focusTeaser = resource.adaptTo(FocusTeaser.class);
    }

    @Test
    public void testGetters() {
        assertEquals(TEXT, focusTeaser.getText());
        assertEquals(IMAGE_REFERENCE, focusTeaser.getImage());
        assertEquals(ALIGNMENT, focusTeaser.getAlignnment());
        assertEquals(PREFERENCE_LIST, focusTeaser.getPreferenceList());
    }
}
