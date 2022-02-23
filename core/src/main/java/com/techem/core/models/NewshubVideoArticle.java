package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import com.day.crx.JcrConstants;
import javax.annotation.PostConstruct;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.api.resource.ResourceResolver;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import javax.inject.Named;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewshubVideoArticle {

    private Logger logger = LoggerFactory.getLogger(NewshubVideoArticles.class);

    @SlingObject
    private ResourceResolver resourceResolver;

    @Inject
    @Named("articlePath")
    private Resource articleResource;

    @ValueMapValue(name = "headline")
    private String headline;

    @ValueMapValue(name = "linkTo")
    private String linkTo;

    @ValueMapValue(name = "videoID")
    private String videoID;

    private String articlePath;

    @PostConstruct
    protected void init() {
        try {
            if (Objects.nonNull(articleResource) && Objects.nonNull(articleResource.getChild(JcrConstants.JCR_CONTENT))) {
                articlePath = articleResource.getPath() + ".html";
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public Resource getArticleResource() {
        return articleResource;
    }

    public String getArticlePath() {
        return articlePath;
    }

    public String getHeadline() {
        return headline;
    }

    public String getLinkTo() {
        if (linkTo != null) {
            Resource pathResource = resourceResolver.getResource(linkTo);
            // check if resource exists and link is internal
            if (pathResource != null) {
                linkTo = linkTo + ".html";
            }
        }
        return linkTo;
    }

    public String getVideoID() {
        return videoID;
    }
}