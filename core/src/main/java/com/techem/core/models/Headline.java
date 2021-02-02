package com.techem.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = Headline.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/headline")
public class Headline {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @ValueMapValue(name = "types")
    private String types;

    @ValueMapValue(name = "headline")
    @Default(values = "Headline of the component")
    private String headline;

    @ValueMapValue(name = "text")
    private String text;

    public String getTypes() {
        return types;
    }

    public String getHeadline() {
        return headline;
    }

    public String getText() { return text;}
}
