package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactFormInput {

  @ValueMapValue(name = "inputLabel")
  private String inputLabel;

  @ValueMapValue(name = "inputValue")
  private String inputValue;

  public String getInputLabel() {
    return inputLabel;
  }

  public String getInputValue() {
    return inputValue;
  }
}