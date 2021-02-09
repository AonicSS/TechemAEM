package com.techem.core.models;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavigationDetails extends Header {

    @ValueMapValue(name = "image")
    private String pageImagePath;

    @ValueMapValue(name = "hideImageInHeader")
    private boolean hideImageInHeader;

    @ValueMapValue(name = "jcr:description")
    private String pageDescription;

    @PostConstruct
    protected void init() {
        super.init();
    }

    public String getPageDescription() {
        return pageDescription;
    }

    public String getPageImagePath() {
        return  hideImageInHeader == false ? pageImagePath : StringUtils.EMPTY;
    }

    public boolean isHideImageInHeader() { return hideImageInHeader;}
}