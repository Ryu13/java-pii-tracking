package piitracker.constants;

import piitracker.annotations.PiiField;
import piitracker.aop.LoggableI;
import piitracker.aop.LoggingUtils;

import java.util.Date;

public class SampleParentDto implements LoggableI {
    @PiiField(piiPairing=PiiPartPairing.A_NUM_PAIRING)
    private String alienNum = "123456789";

    @PiiField(piiPairing=PiiPartPairing.DOB_PAIRING)
    private Date dob;

    private String safeField = "SAFE";

    public String getAlienNum() {
        return alienNum;
    }

    public void setAlienNum(String alienNum) {
        this.alienNum = alienNum;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getSafeField() {
        return safeField;
    }

    public void setSafeField(String safeField) {
        this.safeField = safeField;
    }

    public String toSafeString() {
        return LoggingUtils.toSafeString(this);
    }

    public String toFullString() {
        return LoggingUtils.toFullString(this);
    }

    @Override
    public String toString() {
        return toSafeString();
    }
}
