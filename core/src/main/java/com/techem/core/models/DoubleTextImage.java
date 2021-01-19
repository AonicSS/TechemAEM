package com.techem.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = DoubleTextImage.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/double-text-image")
public class DoubleTextImage {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @Inject
    @Default(values = "Double Text Image Headline")
    private String headline;

    public String getHeadline() {
        if (this.headline == null) {
            this.headline = this.resource.getValueMap().get("headline", String.class);
        }
        return headline;
    }
}
