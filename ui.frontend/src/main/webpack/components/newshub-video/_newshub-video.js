import Swiper from "swiper";

(function ($) {
  "use strict";

  function initSwiper(el) {
    const swiperOptions = {
      slidesPerView: "auto",
      observer: true,
      observeSlideChildren: true,
      observeParents: true,
      grabCursor: true,
      spaceBetween: 40,
      slidesOffsetAfter: 100,

      breakpoints: {
        1025: {
          spaceBetween: 100,
          slidesOffsetAfter: 280,
        },

        768: {
          spaceBetween: 60,
          slidesOffsetAfter: 160,
        },
      },
    };

    const swiperContainer = el.querySelector(
      ".cmp-newshub-video__article-container"
    );
    const swiper = new Swiper(swiperContainer, swiperOptions);
  }

  $(document).ready(function () {
    const $newshubVideo = $("div[data-component-name='newshub-video']");
    Array.prototype.forEach.call($newshubVideo, function (element) {
      if (element && element.dataset.initialized !== "true") {
        initSwiper(element);
      }
    });
  });
})(jQuery);