package com.techem.core.servlets;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ParameterUtilTest {

    @Test
    void testToSimpleEntry() {
        String value = "Test simple";
        String separator = " ";
        String otherValue = "Test ";

        AbstractMap.SimpleEntry<String, String> expectedEntry = new AbstractMap.SimpleEntry<>("Test", "simple");
        AbstractMap.SimpleEntry<String, String> expectedOtherEntry = new AbstractMap.SimpleEntry<String, String>("Test", null);

        assertEquals(null, ParameterUtil.toMapEntry(null, separator));
        assertEquals(expectedEntry, ParameterUtil.toMapEntry(value, separator));
        assertEquals(null, ParameterUtil.toMapEntry(otherValue, separator));
        assertEquals(expectedOtherEntry, ParameterUtil.toMapEntryWithOptionalValue(otherValue, separator));
    }

    @Test
    void testToMapNull () {
        Map<String, String> map = new LinkedHashMap<String, String>();

        assertEquals(map, ParameterUtil.toMap(null, ""));
        assertEquals(map, ParameterUtil.toMap(null, "", true, "default"));

    }

    @Test
    void testToMapLength1 () {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("Test", "default");

        Map<String, String> mapKeyless = new LinkedHashMap<String, String>();

        String[] shortValue = new String[]{"Test"};
        String[] shortKeylessValue = new String[]{" da"};

        assertEquals(map, ParameterUtil.toMap(shortValue, " ", true, "default"));
        assertEquals(mapKeyless, ParameterUtil.toMap(shortKeylessValue, " ", true, "default"));
    }

    @Test
    void testToMapLength2 () {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("Test", "length");

        Map<String, String> mapKeyless = new LinkedHashMap<String, String>();

        String[] value = new String[]{"Test length"};

        assertEquals(map, ParameterUtil.toMap(value, " ", true, "default"));
    }

    @Test
    void testToMapWithKeys () {
        Map<String, String[]> map = new LinkedHashMap<String, String[]>();
        String[] entryValue = new String[]{"list", "doi"};
        map.put("Test", entryValue);
        String[] value = new String[]{"Test.list doi"};
        Map<String, String[]> result = new LinkedHashMap<String, String[]>();
        result = ParameterUtil.toMap(value, ".", " ");
        assertTrue(areEqualWithArrayValue(map, result));
    }

    @Test
    void testToPatterns () {
        String[] values = new String[]{"abc", "cba"};

        List<Pattern> emptyPatterns = new ArrayList<Pattern>();
        List<Pattern> valuesWithPatterns = new ArrayList<Pattern>();
        valuesWithPatterns.add(Pattern.compile("abc"));
        valuesWithPatterns.add(Pattern.compile("cba"));
        List<Pattern> result = ParameterUtil.toPatterns(values);

        assertEquals(emptyPatterns, ParameterUtil.toPatterns(null));
        assertNotNull(ParameterUtil.toPatterns(values));
    }

    private boolean areEqualWithArrayValue(Map<String, String[]> first, Map<String, String[]> second) {
        if (first.size() != second.size()) {
            return false;
        }

        return first.entrySet().stream()
                .allMatch(e -> Arrays.equals(e.getValue(), second.get(e.getKey())));
    }

}