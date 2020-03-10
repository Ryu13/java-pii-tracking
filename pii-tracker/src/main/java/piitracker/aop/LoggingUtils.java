package piitracker.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import piitracker.annotations.PiiField;
import piitracker.constants.PiiPart;
import piitracker.constants.PiiPartPairing;
import piitracker.constants.PiiPartPairingType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoggingUtils {
    private static final String JACOCO_EMBEDDED_FIELD = "$jacocoData";

    public static String toSafeString(Object obj) {
        List<String> parts = new ArrayList<>();

        // set of pii parts in obj for pairing comparison
        final Set<PiiPart> piiPartsInObj = Stream.of(obj.getClass().getDeclaredFields()).filter(field -> {
            return !field.getName().equals(JACOCO_EMBEDDED_FIELD) && field.getDeclaredAnnotation(PiiField.class) != null;
        }).map(field -> {
            final PiiField piiFieldAnnotation = field.getDeclaredAnnotation(PiiField.class);
            final PiiPartPairing pairing = piiFieldAnnotation.piiPairing();
            return pairing.getPart();
        }).collect(Collectors.toSet());

        // loop over ALL fields now and determine if pii field, whether standalone or if type combination is satisfied by full set generated above
        parts.addAll(Stream.of(obj.getClass().getDeclaredFields()).filter(field -> !field.getName().equals(JACOCO_EMBEDDED_FIELD)).map(field -> {
            StringBuilder localSb = new StringBuilder();
            field.setAccessible(true);

            final boolean isPiiField = isPiiField(field, piiPartsInObj);

            // final boolean isPiiField = piiFieldAnnotation != null;
            if (!isPiiField) {
                localSb.append("\"");
                localSb.append(field.getName());
                localSb.append("\"");
                localSb.append(":");

                try {
                    localSb.append("\"");
                    final Object value = field.get(obj);
                    if (LoggableI.class.isAssignableFrom(value.getClass())) {
                        localSb.append(((LoggableI) value).toSafeString());
                    } else {
                        localSb.append(value);
                    }

                    localSb.append("\"");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            return localSb.toString();
        }).filter(str -> StringUtils.isNotEmpty(str)).collect(Collectors.toList()));

        return "{" + StringUtils.join(parts, ',') + "}";
    }

    public static String toFullString(Object obj) {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isPiiField(Field field, Set<PiiPart> piiPartsInObj) {
        boolean isPiiField = false;

        final PiiField piiFieldAnnotation = field.getDeclaredAnnotation(PiiField.class);

        if (piiFieldAnnotation != null) {
            final PiiPartPairing pairing = piiFieldAnnotation.piiPairing();
            if (pairing.getPairingMap() == null) { // standalone so it is PII
                isPiiField = true;
            } else { // instead, check pairing map to see if all are inside full set of parts
                for (PiiPartPairingType type : pairing.getPairingMap().keySet()) {
                    switch (type) {
                        // matches ANY of them, is pii
                        case PAIRING_OR: {
                            for (PiiPart part : pairing.getPairingMap().get(type)) {
                                if (piiPartsInObj.contains(part)) {
                                    isPiiField = true;
                                    break;
                                }
                            }

                            break;
                        }
                        // matches ALL of them, then pii
                        case PAIRING_AND: {
                            isPiiField = true;
                            for (PiiPart part : pairing.getPairingMap().get(type)) {
                                if (!piiPartsInObj.contains(part)) {
                                    isPiiField = false;
                                    break;
                                }
                            }

                            break;
                        }
                    }
                }
            }
        }

        return isPiiField;
    }
}
