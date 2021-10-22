package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,resourceType = "techem/components/login/login-container")
public class LoginContainer {

    @Inject
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(name="linkURL")
    private String linkURL;

    @PostConstruct
    protected void init() {
    }

    public String getLinkURL() {
        if (linkURL != null) {
            Resource pathResource = resourceResolver.getResource(linkURL);
            // check if resource exists and link is internal
            if (pathResource != null) {
                linkURL = linkURL + ".html";
            }
        }
        return linkURL;
    }

}
