package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Header {

    @Self
    private Resource resource;

    @ValueMapValue(name = "navTitle")
    private String navTitle;

    @ValueMapValue(name = "pageTitle")
    private String pageTitle;

    private String pagePath;

    @PostConstruct
    protected void init() {
        if(Objects.nonNull(resource)) {
            pagePath = resource.getParent().getPath();
        }
    }

    public String getNavTitle() {
        return navTitle;
    }

    public String getPagePath() {
        return pagePath;
    }

    public String getPageTitle() {
        return pageTitle;
    }
}