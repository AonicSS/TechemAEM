package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = ReferenceSlider.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/reference-slider-item")
public class ReferenceSlider {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @ValueMapValue(name="headline")
    @Default(values = "Reference Slider Item Headline")
    private String headline;

    @ValueMapValue(name="title")
    @Default(values = "Title")
    private String title;

    @ValueMapValue(name="sourceAuthor")
    @Default(values = "Source author")
    private String sourceAuthor;

    @ValueMapValue(name="sourceDetails")
    @Default(values = "Source details")
    private String sourceDetails;

    @ValueMapValue(name="description")
    private String description;

    public String getHeadline() {
        return headline;
    }

    public String getTitle() {
        return title;
    }

    public String getSourceAuthor() {
        return sourceAuthor;
    }

    public String getSourceDetails() {
        return sourceDetails;
    }

    public String getDescription() {
        return description;
    }
}