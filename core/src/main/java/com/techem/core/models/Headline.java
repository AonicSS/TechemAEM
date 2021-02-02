package com.techem.core.models;


import org.apache.sling.api.resource.Resource;
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

    @ValueMapValue(name="types")
    private String types;

    @ValueMapValue(name="headlineH2")
    private String headlineH2;

    @ValueMapValue(name="headlineH3")
    private String headlineH3;

    @ValueMapValue(name="headlineH4")
    private String headlineH4;

    @ValueMapValue(name="text")
    private String text;

    public String getTypes() {
        return types;
    }

    public String getHeadlineH2() {
        return headlineH2;
    }

    public String getHeadlineH3() {
        return headlineH3;
    }

    public String getHeadlineH4() {
        return headlineH4;
    }

    public String getText() { return text;}
}
