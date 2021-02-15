package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Model(adaptables = Resource.class,  defaultInjectionStrategy =  DefaultInjectionStrategy.OPTIONAL)
public class Stage{

    private static final String DATE_PATTERN = "dd.MM.yyy";

    @ValueMapValue(name = "headline")
    private String headline;

    @ValueMapValue(name ="category")
    private String category;

    @ValueMapValue(name = "date")
    private Date dateObject;

    @ValueMapValue(name = "cq:lastModified")
    private Date lastModifiedObject;

    @ValueMapValue(name = "text")
    private String text;

    private String date;

    @PostConstruct
    protected void init() {
        if(Objects.nonNull(dateObject)) {
            DateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
            date = formatter.format(dateObject);
        }
    }

    public Date getDateObject() {
        return dateObject;
    }

    public Date getLastModifiedObject() {
        return lastModifiedObject;
    }

    public String getHeadline() {
        return headline;
    }

    public String getText() {
        return text;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() { return date; }

}
