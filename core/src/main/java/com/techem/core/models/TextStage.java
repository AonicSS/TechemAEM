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
public class TextStage {

    private Logger logger = LoggerFactory.getLogger(TextStage.class);

    @ScriptVariable(name="currentPage")
    private Page currentPage;

    private Stage stage;

    @PostConstruct
    protected void init() {
        if(Objects.nonNull(currentPage)) {
            stage = currentPage.getContentResource().adaptTo(Stage.class);
        }
    }

    public Stage getStage() {
        return stage;
    }
}
