package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class ,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewshubSimilarArticlesItem {

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(name="articleTitle")
    private String articleTitle;

    @ValueMapValue(name="articleLink")
    private String articleLink;

    @ValueMapValue(name="articleFileReference")
    private String articleFileReference;

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleLink() {
        if (articleLink != null) {
            Resource pathResource = resourceResolver.getResource(articleLink);
            // check if resource exists and link is internal
            if (pathResource != null) {
                articleLink = articleLink + ".html";
            }
        }
        return articleLink;
    }

    public String getArticleFileReference() {
        return articleFileReference;
    }
}