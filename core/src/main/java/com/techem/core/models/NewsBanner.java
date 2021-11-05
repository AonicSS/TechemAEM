package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.day.cq.commons.jcr.JcrConstants;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewsBanner {

    @Inject
    @Named("articlePath")
    private Resource articleResource;

    @ValueMapValue(name = "articleImg")
    private String articleImage;

    @ValueMapValue(name = "articleTitle")
    private String articleTitle;

    @ValueMapValue(name = "articleSubTitle")
    private String articleSubTitle;

    private String articlePath;

    private Logger logger = LoggerFactory.getLogger(NewsArticles.class);

    @PostConstruct
    protected void init() {
        try {
            if(articleResource != null) {
                articlePath = articleResource.getPath();
                Stage articleStage = articleResource.getChild(JcrConstants.JCR_CONTENT).adaptTo(Stage.class);

                /* Auto-populate the article headline field if it's not authored. */
                if(articleTitle == null) {
                    articleTitle = articleStage.getHeadline();
                }

                /* Auto-populate the article subtitle field if it's not authored. */
                if(articleSubTitle == null) {
                    articleSubTitle = articleStage.getCategory() + " - " + articleStage.getDate();
                }
            }
        }catch (NullPointerException e) {
            logger.info("Invalid article selected.");
        }
    }

    public String getArticleImage() {
        return articleImage;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleSubTitle() {
        return articleSubTitle;
    }

    public String getArticlePath() {
        return articlePath;
    }

}