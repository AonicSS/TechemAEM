package com.techem.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class NewshubArticleFeedTest {

    private final AemContext ctx = new AemContext();

    private NewshubArticleFeed newshubArticleFeed;

    private Resource resource;

    @Mock
    private FeedChannel feedChannel;

    @Test
    void shouldReturnArticlesFeed() throws UnsupportedEncodingException {

        when(feedChannel.isCached()).thenReturn(true);
        ctx.registerAdapter(ResourceResolver.class, FeedChannel.class, feedChannel);
        resource = ctx.load().json("/com/techem/core/models/NewshubArticleFeed.json","/newshub-article-feed");

        newshubArticleFeed = resource.adaptTo(NewshubArticleFeed.class);

        assertThat(newshubArticleFeed, notNullValue());
        assertThat(newshubArticleFeed.getFeedHeadline(), is("Headline"));
        assertThat(newshubArticleFeed.getMaxArticles(), is("10"));
        assertThat(newshubArticleFeed.getMonthHeadline(), is("Headline"));
        assertThat(newshubArticleFeed.getNumber(), is("5"));
        assertThat(newshubArticleFeed.getNumberUnit(), is("unit"));
        assertThat(newshubArticleFeed.getNumberDescription(), is("Description"));
        assertThat(newshubArticleFeed.getRssFeedURL(), is("/some/path/feeds.rss"));
        assertThat(newshubArticleFeed.getArticleURLPath(), is("/some/path/article.html"));
        assertThat(newshubArticleFeed.getBackgroundImage(), is("/some/path/image.png"));
        assertThat(newshubArticleFeed.isBypassCache(), is(false));
        assertThat(newshubArticleFeed.getRssArticles(), notNullValue());

    }
}