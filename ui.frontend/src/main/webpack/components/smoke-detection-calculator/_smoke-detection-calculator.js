(function ($) {
    "use strict";
  
    function initCalculator(el) {        
        el.dataset.initialized = true;
        const calculatorRoot = "#/calculator";
        var hasRoute = window.location.hash.indexOf(calculatorRoot) !== -1;

        if(!hasRoute) {
            window.location.hash = calculatorRoot;
        }
    }

    $(document).ready(function () {
        const $calculator = $("div[data-component-name='smoke-detection-calculator']");

        Array.prototype.forEach.call($calculator, function (element) {
            if (element && element.dataset.initialized !== "true") {
                initCalculator(element);
            }
        });
    });
    
  })(jQuery);