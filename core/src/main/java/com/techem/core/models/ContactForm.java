package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = "techem/components/contact-form")
public class ContactForm {

  @Inject
  @Named("salutationOptions")
  private List<ContactFormInput> options;

  @ValueMapValue(name = "endpointEmail")
  private String endpointEmail;

  @ValueMapValue(name = "title")
  private String title;

  @ValueMapValue(name = "shortTitle")
  private String shortTitle;

  @ValueMapValue(name = "salutationLabel")
  private String salutationLabel;

  @ValueMapValue(name = "nameLabel")
  private String nameLabel;

  @ValueMapValue(name = "phoneLabel")
  private String phoneLabel;

  @ValueMapValue(name = "mailLabel")
  private String mailLabel;

  @ValueMapValue(name = "friendlyCaptcha")
  private Boolean showFriendlyCaptcha;

  @ValueMapValue(name = "checkboxLabel")
  private String checkboxLabel;

  @ValueMapValue(name = "checkboxName")
  private String checkboxName;

  @ValueMapValue(name = "checkboxRequired")
  private Boolean checkboxRequired;

  @ValueMapValue(name = "successMessage")
  private String successMessage;

  @ValueMapValue(name = "submitLabel")
  private String submitLabel;

  public String getEndpointEmail() {
    return endpointEmail;
  }

  public String getTitle() {
    return title;
  }

  public String getShortTitle() {
    return shortTitle;
  }

  public String getSalutationLabel() {
    return salutationLabel;
  }

  public List<ContactFormInput> getOptions() {
    return options;
  }

  public String getNameLabel() {
    return nameLabel;
  }

  public String getPhoneLabel() {
    return phoneLabel;
  }

  public String getMailLabel() {
    return mailLabel;
  }

  public Boolean getShowFriendlyCaptcha() {
    return showFriendlyCaptcha;
  }

  public String getCheckboxLabel() {
    return checkboxLabel;
  }

  public String getCheckboxName() {
    return checkboxName;
  }

  public Boolean getCheckboxRequired() {
    return checkboxRequired;
  }

  public String getSuccessMessage() {
    return successMessage;
  }

  public String getSubmitLabel() {
    return submitLabel;
  }

}