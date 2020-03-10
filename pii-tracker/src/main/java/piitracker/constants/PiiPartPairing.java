package piitracker.constants;

import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

/**
 * Based off
 * https://www.dhs.gov/sites/default/files/publications/handbookforsafeguardingsensitivePII_march_2012_webversion_0.pdf
 */
public enum PiiPartPairing {
    // standalone
    SSN_PAIRING(PiiPart.SSN),
    DL_PAIRING(PiiPart.DRIVERS_LICENSE),
    PASSPORT_PAIRING(PiiPart.PASSPORT_NUM),
    A_NUM_PAIRING(PiiPart.A_NUM),
    FIN_PAIRING(PiiPart.FIN),
    BIO_ID_PAIRING(PiiPart.BIOMETRIC_IDENTIFIER),

    // paired
    CIT_IMM_PAIRING(PiiPart.CIT_IMM_STATUS, Map.ofEntries(
        entry(PiiPartPairingType.PAIRING_OR, PiiPart.getValuesAsSet())
    )),
    MEDICAL_PAIRING(PiiPart.MEDICAL_INFO, Map.ofEntries(
        entry(PiiPartPairingType.PAIRING_OR, PiiPart.getValuesAsSet())
    )),
    ETHNIC_RELIGIOUS_PAIRING(PiiPart.ETHNIC_OR_RELIGIOUS_INFO, Map.ofEntries(
        entry(PiiPartPairingType.PAIRING_OR, PiiPart.getValuesAsSet())
    )),
    SEXUAL_ORIENTATION_PAIRING(PiiPart.SEXUAL_ORIENTATION, Map.ofEntries(
        entry(PiiPartPairingType.PAIRING_OR, PiiPart.getValuesAsSet())
    )),
    ACCOUNT_PW_PAIRING(PiiPart.ACCOUNT_PASSWORD, Map.ofEntries(
        entry(PiiPartPairingType.PAIRING_OR, PiiPart.getValuesAsSet())
    )),
    SSN_SUFFIX_4_PAIRING(PiiPart.SSN_SUFFIX_4, Map.ofEntries(
        entry(PiiPartPairingType.PAIRING_OR, PiiPart.getValuesAsSet())
    )),
    DOB_PAIRING(PiiPart.DOB, Map.ofEntries(
        entry(PiiPartPairingType.PAIRING_OR, PiiPart.getValuesAsSet())
    )),
    CRIMINAL_HIST_PAIRING(PiiPart.CRIMINAL_HISTORY, Map.ofEntries(
        entry(PiiPartPairingType.PAIRING_OR, PiiPart.getValuesAsSet())
    )),
    MOTHER_MAIDEN_PAIRING(PiiPart.MOTHER_MAIDEN_NAME, Map.ofEntries(
        entry(PiiPartPairingType.PAIRING_OR, PiiPart.getValuesAsSet())
    ))

    ;

    private PiiPart part;
    private Map<PiiPartPairingType, Set<PiiPart>> pairingMap;

    PiiPartPairing(PiiPart part) {
        this.part = part;
    }

    PiiPartPairing(PiiPart part, Map<PiiPartPairingType, Set<PiiPart>> pairingMap) {
        this(part);

        // don't include self pairing since
        // pii type included with itself isn't really being paired ...
        for (PiiPartPairingType pairingType: pairingMap.keySet()) {
            Set<PiiPart> pairings = pairingMap.get(pairingType);
            pairings.remove(part);
        }
        this.pairingMap = pairingMap;
    }

    public PiiPart getPart() {
        return part;
    }

    public void setPart(PiiPart part) {
        this.part = part;
    }

    public Map<PiiPartPairingType, Set<PiiPart>> getPairingMap() {
        return pairingMap;
    }

    public void setPairingMap(Map<PiiPartPairingType, Set<PiiPart>> pairingMap) {
        this.pairingMap = pairingMap;
    }

    public static PiiPartPairing findByPart(PiiPart part) {
        for (final PiiPartPairing pairing : PiiPartPairing.values()) {
            if (pairing.part.equals(part)) {
                return pairing;
            }
        }
        return null;
    }

}
