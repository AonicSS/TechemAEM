/*global Cookiebot */

(function($) {
    "use strict";

    function renderVideo($vidCmp) {
        const src = $vidCmp.data('src');
        const iframeContainer = $vidCmp.find('.cmp-video-consent__iframe');
        $vidCmp.find('iframe').remove();
        $('<iframe>')
            .attr('src', src)
            .attr('width', '100%')
            .attr('frameborder', '0')
            .attr('aria-label', 'YouTube Video')
            .attr('allowfullscreen', 'true')
            .appendTo(iframeContainer);
    }

    function renderAllVids($cmpList) {
        $cmpList.each(function (index, vidCmp) {
            const $vidCmp = $(vidCmp);
            const $consent = $vidCmp.find('.cmp-video-consent__container');
            $consent.attr('hidden', 'true');
            renderVideo($vidCmp);
        });
    }

    function renderVidsPlaceholder($cmpList) {
        $cmpList.each(function (index, vidCmp) {
            const $vidCmp = $(vidCmp);
            const $consent = $vidCmp.find('.cmp-video-consent__container');
            const $iframeContainer = $vidCmp.find('.cmp-video-consent__iframe');
            $consent.removeAttr('hidden');
            $iframeContainer.find('iframe').remove();
            $iframeContainer.attr('hidden', 'true');
        });
    }

    function checkMarketingConsent($cmpList) {
        // Cookiebot is present
        if (typeof Cookiebot !== 'undefined') {
            if (Cookiebot.consent && Cookiebot.consent.marketing) {
                // Marketing consent true
                renderAllVids($cmpList);
            } else {
                // Marketing consent false
                renderVidsPlaceholder($cmpList);
            }
        } else { // Cookiebot not present (e.g. localhost)
            renderAllVids($cmpList);
        }
    }

    function initConsent($cmpList) {
        window.addEventListener('CookiebotOnAccept', function (e) {
            checkMarketingConsent($cmpList);
        }, false);

        $cmpList.each(function (index, vidCmp) {
           const $vidCmp = $(vidCmp);
           const $btn = $vidCmp.find('.cmp-video-consent__button');
           $btn.on('click', () => {
               try {
                   Cookiebot.renew();
               } catch (e) {
                   console.log('Cookiebot not defined');
               }
           });
        });

        checkMarketingConsent($cmpList);
    }

    $(document).ready(function() {
        const $cmpList = $("div[data-component-name='video-consent']");

        if ($cmpList.length) {
            initConsent($cmpList);
        }
    });
}(jQuery));