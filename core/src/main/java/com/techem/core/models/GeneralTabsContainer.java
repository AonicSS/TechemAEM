package com.techem.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = GeneralTabsContainer.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/general-tabs-container")
public class GeneralTabsContainer {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @Inject
    @Default(values = "Headline General Tabs Container")
    private String headline;

    @Inject
    private String subtitle;

    public String getHeadline() {
        if (this.headline == null) {
            this.headline = this.resource.getValueMap().get("headline", String.class);
        }
        return headline;
    }

    public String getSubtitle() {
        if (this.subtitle == null) {
            this.subtitle = this.resource.getValueMap().get("subtitle", String.class);
        }
        return subtitle;
    }
}