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
      slidesOffsetAfter: 80,
      slidesPerGroup: 1,

      breakpoints: {
        1025: {
          slidesPerView: "auto",
          spaceBetween: 105,
          slidesOffsetAfter: 293,
          slidesPerGroup: 3
        },

        768: {
          slidesPerView: "auto",
          spaceBetween: 75,
          slidesOffsetAfter: 75,
          slidesPerGroup: 1
        }
      }
    };

    const swiperContainer = el.querySelector(
      ".cmp-newshub-news-slider__container"
    );
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