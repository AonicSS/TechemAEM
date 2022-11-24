import { WidgetInstance } from "friendly-challenge";
(function($) {
    "use strict";

    function FCaptcha(el) {
        el.dataset.initialized = true;

        this.fcEl = $(el);

        this.constants = {
            fCaptchaServletURL: "/eu/techem/friendlycaptcha",
            fCaptchaToken: ":captchaToken",
            fCaptchaSol: ":captchaSol"
        };

        this.selectors = {
            fCaptcha: ".friendly-captcha",
            parentForm: "form",
            errorMsg: ".error-friendly-cmp",
            submitButton: "button[type='submit']",
            fCaptchaLangData: "fc-lang",
            fCaptchaTknField: "input[name='" + this.constants.fCaptchaToken + "']"
        };

        this.init = function() {
            this.initFCForm();
            this.initFormSubmissionHandler();
            this.initFormChangeHandler();
        }.bind(this);

        this.initFCForm = function() {
            var parentForm = this.fcEl.parents(this.selectors.parentForm).first();
            
            if(!parentForm) { return; }
            
            var widgetEl = el.querySelector(this.selectors.fCaptcha);
            var formSubmitBtn = parentForm.find(this.selectors.submitButton);

            var skipButton = el.hasAttribute("data-skip-button");
            
            if(!widgetEl) { return; }
            
            var widgetLang = $(widgetEl).data(this.selectors.fCaptchaLangData);

            const options = {
                solutionFieldName: this.constants.fCaptchaSol,
                doneCallback: this.fCaptchaCallback,
                language: widgetLang || "en"
            };

            this.widget = new WidgetInstance(widgetEl, options);
            /* 
                Adding the 'frc-captcha' class directly to the element will auto-generate the captcha.
                We don't want that, so instead add it after creating our widget.
            */
            $(widgetEl).addClass("frc-captcha");
            if (!skipButton) {
                $(formSubmitBtn).attr("disabled", true);
            }
        }.bind(this);

        this.fCaptchaCallback = function(solution) {

            if(!solution) { 
                return;
            }

            var parentForm = this.fcEl.parents(this.selectors.parentForm).first();
            var fCaptchaTknInput = parentForm.find(this.selectors.fCaptchaTknField);
            var errMsg = parentForm.find(this.selectors.errorMsg);
            var formSubmitBtn = parentForm.find(this.selectors.submitButton);
            const siteKey = this.widget.opts.sitekey;

            $.post(this.constants.fCaptchaServletURL, { solution: solution })
            .done(() => {
                if(this.isFormValid()) {
                    $(formSubmitBtn).removeAttr("disabled");
                }

                if(fCaptchaTknInput) {
                    fCaptchaTknInput.remove();
                }

                $("<input>").attr({
                    type: "hidden",
                    name: this.constants.fCaptchaToken,
                    value: siteKey + solution
                }).appendTo(parentForm);
            }).fail(function() {
                $(errMsg).show();
            });
        }.bind(this);

        this.initFormSubmissionHandler = function() {
            var parentForm = this.fcEl.parents(this.selectors.parentForm).first();
            if(!parentForm || !this.widget) { return; }
            var skipButton = el.hasAttribute("data-skip-button");

            $(parentForm).on("submit", (e) => {
                var formSubmitBtn = parentForm.find(this.selectors.submitButton);
                if (!skipButton) {
                    $(formSubmitBtn).attr("disabled", true);
                }
                
                if(!this.widget.valid) {
                    e.preventDefault();
                    return false;
                }

                this.widget.valid = false;
                this.widget.reset();
            });
        }.bind(this);

        this.initFormChangeHandler = function() {
            var parentForm = this.fcEl.parents(this.selectors.parentForm).first();
            var skipButton = el.hasAttribute("data-skip-button");

            if(!parentForm || !this.widget) { return; }

            $(parentForm).find(":input").on("keyup change paste", () => {
                var formSubmitBtn = parentForm.find(this.selectors.submitButton);

                if(this.isFormValid() && this.widget.valid) {
                    $(formSubmitBtn).removeAttr("disabled");
                }else if (!skipButton) {
                    $(formSubmitBtn).attr("disabled", true);
                }
            });
        }.bind(this);

        this.isFormValid = function() {
            var parentForm = this.fcEl.parents(this.selectors.parentForm).first();

            return !parentForm.find(":input[required]").filter(function() {
                return !$(this).prop("disabled") && (this.type != "checkbox" ? !this.value : !this.checked);
            }).length;
        }.bind(this);

        this.init();

        return this;
    }

    $(document).ready(function() {
        var fCaptcha = $("[data-component-name='friendly-captcha']");
        Array.prototype.forEach.call(fCaptcha, function(element) {
            if (element && element.dataset.initialized !== "true") {
                new FCaptcha(element);
            }
        });
    });
}(jQuery));