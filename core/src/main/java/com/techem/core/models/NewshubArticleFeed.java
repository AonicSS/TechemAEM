package com.techem.core.models;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Getter
@Model(adaptables = Resource.class, adapters = NewshubArticleFeed.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = "techem/components/newshub-article-feed")
public class NewshubArticleFeed {

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue
    private String feedHeadline;

    @ValueMapValue
    private String maxArticles;

    @ValueMapValue
    private String monthHeadline;

    @ValueMapValue
    private String number;

    @ValueMapValue
    private String numberUnit;

    @ValueMapValue
    private String numberDescription;

    @ValueMapValue
    private String rssFeedURL;

    @ValueMapValue
    private String articleURLPath;

    @ValueMapValue
    private String backgroundImage;

    @ValueMapValue
    private boolean bypassCache;

    @Getter
    private List<FeedItem> rssArticles = new ArrayList<>();

    @PostConstruct
    protected void init() throws IOException, XMLStreamException {

        if (isEmpty(rssFeedURL)) {
            return;
        }

        FeedChannel fChannel = resourceResolver.adaptTo(FeedChannel.class);

        if (fChannel == null) {
            return;
        }

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

        if(isNotEmpty(fChannel.getChannelItems())) {
            rssArticles = fChannel.getChannelItems().stream().limit(Long.parseLong(maxArticles)).collect(Collectors.toList());
            rssArticles.sort(Comparator.comparing(o -> ((FeedItem) o).getFeedPubDateTime()).reversed());
        }
    }

}