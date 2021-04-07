package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = DoubleButton.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/double-button")
public class DoubleButton {

    @Inject
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(name = "primaryButtonLabel")
    private String primaryButtonLabel;

    @ValueMapValue(name = "primaryLink")
    private String primaryLink;

    @ValueMapValue(name = "secondaryButtonLabel")
    private String secondaryButtonLabel;

    @ValueMapValue(name = "secondaryLink")
    private String secondaryLink;

    @ValueMapValue(name = "showSecondaryButton")
    private boolean showSecondaryButton;

    @ValueMapValue(name = "accessibilityLabel")
    private String accessibilityLabel;

    @ValueMapValue(name = "openNewTabPrimary")
    private Boolean openNewTabPrimary;

    @ValueMapValue(name = "openNewTabSecondary")
    private Boolean openNewTabSecondary;

    @PostConstruct
    protected void init() {

    }

    public String getPrimaryButtonLabel() {
        return primaryButtonLabel;
    }

    public String getPrimaryLink() {
        if (primaryLink != null) {
            Resource pathResource = resourceResolver.getResource(primaryLink);
            // check if resource exists and link is internal
            if (pathResource != null) {
                primaryLink = primaryLink + ".html";
            }
        }
        return primaryLink;
    }

    public String getSecondaryButtonLabel() {
        return  secondaryButtonLabel;
    }

    public String getSecondaryLink() {

        if (secondaryLink != null) {
            Resource pathResource = resourceResolver.getResource(secondaryLink);
            // check if resource exists and link is internal
            if (pathResource != null) {
                secondaryLink = secondaryLink + ".html";
            }
        }
        return secondaryLink;
    }

    public boolean isShowSecondaryButton() {
        return showSecondaryButton;
    }

    public String getAccessibilityLabel() {
        return accessibilityLabel;
    }

    public Boolean getOpenNewTabPrimary() {
        return openNewTabPrimary;
    }

    public Boolean getOpenNewTabSecondary() {
        return openNewTabSecondary;
    }
}
