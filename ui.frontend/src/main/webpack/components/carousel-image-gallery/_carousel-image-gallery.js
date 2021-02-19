import Swiper, { Navigation, Pagination, Autoplay, A11y } from 'swiper';
// configure Swiper to use modules
Swiper.use([Navigation, Pagination, Autoplay, A11y]);

(function($) {
    "use strict";

    function initSwiper(el) {
        const swiperContainer = el.querySelector('.swiper-container');
        const pagination = el.querySelector('.swiper-pagination');
        const autoplay = el.dataset.cmpAutoplay === 'true';
        const autoPlayDelay = el.dataset.cmpDelay;
        const loop = el.dataset.cmpLoop === 'true';
        const accessibilityLabel = el.dataset.cmpAccessibilityLabel;
        const autoHeight = el.dataset.cmpAutoHeight === 'true';
        const swiperOptions = {
            direction: 'horizontal',
            loop: loop,
            allowTouchMove: true,
            autoHeight: autoHeight,

            pagination: {
                el: pagination,
                type: 'progressbar'
            },

            breakpoints: {
                1025: {
                    allowTouchMove: false,
                },
            },

            // Navigation arrows
            navigation: {
                nextEl: '.swiper-button-next',
                prevEl: '.swiper-button-prev',
            },
        };

        if (autoplay) {
            swiperOptions.autoplay = {
                delay: autoPlayDelay,
            };
        }

        if (accessibilityLabel) {
            swiperOptions.a11y = {
                containerMessage: accessibilityLabel
            };
        }

        const swiper = new Swiper(swiperContainer, swiperOptions);
    }

    $(document).ready(function() {
        const $carousel = $("div[data-component-name='carousel-image-gallery']");
        Array.prototype.forEach.call($carousel, function(element) {
            if (element && element.dataset.initialized !== "true") {
                initSwiper(element);
            }
        });
    });
}(jQuery));

