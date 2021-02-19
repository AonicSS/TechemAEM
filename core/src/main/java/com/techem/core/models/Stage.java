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

import static com.day.cq.wcm.api.NameConstants.PN_PAGE_LAST_MOD;

@Model(adaptables = Resource.class,  defaultInjectionStrategy =  DefaultInjectionStrategy.OPTIONAL)
public class Stage{

    private static final String DATE_PATTERN = "dd.MM.yyy";

    @ValueMapValue(name = "headline")
    private String headline;

    @ValueMapValue(name ="category")
    private String category;

    @ValueMapValue(name = "date")
    private Date dateObject;

    @ValueMapValue(name = PN_PAGE_LAST_MOD)
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

    public Date getDateObject() { return Objects.nonNull(dateObject) ? (Date) dateObject.clone() : null; }

    public Date getLastModifiedObject() { return  Objects.nonNull(lastModifiedObject) ? (Date) lastModifiedObject.clone() : null;}

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
