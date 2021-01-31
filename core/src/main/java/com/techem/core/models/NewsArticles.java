package com.techem.core.models;

import com.day.crx.JcrConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewsArticles {

    private Logger logger = LoggerFactory.getLogger(NewsArticles.class);

    @Inject
    @Named("newsItems")
    private List<Resource> newsItems;

    @ValueMapValue(name = "maxArticles")
    private int maxArticles;

    @ValueMapValue(name = "showMoreLabel")
    private String showMoreLabel;

    private Map<Stage, NewsArticle> newsArticles;

    @PostConstruct
    protected void init() {

        if(CollectionUtils.isNotEmpty(newsItems)) {
            final Map<Stage, NewsArticle> unsortedArticles = getArticles();

            if(Objects.nonNull(unsortedArticles)) {
                newsArticles = unsortedArticles.entrySet().stream()
                          .sorted((d1,d2) ->
                                  Long.valueOf(d1.getKey().getDateObject().getTime())
                                  .compareTo(d2.getKey().getDateObject().getTime()))
                          .limit(maxArticles)
                          .collect(Collectors.toMap(
                                  Map.Entry::getKey, Map.Entry::getValue, (k,v) -> k, LinkedHashMap::new));
            }
        } else {
            logger.info("The newsItems is null or empty {}");
        }
    }

    public Map<Stage, NewsArticle> getArticles() {
        return newsItems.stream()
                .map(item -> item.adaptTo(NewsArticle.class))
                .collect(Collectors.toMap(
                        item -> item.getArticleResource().
                                getChild(JcrConstants.JCR_CONTENT).adaptTo(Stage.class),
                        newsArticle -> newsArticle,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public String getShowMoreLabel() { return showMoreLabel; }

    public Map<Stage, NewsArticle> getNewsArticles() {
        return newsArticles;
    }
}
