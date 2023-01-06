package com.techem.core.models;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.Page;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Model(adaptables = Resource.class, adapters = NewshubSimilarArticles.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/newshub-similar-articles")
public class NewshubSimilarArticles {

    @Getter
    @Inject
    private List<Stage> articleListManually;

    @Inject
    private List<Resource> additionalPaths;

    @SlingObject
    private ResourceResolver resourceResolver;

    @Getter
    @ValueMapValue(name="containerHeadline")
    private String containerHeadline;

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

    @Getter
    private List<Stage> newsArticles;

    private List<Resource> newsItems;

    private static final String NEWSPAGE_RESOURCE_TYPE = "techem/components/structure/news-page";

    @PostConstruct
    protected void init() {
        if (StringUtils.isNotBlank(insertionType) && insertionType.equals("automatically")) {
            newsItems = getArticleResources();
            filterByTags();
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
                    newsArticles.sort(Comparator.comparing(o -> ((Stage) o).getHeadline().toLowerCase().replaceAll("\\s+", "")));
                break;
                case "artiCategory": 
                    newsArticles.sort(Comparator.comparing(o -> ((Stage) o).getCategory().toLowerCase().replaceAll("\\s+", "")));
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

        List<String> paths = CollectionUtils.isNotEmpty(getAdditionalPaths()) ? getAdditionalPaths() : new ArrayList<>();
        if(!paths.contains(filePath)) { paths.add(filePath); }
        
        List<Resource> artiResources = new ArrayList<>();

        for(String p : paths) {
            Resource artiRes = resourceResolver.getResource(p);

            if(artiRes != null && artiRes.hasChildren()) {
                List<Resource> tempChildren = Lists.newArrayList(artiRes.getChildren()).stream().filter((item) -> {
                    PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
                    Page page = pageManager.getPage(item.getPath());
                    return page != null && page.hasContent() && page.getContentResource().getValueMap().get(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY).equals(NEWSPAGE_RESOURCE_TYPE);
                }).collect(Collectors.toList());

                artiResources = ListUtils.union(artiResources, tempChildren);
            }
        }

        return artiResources;
    }


    public List<String> getAdditionalPaths() {
        if(CollectionUtils.isEmpty(additionalPaths)) { return Collections.emptyList(); }

        List<String> paths = new ArrayList<>();

        for(Resource res : additionalPaths) {
            String p = (String) res.getValueMap().get("path");
            if(StringUtils.isNotBlank(p) && !paths.contains(p)) {
                paths.add(p);
            }
        }

        return paths;
    }

}