package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

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

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(name="headline")
    @Default(values = "Headline")
    private String headline;

    @ValueMapValue(name="body")
    private String body;

    @ValueMapValue(name="linkURL2")
    private String linkURL2;

    @ValueMapValue(name="fileReference2")
    private String fileReference2;

    public String getHeadline() {
        return headline;
    }

    public String getBody() {
        return body;
    }

    public String getLinkURL2() {
        if (linkURL2 != null) {
            Resource pathResource = resourceResolver.getResource(linkURL2);
            // check if resource exists and link is internal
            if (pathResource != null) {
                linkURL2 = linkURL2 + ".html";
            }
        }
        return linkURL2;
    }

    public String getFileReference2() {
        return fileReference2;
    }
}
