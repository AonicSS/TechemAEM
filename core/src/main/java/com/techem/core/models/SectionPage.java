package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SectionPage {

    @ValueMapValue(name = "stageHeadline")
    private String stageHeadline;

    @ValueMapValue(name ="stageText")
    private String stageText;

    @ValueMapValue(name ="image")
    private String image;


    public String getStageHeadline() {
        return stageHeadline;
    }

    public String getStageText() {
        return stageText;
    }

    public String getImage() {
        return image;
    }
}
