package com.techem.core.selectors;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Designate(ocd = TrengoChatImpl.Config.class)
@Component(service = TrengoChat.class, configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true)
public class TrengoChatImpl implements TrengoChat
{
    @ObjectClassDefinition(name = "API Key", description = "Get API key for trengo chat")
    public @interface Config
    {
        @AttributeDefinition(name = "API Key", description = "Get API key for trengo chat")
        String apiKey() default StringUtils.EMPTY;
    }
    private TrengoChatImpl.Config config;
    @Activate
    @Modified
    public void activate(final TrengoChatImpl.Config config)
    {
        this.config = config;
    }
    @Deactivate
    public void deactivate()
    {
    }
    @Override
    public String getAPIKey()
    {
        return this.config.apiKey();
    }
}