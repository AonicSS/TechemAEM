package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
import javax.inject.Named;

import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, adapters = NewshubSimilarArticles.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/newshub-similar-articles")
public class NewshubSimilarArticles {

    @Inject @Named("articles")
    private List<NewshubSimilarArticlesItem> articles;

    @ValueMapValue(name="headline")
    private String headline;

    public List<NewshubSimilarArticlesItem> getArticles() {
        return new ArrayList<>(articles);
    }

    public String getHeadline() {
        return headline;
    }
}