package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Button {

    @ValueMapValue(name = "openNewTab")
    private Boolean openNewTab;

    @PostConstruct
    protected void init() {
    }

    public Boolean getOpenNewTab() {
        return openNewTab;
    }
}
