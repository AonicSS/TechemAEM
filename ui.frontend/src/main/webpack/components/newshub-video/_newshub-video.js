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
      watchOverflow: true,
      navigation: {
        nextEl: el.querySelector(".cmp-newshub-video__next"),
        prevEl: el.querySelector(".cmp-newshub-video__prev"),
        disabledClass: "cmp-newshub-video__navigation--disabled",
        hiddenClass: "cmp-newshub-video__navigation--hidden"
      }
    };

    const swiper = new Swiper(swiperContainer, swiperOptions);
  }

  function setLastSlideMargin() {
    const windowWidth = $(window).width();
    const containerWidth = $(".cmp-newshub-video__video").width();
    const videoPadding = parseInt($(".cmp-newshub-video").css("padding-left"));
    const root = document.documentElement;

    const margin = windowWidth - videoPadding - containerWidth;

    root.style.setProperty("--video-slide-margin", margin + "px");
  }

  $(document).ready(function () {
    const $newshubVideo = $("div[data-component-name='newshub-video']");
    Array.prototype.forEach.call($newshubVideo, function (element) {
      if (element && element.dataset.initialized !== "true") {
        initSwiper(element);

        let timer;
        setLastSlideMargin();

        window.addEventListener("resize", () => {
          clearTimeout(timer);

          timer = setTimeout(() => {
            setLastSlideMargin();
          }, 300);
        });
      }
    });
  });
})(jQuery);