package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class ,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewshubProductItem {

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(name="productTitle")
    private String productTitle;

    @ValueMapValue(name="productBody")
    private String productBody;

    @ValueMapValue(name="productLink")
    private String productLink;

    @ValueMapValue(name="productFileReference")
    private String productFileReference;

    public String getProductTitle() {
        return productTitle;
    }

    public String getProductBody() { return productBody; }

    public String getProductLink() {
        if (productLink != null) {
            Resource pathResource = resourceResolver.getResource(productLink);
            // check if resource exists and link is internal
            if (pathResource != null) {
                productLink = productLink + ".html";
            }
        }
        return productLink;
    }

    public String getProductFileReference() {
        return productFileReference;
    }
}