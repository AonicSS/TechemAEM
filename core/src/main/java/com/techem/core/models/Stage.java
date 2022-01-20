package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

import com.day.cq.commons.jcr.JcrConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static com.day.cq.wcm.api.NameConstants.PN_PAGE_LAST_MOD;

@Model(adaptables = Resource.class,  defaultInjectionStrategy =  DefaultInjectionStrategy.OPTIONAL)
public class Stage {

    private static final String DATE_PATTERN = "dd.MM.yyy";

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(name = "headline")
    private String headline;

    @ValueMapValue(name ="category")
    private String category;

    @ValueMapValue(name = "date")
    private Date dateObject;

    @ValueMapValue(name = PN_PAGE_LAST_MOD)
    private Date lastModifiedObject;

    @ValueMapValue(name = JcrConstants.JCR_CREATED)
    private Date createdDate;

    @ValueMapValue(name = "path")
    private String path;

    @ValueMapValue(name = "text")
    private String text;

    @ValueMapValue(name = "image")
    private String image;

    private String date;

    @PostConstruct
    protected void init() {
        if(Objects.nonNull(dateObject)) {
            DateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
            date = formatter.format(dateObject);
        }
    }

    public Date getDateObject() { return Objects.nonNull(dateObject) ? (Date) dateObject.clone() : null; }

    public Date getLastModifiedObject() { return  Objects.nonNull(lastModifiedObject) ? (Date) lastModifiedObject.clone() : null;}

    public Date getCreatedDate() { return  Objects.nonNull(createdDate) ? (Date) createdDate : null; }

    public String getHeadline() {
        return headline;
    }

    public String getText() {
        return text;
    }

    public String getCategory() {
        return category;
    }

    public String getPath() {
        if (path != null) {
            Resource pathResource = resourceResolver.getResource(path);
            // check if resource exists and link is internal
            if (pathResource != null) {
                path = path + ".html";
            }
        }
        return path;
    }

    public void setPath(String p) {
        path = p;
    }

    public void setText(String t) {
        text = t;
    }

    public void setHeadline(String headLine) {
        if(headLine != null && !(headLine.trim().length() == 0)) { headline = headLine; }
    }

    public void setCategory(String categoryStr) {
        if(categoryStr != null && !(categoryStr.trim().length() == 0)) { category = categoryStr; }
    }

    public void setDateObj(Object dateObj) {
        if(dateObj != null) {
            DateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
            date = formatter.format(dateObj);
        }
    }

    public String getDate() { return date; }

    public String getImage() { return image; }
}