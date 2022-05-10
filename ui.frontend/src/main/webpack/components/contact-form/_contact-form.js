import { disableBodyScroll, enableBodyScroll } from 'body-scroll-lock';

(function ($) {
  "use strict";

  function initContactForm(el) {
    const formContainer = el.querySelector(".contact-form__container");
    const formWrapper = el.querySelector(".contact-form__wrapper");
    const form = el.querySelector(".contact-form__form");
    const triggerButton = el.querySelector(".contact-form__trigger-btn");
    const closeButton = el.querySelector(".contact-form__close-btn");
    const submitButton = el.querySelector(".contact-form__submit-btn");
    const checkboxInput = el.querySelector("#checkbox-input");
    const successMessage = el.querySelector(".contact-form__success");
    var fCaptcha = el.querySelector(".friendly-captcha");
    let windowWidth,
      windowWidthOriginal = window.innerWidth;
    let windowHeight = window.innerHeight;
    let origialHeight = 0;

    function resetHeight() {
      formContainer.classList.remove("max");
    }

    function setHeight() {
      formContainer.classList.add("max");
    }

    function checkHeight() {
      if (window.innerWidth <= 1024) {
        if (origialHeight === 0) {
          resetHeight();
          origialHeight = formContainer.offsetHeight;
        }

        // minus 60 for the header
        // minus twenty for the spacing on the bottom
        if (window.innerHeight - 80 <= origialHeight) {
          setHeight();
        } else {
          resetHeight();
        }
      }
    }

    function checkRightPos() {
      const $homeStage = $(".home-stage");

      if (window.innerWidth > 1024) {
        if (window.innerWidth > $homeStage.innerWidth()) {
          const rightPos = ((window.innerWidth - parseInt($homeStage.innerWidth())) / 2) + 105;
          $(formContainer).css("right", rightPos);
        } else {
          $(formContainer).css("right", "105px");
        }
      } else {
        $(formContainer).removeAttr("style");
      }
    }

    function hideForm() {
      enableBodyScroll(formContainer);
      $("body").removeAttr("style");
      formContainer.classList.add("hidden");
      formContainer.classList.remove("visible");
      triggerButton.classList.remove("hidden");
      triggerButton.focus();
    }

    function showForm() {
      disableBodyScroll(formContainer);
      $("body").css("right", 0);
      formContainer.classList.remove("hidden");
      formContainer.classList.add("visible");
      triggerButton.classList.add("hidden");
      closeButton.focus();
      checkHeight();
    }

    function disableButton() {
      submitButton.setAttribute("disabled", true);
    }

    function enableButton() {
      submitButton.removeAttribute("disabled");
    }

    $(".contact-form__form :input").change(function () {
      var fCaptchaWidget = fCaptcha ? fCaptcha.friendlyChallengeWidget : false;

      if(fCaptchaWidget && !fCaptchaWidget.valid) {
        return;
      }

      if (checkboxInput && !checkboxInput.checked && checkboxInput.required) {
        disableButton();
      } else {
        enableButton();
      }
    });

    triggerButton.addEventListener("click", () => {
      showForm();
    });

    closeButton.addEventListener("click", () => {
      hideForm();
    });

    form.addEventListener("submit", (e) => {
      e.preventDefault();

      disableButton();
      const $formData = $(form).serialize();
      var fCaptchaWidget = fCaptcha ? fCaptcha.friendlyChallengeWidget : false;

      $.post({
        url: $(form).attr("action"),
        data: $formData
      }).done(function () {
        $(form).trigger("reset");
        var isCheckboxReq = checkboxInput && checkboxInput.required;
        if(!fCaptchaWidget && !isCheckboxReq) {
          enableButton();
        }
        successMessage.classList.remove('hidden');
      });
    });

    checkHeight();
    checkRightPos();

    let timerWidth, timerHeight;
    window.addEventListener("resize", () => {
      clearTimeout(timerWidth);
      clearTimeout(timerHeight);


      timerWidth = setTimeout(() => {
        if (window.innerWidth > 1024) {
          showForm();
        } else {
          if (windowWidth > 1024) {
            hideForm();
          }
        }
        windowWidth = window.innerWidth;
      }, 200);

      if (window.innerWidth <= 1024) {
        timerHeight = setTimeout(() => {
          if (window.innerHeight !== windowHeight) {
            windowHeight = window.innerHeight;
          }

          if (
            (windowWidthOriginal < 768 && window.innerWidth > 767) ||
            (windowWidthOriginal > 768 && window.innerWidth < 769)
          ) {
            origialHeight = 0;
            windowWidthOriginal = window.innerWidth;
          }
          checkHeight();
        }, 200);
      } else {
        resetHeight();
      }

      checkRightPos();
    });
  }

  $(document).ready(function () {
    const $contactForm = $("div[data-component-name='contact-form']");
    Array.prototype.forEach.call($contactForm, function (element) {
      if (element && element.dataset.initialized !== "true") {
        initContactForm(element);
      }
    });
  });
})(jQuery);