package com.techem.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AemContextExtension.class)
public class FooterTest {

    private static final String COPYRIGHT = "© Techem GmbH";
    private static final String LINK_TEXT = "Impressum";
    private static final String LINK_URL = "/content/techem/us/en/Abrechnung";
    private static final String CATEGORY_TITLE = "Uber Uns";
    private static final String CATEGORY_LINK = "/content/techem/us/en/Abrechnung/monitoring";

    private AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private Footer footer;

    @BeforeEach
    public void setUp() throws Exception {
        context.load().json("/footer.json", "/content/components/footer");
        Resource footerResource =  context.currentResource("/content/components/footer");

        footer = footerResource.adaptTo(Footer.class);
    }

    @Test
    public void testGetFooterItems() {
        final Map<FooterCategory, List<FooterLink>> footerItems = footer.getFooterItems();
        final FooterCategory category = footerItems.keySet().stream().findFirst().get();
        final List<FooterLink> footerLinks = footerItems.get(category);
        final FooterLink footerLink = footerLinks.get(0);

        assertNotNull(category);
        assertEquals(CATEGORY_TITLE, category.getCategoryTitle());
        assertEquals(CATEGORY_LINK, category.getCategoryLink());

        assertEquals(2, footerLinks.size());
        assertEquals(LINK_TEXT, footerLink.getLinkText());
        assertEquals(LINK_URL, footerLink.getLinkUrl());
        assertEquals(Boolean.TRUE, footerLink.getOpenNewTab());
    }

    @Test
    public void testGetBottomItems() {
      final List<FooterLink> bottomItems = footer.getBottomItems();
      final FooterLink footerLink = bottomItems.get(0);

      assertEquals(2, bottomItems.size());
      assertEquals(LINK_TEXT, footerLink.getLinkText());
      assertEquals(LINK_URL, footerLink.getLinkUrl());
      assertEquals(Boolean.TRUE, footerLink.getOpenNewTab());
    }

    @Test
    public void testGetCopyright() {
        assertEquals(COPYRIGHT, footer.getCopyright());
    }

    @Test
    public void testGetLogoLink() {
        assertEquals(LINK_URL, footer.getLogoLink());
    }
}
