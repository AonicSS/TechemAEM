import Swiper from "swiper";

(function ($) {
  "use strict";

  function initSwiper(el) {
    let isVariation = false;

    if (String(el.className).includes("isVariation")) {
      isVariation = true;
    }

    const pagination = el.querySelector(".cmp-newshub-contact__pagination");
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
          slidesPerView: isVariation ? 2 : "auto",
          spaceBetween: isVariation ? 50 : 90,
          slidesOffsetAfter: isVariation ? 0 : 100,
          touchRatio: isVariation ? 0 : 1
        },

        768: {
          slidesPerView: "auto",
          spaceBetween: isVariation ? 20 : 80,
          slidesOffsetAfter: isVariation ? 0 : 114,
          touchRatio: isVariation ? 0 : 1
        },
      },
    };

    const swiperContainer = el.querySelector(
      ".cmp-newshub-contact__contact-container"
    );
    const swiper = new Swiper(swiperContainer, swiperOptions);
  }

  $(document).ready(function () {
    const $contacts = $("div[data-component-name='newshub-contacts']");
    Array.prototype.forEach.call($contacts, function (element) {
      if (element && element.dataset.initialized !== "true") {
        initSwiper(element);
      }
    });
  });
})(jQuery);