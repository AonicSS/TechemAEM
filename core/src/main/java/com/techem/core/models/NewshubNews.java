package com.techem.core.models;

import com.day.crx.JcrConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewshubNews {

    private Logger logger = LoggerFactory.getLogger(NewshubNews.class);

    @Inject
    @Named("newsItems")
    private List<Resource> newsItems;

    @ValueMapValue
    private String filePath;

    @ValueMapValue(name = "loadingType")
    private String loadingType;

    @ValueMapValue(name = "headline")
    private String headline;

    @ValueMapValue(name = "primaryButtonLabel")
    private String primaryButtonLabel;

    @ValueMapValue(name = "primaryButtonLink")
    private String primaryButtonLink;

    @ValueMapValue(name = "secondaryButtonLabel")
    private String secondaryButtonLabel;

    @ValueMapValue(name = "secondaryButtonLink")
    private String secondaryButtonLink;

    @ValueMapValue(name = "addManually")
    private Boolean addManually;

    private Map<Stage, NewsArticle> newsArticles;

    @SlingObject
    private ResourceResolver resourceResolver;

    @PostConstruct
    protected void init() {
        if (loadingType != null && !loadingType.isEmpty()) {
            if (loadingType.equals("auto")) {
                newsItems = Lists.newArrayList(getArticleResources()).stream().filter((item) -> {
                    return item.getValueMap().get("jcr:primaryType").equals("cq:Page")
                            && item.getChild("jcr:content").adaptTo(ValueMap.class).get("sling:resourceType")
                                    .equals("techem/components/structure/news-page");
                }).collect(Collectors.toList());
            }

            mapArticles();
        } else {
            logger.error("LoadingType empty");
        }
    }

    protected void mapArticles() {
        if (CollectionUtils.isNotEmpty(newsItems)) {
            final Map<Stage, NewsArticle> tmpNewsArticles;

            tmpNewsArticles = newsItems.stream().map(item -> item.adaptTo(NewsArticle.class))
                    .collect(Collectors.toMap(
                            item -> item.getArticleResource().getChild(JcrConstants.JCR_CONTENT).adaptTo(Stage.class),
                            newsArticle -> newsArticle, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            newsArticles = tmpNewsArticles.entrySet().stream().sorted((d1, d2) -> compareByDate(d1, d2)).limit(6)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> k, LinkedHashMap::new));

        } else {
            logger.error("NewsItems empty");
        }
    }

    protected int compareByDate(Map.Entry<Stage, NewsArticle> object1, Map.Entry<Stage, NewsArticle> object2) {
        final Stage stage1 = object1.getKey();
        final Stage stage2 = object2.getKey();

        return Objects.nonNull(stage1.getDateObject()) && Objects.nonNull(stage2.getDateObject())
                ? compare(stage1.getDateObject(), stage2.getDateObject())
                : compare(stage1.getLastModifiedObject(), stage2.getLastModifiedObject());
    }

    protected int compare(Date date1, Date date2) {
        return Long.compare(date1.getTime(), date2.getTime()) * -1;
    }

    protected Iterator<Resource> getArticleResources() {
        if (this.filePath == null || this.filePath.isEmpty()) {
            return Collections.emptyIterator();
        }

        Resource resource = null;

        try {
            resource = resourceResolver.getResource(this.filePath);
        } catch (Error error) {
            logger.error("Error fetching Resource: " + error);
        }

        if (resource == null) {
            return Collections.emptyIterator();
        }

        return resource.listChildren();
    }

    public String getFilePath() {
        return filePath;
    }

    public String getHeadline() {
        return headline;
    }

    public String getPrimaryButtonLabel() {
        return primaryButtonLabel;
    }

    public String getPrimaryButtonLink() {
        return primaryButtonLink;
    }

    public String getSecondaryButtonLabel() {
        return secondaryButtonLabel;
    }

    public String getSecondaryButtonLink() {
        return secondaryButtonLink;
    }

    public Boolean getAddManually() {
        return addManually;
    }

    public Map<Stage, NewsArticle> getNewsArticles() {
        return newsArticles;
    }
}