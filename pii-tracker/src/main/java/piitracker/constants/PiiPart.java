package piitracker.constants;

import java.util.*;
import java.util.stream.Collectors;

public enum PiiPart {
    // standalone
    SSN("Social Security Number"),
    DRIVERS_LICENSE ("Driver's License"),
    PASSPORT_NUM("Passport Number"),
    A_NUM("Alien Registration Number"),
    FIN("Financial Account Number"),
    BIOMETRIC_IDENTIFIER("Biometric Identifier"),

    // paired
    CIT_IMM_STATUS("Citizenship or Immigration Status"),
    MEDICAL_INFO("Medical Information"),
    ETHNIC_OR_RELIGIOUS_INFO("Ethnic or Religious Information"),
    SEXUAL_ORIENTATION("Sexual Orientation"),
    ACCOUNT_PASSWORD("Account Password"),
    SSN_SUFFIX_4("Last 4 digits of SSN"),
    DOB("Date of Birth"),
    CRIMINAL_HISTORY("Criminal History"),
    MOTHER_MAIDEN_NAME("Mother's Maiden Name")
    ;

    private String description;

    PiiPart(String description) {
        this.description = description;
    }

   public String getDescription() {
        return description;
   }

   public void setDescription(String description) {
        this.description = description;
   }

    public static Set<PiiPart> getValuesAsSet() {
        return Arrays.stream(PiiPart.values()).collect(Collectors.toSet());
    }

    public PiiPartPairing getPairing() {
        return PiiPartPairing.findByPart(this);
    }

}
