package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, adapters=ResetPassword.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = "techem/components/reset-password")
public class ResetPassword {

    @ValueMapValue(name = "headline")
    private String headline;

    @ValueMapValue(name = "subHeading")
    private String subHeading;

    @ValueMapValue(name = "description")
    private String description;

    @ValueMapValue(name = "inputLabel")
    private String inputLabel;

    @ValueMapValue(name = "submitLabel")
    private String submitLabel;

    @ValueMapValue(name = "returnLink")
    private String returnLink;

    @ValueMapValue(name = "returnLinkText")
    private String returnLinkText;

    @ValueMapValue(name = "endpointURL")
    private String endpointURL;

    @ValueMapValue(name = "mailSentMessage")
    private String mailSentMessage;

    @ValueMapValue(name = "setPasswordLocation")
    private String setPasswordLocation;

    @ValueMapValue(name = "mailErrorMessage")
    private String mailErrorMessage;

    public String getMailErrorMessage() {
        return mailErrorMessage;
    }

    public String getHeadline() {
        return headline;
    }

    public String getDescription() {
        return description;
    }

    public String getInputLabel() {
        return inputLabel;
    }

    public String getSubmitLabel() {
        return submitLabel;
    }

    public String getReturnLink() {
        return returnLink;
    }

    public String getSubHeading() {
        return subHeading;
    }

    public String getReturnLinkText() {
        return returnLinkText;
    }

    public String getEndpointURL() {
        return endpointURL;
    }

    public String getMailSentMessage() {
        return mailSentMessage;
    }

    public String getSetPasswordLocation() {
        return setPasswordLocation;
    }
}
