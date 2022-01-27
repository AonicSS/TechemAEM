import Swiper, { Navigation } from "swiper";

(function ($) {
  "use strict";

  function initSwiper(el) {
    const swiperContainer = el.querySelector(".cmp-newshub-video__container");
    const slides = swiperContainer.querySelectorAll(".swiper-slide");
    let isNotSlideable = false;

    if (slides.length < 2) {
      isNotSlideable = true;
    }

    const swiperOptions = {
      modules: [Navigation],

      slidesPerView: "auto",
      observer: true,
      observeSlideChildren: true,
      observeParents: true,
      grabCursor: true,
      spaceBetween: 40,
      watchOverflow: true,
      slidesOffsetBefore: 1,
      slidesOffsetAfter: isNotSlideable ? 0 : 80,

      navigation: {
        nextEl: el.querySelector(".cmp-newshub-video__next"),
        prevEl: el.querySelector(".cmp-newshub-video__prev"),
        disabledClass: "cmp-newshub-video__navigation--disabled",
        hiddenClass: "cmp-newshub-video__navigation--hidden",
      },

      breakpoints: {
        1400: {
          spaceBetween: 105,
          slidesOffsetBefore: 1,
          slidesOffsetAfter: isNotSlideable ? 0 : 221,
        },

        1025: {
          spaceBetween: 105,
          slidesOffsetBefore: 1,
          slidesOffsetAfter: isNotSlideable ? 0 : 280,
        },

        768: {
          spaceBetween: 60,
          slidesOffsetBefore: 1,
          slidesOffsetAfter: isNotSlideable ? 0 : 88,
        },
      },
    };

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