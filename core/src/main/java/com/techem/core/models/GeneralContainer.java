package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;


@Model(adaptables = Resource.class, adapters = GeneralContainer.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/general-container")
public class GeneralContainer {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @Inject
    @Default(values = "Headline")
    private String headline;

    @Inject
    @Default(values = "white")
    private String background;

    public String getHeadline() {
        if (this.headline == null) {
            this.headline = this.resource.getValueMap().get("headline", String.class);
        }
        return headline;
    }

    public String getBackground() {
        if (this.background == null) {
            this.background = this.resource.getValueMap().get("background", String.class);
        }
        return background;
    }
}
