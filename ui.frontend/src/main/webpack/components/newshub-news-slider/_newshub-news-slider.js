import Swiper, { Navigation } from "swiper";

(function ($) {
  "use strict";

  function initSwiper(el) {

    const swiperContainer = el.querySelector(
      ".cmp-newshub-news-slider__container"
    );
    const slides = swiperContainer.querySelectorAll(".swiper-slide");

    let isNotSlideable = false;
    let notSlideableOnTablet = false;

    if (slides.length < 4) {
      isNotSlideable = true;

      if (slides.length < 3) {
        notSlideableOnTablet = true;
      }
    }

    const swiperOptions = {
      modules: [Navigation],

      slidesPerView: "auto",
      observer: true,
      observeSlideChildren: true,
      observeParents: true,

      watchOverflow: true,
      grabCursor: true,
      spaceBetween: 40,
      slidesOffsetBefore: 1,
      slidesOffsetAfter: 75,
      slidesPerGroup: 1,

      navigation: {
        nextEl: el.querySelector(".cmp-newshub-news-slider__next"),
        prevEl: el.querySelector(".cmp-newshub-news-slider__prev"),
        disabledClass: "cmp-newshub-news-slider__navigation--disabled",
        hiddenClass: "cmp-newshub-news-slider__navigation--hidden"
      },

      breakpoints: {
        1460: {
          spaceBetween: 105,
          slidesOffsetBefore: 1,
          slidesOffsetAfter: 0
        },

        1400: {
          spaceBetween: 105,
          slidesOffsetBefore: 1,
          slidesOffsetAfter: isNotSlideable ? 0 : 160
        },

        1025: {
          spaceBetween: 105,
          slidesOffsetBefore: 1,
          slidesOffsetAfter: isNotSlideable ? 0 : 145
        },

        768: {
          spaceBetween: 75,
          slidesOffsetBefore: isNotSlideable ? 0 : 1,
          slidesOffsetAfter: notSlideableOnTablet ? 0 : 100
        }
      }
    };

    const swiper = new Swiper(swiperContainer, swiperOptions);
  }

  $(document).ready(function () {
    const $newsArticles = $("div[data-component-name='newshub-news-slider']");
    Array.prototype.forEach.call($newsArticles, function (element) {
      if (element && element.dataset.initialized !== "true") {
        initSwiper(element);
      }
    });
  });
})(jQuery);