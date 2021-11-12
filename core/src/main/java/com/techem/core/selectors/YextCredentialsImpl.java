package com.techem.core.selectors;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Designate(ocd = YextCredentialsImpl.Config.class)
@Component(service = YextCredentialsImpl.class, configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true)
public class YextCredentialsImpl implements YextCredentials
{
    @ObjectClassDefinition(name = "Credentials", description = "Get Credentials")
    public @interface Config
    {
        @AttributeDefinition(name = "Credentials", description = "Get Credentials")
        String credentials() default StringUtils.EMPTY;
        String redirectURL() default StringUtils.EMPTY;
    }
    private YextCredentialsImpl.Config config;
    @Activate
    @Modified
    public void activate(final YextCredentialsImpl.Config config)
    {
        this.config = config;
    }
    @Deactivate
    public void deactivate()
    {
    }

    @Override
    public String getCredentials()
    {
        return this.config.credentials();
    }

    @Override
    public String getRedirectURL()
    {
        return this.config.redirectURL();
    }
}