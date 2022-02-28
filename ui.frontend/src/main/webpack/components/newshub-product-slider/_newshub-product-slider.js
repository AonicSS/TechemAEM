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

      navigation: {
        nextEl: el.querySelector(".cmp-newshub-product__next"),
        prevEl: el.querySelector(".cmp-newshub-product__prev"),
        disabledClass: "cmp-newshub-product__navigation--disabled",
        hiddenClass: "cmp-newshub-product__navigation--hidden",
      },
    };

    const swiper = new Swiper(swiperContainer, swiperOptions);
  }

  function setLastSlideMargin() {
    const windowWidth = $(window).width();
    const $container = $(".cmp-newshub-product__product");
    const containerWidth = $container.width();
    const containerMargin = parseInt($container.css("margin-right"));
    const productPadding = parseInt(
      $(".cmp-newshub-product").css("padding-left")
    );
    const root = document.documentElement;
    let margin;

    if (window.matchMedia("(max-width: 767px )").matches) {
      margin = windowWidth - productPadding - containerWidth;
    } else if (window.matchMedia("(min-width: 1025px ) and (max-width: 1229px )").matches) {
      margin = windowWidth - productPadding - (2 * containerWidth + containerMargin);
    } else {
      margin = windowWidth - productPadding - (3 * containerWidth + 2 * containerMargin);
    }

    root.style.setProperty("--product-slide-margin", margin + "px");
  }

  $(document).ready(function () {
    const $productSlider = $("div[data-component-name='newshub-product']");
    Array.prototype.forEach.call($productSlider, function (element) {
      if (element && element.dataset.initialized !== "true") {
        initSwiper(element);
      }
    });

    let timer;
    setLastSlideMargin();

    window.addEventListener("resize", () => {
      clearTimeout(timer);

      timer = setTimeout(() => {
        setLastSlideMargin();
      }, 300);
    });
  });
})(jQuery);