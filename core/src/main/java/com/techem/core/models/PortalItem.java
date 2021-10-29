package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PortalItem {

    @ValueMapValue(name = "portalItemLabel")
    private String portalItemLabel;

    @ValueMapValue(name = "portalItemLink")
    private String portalItemLink;

    @PostConstruct
    protected void init() {
    }

    public String getPortalItemLabel() {
        return portalItemLabel;
    }

    public String getPortalItemLink() {
        return portalItemLink;
    }
}