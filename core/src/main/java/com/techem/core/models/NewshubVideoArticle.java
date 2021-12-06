package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Objects;

@Model(adaptables = Resource.class ,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewshubVideoArticle {

    @Inject
    @Named("articlePath")
    private Resource articleResource;

    @ValueMapValue(name = "videoID")
    private String videoID;

    private String articlePath;

    @PostConstruct
    protected void init() {
        if(Objects.nonNull(articleResource)) {
            articlePath = articleResource.getPath();
        }
    }

    public Resource getArticleResource() {
        return articleResource;
    }

    public String getArticlePath() { return articlePath; }

    public String getVideoID() { return videoID; }
}