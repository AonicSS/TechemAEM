package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Media {

    @ValueMapValue(name = "title")
    private String title;

    @ValueMapValue(name = "altText")
    private String altText;

    @ValueMapValue(name = "icon")
    private String icon;

    @ValueMapValue(name = "link")
    private String link;

    @ValueMapValue(name = "openNewTab")
    private Boolean openNewTab;

    @PostConstruct
    protected void init() {

    }

    public String getTitle() { return title; }

    public String getIcon() {
        return icon;
    }

    public String getLink() {
        return link;
    }

    public String getAltText() {
        return altText;
    }

    public Boolean getOpenNewTab() { return openNewTab; }
}
