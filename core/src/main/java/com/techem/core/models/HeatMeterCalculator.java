package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeatMeterCalculator {

    @ValueMapValue(name = "headline")
    @Default(values = "Dimensioning calculator")
    private String headline;

    @ValueMapValue(name = "description")
    @Default(values = "Dimension calculator made easy! Our Techem heat meter calculator: A quick way to optimally design the measurement technology for closed heating circuits.")
    private String description;

    @ValueMapValue(name = "title")
    @Default(values = "Calculate!")
    private String title;

    @ValueMapValue(name = "legend")
    @Default(values = "Flow")
    private String legend;

    @ValueMapValue(name = "radioButtonConstantLabel")
    @Default(values = "constant")
    private String radioButtonConstantLabel;

    @ValueMapValue(name = "radioButtonVariableLabel")
    @Default(values = "variable")
    private String radioButtonVariableLabel;

    @ValueMapValue(name = "temperatureDifferenceLabel")
    @Default(values = "Difference of temperature")
    private String temperatureDifferenceLabel;

    @ValueMapValue(name = "temperatureDifferenceErrorLabel")
    @Default(values = "Min. 5k / max. 60k")
    private String temperatureDifferenceErrorLabel;

    @ValueMapValue(name = "heatPowerLabel")
    @Default(values = "Heating capacity")
    private String heatPowerLabel;

    @ValueMapValue(name = "heatPowerErrorLabel")
    @Default(values = "Min. 1kW / max. 18.000kW")
    private String heatPowerErrorLabel;

    @ValueMapValue(name = "flowMinResultLabel")
    @Default(values = "Result flow rate")
    private String flowMinResultLabel;

    @ValueMapValue(name = "flowBestResultLabel")
    @Default(values = "Suitable heat meter nominal flow rate Qn")
    private String flowBestResultLabel;

    @ValueMapValue(name = "submitButtonLabel")
    @Default(values = "Results")
    private String submitButtonLabel;

    @ValueMapValue(name = "individualInvitation")
    @Default(values = "For an individual offer, please contact your branch")
    private String individualInvitation;

    public String getHeadline() {
        return headline;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getLegend() {
        return legend;
    }

    public String getRadioButtonConstantLabel() {
        return radioButtonConstantLabel;
    }

    public String getRadioButtonVariableLabel() {
        return radioButtonVariableLabel;
    }

    public String getTemperatureDifferenceLabel() {
        return temperatureDifferenceLabel;
    }

    public String getTemperatureDifferenceErrorLabel() {
        return temperatureDifferenceErrorLabel;
    }

    public String getHeatPowerLabel() {
        return heatPowerLabel;
    }

    public String getHeatPowerErrorLabel() {
        return heatPowerErrorLabel;
    }

    public String getFlowMinResultLabel() {
        return flowMinResultLabel;
    }

    public String getFlowBestResultLabel() {
        return flowBestResultLabel;
    }

    public String getSubmitButtonLabel() {
        return submitButtonLabel;
    }

    public String getIndividualInvitation() {
        return individualInvitation;
    }
}