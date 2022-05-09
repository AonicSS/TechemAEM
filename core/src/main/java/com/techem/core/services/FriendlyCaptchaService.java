package com.techem.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


public interface FriendlyCaptchaService {

    public static final String FCC_SECRET = "secret";
    public static final String FCC_SITEKEY = "sitekey";
    public static final String FCC_SOLUTION = "solution";
    public static final String FCC_SUCCESS = "success";
    public static final String FCC_ERRORS = "errors";
    public static final String FC_LANG_PATH = "/apps/techem/components/form/friendly-captcha/fc_lang_data.json";
    public static final String FC_LANG_LIST = "fcLangList";
    public static final String FC_ENABLED = ":captchaEnabled";
    public static final String FC_SOLUTION_PARAM = ":captchaSol";
    public static final String FC_TOKEN = ":captchaToken";

    @ObjectClassDefinition(
        name = "Techem FriendlyCaptcha Service Config", 
		description = "Configure the FriendlyCaptcha.")
    public @interface FriendlyCaptchaConfig {

        @AttributeDefinition(
            name = "API EndPoint", 
            description = "Friendly Captcha API Endpoint.", 
            type = AttributeType.STRING)
        public String apiEndpoint();

        @AttributeDefinition(
            name = "API Secret", 
            description = "Friendly Captcha API Secret Key.", 
            type = AttributeType.STRING)
        public String apiSecret();

        @AttributeDefinition(
            name = "API Site Key", 
            description = "Friendly Captcha API Site Key.", 
            type = AttributeType.STRING)
        public String apiSiteKey();
    }

    public FriendlyCaptchaConfig getConfig();
    public boolean validateCaptcha(String solutionStr);
    public boolean validateCaptchaToken(String soluString, String captchaToken);
}