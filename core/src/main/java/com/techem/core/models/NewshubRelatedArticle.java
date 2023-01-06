package com.techem.core.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.Page;
import com.google.common.collect.Lists;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.commons.collections4.CollectionUtils;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = "techem/components/newshub-related-article")
public class NewshubRelatedArticle {

    @Inject
    @Getter
    private List<Stage> articleListManually;

    @Inject
    private List<Stage> customAutoArti;

    @SlingObject
    private ResourceResolver resourceResolver;

    @Getter
    @ValueMapValue(name="insertionType")
    private String insertionType;

    @ValueMapValue(name="orderBy")
    private String orderBy;

    @ValueMapValue(name="sortOrder")
    private String sortOrder;

    @Getter
    @ValueMapValue(name="maxArticles")
    private int maxArticles;

    @Getter
    @ValueMapValue(name="filePath")
    private String filePath;

    @Getter
    @ValueMapValue(name="filteringTags")
    private String[] filteringTags;

    @ValueMapValue(name="teaserType")
    @Default(values = "artiText")
    private String teaserType;

    @ValueMapValue(name="maxTeaserText")
    @Default(intValues = 1000)
    private int maxTeaserText;

    @Getter
    private List<Stage> newsArticles;

    private List<Resource> newsItems;

    private static final String NEWSPAGE_RESOURCE_TYPE = "techem/components/structure/news-page";
    private static final String TEXT_RESOURCE_TYPE = "techem/components/text";

    @PostConstruct
    protected void init() {
        if (StringUtils.isNotBlank(insertionType) && insertionType.equals("automatically")) {
            newsItems = getArticleResources();
            filterByTags();
            customizeArticles();
            orderList();
        }
    }

    private void filterByTags() {
        if (CollectionUtils.isNotEmpty(newsItems)) {
            newsArticles = new ArrayList<>();
            
            for(Resource res : newsItems) {
                Resource artiContent = res.getChild(JcrConstants.JCR_CONTENT);
                String[] artiTags = (String[]) artiContent.getValueMap().get(NameConstants.PN_TAGS);
                boolean isNoCommonElements = true;
                
                if(artiTags != null){
                    isNoCommonElements = Collections.disjoint(Arrays.asList(artiTags), Arrays.asList(filteringTags));

                    if (!isNoCommonElements){
                        Stage artiStage = res.getChild(JcrConstants.JCR_CONTENT).adaptTo(Stage.class);
                        artiStage.setPath(res.getPath());
                        newsArticles.add(artiStage);
                    }
                }
            }
        }
    }

    private void orderList() {
        if(StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(sortOrder) && CollectionUtils.isNotEmpty(newsArticles)) {
            switch(orderBy) {
                case "dateArticle":
                    newsArticles.sort(Comparator.comparing(o -> ((Stage) o).getDateObject()));
                break;
                case "dateModified":    
                    newsArticles.sort(Comparator.comparing(o -> ((Stage) o).getLastModifiedObject()));
                break;
                case "dateCreation":
                    newsArticles.sort(Comparator.comparing(o -> ((Stage) o).getCreatedDate()));
                break;
                case "artiTitle":
                    newsArticles.sort(Comparator.comparing(o -> ((Stage) o).getHeadline().toLowerCase().replaceAll("\\s+","")));
                break;
                case "artiCategory": 
                    newsArticles.sort(Comparator.comparing(o -> ((Stage) o).getCategory().toLowerCase().replaceAll("\\s+","")));
                break;
                default: break;
            }

            if(sortOrder.equals("descending")) {
                Collections.reverse(newsArticles);
            }
            newsArticles = newsArticles.stream().limit(maxArticles).collect(Collectors.toList());
        }
    }

    private List<Resource> getArticleResources() {
        if (StringUtils.isBlank(filePath)) {
            return Collections.emptyList();
        }

        Resource artiRes = resourceResolver.getResource(filePath);

        return Lists.newArrayList(artiRes.getChildren()).stream().filter((item) -> {
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            Page page = pageManager.getPage(item.getPath());
            return page != null && page.hasContent() && page.getContentResource().getValueMap().get(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY).equals(NEWSPAGE_RESOURCE_TYPE);
        }).collect(Collectors.toList());
    }

    private void customizeArticles() {
        if(CollectionUtils.isNotEmpty(customAutoArti)) {
            for(Stage cArti : customAutoArti) {
                Stage arti = newsArticles.stream().filter(p -> StringUtils.isNotBlank(p.getPath()) && p.getPath().equals(cArti.getPath())).findFirst().orElse(null);
                if(arti == null) { continue; }
                
                arti.setCustomURL(cArti.getCustomURL());
                arti.setCategory(cArti.getCategory());
            }
        }

        if(StringUtils.isNotBlank(teaserType) && teaserType.equals("artiText")) {
            for(Stage artiStage : newsArticles) {
                String artiText = findArticleText(artiStage.getPath());

                if(StringUtils.isNotBlank(artiText)) {
                    artiStage.setText(artiText);
                }
            }
        }
    }





    private String findArticleText(String path) {
        if(StringUtils.isBlank(path)) { return StringUtils.EMPTY; }

        path = path.replaceAll(".html", "");

        Resource artiRes = resourceResolver.getResource(path);
        String artiText = StringUtils.EMPTY;

        if(artiRes != null) {
            Resource containerRes = artiRes.getChild(JcrConstants.JCR_CONTENT + "/root/container");
            
            if(containerRes != null) {
                for(Resource children : containerRes.getChildren()) {
                    if(children.isResourceType(TEXT_RESOURCE_TYPE)) {
                        artiText = (String) children.getValueMap().getOrDefault("text", StringUtils.EMPTY);
                        artiText = artiText.length() >= maxTeaserText ? artiText.substring(0, maxTeaserText) + "..." : artiText;
                        break;
                    }
                }
            }
        }
        
        return artiText;
    }

}