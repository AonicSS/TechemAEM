(function ($, $document) {
    "use strict";

     var selectors = {
         MULTIFILD: ".kefacts_multifild",
         MULTIFILD_ITEM: "coral-multifield-item",
         MULTIFIELD_MAX_ITEMS: "data-max-items",
         REMOVE_BUTTON: "button[coral-multifield-remove]",
         ADD_BUTTON: "button[coral-multifield-add]",
     };

     var attributes = {
          DISABLED: "disabled",
          FOUNDATION_UI: "foundation-ui"
     };

     $document.on("dialog-ready", function(e) {
        e.stopPropagation();
        e.preventDefault();

        var $addButton = $(selectors.MULTIFILD).find(selectors.ADD_BUTTON);

        $addButton.on('click', function(e) {
            setMaxItem(true);
            removeItem();
        });
        removeItem();
     });

    function setMaxItem(add) {
        var $multifield = $(selectors.MULTIFILD);
        var $multifield_items = $multifield.find(selectors.MULTIFILD_ITEM);
        var max_items = $multifield.attr(selectors.MULTIFIELD_MAX_ITEMS);
        var $addButton = $multifield.find(selectors.ADD_BUTTON);
        var ui = $(window).adaptTo(attributes.FOUNDATION_UI);
        var maxSize = max_items && parseInt(max_items);
        var numberOfItems;

        if(add === true) {
            numberOfItems = $multifield_items.length;
        } else {
            numberOfItems = $multifield_items.length - 1
        }

        if(numberOfItems >= maxSize) {
            ui.alert("Warning", "Maximum " +  maxSize + " items are allowed", "notice");
            $addButton.attr(attributes.DISABLED, true);
            return false;
        } else {
            $addButton.attr(attributes.DISABLED, false);
        }
    };


    function removeItem() {
        var $removeButtons =  $(selectors.MULTIFILD).find(selectors.REMOVE_BUTTON);
        $removeButtons.on("click", function() {
            setMaxItem(false);
        });
    };
})($, $(document));