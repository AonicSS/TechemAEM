(function ($, $document) {
    "use strict";
  
    var selectors = {
      MULTIFIELD: ".video_multifield",
      MULTIFIELD_ITEM: "coral-multifield-item",
      MULTIFIELD_MAX_ITEMS: "data-max-items",
      REMOVE_BUTTON: "button[coral-multifield-remove]",
      ADD_BUTTON: "button[coral-multifield-add]",
    };
  
    var attributes = {
      DISABLED: "disabled",
      FOUNDATION_UI: "foundation-ui",
    };
  
    function setMaxItem(add) {
      var $multifield = $(selectors.MULTIFIELD);
      var $multifieldItems = $multifield.find(selectors.MULTIFIELD_ITEM);
      var maxItems = $multifield.attr(selectors.MULTIFIELD_MAX_ITEMS);
      var $addButton = $multifield.find(selectors.ADD_BUTTON);
      var ui = $(window).adaptTo(attributes.FOUNDATION_UI);
      var maxSize = maxItems && parseInt(maxItems);
      var numberOfItems;
  
      if (add === true) {
        numberOfItems = $multifieldItems.length;
      } else {
        numberOfItems = $multifieldItems.length - 1;
      }
  
      if (numberOfItems >= maxSize) {
        ui.alert(
          "Warning",
          "Maximum " + maxSize + " items are allowed",
          "notice"
        );
        $addButton.attr(attributes.DISABLED, true);
        return false;
      } else {
        $addButton.attr(attributes.DISABLED, false);
      }
    }
  
    function removeItem() {
      var $removeButtons = $(selectors.MULTIFIELD).find(selectors.REMOVE_BUTTON);
      $removeButtons.on("click", function () {
        setMaxItem(false);
      });
    }
  
    $document.on("dialog-ready", function (e) {
      if ($("div[data-component-name='newshub-video']").length) {
        e.stopPropagation();
        e.preventDefault();
  
        var $addButton = $(selectors.MULTIFIELD).find(selectors.ADD_BUTTON);
  
        $addButton.on("click", function () {
          setMaxItem(true);
          removeItem();
        });
        removeItem();
      }
    });
  })($, $(document));