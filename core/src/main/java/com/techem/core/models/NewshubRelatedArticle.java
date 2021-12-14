package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = "techem/components/newshub-related-article")
public class NewshubRelatedArticle {
    @ValueMapValue(name = "headline")
    private String headline;

    @ValueMapValue(name = "imageFileReference")
    private String imageFileReference;

    @ValueMapValue(name = "articleTeaser")
    private String articleTeaser;

    @ValueMapValue(name = "imageDetails")
    private String imageDetails;
    
    @ValueMapValue(name = "articleLink")
    private String articleLink;

    public String getHeadline() {
        return headline;
    }

    public String getImageFileReference() {
        return imageFileReference;
    }

    public String getArticleTeaser() {
        return articleTeaser;
    }

    public String getImageDetails() {
        return imageDetails;
    }

    public String getArticleLink() {
        return articleLink;
    }
}