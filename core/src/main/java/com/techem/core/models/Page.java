package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = Page.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/page")
public class Page {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @ValueMapValue(name="noIndex")
    private String noIndex;

    @ValueMapValue(name="noFollow")
    private String noFollow;

    public String getNoIndex() {
        return noIndex;
    }

    public String getNoFollow() {
        return noFollow;
    }

}
