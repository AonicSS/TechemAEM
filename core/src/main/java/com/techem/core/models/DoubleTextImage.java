package com.techem.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

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

    @ValueMapValue(name="headline")
    private String headline;

    public String getHeadline() { return headline; }
}
