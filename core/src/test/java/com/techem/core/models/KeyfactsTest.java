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
public class KeyfactsTest {

    private static final String HEADLINE = "Keyfacts Title";
    private static final String TEXT = "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit</p>";
    private static final String NUMBER = "12.31231";
    private static final String UNIT = "m2";
    private static final String UNIT_DESCRIPTION = "Unit description";

    private AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private Keyfacts keyfacts;

    @BeforeEach
    public void setUp() throws Exception {
        context.load().json("/keyfacts.json", "/content/components/keyfacts");
        Resource keyfactResource = context.currentResource("/content/components/keyfacts");

        keyfacts = keyfactResource.adaptTo(Keyfacts.class);
    }

    @Test
    public void testGetHeadline() {
        assertEquals(HEADLINE, keyfacts.getHeadline());
    }

    @Test
    public void testGetText() {
        assertEquals(TEXT, keyfacts.getText());
    }

    @Test
    public void testGetUnitList() {
        final List<Unit> unitList = keyfacts.getUnitList();
        final Unit unit = unitList.get(0);

        assertEquals(2, unitList.size());
        assertEquals(NUMBER, unit.getNumber());
        assertEquals(UNIT, unit.getUnit());
        assertEquals(UNIT_DESCRIPTION, unit.getUnitDescription());
    }
}
