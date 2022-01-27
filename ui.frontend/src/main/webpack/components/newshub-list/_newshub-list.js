(function ($) {
    "use strict";
  
    
      function showMore (el) {
          const listContainer = $(el).find(".cmp-newshub-list__container");
          const loadMoreBtn = $(el).find(".button-loadmore");
          const initialAmount = $(el).data("initial");
          const maxAmount = $(el).data("max");
          const increaseBy = $(el).data("increase-by");
          var visibleElements = $(listContainer).find(".cmp-newshub-list-item-visible");
          var hiddenElements = $(listContainer).find(".cmp-newshub-list-item-hidden");
          var visibleElementsLen = $(visibleElements).length;
  
          if(visibleElementsLen == 0) {
              for (let i = 0; i < initialAmount; i++) {
                  $(hiddenElements[i]).removeClass("cmp-newshub-list-item-hidden").addClass("cmp-newshub-list-item-visible");
                  visibleElementsLen++;
              }
          }else {
              for (let i = 0; (i < increaseBy) && (visibleElementsLen <= maxAmount); i++) {
                  $(hiddenElements[i]).removeClass("cmp-newshub-list-item-hidden").addClass("cmp-newshub-list-item-visible");
                  visibleElementsLen++;
              }
          }
  
          if(visibleElementsLen >= maxAmount) {
              $(loadMoreBtn).hide();
          }
      }
  
      function init(el) {
          const listContainer = $(el).find(".cmp-newshub-list__container");
          const loadMoreBtn = $(el).find(".button-loadmore");
          
          if($(loadMoreBtn).length > 0 && $(listContainer).length > 0) {
  
              showMore(el);
  
              $(loadMoreBtn).on("click", function () {
                  showMore(el);
              });
          }
      }
  
  
        $(document).ready(function () {
          const $newsList = $("div[data-component-name='newshub-list']");
          Array.prototype.forEach.call($newsList, function (element) {
              if (element && element.dataset.initialized !== "true") {
                  init(element);
              }
          });
      });
  })(jQuery);