package com.techem.core.models;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Getter
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = "techem/components/contact-form")
public class ContactForm {

  @ChildResource(name = "salutationOptions")
  private List<ContactFormInput> options;

  @ValueMapValue
  private String endpointEmail;

  @ValueMapValue
  private String title;

  @ValueMapValue
  private String shortTitle;

  @ValueMapValue
  private String salutationLabel;

  @ValueMapValue
  private String nameLabel;

  @ValueMapValue
  private String phoneLabel;

  @ValueMapValue
  private String mailLabel;

  @ValueMapValue(name = "friendlyCaptcha")
  private Boolean showFriendlyCaptcha;

  @ValueMapValue
  private String checkboxLabel;

  @ValueMapValue
  private String checkboxName;

  @ValueMapValue
  private Boolean checkboxRequired;

  @ValueMapValue(name = "successMessage")
  private String successMessage;

  @ValueMapValue(name = "submitLabel")
  private String submitLabel;

}