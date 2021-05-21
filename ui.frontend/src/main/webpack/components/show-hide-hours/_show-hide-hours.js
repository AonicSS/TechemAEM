(function($) {
  "use strict";

  function initShowHideHours(el) {
    const $el = $(el);
    const date = new Date();
    const isAuthor = $el.hasClass('author-mode');

    if (isAuthor) {
      return;
    }

    const startTime = $el.data('start-time');
    const endTime = $el.data('end-time');
    const startTimeHours = startTime.split(':')[0];
    const startTimeMinutes = startTime.split(':')[1];
    const endTimeHours = endTime.split(':')[0];
    const endTimeMinutes = endTime.split(':')[1];

    // convert to full minutes in order to compare
    const start = parseInt(startTimeHours) * 60 + parseInt(startTimeMinutes);
    const end = parseInt(endTimeHours) * 60 + parseInt(endTimeMinutes);
    const now = date.getHours() * 60 + date.getMinutes();

    if (start <= now && now <= end) {
      $el.removeClass('hidden');
    } else {
      $el.parent().remove();
    }
  }

  $(document).ready(function() {
    const $cmp = $("div[data-component-name='show-hide-hours']");
    Array.prototype.forEach.call($cmp, function(element) {
      if (element && element.dataset.initialized !== "true") {
        initShowHideHours(element);
      }
    });
  });
}(jQuery));
