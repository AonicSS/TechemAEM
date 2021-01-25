package com.techem.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

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

    @ValueMapValue(name="headline")
    @Default(values = "Headline General Tabs Container")
    private String headline;

    @ValueMapValue(name="subtitle")
    private String subtitle;

    public String getHeadline() {
        return headline;
    }

    public String getSubtitle() {
        return subtitle;
    }
}