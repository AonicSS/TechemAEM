package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TargetItem {

    @ValueMapValue(name = "targetItemLabel")
    private String targetItemLabel;

    @ValueMapValue(name = "targetItemLink")
    private String targetItemLink;

    @SlingObject
    private ResourceResolver resourceResolver;

    public String getTargetItemLabel() {
        return targetItemLabel;
    }

    public String getTargetItemLink() {
        if (targetItemLink != null) {
            Resource pathResource = resourceResolver.getResource(targetItemLink);
            // check if resource exists and link is internal
            if (pathResource != null) {
                targetItemLink = targetItemLink + ".html";
            }
        }
        return targetItemLink;
    }
}