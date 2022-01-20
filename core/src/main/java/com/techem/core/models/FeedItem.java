package com.techem.core.models;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Class representing a <a href="https://cyber.harvard.edu/rss/rss.html#hrelementsOfLtitemgt">RSS Channel Item</a>.
 * An item represents the RSS feed data of the Channel as per <a href="https://cyber.harvard.edu/rss/rss.html">RSS 2.0 Specification</a>. </p>
 * This class acts as a child for the <code>FeedChannel</code> class.
 * @see FeedChannel
 */
@Model(adaptables = { Resource.class, ResourceResolver.class }, adapters = FeedItem.class, defaultInjectionStrategy =  DefaultInjectionStrategy.OPTIONAL)
public class FeedItem {

    @ValueMapValue(name = RSSFeedReader.RSS_FEED_TITLE)
    private String feedTitle;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_LINK)
    private String feedLink;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_GUID)
    private String feedGUID;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_DESCRIPTION)
    private String feedDescription;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_CONTENT)
    private String feedContent;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_CREATOR)
    private String feedCreator;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_PUBDATE)
    private String feedPubDate;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_AUTHOR)
    private String feedAuthor;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_CATEGORY)
    private String feedCategory;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_COMMENTS)
    private String feedComments;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_ENCLOSURE)
    private String feedEnclosure;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_SOURCE)
    private String feedSource;
    @ValueMapValue(name = RSSFeedReader.RSS_FEED_PUBDATETIME)
    private ZonedDateTime feedPubDateTime;

    @PostConstruct
    protected void init() { }

    public FeedItem() { }

    /**
     * Parses a date that is under the RFC-1123 format. Used for sorting the FeedItems by age.
     * @param zDateTime string representing the date to be parsed.
     */
    public void setFeedPubDateTime(String zDateTime) {
        if(zDateTime == null) { return; }
        DateTimeFormatter dateFormat = DateTimeFormatter.RFC_1123_DATE_TIME;
        feedPubDateTime = ZonedDateTime.parse(zDateTime, dateFormat);
    }

    public ZonedDateTime getFeedPubDateTime() {
        return feedPubDateTime;
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

    public String getFeedGUID() {
        return feedGUID;
    }

    public void setFeedGUID(String fGUID) {
        feedGUID = fGUID;
    }

    public String getFeedDescription() {
        return feedDescription;
    }

    public void setFeedDescription(String fDescription) {
        feedDescription = fDescription;
    }

    public String getFeedContent() {
        return feedContent;
    }

    public void setFeedContent(String fContent) {
        feedContent = fContent;
    }

    public String getFeedCreator() {
        return feedCreator;
    }

    public void setFeedCreator(String fCreator) {
        feedCreator = fCreator;
    }

    public String getFeedPubDate() {
        return feedPubDate;
    }

    public void setFeedPubDate(String fPubDate) {
        feedPubDate = fPubDate;
    }

    public String getFeedAuthor() {
        return feedAuthor;
    }

    public void setFeedAuthor(String fAuthor) {
        feedAuthor = fAuthor;
    }

    public String getFeedCategory() {
        return feedCategory;
    }

    public void setFeedCategory(String fCategory) {
        feedCategory = fCategory;
    }

    public String getFeedComments() {
        return feedComments;
    }

    public void setFeedComments(String fComments) {
        feedComments = fComments;
    }

    public String getFeedEnclosure() {
        return feedEnclosure;
    }

    public void setFeedEnclosure(String fEnclosure) {
        feedEnclosure = fEnclosure;
    }

    public String getFeedSource() {
        return feedSource;
    }

    public void setFeedSource(String fSource) {
        feedSource = fSource;
    }

}