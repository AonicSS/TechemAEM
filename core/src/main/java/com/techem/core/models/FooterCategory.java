package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterCategory {

    @ValueMapValue(name = "categoryTitle")
    private String categoryTitle;

    @ValueMapValue(name = "categoryLink")
    private String categoryLink;

    @PostConstruct
    protected void init() {
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public String getCategoryLink() {
        return categoryLink;
    }
}
