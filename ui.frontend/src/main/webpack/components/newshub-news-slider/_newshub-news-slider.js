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

      navigation: {
        nextEl: el.querySelector(".cmp-newshub-news-slider__next"),
        prevEl: el.querySelector(".cmp-newshub-news-slider__prev"),
        disabledClass: "cmp-newshub-news-slider__navigation--disabled",
        hiddenClass: "cmp-newshub-news-slider__navigation--hidden"
      }
    };

    const swiper = new Swiper(swiperContainer, swiperOptions);
  }

  function setLastSlideMargin() {
    const windowWidth = $(window).width();
    const $container = $(".cmp-newshub-news-slider__article-wrapper");
    const containerWidth = $container.width();
    const containerMargin = parseInt($container.css("margin-right"));
    const newsPadding = parseInt(
      $(".cmp-newshub-news-slider").css("padding-left")
    );
    const root = document.documentElement;
    let margin;

    if (window.matchMedia("(max-width: 650px )").matches) {
      margin = windowWidth - newsPadding - containerWidth;
    } else if (window.matchMedia("(min-width: 1280px )").matches) {
      margin =
        windowWidth - newsPadding - (3 * containerWidth + 2 * containerMargin);
    } else {
      margin =
        windowWidth - newsPadding - (2 * containerWidth + containerMargin);
    }

    root.style.setProperty("--news-slide-margin", margin + "px");
  }

  $(document).ready(function () {
    const $newsArticles = $("div[data-component-name='newshub-news-slider']");
    Array.prototype.forEach.call($newsArticles, function (element) {
      if (element && element.dataset.initialized !== "true") {
        initSwiper(element);
        setLastSlideMargin();

        let timer;

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