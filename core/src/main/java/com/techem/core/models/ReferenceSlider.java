package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

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

    @Inject
    @Default(values = "Headline")
    private String headline;

    @Inject
    @Default(values = "Title")
    private String title;

    @Inject
    @Default(values = "Source details")
    private String sourceDetails;

    @Inject
    @Default(values = "description here")
    private String description;

    public String getHeadline() {
        if (this.headline == null) {
            this.headline = this.resource.getValueMap().get("headline", String.class);
        }
        return headline;
    }

    public String getTitle() {
        if (this.title == null) {
            this.title = this.resource.getValueMap().get("title", String.class);
        }
        return title;
    }

    public String getSourceDetails() {
        if (this.sourceDetails == null) {
            this.sourceDetails = this.resource.getValueMap().get("sourceDetails", String.class);
        }
        return sourceDetails;
    }

    public String getDescription() {
        if (this.description == null) {
            this.description = this.resource.getValueMap().get("description", String.class);
        }
        return description;
    }
}