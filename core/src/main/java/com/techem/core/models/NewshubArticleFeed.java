package com.techem.core.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.xml.stream.XMLStreamException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, adapters = NewshubArticleFeed.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = "techem/components/newshub-article-feed")
public class NewshubArticleFeed {

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(name = "feedHeadline")
    private String feedHeadline;

    @ValueMapValue(name = "maxArticles")
    private String maxArticles;

    @ValueMapValue(name = "monthHeadline")
    private String monthHeadline;

    @ValueMapValue(name = "number")
    private String number;

    @ValueMapValue(name = "numberUnit")
    private String numberUnit;

    @ValueMapValue(name = "numberDescription")
    private String numberDescription;

    @ValueMapValue(name = "rssFeedURL")
    private String rssFeedURL;

    @ValueMapValue(name = "articleURLPath")
    private String articleURLPath;

    @ValueMapValue(name = "backgroundImage")
    private String backgroundImage;

    @ValueMapValue(name = "bypassCache")
    private boolean bypassCache;

    private List<FeedItem> rssArticles = new ArrayList<FeedItem>();

    @PostConstruct
    protected void init() throws IOException, XMLStreamException {

        if(rssFeedURL == null || rssFeedURL.length() == 0) { return; }

        FeedChannel fChannel = resourceResolver.adaptTo(FeedChannel.class);
        fChannel.setFeedLink(rssFeedURL);

        if(!fChannel.isCached() || bypassCache) {
            fChannel = new RSSFeedReader(rssFeedURL).readFeed(resourceResolver);
        }

        if(fChannel.isCached() && bypassCache) {
            fChannel.invalidateCache();
        }

        if(!fChannel.isCached() && !bypassCache) {
            fChannel.cacheChannel();
        }

        if(!bypassCache) {
            fChannel.readCache();
        }

        if(fChannel != null && fChannel.getChannelItems().size() > 0) {
            rssArticles = fChannel.getChannelItems().stream().limit(Long.parseLong(maxArticles)).collect(Collectors.toList());
            rssArticles.sort(Comparator.comparing(o -> ((FeedItem) o).getFeedPubDateTime()).reversed());
        }
    }

    public List<FeedItem> getRssArticles() {
        return rssArticles;
    }

    public String getFeedHeadline() {
        return feedHeadline;
    }

    public String getMaxArticles() {
        return maxArticles;
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

    public String rssFeedURL() {
        return rssFeedURL;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getArticleURLPath() {
        return articleURLPath;
    }
}