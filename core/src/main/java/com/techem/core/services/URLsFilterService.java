package com.techem.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

public interface URLsFilterService {

    public static final String CASE_SENSITIVE = "caseSensitive";
    public static final String SLING_REDIRECT = "sling:redirect";
    public static final String SLING_TARGET = "sling:target";
    public static final String CONTENT_ROOT = "/content/techem";

    @ObjectClassDefinition(
            name = "Techem URLs Filter Config",
            description = "Configure the URLs Filter.")
    public @interface URLsFilterConfig {
        @AttributeDefinition(
                name = "Enable Filter",
                description = "Enable the filter for all URLs.",
                type = AttributeType.BOOLEAN)
        public boolean enabled();

        @AttributeDefinition(
                name = "Ignore Extensions",
                description = "The filter will not be applied to requests to the defined extensions.",
                type = AttributeType.STRING)
        public String[] extensions();

        @AttributeDefinition(
                name = "Ignore Paths",
                description = "The filter will not be applied to requests to the defined paths.",
                type = AttributeType.STRING)
        public String[] paths();
    }

    public URLsFilterConfig getConfig();
    public boolean isAllowedExt(String ext);
    public boolean isAllowedPath(String path);
}