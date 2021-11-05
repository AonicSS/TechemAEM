package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,  defaultInjectionStrategy =  DefaultInjectionStrategy.OPTIONAL)
public class NewshubStageContentPage {

    @ValueMapValue(name = "pageTitle")
    private String pageTitle;

    @ValueMapValue(name = "subtitle")
    private String subtitle;

    public String getPageTitle() {
        return pageTitle;
    }

    public String getSubtitle() {
        return subtitle;
    }
}