package com.consol.citrus.simulator.porting.starter;

import com.consol.citrus.simulator.model.ScenarioParameter;
import com.consol.citrus.simulator.model.ScenarioParameterBuilder;

import java.util.UUID;

public class ScenarioParameterHelper {
    public static final String REQUEST_ID = "requestId";
    public static final String RECEIVING_CARRIER = "receivingCarrier";
    public static final String DONATING_CARRIER = "donatingCarrier";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String FORENAME = "forename";
    public static final String SURNAME = "surname";
    public static final String DATE_OF_BIRTH = "dateOfBirth";
    public static final String CONFIRMATION_CODE = "code";
    public static final String CONFIRMATION_TEXT = "text";

    /**
     * Generates the placeholder for the supplied {@code variableName}.
     * <br>E.g. For variable 'id' the placeholder '${id}' is returned
     *
     * @param variableName the variable to generate the placeholder for
     * @return the placeholder equivalent
     */
    public static String placeholder(String variableName) {
        return String.format("${%s}", variableName);
    }

    public static ScenarioParameter requestId() {
        return new ScenarioParameterBuilder()
                .textbox()
                .name(REQUEST_ID)
                .label("Request ID")
                .required()
                .value(UUID.randomUUID().toString())
                .build();
    }

    public static ScenarioParameter receivingCarrier() {
        return carrier(RECEIVING_CARRIER, "Receiving Carrier");
    }

    public static ScenarioParameter donatingCarrier() {
        return carrier(DONATING_CARRIER, "Donating Carrier");
    }

    private static ScenarioParameter carrier(String name, String label) {
        return new ScenarioParameterBuilder()
                .name(name)
                .label(label)
                .required()
                .dropdown()
                .addOption("DEU.COOLTEL", "CoolTel")
                .addOption("DEU.MYTEL", "MyTel")
                .value("DEU.COOLTEL") // TODO MM remove default
                .build();
    }

    public static ScenarioParameter phoneNumber() {
        return new ScenarioParameterBuilder()
                .textbox()
                .name(PHONE_NUMBER)
                .label("Phone Number")
                .required()
                .value("12345678")
                .build();
    }

    public static ScenarioParameter forename() {
        return new ScenarioParameterBuilder()
                .textbox()
                .name(FORENAME)
                .label("Forename")
                .required()
                .value("Joe")
                .build();

    }

    public static ScenarioParameter surname() {
        return new ScenarioParameterBuilder()
                .textbox()
                .name(SURNAME)
                .label("Surname")
                .required()
                .value("Bloggs")
                .build();

    }

    public static ScenarioParameter dateOfBirth() {
        return new ScenarioParameterBuilder()
                .textbox()
                .name(DATE_OF_BIRTH)
                .label("Date Of Birth")
                .required()
                .value("23.02.1998")
                .build();
    }

    public static ScenarioParameter confirmationCode() {
        return new ScenarioParameterBuilder()
                .name(CONFIRMATION_CODE)
                .label("Confirmation Code")
                .required()
                .dropdown()
                .addOption("APPROVED", "Approved")
                .addOption("REJECTED", "Rejected")
                .value("APPROVED")
                .build();
    }

    public static ScenarioParameter confirmationText() {
        return new ScenarioParameterBuilder()
                .textbox()
                .name(CONFIRMATION_TEXT)
                .label("Confirmation Text")
                .value("")
                .optional()
                .build();
    }


}
