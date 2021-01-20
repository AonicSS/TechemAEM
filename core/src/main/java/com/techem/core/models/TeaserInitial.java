package com.techem.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = TeaserInitial.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/teaser-initial")
public class TeaserInitial {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @ValueMapValue(name="containerHeadline")
    private String containerHeadline;

    @ValueMapValue(name="containerSubtitle")
    private String containerSubtitle;

    public String getContainerHeadline() { return containerHeadline; }

    public String getContainerSubtitle() { return containerSubtitle; }

}
