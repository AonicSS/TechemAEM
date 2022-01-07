package com.techem.core.models;

import com.day.crx.JcrConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewshubVideoArticles {

    private Logger logger = LoggerFactory.getLogger(NewshubVideoArticles.class);

    @SlingObject
    private ResourceResolver resourceResolver;
    
    @Inject
    @Named("newsItems")
    private List<Resource> newsItems;

    @ValueMapValue(name = "primaryButtonLabel")
    private String primaryButtonLabel;

    @ValueMapValue(name = "primaryButtonLink")
    private String primaryButtonLink;

    @ValueMapValue(name = "secondaryButtonLabel")
    private String secondaryButtonLabel;

    @ValueMapValue(name = "secondaryButtonLink")
    private String secondaryButtonLink;

    @ValueMapValue(name = "cookieConsentMessage")
    private String cookieConsentMessage;

    @ValueMapValue(name = "cookieConsentButtonLabel")
    private String cookieConsentButtonLabel;

    private Map<Stage, NewshubVideoArticle> videoArticles;

    @PostConstruct
    protected void init() {
        if(CollectionUtils.isNotEmpty(newsItems)) {
            videoArticles = getArticles();
        } else {
            logger.info("The newsItems are null or empty {}");
        }
    }

    protected Map<Stage, NewshubVideoArticle> getArticles() {
        return newsItems.stream()
                .map(item -> item.adaptTo(NewshubVideoArticle.class))
                .collect(Collectors.toMap(
                        item -> item.getArticleResource().
                                getChild(JcrConstants.JCR_CONTENT).adaptTo(Stage.class),
                        newsArticle -> newsArticle, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public String getPrimaryButtonLabel() {
        return primaryButtonLabel;
    }

    public String getPrimaryButtonLink() {
        if (primaryButtonLink != null) {
            Resource pathResource = resourceResolver.getResource(primaryButtonLink);

            if (pathResource != null) {
                primaryButtonLink = primaryButtonLink + ".html";
            }
        }
        return primaryButtonLink;
    }

    public String getSecondaryButtonLabel() {
        return secondaryButtonLabel;
    }

    public String getSecondaryButtonLink() {
        if (secondaryButtonLink != null) {
            Resource pathResource = resourceResolver.getResource(secondaryButtonLink);

            if (pathResource != null) {
                secondaryButtonLink = secondaryButtonLink + ".html";
            }
        }
        return secondaryButtonLink;
    }

    public String getCookieConsentButtonLabel() {
        return cookieConsentButtonLabel;
    }

    public String getCookieConsentMessage() {
        return cookieConsentMessage;
    }

    public Map<Stage, NewshubVideoArticle> getVideoArticles() {
        return videoArticles;
    }
}