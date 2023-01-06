package com.techem.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@ExtendWith(AemContextExtension.class)
class NewshubContactTest {

    private final AemContext ctx = new AemContext();

    private NewshubContact newshubContact;

    private Resource resource;

    @Test
    void shouldReturnNewsHubContact() {

        resource = ctx.load().json("/com/techem/core/models/NewshubContact.json", "/newshub-contact");

        newshubContact = resource.adaptTo(NewshubContact.class);

        assertThat(newshubContact, notNullValue());

        assertThat(newshubContact.getHeadline(), is("Headline"));
        assertThat(newshubContact.getBackgroundColor(), is("white"));
        assertThat(newshubContact.getIsVariation(), is(true));
        assertThat(newshubContact.getContacts(),hasSize(1));
        assertThat(newshubContact.getContacts().get(0).getContactName(), is("Name"));
        assertThat(newshubContact.getContacts().get(0).getContactDescription(), is("Description"));
        assertThat(newshubContact.getContacts().get(0).getContactImageReference(), is("Image Reference"));
        assertThat(newshubContact.getContacts().get(0).getDownloadItems(), nullValue());

    }

    @Test
    void shouldReturnNewsHubContacNoVariation() {

        resource = ctx.load().json("/com/techem/core/models/NewshubContact-noVariation.json", "/newshub-contact");

        newshubContact = resource.adaptTo(NewshubContact.class);

        assertThat(newshubContact, notNullValue());

        assertThat(newshubContact.getHeadline(), is("Headline"));
        assertThat(newshubContact.getBackgroundColor(), is("white"));
        assertThat(newshubContact.getIsVariation(), is(false));
        assertThat(newshubContact.getContacts(),hasSize(3));

    }

    @Test
    void shouldNotFailWhenNoContacts() {
        resource = ctx.create().resource("/newshub-contact");

        newshubContact = resource.adaptTo(NewshubContact.class);

        assertThat(newshubContact, notNullValue());
        assertThat(newshubContact.getIsVariation(), is(false));
    }

}