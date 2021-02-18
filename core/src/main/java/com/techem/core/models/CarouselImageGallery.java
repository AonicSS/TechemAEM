package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = CarouselImageGallery.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/carousel-image-gallery")
public class CarouselImageGallery {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @ValueMapValue(name = "loop")
    private Boolean loop;

    @ValueMapValue(name = "autoHeight")
    private Boolean autoHeight;

    public Boolean getLoop() {
        return loop;
    }

    public Boolean getAutoHeight() {
        return autoHeight;
    }
}