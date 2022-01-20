(function ($, Coral) {
    "use strict";

    const registry = $(window).adaptTo("foundation-registry");

    if(registry != null) {
        registry.register("foundation.validation.validator", {
            selector: "[data-foundation-validation^='techem-validate-multifield']",
            validate: function(el) {
                const min = $(el).data("min");
                const max = $(el).data("max");
                const addItemsBtn = $(el).find("button[coral-multifield-add]");
                const isRequired = $(el).attr("aria-required") == "true";
                var currentItems = $(el).children("coral-multifield-item").length;

                if(isRequired) {
                    if(currentItems >= max) {
                        $(addItemsBtn).hide();
                    }else {
                        $(addItemsBtn).show();
                    }

                    if(currentItems < min) {
                        return "You must add at least " + min + " item" + (min > 1 ? "s" : "") + "!";
                    }

                    if(currentItems > max) {
                        return "You have reached the maximum number of items! Please remove " + (currentItems - max) + " item" + ((currentItems - max) > 1 ? "s" : "");
                    }
                }
            }
        });
    }
})(jQuery, Coral);