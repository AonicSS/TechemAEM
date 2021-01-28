package com.techem.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = ServiceItem.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/service-item")
public class ServiceItem {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @ValueMapValue(name="productTitle")
    @Default(values = "Title of the Service Item")
    private String productTitle;

    @ValueMapValue(name="linkURL")
    private String linkURL;

    @ValueMapValue(name="description")
    private String description;

    public String getProductTitle() {
        return productTitle;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public String getDescription() {
        return description;
    }
}
