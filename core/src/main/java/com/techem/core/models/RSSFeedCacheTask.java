package com.techem.core.schedulers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.techem.core.models.FeedChannel;
import com.techem.core.models.RSSFeedReader;
import com.techem.core.servlets.ResourceResolverUtil;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.framework.Constants;
import org.apache.sling.api.resource.Resource;

/**
 Cron task that caches all RSS Feeds present on the site on a given interval. <p>
 This task can be configured by OSGi config (PID: <code>com.techem.core.schedulers.RSSFeedCacheTask</code>) and is disabled by default.
 @see FeedChannel
 */
@Designate(ocd = RSSFeedCacheTask.RSSConfig.class)
@Component(immediate = true, service = Runnable.class, property = { Constants.SERVICE_ID + "=" + RSSFeedReader.RSS_TASK_NAME })
public class RSSFeedCacheTask implements Runnable {

    @Reference
    private Scheduler scheduler;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private ResourceResolver resResolver;
    private boolean isEnabled = false;
    private String RSS_CACHE_LOCATION;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     OSGi configuration related to the RSS Feed Cache Cron Task.
     */
    @ObjectClassDefinition(name = "RSS Feed Scheduled Cache", description = "RSS Feed Caching Scheduled Task.")
    public static @interface RSSConfig {
        @AttributeDefinition(
                name = "Enabled",
                description = "Enabled/Disable Scheduled Task.",
                type = AttributeType.BOOLEAN
        )
        boolean enabled() default false;

        @AttributeDefinition(
                name = "Cron-Job Expression",
                description = "Defines when and how to run this task. Check out https://www.freeformatter.com/cron-expression-generator-quartz.html",
                type = AttributeType.STRING
        )
        String schedulerExpression() default "0 0 0 * * ?";

        @AttributeDefinition(
                name = "RSS Cached Articles Location",
                description = "JCR path where the RSS feed will be cached.",
                type = AttributeType.STRING
        )
        String rssPath() default "/etc/rss";
    }

    @Modified
    protected void modified(RSSConfig config) {
        isEnabled = config.enabled();
        RSS_CACHE_LOCATION = config.rssPath() == null ? RSSFeedReader.RSS_CACHE_DEFAULT : config.rssPath();
        removeScheduler(config);
        addScheduler(config);
    }

    @Activate
    protected void activate(RSSConfig config) {
        isEnabled = config.enabled();
        RSS_CACHE_LOCATION = config.rssPath() == null ? RSSFeedReader.RSS_CACHE_DEFAULT : config.rssPath();
        if(isEnabled) {
            addScheduler(config);
        }
    }

    @Deactivate
    protected void deactivate(RSSConfig config) {
        removeScheduler(config);
    }

    private void addScheduler(RSSConfig configuration) {
        if (configuration.enabled()) {
            ScheduleOptions scheduleOptions = scheduler.EXPR(configuration.schedulerExpression());
            scheduleOptions.name(String.valueOf(RSSFeedReader.RSS_TASK_NAME));
            scheduleOptions.canRunConcurrently(false);
            scheduler.schedule(this, scheduleOptions);
        } else {
            removeScheduler(configuration);
        }
    }

    private void removeScheduler(RSSConfig configuration) {
        scheduler.unschedule(RSSFeedReader.RSS_TASK_NAME);
    }

    @Override
    public void run() {
        if(isEnabled) {
            logger.info("Running job '{}'.", RSSFeedReader.RSS_TASK_NAME);
            cacheRSSFeed();
        }
    }

    /**
     Caches all RSS feeds present on the website while also removing cached data left over from removed components/components which bypass the cache.
     */
    private void cacheRSSFeed() {
        try {
            resResolver = ResourceResolverUtil.getResolver(resourceResolverFactory);
            List<String> feedURLs = getArticleFeedURLs();
            List<Resource> cachedFeeds = getCachedRSSFeeds();

            for(Resource cachedFeed : cachedFeeds) {
                String feedURL = (String) cachedFeed.getValueMap().get(RSSFeedReader.RSS_FEED_LINK);

                if(!feedURLs.contains(feedURL)) {
                    resResolver.delete(cachedFeed);
                }
            }
            resResolver.commit();

            for(String url : feedURLs) {
                logger.info("Updating cache for URL '{}'.", url);
                FeedChannel fChannel = new RSSFeedReader(url).readFeed(resResolver);
                fChannel.cacheChannel();
            }
        } catch(Exception e) {
            logger.error("Could not cache feed. Ex: ", e);
        }
    }

    /**
     Looks for any component that is of <code>sling:resourceType</code> 'techem/components/newshub-article-feed'.
     Ignores duplicates and components which bypass cache.
     @return <code>List</code> of Strings representing the URLs pointing to RSS feeds.
     */
    private List<String> getArticleFeedURLs() {
        List<String> articleFeeds = new ArrayList<String>();
        Map<String, String> predicateMap = new HashMap<>();

        predicateMap.put("path", "/content/techem");
        predicateMap.put("1_property", "sling:resourceType");
        predicateMap.put("1_property.value", "techem/components/newshub-article-feed");
        predicateMap.put("p.limit", "-1");

        Iterator<Resource> resources = doQuery(predicateMap).getResources();

        while (resources.hasNext()) {
            Resource feedRes = resources.next();
            String feedURL = (String)feedRes.getValueMap().get("rssFeedURL");
            Boolean cachedBypassed = Boolean.valueOf((String)(feedRes.getValueMap().get("bypassCache") == null ? "false" : feedRes.getValueMap().get("bypassCache")));

            if(feedURL != null && !articleFeeds.contains(feedURL) && !cachedBypassed) {
                articleFeeds.add(feedURL);
            }
        }
        return articleFeeds;
    }

    /**
     Gets all currently cached feeds.
     @return <code>List</code> of Resources representing the cached {@link FeedChannel}s.
     */
    private List<Resource> getCachedRSSFeeds() {
        List<Resource> rssFeeds = new ArrayList<Resource>();

        Resource rssRoot = resResolver.getResource(RSS_CACHE_LOCATION);

        if(rssRoot != null) {
            for(Resource child : rssRoot.getChildren()) {
                rssFeeds.add(child);
            }
        }

        return rssFeeds;
    }

    /**
     Executes the given query and returns the result.
     @param predicateMap containing predicates needed for the search.
     @return <code>SearchResult</code> the result of the query.
     */
    private SearchResult doQuery(Map<String, String> predicateMap) {
        QueryBuilder queryBuilder = resResolver.adaptTo(QueryBuilder.class);
        Session session = resResolver.adaptTo(Session.class);
        com.day.cq.search.Query query = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);
        return query.getResult();
    }

}