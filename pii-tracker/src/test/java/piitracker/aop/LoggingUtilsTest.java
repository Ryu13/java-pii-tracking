package piitracker.aop;

import org.junit.jupiter.api.Test;
import piitracker.constants.SampleParentDto;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoggingUtilsTest {

    private final static String NEWLINE = System.getProperty("os.name").startsWith("Windows") ? "\r\n" : "\n";

    @Test
    public void testToSafeString() {
        final Date dobDate = new Date();
        final SampleParentDto sample = new SampleParentDto();
        sample.setDob(dobDate);
        assertEquals("{\"safeField\":\"SAFE\"}", sample.toSafeString());
    }

    @Test
    public void testToFullString() {
        final Date dobDate = new Date();
        final SampleParentDto sample = new SampleParentDto();
        sample.setDob(dobDate);
        assertEquals("{" + NEWLINE +
            "  \"alienNum\" : \"123456789\"," + NEWLINE +
            "  \"dob\" : " + dobDate.getTime() + "," + NEWLINE +
            "  \"safeField\" : \"SAFE\"" + NEWLINE +
            "}",
            sample.toFullString()
        );
    }
}
