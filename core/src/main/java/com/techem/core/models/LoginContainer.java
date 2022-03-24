package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
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

    @ValueMapValue(name="emailPlaceholder")
    private String emailPlaceholder;

    @ValueMapValue(name="passwordPlaceholder")
    private String passwordPlaceholder;

    @ValueMapValue(name="emailLabel")
    @Default(values = "Username")
    private String emailLabel;

    @ValueMapValue(name="passwordLabel")
    @Default(values = "Password")
    private String passwordLabel;

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

    public String getEmailPlaceholder() {
        return emailPlaceholder;
    }

    public String getPasswordPlaceholder() {
        return passwordPlaceholder;
    }

    public String getEmailLabel() {
        return emailLabel;
    }

    public String getPasswordLabel() {
        return passwordLabel;
    }
}