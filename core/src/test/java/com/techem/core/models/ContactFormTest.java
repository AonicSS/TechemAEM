package com.techem.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AemContextExtension.class)
public class ContactFormTest {

    private final AemContext ctx = new AemContext();

    private ContactForm contactForm;

    private Resource resource;

    @BeforeEach
    void setup() {
        resource = ctx.load().json("/com/techem/core/models/ContactForm.json", "/techem/components/contact-form");
    }

    @Test
    public void shouldReturnContactForm() {

        contactForm = resource.adaptTo(ContactForm.class);

        assertNotNull(contactForm);

        assertThat(contactForm.getTitle(), is("Title"));
        assertThat(contactForm.getShortTitle(), is("Short Title"));
        assertThat(contactForm.getNameLabel(), is("Name Label"));
        assertThat(contactForm.getCheckboxLabel(), is("Checkbox Label"));
        assertThat(contactForm.getCheckboxName(), is("Checkbox Name"));
        assertThat(contactForm.getCheckboxRequired(), is(true));
        assertThat(contactForm.getShowFriendlyCaptcha(), is(true));
        assertThat(contactForm.getSuccessMessage(), is("Success Message"));
        assertThat(contactForm.getEndpointEmail(), is("email@email.com"));
        assertThat(contactForm.getMailLabel(), is("Email Label"));
        assertThat(contactForm.getPhoneLabel(), is("Phone Label"));
        assertThat(contactForm.getSalutationLabel(), is("Salutation Label"));
        assertThat(contactForm.getSubmitLabel(), is("Submit"));
        assertThat(contactForm.getOptions(), notNullValue());
        assertThat(contactForm.getOptions(), hasSize(4));
        contactForm.getOptions().forEach(option -> {
            assertThat(option.getInputLabel(), notNullValue());
            assertThat(option.getInputValue(), notNullValue());
        });
    }
}