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
public class UnitTest {

    private static final String NUMBER = "12.312312";
    private static final String UNIT_OF_MEASUREMENT = "m2";
    private static final String UNIT_DESCRIPTION = "Unit description";

    private AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private Unit unit;

    @BeforeEach
    public void setUp() throws Exception {

        final Resource resource = context.create().resource("/content/components/unit",
                ImmutableMap.<String, Object>builder()
                        .put("number", NUMBER)
                        .put("unit", UNIT_OF_MEASUREMENT)
                        .put("unitDescription", UNIT_DESCRIPTION)
                        .build());

        unit = resource.adaptTo(Unit.class);
    }

    @Test
    public void testGetters() {
        assertEquals(NUMBER, unit.getNumber());
        assertEquals(UNIT_OF_MEASUREMENT, unit.getUnit());
        assertEquals(UNIT_DESCRIPTION, unit.getUnitDescription());
    }
}
