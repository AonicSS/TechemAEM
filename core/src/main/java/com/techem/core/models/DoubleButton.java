package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DoubleButton {

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

    @PostConstruct
    protected void init() {

    }

    public String getPrimaryButtonLabel() {
        return primaryButtonLabel;
    }

    public String getPrimaryLink() {
        return primaryLink;
    }

    public String getSecondaryButtonLabel() {
        return  secondaryButtonLabel;
    }

    public String getSecondaryLink() {
        return secondaryLink;
    }

    public boolean isShowSecondaryButton() {
        return showSecondaryButton;
    }

    public String getAccessibilityLabel() {
        return accessibilityLabel;
    }
}
