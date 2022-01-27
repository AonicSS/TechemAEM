package com.techem.core.models;

import com.day.cq.wcm.api.NameConstants;
import com.day.crx.JcrConstants;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Model(adaptables = Resource.class, adapters = NewshubListContainer.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/newshub-list")
public class NewshubListContainer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private List<Stage> articleListManually;

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(name="containerHeadline")
    private String containerHeadline;

    @ValueMapValue(name="buttonLabel")
    private String buttonLabel;

    @ValueMapValue(name="buttonLabelLink")
    private String buttonLabelLink;

    @ValueMapValue(name="buttonLink")
    private String buttonLink;

    @ValueMapValue(name="insertionType")
    private String insertionType;

    @ValueMapValue(name="orderBy")
    private String orderBy;

    @ValueMapValue(name="sortOrder")
    private String sortOrder;

    @ValueMapValue(name="maxArticles")
    private int maxArticles;
    
    @ValueMapValue(name="filePath")
    private String filePath;

    @ValueMapValue(name="filteringTags")
    private String[] filteringTags;

    @ValueMapValue(name="buttonType")
    private String buttonType;

    @ValueMapValue(name="artisToAdd")
    private int artisToAdd;

    @ValueMapValue(name="initialAmount")
    private int initialAmount;

    private List<Stage> newsArticles;

    private List<Resource> newsItems;

    @PostConstruct
    protected void init() {
        if (insertionType != null && !insertionType.isEmpty()) {
            if (insertionType.equals("automatically")) {
                newsItems = Lists.newArrayList(getArticleResources()).stream().filter((item) -> {
                    return item.getValueMap().get(JcrConstants.JCR_PRIMARYTYPE).equals(NameConstants.NT_PAGE)
                            && item.getChild(JcrConstants.JCR_CONTENT).adaptTo(ValueMap.class).get("sling:resourceType")
                            .equals("techem/components/structure/news-page");
                }).collect(Collectors.toList());
                filterByTags();
                orderList();
            }
            
            if(insertionType.equals("manually")) {
                if(articleListManually != null && articleListManually.size() > 0) {
                    for(Stage arti : articleListManually) {
                        Resource originArti = resourceResolver.getResource(arti.getPath().replace(".html", ""));

                        if(originArti == null) { continue; }

                        ValueMap artiVals = originArti.getChild(JcrConstants.JCR_CONTENT).getValueMap();

                        if(arti.getHeadline() == null) {
                            arti.setHeadline((String) artiVals.get("headline"));
                        }

                        if(arti.getCategory() == null) {
                            arti.setCategory((String) artiVals.get("category"));
                        }

                        if(arti.getDateObject() == null) {
                            GregorianCalendar calendar = (GregorianCalendar) artiVals.get("date");
                            arti.setDateObj(calendar.getTime());
                        }

                        if(arti.getText() == null) {
                            arti.setText((String) artiVals.get("text"));
                        }
                    }
                }
            }
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
        if(orderBy != null && sortOrder != null) {            
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

    private Iterator<Resource> getArticleResources() {
        if (filePath == null || filePath.isEmpty()) {
            return Collections.emptyIterator();
        }

        Resource resource = null;

        try {
            resource = resourceResolver.getResource(filePath);
        } catch (Exception ex) {
            logger.error("Could not fetch resource. Ex: ", ex);
        }

        if (resource == null) {
            return Collections.emptyIterator();
        }

        return resource.listChildren();
    }

    public List<Stage> getArticleListManually() {
        return articleListManually;
    }

    public String getContainerHeadline() {
        return containerHeadline;
    }

    public String buttonLabelLink() {
        return buttonLabelLink;
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public String getButtonType() {
        return buttonType;
    }

    public int getArtisToAdd() {
        return artisToAdd;
    }

    public String getButtonLink() {
        return buttonLink;
    }

    public String getInsertionType() {
        return insertionType;
    }

    public int getMaxArticles() {
        return maxArticles;
    }

    public int getInitialAmount() {
        return initialAmount;
    }

    public String getFilePath() {
        return filePath;
    }

    public String[] getFilteringTags() {
        return filteringTags;
    }

    public List<Stage> getNewsArticles() {
        return newsArticles;
    }

}