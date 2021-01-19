package com.techem.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = TextImage.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/text-image")
public class TextImage {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @ValueMapValue(name="textPosition")
    @Default(values = "left")
    private String textPosition;

    @ValueMapValue(name="headline")
    @Default(values = "headline")
    private String headline;

    @ValueMapValue(name="text")
    @Default(values = "description")
    private String text;

    public String getTextPosition() { return textPosition; }

    public String getHeadline() { return headline; }

    public String getText() { return text;}
}
