(function ($) {
  "use strict";

  function controlForm() {
    // inputs
    const $inputTemperature = $("#temperature-difference");
    const $inputPower = $("#heat-power");
    const $radioConstant = $("#constant");

    // submit button
    const $submitButton = $(".cmp-heat-meter__submit-btn");

    // error messages
    const $errorTemp = $(".cmp-heat-meter__input-error.temperature-difference");
    const $errorPower = $(".cmp-heat-meter__input-error.heat-power");

    // result
    const $resultWrapper = $(".cmp-heat-meter__result-wrapper");
    const $flowSpanValueBest = $(".cmp-heat-meter__flow-best-value");
    const $flowSpanValueMin = $(".cmp-heat-meter__flow-min-value");

    // individual invitation statement
    const $individual = $(".cmp-heat-meter__individual-text");

    // Does the calculations
    function calculateDimensioning() {
      const roundedVal = [
        0.6, 1.5, 2.5, 3.5, 6, 10, 15, 25, 40, 60, 100, 150, 250,
      ];
      const temperatureDifference = Number.parseInt($inputTemperature.val());
      const heatPower = Number.parseInt($inputPower.val());
      const flow = heatPower / (temperatureDifference * 1.162);

      let nominalFlowMin = 0;
      let nominalFlowBest = 0;

      if ($radioConstant.prop("checked")) {
        // Calculate constant value
        nominalFlowMin = flow * 1.25;
        nominalFlowBest = Math.round(nominalFlowMin);
      } else {
        // Calculate variable value
        nominalFlowMin = flow;
      }

      // Get best nominal flow
      for (var i = 0; i < roundedVal.length; i++) {
        if (roundedVal[i] > nominalFlowMin) {
          nominalFlowBest = roundedVal[i];
          break;
        }
      }

      // set result values
      $flowSpanValueMin.html(nominalFlowMin.toFixed(2) + " [m<sup>3</sup>/h]");
      if (nominalFlowMin <= 250) {
        $flowSpanValueBest.html(
          nominalFlowBest.toFixed(2) + " [m<sup>3</sup>/h]"
        );
      } else {
        $flowSpanValueBest.text($individual.text());
      }
    }

    // Check if input data is numeric and valid
    function isInputValueValid($inputField, minVal, maxVal) {
      let inputValue = $inputField.val();

      // allow commas
      inputValue = inputValue.replace(",", ".");

      if (!isNaN(inputValue)) {
        if (inputValue >= minVal && inputValue <= maxVal) {
          // allow only one digit
          if (inputValue.indexOf(".") != -1) {
            if (inputValue.length + 2 > inputValue.indexOf(".")) {
              inputValue = inputValue.substring(0, inputValue.indexOf(".") + 2);
            }
          }
          $inputField.val(inputValue);
          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    }

    // Validates Form Input
    function isFormValid() {
      const inputPowerValid =
        !!$inputPower.val() && isInputValueValid($($inputPower), 1, 18000);
      !inputPowerValid && $errorPower.fadeIn();

      const inputTempValid =
        !!$inputTemperature.val() &&
        isInputValueValid($($inputTemperature), 5, 60);
      !inputTempValid && $errorTemp.fadeIn();

      return inputPowerValid && inputTempValid;
    }

    // fades error in or out
    function toggleTempError() {
      if (!isInputValueValid($(this), 5, 60)) {
        $errorTemp.fadeIn();
      } else {
        $errorTemp.fadeOut();
      }
    }

    // fades error in or out
    function togglePowerError() {
      if (!isInputValueValid($(this), 1, 18000)) {
        $errorPower.fadeIn();
      } else {
        $errorPower.fadeOut();
      }
    }

    function initForm() {
      $inputTemperature.on("click focusout keyup", toggleTempError);
      $inputPower.on("click focusout keyup", togglePowerError);

      $submitButton.click(function (e) {
        e.preventDefault();

        if (isFormValid()) {
          $errorTemp.fadeOut();
          $errorPower.fadeOut();
          calculateDimensioning();
          $resultWrapper.show();
        } else {
          $resultWrapper.hide();
        }
      });
    }

    initForm();
  }

  $(document).ready(function () {
    const $heatMeter = $("div[data-component-name='heat-meter']");
    if ($heatMeter.length) {
      if ($heatMeter[0].dataset.initialized !== "true") {
        controlForm();
      }
    }
  });
})(jQuery);