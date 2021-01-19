package com.techem.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = TextImage.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/text-image")
public class TextImage {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @Inject
    @Default(values = "left")
    private String textPosition;

    @Inject
    @Default(values = "headline")
    private String headline;

    @Inject
    @Default(values = "description")
    private String text;

    public String getTextPosition() {
        if (this.textPosition == null) {
            this.textPosition = this.resource.getValueMap().get("textPosition", String.class);
        }
        return textPosition;
    }

    public String getHeadline() {
        if (this.headline == null) {
            this.headline = this.resource.getValueMap().get("headline", String.class);
        }
        return headline;
    }

    public String getText() {
        if (this.text == null) {
            this.text = this.resource.getValueMap().get("text", String.class);
        }
        return text;
    }
}
