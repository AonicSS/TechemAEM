import { debounce } from '../../helpers/_debounce';

(function($) {
  "use strict";

  function scrollUp() {
    window.scrollTo({top: 0, behavior: "smooth"});
  }

  $(document).ready(function() {
    const $backToTop = $('.cmp-back-to-top');

    function toggleBackToTop() {
      const windowY = window.scrollY || window.pageYOffset;
      if (windowY > 1000) {
        $backToTop.removeClass('cmp-back-to-top-hidden');
      } else {
        $backToTop.addClass('cmp-back-to-top-hidden');
      }
    }

    if ($backToTop.length) {
      const debouncedMethod = debounce(toggleBackToTop, 150);
      window.addEventListener('scroll', debouncedMethod, { passive: true });
      $backToTop.on('click', scrollUp);
      toggleBackToTop();
    }
  });
}(jQuery));

