package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,  defaultInjectionStrategy =  DefaultInjectionStrategy.OPTIONAL)
public class StageContentPage {

    @ValueMapValue(name = "headline")
    private String headline;

    @ValueMapValue(name = "text")
    private String text;

    public String getHeadline() {
        return headline;
    }

    public String getText() {
        return text;
    }

}
