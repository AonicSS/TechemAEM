package com.techem.core.selectors;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Designate(ocd = EnvironmentSelectorImpl.Config.class)
@Component(service = EnvironmentSelector.class, configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true)
public class EnvironmentSelectorImpl implements EnvironmentSelector
{

    private static final String AUTHOR_MODE = "author";

    @ObjectClassDefinition(name = "Environment", description = "Get run mode environment")
    public @interface Config
    {
        @AttributeDefinition(name = "Environment", description = "Get run mode environment")
        String environment() default StringUtils.EMPTY;

        @AttributeDefinition(name = "Environment mode", description = "Environment mode, author or publish")
        String mode() default AUTHOR_MODE;
    }
    private EnvironmentSelectorImpl.Config config;
    @Activate
    @Modified
    public void activate(final EnvironmentSelectorImpl.Config config)
    {
        this.config = config;
    }
    @Deactivate
    public void deactivate()
    {
    }
    @Override
    public String getEnvironment()
    {
        return this.config.environment();
    }

    @Override
    public boolean isAuthor() {
        return StringUtils.equalsIgnoreCase(this.config.mode(), AUTHOR_MODE);
    }


}