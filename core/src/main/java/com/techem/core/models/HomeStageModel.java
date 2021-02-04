package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = HomeStageModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/home-stage")
public class HomeStageModel
{

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @ValueMapValue(name="transitionSpeed")
    private String transitionSpeed;


    public String getTransitionSpeed() {
        return transitionSpeed;
    }
}