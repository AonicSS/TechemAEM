package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterLink {

    @ValueMapValue(name = "linkText")
    private String linkText;

    @ValueMapValue(name = "linkUrl")
    private String linkUrl;

    @ValueMapValue(name = "openNewTab")
    private Boolean openNewTab;

    @PostConstruct
    protected void init() {
    }

    public String getLinkText() {
        return linkText;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public Boolean getOpenNewTab() {
        return openNewTab;
    }
}
