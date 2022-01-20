package com.techem.core.models;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.xml.stream.XMLStreamException;

import com.day.cq.commons.jcr.JcrConstants;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class representing a <a href="https://cyber.harvard.edu/rss/rss.html#requiredChannelElements">RSS Channel</a>.
 * A Channel will act as a parent and will contain the feed <a href="https://cyber.harvard.edu/rss/rss.html#hrelementsOfLtitemgt">items</a> data as children.
 * @see FeedItem
 */
@Model(adaptables = { Resource.class, ResourceResolver.class }, adapters = FeedChannel.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FeedChannel {

    @ValueMapValue(name = RSSFeedReader.RSS_FEED_TITLE)
    private String feedTitle;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_LINK)
    private String feedLink;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_DESCRIPTION)
    private String feedDescription;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_LANGUAGE)
    private String feedLang;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_COPYRIGHT)
    private String feedCopyright;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_MANAGINGEDITOR)
    private String feedManagingEditor;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_WEBMASTER)
    private String feedWebMaster;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_PUBDATE)
    private String feedPubDate;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_LASTBUILDDATE)
    private String feedLastBuildDate;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_CATEGORY)
    private String feedCategory;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_GENERATOR)
    private String feedGenerator;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_DOCS)
    private String feedDocs;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_CLOUD)
    private String feedCloud;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_TTL)
    private String feedTtl;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_IMAGE)
    private String feedImage;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_RATING)
    private String feedRating;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_TEXTINPUT)
    private String feedTextInput;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_SKIPHOURS)
    private String feedSkipHours;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_SKIPDAYS)
    private String feedSkipDays;

    private List<FeedItem> channelItems = new ArrayList<FeedItem>();

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String RSS_CFG_PID = "com.techem.core.schedulers.RSSFeedCacheTask";
    private String RSS_CACHE_LOCATION;

    @SlingObject
    private ResourceResolver resourceResolver;

    @Inject
    private ConfigurationAdmin configAdmin;

    @PostConstruct
    protected void init() throws IOException, XMLStreamException {
        Configuration cfg = configAdmin.getConfiguration(RSS_CFG_PID);
        RSS_CACHE_LOCATION = cfg.getProperties().get("rssPath") == null ? RSSFeedReader.RSS_CACHE_DEFAULT : (String) cfg.getProperties().get("rssPath");
    }

    public FeedChannel() { }

    public void setFeedItems(List<FeedItem> items) {
        channelItems = items;
    }

    /**
     * Returns a List containing the items of this channel.
     * @return a <code>List</code> containing the <code>FeedItem</code> children of this Channel.
     * @see FeedItem
     */
    public List<FeedItem> getChannelItems() {
        return channelItems;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String fTitle) {
        feedTitle = fTitle;
    }

    public String getFeedLink() {
        return feedLink;
    }

    public void setFeedLink(String fLink) {
        feedLink = fLink;
    }

    public String getFeedDescription() {
        return feedDescription;
    }

    public void setFeedDescription(String fDescription) {
        feedDescription = fDescription;
    }

    public String getFeedLang() {
        return feedLang;
    }

    public void setFeedLang(String fLang) {
        feedLang = fLang;
    }

    public String getFeedCopyright() {
        return feedCopyright;
    }

    public void setFeedCopyright(String fCopyright) {
        feedCopyright = fCopyright;
    }

    public String getFeedManagingEditor() {
        return feedManagingEditor;
    }

    public void setFeedManagingEditor(String fManagingEditor) {
        feedManagingEditor = fManagingEditor;
    }

    public String getFeedWebMaster() {
        return feedWebMaster;
    }

    public void setFeedWebMaster(String fWebMaster) {
        feedWebMaster = fWebMaster;
    }

    public String getFeedPubDate() {
        return feedPubDate;
    }

    public void setFeedPubDate(String fPubDate) {
        feedPubDate = fPubDate;
    }

    public String getFeedLastBuildDate() {
        return feedLastBuildDate;
    }

    public void setFeedLastBuildDate(String fLastBuildDate) {
        feedLastBuildDate = fLastBuildDate;
    }

    public String getFeedCategory() {
        return feedCategory;
    }

    public void setFeedCategory(String fCategory) {
        feedCategory = fCategory;
    }

    public String getFeedGenerator() {
        return feedGenerator;
    }

    public void setFeedGenerator(String fGenerator) {
        feedGenerator = fGenerator;
    }

    public String getFeedDocs() {
        return feedDocs;
    }

    public void setFeedDocs(String fDocs) {
        feedDocs = fDocs;
    }

    public String getFeedCloud() {
        return feedCloud;
    }

    public void setFeedCloud(String fCloud) {
        feedCloud = fCloud;
    }

    public String getFeedTtl() {
        return feedTtl;
    }

    public void setFeedTtl(String fTtl) {
        feedTtl = fTtl;
    }

    public String getFeedImage() {
        return feedImage;
    }

    public void setFeedImage(String fImage) {
        feedImage = fImage;
    }

    public String getFeedRating() {
        return feedRating;
    }

    public void setFeedRating(String fRating) {
        feedRating = fRating;
    }

    public String getFeedTextInput() {
        return feedTextInput;
    }

    public void setFeedTextInput(String fRating) {
        feedTextInput = fRating;
    }

    public String getFeedSkipHours() {
        return feedSkipHours;
    }

    public void setFeedSkipHours(String fSkipHours) {
        feedSkipHours = fSkipHours;
    }

    public String getFeedSkipDays() {
        return feedSkipDays;
    }

    public void setFeedSkipDays(String fSkipDays) {
        feedSkipDays = fSkipDays;
    }

    /**
     * Caches the current channel and its FeedItem children to the JCR repo
     * for future reads. Will create the parent node if it doesn't exist automatically. <p>
     * This method will create the nodes if none are present OR will update already cached data to be up to date.
     * @throws UnsupportedEncodingException
     * @see FeedItem
     * @see com.techem.core.schedulers.RSSFeedCacheTask
     */
    public void cacheChannel() throws PersistenceException, UnsupportedEncodingException {
        Resource rootRSSResource = resourceResolver.getResource(RSS_CACHE_LOCATION);

        if(rootRSSResource == null) {
            try {
                rootRSSResource = ResourceUtil.getOrCreateResource(resourceResolver, RSS_CACHE_LOCATION, JcrConstants.NT_UNSTRUCTURED, JcrConstants.NT_UNSTRUCTURED, true);
            } catch (Exception e) {
                logger.error("Could not create cache parent node. Ex: ", e);
            }
        }

        if(rootRSSResource != null && channelItems.size() > 0) {
            String channelNodeName = URLEncoder.encode(getFeedLink(), StandardCharsets.UTF_8.name());
            Map<String, Object> propsHeader = new HashMap<>();
            propsHeader.put(RSSFeedReader.RSS_FEED_TITLE, getFeedTitle());
            propsHeader.put(RSSFeedReader.RSS_FEED_LINK, getFeedLink());
            propsHeader.put(RSSFeedReader.RSS_FEED_DESCRIPTION, getFeedDescription());
            propsHeader.put(RSSFeedReader.RSS_FEED_LANGUAGE, getFeedLang());
            propsHeader.put(RSSFeedReader.RSS_FEED_COPYRIGHT, getFeedCopyright());
            propsHeader.put(RSSFeedReader.RSS_FEED_MANAGINGEDITOR, getFeedManagingEditor());
            propsHeader.put(RSSFeedReader.RSS_FEED_WEBMASTER, getFeedWebMaster());
            propsHeader.put(RSSFeedReader.RSS_FEED_PUBDATE, getFeedPubDate());
            propsHeader.put(RSSFeedReader.RSS_FEED_LASTBUILDDATE, getFeedLastBuildDate());
            propsHeader.put(RSSFeedReader.RSS_FEED_CATEGORY, getFeedCategory());
            propsHeader.put(RSSFeedReader.RSS_FEED_GENERATOR, getFeedGenerator());
            propsHeader.put(RSSFeedReader.RSS_FEED_DOCS, getFeedDocs());
            propsHeader.put(RSSFeedReader.RSS_FEED_CLOUD, getFeedCloud());
            propsHeader.put(RSSFeedReader.RSS_FEED_TTL, getFeedTtl());
            propsHeader.put(RSSFeedReader.RSS_FEED_IMAGE, getFeedImage());
            propsHeader.put(RSSFeedReader.RSS_FEED_RATING, getFeedRating());
            propsHeader.put(RSSFeedReader.RSS_FEED_TEXTINPUT, getFeedTextInput());
            propsHeader.put(RSSFeedReader.RSS_FEED_SKIPDAYS, getFeedSkipDays());
            propsHeader.put(RSSFeedReader.RSS_FEED_SKIPHOURS, getFeedSkipHours());
            propsHeader.put(JcrConstants.JCR_LASTMODIFIED, System.currentTimeMillis());
            propsHeader.entrySet().removeIf(entry -> entry.getValue() == null);

            Resource channelNode = resourceResolver.getResource(RSS_CACHE_LOCATION + "/" + channelNodeName);

            if(channelNode != null) { resourceResolver.delete(channelNode); }
            channelNode = resourceResolver.create(rootRSSResource, channelNodeName, propsHeader);

            int itemID = 0;
            for(FeedItem item : channelItems) {
                String itemNodeName = "chItem_" + ++itemID;
                Map<String, Object> props = new HashMap<>();
                props.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
                props.put(RSSFeedReader.RSS_FEED_TITLE, item.getFeedTitle());
                props.put(RSSFeedReader.RSS_FEED_LINK, item.getFeedLink());
                props.put(RSSFeedReader.RSS_FEED_GUID, item.getFeedGUID());
                props.put(RSSFeedReader.RSS_FEED_DESCRIPTION, item.getFeedDescription());
                props.put(RSSFeedReader.RSS_FEED_CONTENT, item.getFeedContent());
                props.put(RSSFeedReader.RSS_FEED_CREATOR, item.getFeedCreator());
                props.put(RSSFeedReader.RSS_FEED_PUBDATETIME, item.getFeedPubDateTime());
                props.put(RSSFeedReader.RSS_FEED_PUBDATE, item.getFeedPubDate());
                props.put(RSSFeedReader.RSS_FEED_AUTHOR, item.getFeedAuthor());
                props.put(RSSFeedReader.RSS_FEED_CATEGORY, item.getFeedCategory());
                props.put(RSSFeedReader.RSS_FEED_COMMENTS, item.getFeedComments());
                props.put(RSSFeedReader.RSS_FEED_ENCLOSURE, item.getFeedEnclosure());
                props.put(RSSFeedReader.RSS_FEED_SOURCE, item.getFeedSource());
                props.put(JcrConstants.JCR_LASTMODIFIED, System.currentTimeMillis());
                props.entrySet().removeIf(entry -> entry.getValue() == null);

                Resource itemNode = resourceResolver.getResource(channelNode.getPath() + "/" + itemNodeName);

                if(itemNode != null) { resourceResolver.delete(itemNode); }
                itemNode = resourceResolver.create(channelNode, itemNodeName, props);

            }
            resourceResolver.commit();
        }
    }

    /**
     * Checks if the current <code>FeedChannel</code> is cached.
     * @return <code>boolean</code> true if the channel is cached, false otherwise.
     * @throws UnsupportedEncodingException
     */
    public boolean isCached() throws UnsupportedEncodingException {
        return resourceResolver.getResource(RSS_CACHE_LOCATION + "/" + URLEncoder.encode(getFeedLink(),  StandardCharsets.UTF_8.name())) != null;
    }

    /**
     * Reads the cache from the JCR repo if the current {@link #FeedChannel()} has already been cached
     * and stores the data as FeedItem objects inside this
     * Channel's {@link #channelItems}.<p>
     * @throws UnsupportedEncodingException
     */
    public void readCache() throws UnsupportedEncodingException {
        if(isCached()) {
            channelItems.clear();
            Resource rootRSS = resourceResolver.getResource(RSS_CACHE_LOCATION + "/" + URLEncoder.encode(getFeedLink(), StandardCharsets.UTF_8.name()));
            Iterable<Resource> rssChildren = rootRSS.getChildren();

            for(Resource child : rssChildren) {
                channelItems.add(child.adaptTo(FeedItem.class));
            }
        }
    }

    /**
     * Invalidates the current {@link #FeedChannel()}'s cache only if it has already been cached before.
     * @throws UnsupportedEncodingException
     */
    public void invalidateCache() throws UnsupportedEncodingException {
        if(isCached()) {
            Resource rootRSS = resourceResolver.getResource(RSS_CACHE_LOCATION + "/" + URLEncoder.encode(getFeedLink(),  StandardCharsets.UTF_8.name()));

            try {
                resourceResolver.delete(rootRSS);
                resourceResolver.commit();
            } catch (PersistenceException e) {
                logger.error("Could not invalidate cache for parent '{}'. Skipping. Ex: ", rootRSS.getName(), e);
            }
        }
    }

}