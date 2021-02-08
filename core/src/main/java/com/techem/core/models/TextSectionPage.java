package com.techem.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Model(adaptables = SlingHttpServletRequest.class)
public class TextSectionPage {

    @ScriptVariable
    private Page currentPage;

    private SectionPage sectionPage;

    @PostConstruct
    protected void init() {
        if(Objects.nonNull(currentPage)) {
            sectionPage = currentPage.getContentResource().adaptTo(SectionPage.class);
        }
    }

    public SectionPage getSectionPage() {
        return sectionPage;
    }
}