/*global Cookiebot */

(function() {
    "use strict";

    function renderVideo($vidCmp) {
        const src = $vidCmp.dataset.src;
        const iframeContainer = $vidCmp.querySelector('.cmp-video-consent__iframe');
        const currentIframe = $vidCmp.querySelector('iframe');

        if (currentIframe) {
            currentIframe.remove();
        }

        const ifrm = document.createElement("iframe");
        ifrm.setAttribute("src", src);
        ifrm.width = "100%";
        ifrm.frameBorder = "0";
        ifrm.ariaLabel = "YouTube Video";
        ifrm.allowFullScreen = "true";
        iframeContainer.appendChild(ifrm);
    }

    function renderAllVids($cmpList) {
        $cmpList.forEach(cmp => {
            const $consent = cmp.querySelector('.cmp-video-consent__container');
            const $iframeContainer = cmp.querySelector('.cmp-video-consent__iframe');
            $consent.setAttribute('hidden', 'true');
            $iframeContainer.removeAttribute('hidden');
            renderVideo(cmp);
        });
    }

    function renderVidsPlaceholder($cmpList) {
        $cmpList.forEach(cmp => {
            const $consent = cmp.querySelector('.cmp-video-consent__container');
            const $iframeContainer = cmp.querySelector('.cmp-video-consent__iframe');
            const currentIframe = cmp.querySelector('iframe');

            if (currentIframe) {
                currentIframe.remove();
            }

            $consent.removeAttribute('hidden');
            $iframeContainer.setAttribute('hidden', 'true');
        });
    }

    function checkMarketingConsent($cmpList) {
        // Cookiebot is present
        if (Cookiebot && Cookiebot.consent && Cookiebot.consent.marketing) {
            // Marketing consent true
            renderAllVids($cmpList);
        } else {
            // Marketing consent false
            renderVidsPlaceholder($cmpList);
        }
    }

    function initConsent($cmpList) {
        window.addEventListener('CookiebotOnAccept', function (e) {
            checkMarketingConsent($cmpList);
        }, false);

        $cmpList.forEach(cmp => {
            const $btn = cmp.querySelector('.cmp-video-consent__button');
            $btn.addEventListener('click', () => {
                try {
                    Cookiebot.renew();
                } catch (e) {
                    console.log('Cookiebot not defined');
                }
            });
        });

        checkMarketingConsent($cmpList);
    }

    document.addEventListener('DOMContentLoaded', function() {
        const $cmpList = document.querySelectorAll("[data-component-name='video-consent']");

        if ($cmpList.length) {
            initConsent($cmpList);
        }
    });
}());