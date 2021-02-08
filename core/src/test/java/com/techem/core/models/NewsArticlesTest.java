package com.techem.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AemContextExtension.class)
public class NewsArticlesTest {

    private static final String HEADLINE = "Lorem ipsum dolor sit amet";
    private static final String TEXT = "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit</p>";
    private static final String DATE = "15.04.2021";
    private static final String CATEGORY = "newCaregory";
    private static final String SHOW_MORE_LABEL = "Show more";
    private static final String IMAGE_REFERENCE = "/content/dam/core-components-examples/library/sample-assets/mountain-range.jpg";
    private static final String ARTICLE_PATH = "/content/components/news-article/newsItems/item0/articlePath";

    private AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private NewsArticles newsArticles;

    @BeforeEach
    public void setUp() throws Exception {
        context.load().json("/news-article.json", "/content/components/news-article");
        Resource newsArticle =  context.currentResource("/content/components/news-article");

        newsArticles = newsArticle.adaptTo(NewsArticles.class);
    }

    @Test
    public void testGetArticles() {
      final Map<Stage, NewsArticle> map =  newsArticles.getNewsArticles();
      final NewsArticle newsArticle = map.values().stream().findFirst().get();
      final Set<Stage> stages = map.keySet();
      final Stage stage = stages.stream().findFirst().get();

      assertNotNull(map);
      assertNotNull(newsArticle);
      assertNotNull(stage);

      assertEquals(3, map.size());
      assertEquals(HEADLINE, stage.getHeadline());
      assertEquals(TEXT, stage.getText());
      assertEquals(DATE, stage.getDate());
      assertEquals(CATEGORY, stage.getCategory());

      assertEquals(IMAGE_REFERENCE, newsArticle.getArticleImage());
      assertEquals(ARTICLE_PATH, newsArticle.getArticlePath());
    }

    @Test
    public void testGetShowMoreLabel() {
        assertEquals(SHOW_MORE_LABEL, newsArticles.getShowMoreLabel());
    }
}
