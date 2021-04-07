package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterLink {

    @Inject
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

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
        if (linkUrl != null) {
            Resource pathResource = resourceResolver.getResource(linkUrl);
            // check if resource exists and link is internal
            if (pathResource != null) {
                linkUrl = linkUrl + ".html";
            }
        }
        return linkUrl;
    }

    public Boolean getOpenNewTab() {
        return openNewTab;
    }
}
