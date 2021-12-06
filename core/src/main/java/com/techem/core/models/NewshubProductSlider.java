package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, adapters = NewshubProductSlider.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/newshub-product-slider")
public class NewshubProductSlider {

    @Inject
    private List<NewshubProductItem> productItems;

    @ValueMapValue(name="headline")
    private String headline;

    @ValueMapValue(name="alignment")
    private String alignment;

    public List<NewshubProductItem> getProductItems() {
        return new ArrayList<>(productItems);
    }

    public String getHeadline() {
        return headline;
    }

    public String getAlignment() {
        return alignment;
    }
}