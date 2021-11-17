package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class, adapters = NewshubArticleFeed.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = "techem/components/newshub-article-feed")
public class NewshubArticleFeed {

    private Logger logger = LoggerFactory.getLogger(NewshubArticleFeed.class);

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(name = "feedHeadline")
    private String feedHeadline;

    @ValueMapValue(name = "maxArticles")
    private String maxArticles;

    @ValueMapValue(name = "articlePath")
    private String articlePath;

    @ValueMapValue(name = "monthHeadline")
    private String monthHeadline;

    @ValueMapValue(name = "number")
    private String number;

    @ValueMapValue(name = "numberUnit")
    private String numberUnit;

    @ValueMapValue(name = "numberDescription")
    private String numberDescription;

    @ValueMapValue(name = "articleURLPath")
    private String articleURLPath;

    @ValueMapValue(name = "backgroundImage")
    private String backgroundImage;

    public String getFeedHeadline() {
        return feedHeadline;
    }

    public String getMaxArticles() {
        return maxArticles;
    }

    public String getArticlePath() {
        return articlePath;
    }

    public String getMonthHeadline() {
        return monthHeadline;
    }

    public String getNumber() {
        return number;
    }

    public String getNumberUnit() {
        return numberUnit;
    }

    public String getNumberDescription() {
        return numberDescription;
    }

    public String getArticleURLPath() {
        if (articleURLPath != null) {
            Resource pathResource = resourceResolver.getResource(articleURLPath);

            if (pathResource != null) {
                articleURLPath = articleURLPath + ".html";
            }
        }
        return articleURLPath;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }
}