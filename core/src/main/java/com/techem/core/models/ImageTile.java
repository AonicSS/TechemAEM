package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = ImageTile.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/image-tile")
public class ImageTile {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @Inject
    @Default(values = "Headline")
    private String headline;

    @Inject
    @Default(values = "Body")
    private String body;

    @Inject
    private String linkURL2;

    @Inject
    private String fileReference2;

    public String getHeadline() {
        if (this.headline == null) {
            this.headline = this.resource.getValueMap().get("headline", String.class);
        }
        return headline;
    }

    public String getBody() {
        if (this.body == null) {
            this.body = this.resource.getValueMap().get("body", String.class);
        }
        return body;
    }

    public String getLinkURL2() {
        if (this.linkURL2 == null) {
            this.linkURL2 = this.resource.getValueMap().get("linkURL2", String.class);
        }
        return linkURL2;
    }

    public String getFileReference2() {
        if (this.fileReference2 == null) {
            this.fileReference2 = this.resource.getValueMap().get("fileReference2", String.class);
        }
        return fileReference2;
    }
}
