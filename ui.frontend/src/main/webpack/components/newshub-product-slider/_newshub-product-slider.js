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
			spaceBetween: 45,
			slidesOffsetAfter: 80,
			slidesPerGroup: 1,

			breakpoints: {
				1025: {
					slidesPerView: "auto",
					spaceBetween: 160,
					slidesOffsetAfter: 240,
					slidesPerGroup: 3
				},

				768: {
					slidesPerView: "auto",
					slidesOffsetAfter: 148,
					spaceBetween: 60,
					slidesOffsetAfter: 78,
					slidesPerGroup: 1
				},
			},
		};

		const swiperContainer = el.querySelector(".cmp-newshub-product__product-container ");
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