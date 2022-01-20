package com.techem.core.models;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used for reading RSS data from an RSS feed by
 * a given URL. This class requires that the origin file
 * is structured according to the <a href="https://cyber.harvard.edu/rss/rss.html">RSS 2.0 Specification</a>.
 */
public class RSSFeedReader {

    public static final String RSS_FEED_ITEM = "item";
    public static final String RSS_FEED_TITLE = "title";
    public static final String RSS_FEED_LINK = "link";
    public static final String RSS_FEED_DESCRIPTION = "description";
    public static final String RSS_FEED_LANGUAGE = "language";
    public static final String RSS_FEED_COPYRIGHT = "copyright";
    public static final String RSS_FEED_MANAGINGEDITOR = "managingEditor";
    public static final String RSS_FEED_WEBMASTER = "webMaster";
    public static final String RSS_FEED_PUBDATE = "pubDate";
    public static final String RSS_FEED_PUBDATETIME = "pubDateTime";
    public static final String RSS_FEED_LASTBUILDDATE = "lastBuildDate";
    public static final String RSS_FEED_CATEGORY = "category";
    public static final String RSS_FEED_GENERATOR = "generator";
    public static final String RSS_FEED_DOCS = "docs";
    public static final String RSS_FEED_CLOUD = "cloud";
    public static final String RSS_FEED_TTL = "ttl";
    public static final String RSS_FEED_IMAGE = "image";
    public static final String RSS_FEED_RATING = "rating";
    public static final String RSS_FEED_TEXTINPUT = "textInput";
    public static final String RSS_FEED_SKIPHOURS = "skipHours";
    public static final String RSS_FEED_SKIPDAYS = "skipDays";
    public static final String RSS_FEED_CONTENT = "content";
    public static final String RSS_FEED_ENCODED = "encoded";
    public static final String RSS_FEED_CREATOR = "creator";
    public static final String RSS_FEED_AUTHOR = "author";
    public static final String RSS_FEED_COMMENTS = "comments";
    public static final String RSS_FEED_ENCLOSURE = "enclosure";
    public static final String RSS_FEED_SOURCE = "source";
    public static final String RSS_FEED_GUID = "guid";
    public static final String RSS_CACHE_DEFAULT = "/etc/rss";
    public static final String RSS_TASK_NAME = "RSS_SCHEDULED_TASK";
    private URL RSS_FEED_URL;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public RSSFeedReader(String feedURL) {
        try {
            RSS_FEED_URL = new URL(feedURL);
        }catch(MalformedURLException e) {
            logger.error("Could not load RSS feed from URL {}. Ex trace: ", feedURL, e);
        }
    }

    /**
     * This method will read an RSS feed from the given URL and will
     * parse the data into a <code>FeedChannel</code> object.
     * @param resResolver the <code>ResourceResolver</code> instance.
     * @return <code>FeedChannel</code> object containing the RSS feed data.
     * @see FeedChannel
     */
    public FeedChannel readFeed(ResourceResolver resResolver) throws IOException, XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream RSSFeedStream = RSS_FEED_URL.openStream();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(RSSFeedStream);
        FeedChannel fChannel = resResolver.adaptTo(FeedChannel.class);

        if(fChannel.getChannelItems() != null && fChannel.getChannelItems().size() > 0) {
            fChannel.getChannelItems().clear();
        }

        boolean isFeedHeader = true;
        HashMap<String, String> elementProps = new HashMap<String, String>();

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();

