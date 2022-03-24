import { iframeResize } from 'iframe-resizer';

/*
make sure to pass data-component-name to the iframe you are planning to resize
<iframe data-component-name="iframe-resizer" src="https://..." style="width: 100%" height="2000" frameborder="0"></iframe>
*/

(function($) {
    "use strict";

    function init(el) {
        iframeResize({ log: true }, el);
    }

    $(document).ready(function() {
        const $cmp = $("[data-component-name='iframe-resizer']");
        Array.prototype.forEach.call($cmp, function(element) {
            if (element && element.dataset.initialized !== "true") {
                init(element);
            }
        });
    });
}(jQuery));