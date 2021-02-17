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
import java.util.*;
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

    @ValueMapValue(name = "showMorelinkUrl")
    private String showMorelinkUrl;

    @ValueMapValue(name = "unsorted")
    private boolean unsorted;

    private Map<Stage, NewsArticle> newsArticles;

    @PostConstruct
    protected void init() {
        if(CollectionUtils.isNotEmpty(newsItems)) {
            final Map<Stage, NewsArticle> unsortedArticles = getUnsortedArticles();

            if(unsorted == true) {
                newsArticles = unsortedArticles.entrySet().stream()
                        .limit(maxArticles)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (k,v) -> k, LinkedHashMap::new));
            } else if(unsorted == false && !unsortedArticles.isEmpty()) {
                newsArticles = getSortedArticles(unsortedArticles);
            }
        } else {
            logger.info("The newsItems are null or empty {}");
        }
    }


    protected Map<Stage, NewsArticle> getUnsortedArticles() {
        return newsItems.stream()
                .map(item -> item.adaptTo(NewsArticle.class))
                .collect(Collectors.toMap(
                        item -> item.getArticleResource().
                                getChild(JcrConstants.JCR_CONTENT).adaptTo(Stage.class),
                        newsArticle -> newsArticle,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    protected Map<Stage, NewsArticle> getSortedArticles(final Map<Stage, NewsArticle> unsortedArticles) {
        return newsArticles = unsortedArticles.entrySet().stream()
                .sorted((d1,d2) -> compareByDate(d1, d2))
                .limit(maxArticles)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (k,v) -> k, LinkedHashMap::new));
    }

    public int compareByDate(Map.Entry<Stage, NewsArticle> object1, Map.Entry<Stage, NewsArticle> object2) {
        final Stage stage1 = object1.getKey();
        final Stage stage2 = object2.getKey();

        return  Objects.nonNull(stage1.getDateObject()) &&
                Objects.nonNull(stage2.getDateObject()) ?
                compare(stage1.getDateObject(), stage2.getDateObject()) :
                compare(stage1.getLastModifiedObject(), stage2.getLastModifiedObject());
    }

    public int compare(Date date1, Date date2) {
       return Long.compare(date1.getTime(),
               date2.getTime()) * -1;
    }

    public String getShowMoreLabel() { return showMoreLabel; }

    public String getShowMorelinkUrl() { return showMorelinkUrl; }

    public Map<Stage, NewsArticle> getNewsArticles() {
        return newsArticles;
    }
}