            if (event.isStartElement()) {
                String localPart = event.asStartElement().getName().getLocalPart();

                if(localPart.equals(RSS_FEED_ITEM)) {
                    if (isFeedHeader) {
                        isFeedHeader = false;

                        fChannel.setFeedTitle(elementProps.get(RSS_FEED_TITLE));
                        fChannel.setFeedLink(elementProps.get(RSS_FEED_LINK));
                        fChannel.setFeedDescription(elementProps.get(RSS_FEED_DESCRIPTION));
                        fChannel.setFeedLang(elementProps.get(RSS_FEED_LANGUAGE));
                        fChannel.setFeedCopyright(elementProps.get(RSS_FEED_COPYRIGHT));
                        fChannel.setFeedManagingEditor(elementProps.get(RSS_FEED_MANAGINGEDITOR));
                        fChannel.setFeedWebMaster(elementProps.get(RSS_FEED_WEBMASTER));
                        fChannel.setFeedPubDate(formateDate(elementProps.get(RSS_FEED_PUBDATE)));
                        fChannel.setFeedLastBuildDate(formateDate(elementProps.get(RSS_FEED_LASTBUILDDATE)));
                        fChannel.setFeedCategory(elementProps.get(RSS_FEED_CATEGORY));
                        fChannel.setFeedGenerator(elementProps.get(RSS_FEED_GENERATOR));
                        fChannel.setFeedDocs(elementProps.get(RSS_FEED_DOCS));
                        fChannel.setFeedCloud(elementProps.get(RSS_FEED_CLOUD));
                        fChannel.setFeedTtl(elementProps.get(RSS_FEED_TTL));
                        fChannel.setFeedImage(elementProps.get(RSS_FEED_IMAGE));
                        fChannel.setFeedRating(elementProps.get(RSS_FEED_RATING));
                        fChannel.setFeedTextInput(elementProps.get(RSS_FEED_TEXTINPUT));
                        fChannel.setFeedSkipDays(elementProps.get(RSS_FEED_SKIPDAYS));
                        fChannel.setFeedSkipHours(elementProps.get(RSS_FEED_SKIPHOURS));
                    }
                    continue;
                }

                String data = getCharacterData(event, eventReader);

                if(localPart.equals(RSS_FEED_LINK) && StringUtils.isBlank(data)) {
                    elementProps.put(localPart, event.asStartElement().getAttributeByName(new QName("href")).getValue());
                    continue;
                }
                elementProps.put(localPart, data);
            }

            if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals(RSS_FEED_ITEM)) {
                FeedItem fItem = new FeedItem();
                fItem.setFeedAuthor(elementProps.get(RSS_FEED_AUTHOR));
                fItem.setFeedCategory(elementProps.get(RSS_FEED_CATEGORY));
                fItem.setFeedComments(elementProps.get(RSS_FEED_COMMENTS));
                fItem.setFeedContent(elementProps.get(RSS_FEED_CONTENT) == null ? elementProps.get(RSS_FEED_ENCODED) : elementProps.get(RSS_FEED_CONTENT));
                fItem.setFeedCreator(elementProps.get(RSS_FEED_CREATOR));
                fItem.setFeedDescription(elementProps.get(RSS_FEED_DESCRIPTION));
                fItem.setFeedEnclosure(elementProps.get(RSS_FEED_ENCLOSURE));
                fItem.setFeedGUID(elementProps.get(RSS_FEED_GUID));
                fItem.setFeedLink(elementProps.get(RSS_FEED_LINK));
                fItem.setFeedPubDateTime(elementProps.get(RSS_FEED_PUBDATE));
                fItem.setFeedPubDate(formateDate(elementProps.get(RSS_FEED_PUBDATE)));
                fItem.setFeedSource(elementProps.get(RSS_FEED_SOURCE));
                fItem.setFeedTitle(elementProps.get(RSS_FEED_TITLE));

                fChannel.getChannelItems().add(fItem);
                event = eventReader.nextEvent();
                continue;
            }
        }

        return fChannel;
    }

    /**
     * Reads the data inside an XML tag as String.
     * @param event the <code>XMLEvent</code>.
     * @param eventReader the <code>XMLEvenetReader</code>.
     * @return <code>String</code> XML tag value as a String.
     * @see XMLEvent
     */
    private String getCharacterData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData().trim();
        }
        return result;
    }

    /**
     * Formats the given date as "dd.MM.yyyy".
     * @param date string representing the date to be formatted.
     * @return <code>String</code> the date under the "dd.MM.yyyy" format or <code>null</code> if the date could not be formatted.
     */
    private String formateDate(String date) {
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.RFC_1123_DATE_TIME;
            ZonedDateTime zoneDate = ZonedDateTime.parse(date, dateFormat);
            return DateTimeFormatter.ofPattern("dd.MM.yyyy").format(zoneDate);
        }catch(Exception e) {
            return null;
        }
    }
}