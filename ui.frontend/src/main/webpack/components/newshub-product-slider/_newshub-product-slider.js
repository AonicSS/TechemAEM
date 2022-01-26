import Swiper, { Navigation } from "swiper";

(function ($) {
  "use strict";

  function initSwiper(el) {
    const swiperContainer = el.querySelector(".cmp-newshub-product__container");
    const slides = swiperContainer.querySelectorAll(".swiper-slide");

    let isNotSlideable = false;

    if (slides.length < 4) {
      isNotSlideable = true;
    }

    const swiperOptions = {
      modules: [Navigation],

      slidesPerView: "auto",
      observer: true,
      observeSlideChildren: true,
      observeParents: true,
      watchOverflow: true,
      grabCursor: true,
      spaceBetween: 45,
      slidesOffsetBefore: 1,
      slidesOffsetAfter: 79,
      slidesPerGroup: 1,

      navigation: {
        nextEl: el.querySelector(".cmp-newshub-product__next"),
        prevEl: el.querySelector(".cmp-newshub-product__prev"),
        disabledClass: "cmp-newshub-product__navigation--disabled",
        hiddenClass: "cmp-newshub-product__navigation--hidden",
      },

      breakpoints: {
        1400: {
          spaceBetween: 155,
          slidesOffsetBefore: 1,
          slidesOffsetAfter: isNotSlideable ? 0 : 210,
        },

        1025: {
          spaceBetween: 155,
          slidesOffsetBefore: 1,
          slidesOffsetAfter: isNotSlideable ? 0 : 124,
        },

        768: {
          spaceBetween: 60,
          slidesOffsetBefore: 1,
          slidesOffsetAfter: isNotSlideable ? 0 : 78,
        },
      },
    };

    const swiper = new Swiper(swiperContainer, swiperOptions);
  }

  $(document).ready(function () {
    const $productSlider = $("div[data-component-name='newshub-product']");
    Array.prototype.forEach.call($productSlider, function (element) {
      if (element && element.dataset.initialized !== "true") {
        initSwiper(element);
      }
    });
  });
})(jQuery);