package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FocusTeaser {

    @ValueMapValue(name = "text")
    private String text;

    @ValueMapValue(name = "fileReference")
    private String image;

    @ValueMapValue(name = "alignnment")
    private String alignnment;

    @ValueMapValue(name = "preferenceList")
    private String preferenceList;

    @PostConstruct
    protected void init() {
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }

    public String getAlignnment() { return alignnment; }

    public String getPreferenceList() {
        return preferenceList;
    }
}
