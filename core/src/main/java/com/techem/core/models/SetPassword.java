package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, adapters= SetPassword.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = "techem/components/set-password")
public class SetPassword {

    @ValueMapValue(name = "headline")
    private String headline;

    @ValueMapValue(name = "subHeading")
    private String subHeading;

    @ValueMapValue(name = "description")
    private String description;

    @ValueMapValue(name = "passwordInputLabel")
    private String passwordInputLabel;

    @ValueMapValue(name = "confirmInputLabel")
    private String confirmInputLabel;

    @ValueMapValue(name = "submitLabel")
    private String submitLabel;

    @ValueMapValue(name = "loginLink")
    private String loginLink;

    @ValueMapValue(name = "loginLabel")
    private String loginLabel;

    @ValueMapValue(name = "endpointURL")
    private String endpointURL;

    @ValueMapValue(name = "successMsg")
    private String successMsg;

    @ValueMapValue(name = "invalidError")
    private String invalidError;

    @ValueMapValue(name = "patternMatchError")
    private String patternMatchError;

    public String getHeadline() {
        return headline;
    }

    public String getDescription() {
        return description;
    }

    public String getSubmitLabel() {
        return submitLabel;
    }

    public String getSubHeading() {
        return subHeading;
    }

    public String getEndpointURL() {
        return endpointURL;
    }

    public String getLoginLink() {
        return loginLink;
    }

    public String getLoginLabel() {
        return loginLabel;
    }

    public String getPasswordInputLabel() {
        return passwordInputLabel;
    }

    public String getConfirmInputLabel() {
        return confirmInputLabel;
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public String getInvalidError() {
        return invalidError;
    }

    public String getPatternMatchError() {
        return patternMatchError;
    }
}
