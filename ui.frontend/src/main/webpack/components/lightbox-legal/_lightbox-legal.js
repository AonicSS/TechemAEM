import * as basicLightbox from 'basiclightbox';
import Cookies from 'js-cookie';
import { disableBodyScroll, enableBodyScroll } from 'body-scroll-lock';

(function($) {
    "use strict";

    function updateButton(lbCheckbox, lbButton) {
        lbButton.setAttribute('disabled', !lbCheckbox.checked);
        if (lbCheckbox.checked) {
            lbButton.removeAttribute('disabled');
        } else {
            lbButton.setAttribute('disabled', true);
        }
    }

    function setLegalInfoCookie() {
        Cookies.set('legalinformationvisited', 'yes');
    }

    function initLightBoxLegal(el) {
        const lbContainer = el.querySelector('.cmp-lightbox-legal__container');
        const lbForm = el.querySelector('.cmp-lightbox-legal__form');
        const lbButton = el.querySelector('.cmp-lightbox-legal__button');
        const lbCheckbox = el.querySelector('.cmp-form-options__field--checkbox');
        const isAuthor = el.classList.contains('author-mode');
        const legalInfoCookie = Cookies.get('legalinformationvisited');
        const lbInstance = basicLightbox.create(lbContainer, {
            closable: false,
            onShow: (instance) => {
                disableBodyScroll(instance.element());
            },
            onClose: (instance) => {
                enableBodyScroll(instance.element());
            }
        });

        el.dataset.initialized = 'true';

        if (isAuthor) {
            return;
        }

        if (typeof legalInfoCookie === 'undefined' || legalInfoCookie !== 'yes') {
            lbInstance.show();
        }

        lbCheckbox.addEventListener('change', () => { updateButton(lbCheckbox, lbButton); }, false);

        lbForm.addEventListener('submit', (e) => {
            e.preventDefault();
            lbInstance.close();
            setLegalInfoCookie();
        });
    }

    $(document).ready(function() {
        const $cmp = $("div[data-component-name='lightbox-legal']");
        Array.prototype.forEach.call($cmp, function(element) {
            if (element && element.dataset.initialized !== "true") {
                initLightBoxLegal(element);
            }
        });
    });
}(jQuery));