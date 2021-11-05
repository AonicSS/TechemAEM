package com.techem.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Model(adaptables = SlingHttpServletRequest.class)
public class NewshubStage {

    private Logger logger = LoggerFactory.getLogger(NewshubStage.class);

    @ScriptVariable(name="currentPage")
    private Page currentPage;

    private NewshubStageContentPage newshubStageContentPage;

    @PostConstruct
    protected void init() {
        if(Objects.nonNull(currentPage)) {
            newshubStageContentPage = currentPage.getContentResource().adaptTo(NewshubStageContentPage.class);
        }
    }

    public NewshubStageContentPage getNewshubStageContentPage() {
        return newshubStageContentPage;
    }
}