import Swiper, { Pagination, Autoplay, A11y } from "swiper";
// configure Swiper to use modules
Swiper.use([Pagination, Autoplay, A11y]);

(function ($) {
  "use strict";

  function initSwiper(el) {
    const swiperContainer = el.querySelector(".cmp-newshub-feed__content");
    const pagination = el.querySelector(".cmp-newshub-feed__pagination");
    const swiperOptions = {
      slidesPerColumnFill: "column",
      slidesPerColumn: 1,
      slidesPerView: 1,
      slidesPerGroup: 1,
      observer: true,
      observeSlideChildren: true,
      observeParents: true,
      grabCursor: true,

      grid: {
        rows: 1,
      },
      pagination: {
        el: pagination,
        clickable: true,
      },

      breakpoints: {
        1025: {
          slidesPerView: 1,
          slidesPerColumn: 3,
          slidesPerColumnFill: "row",
          slidesPerGroup: 1,
          grid: {
            rows: 3,
          },
        },

        768: {
          slidesPerView: "auto",
          slidesPerColumn: 1,
          slidesPerColumnFill: "row",
          slidesPerGroup: 1,
          slidesOffsetAfter: 148,
          grid: {
            rows: 1,
          },
        },
      },
    };

    const swiper = new Swiper(swiperContainer, swiperOptions);
  }

  $(document).ready(function () {
    const $articleFeed = $(".cmp-newshub-feed");
    if($articleFeed.length) {
      initSwiper($articleFeed[0]);
    }
  });
})(jQuery);