import Swiper from "swiper";

(function ($) {
  "use strict";

  function initSwiper(el) {
    const pagination = el.querySelector(".cmp-similar-articles__pagination");
    const swiperOptions = {
      observer: true,
      observeSlideChildren: true,
      observeParents: true,
      updateOnWindowResize: true,

      slidesPerView: "auto",
      slidesOffsetAfter: 20,
      spaceBetween: 20,
      touchRatio: 1,

      pagination: {
        el: pagination,
        clickable: true,
      },

      breakpoints: {
        1400: {
          spaceBetween: 90,
          slidesOffsetAfter: 0,
          touchRatio: 0
        },

        1025: {
          slidesPerView: "auto",
          spaceBetween: 90,
          slidesOffsetAfter: 100,
          touchRatio: 1
        },

        768: {
          slidesPerView: "auto",
          spaceBetween: 80,
          slidesOffsetAfter: 100,
          touchRatio: 1
        },
      },
    };

    const swiperContainer = el.querySelector(
      ".cmp-similar-articles__article-container"
    );
    const swiper = new Swiper(swiperContainer, swiperOptions);
  }

  $(document).ready(function () {
    const $contacts = $("div[data-component-name='similar-articles']");
    Array.prototype.forEach.call($contacts, function (element) {
      if (element && element.dataset.initialized !== "true") {
        initSwiper(element);
      }
    });
  });
})(jQuery);