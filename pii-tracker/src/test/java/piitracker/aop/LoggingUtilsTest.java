package piitracker.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import piitracker.constants.SampleParentDto;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class LoggingUtilsTest {

    private final static String NEWLINE = System.getProperty("os.name").startsWith("Windows") ? "\r\n" : "\n";

    @Test
    public void testEnteringPointcutMessage() {
        final String methodName = "METHOD_NAME";
        final Object arg1 = new SampleParentDto();
        final Object arg2 = "ARG2";
        final Object[] args = new Object[]{arg1, arg2};

        MethodSignature signature = mock(MethodSignature.class);
        when(signature.getDeclaringType()).thenReturn(SampleParentDto.class);
        when(signature.getName()).thenReturn(methodName);

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.getArgs()).thenReturn(args);

        assertEquals(
            "Entering " + SampleParentDto.class.toString() + "." + methodName + " with arg(s) = " + Arrays.toString(args),
            LoggingUtils.enteringPointcutMessage(joinPoint)
        );
    }

    @Test
    public void testLeavingPointcutMessage() {
        final String methodName = "METHOD_NAME";

        MethodSignature signature = mock(MethodSignature.class);
        when(signature.getDeclaringType()).thenReturn(SampleParentDto.class);
        when(signature.getName()).thenReturn(methodName);

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.getSignature()).thenReturn(signature);

        final Object result = new SampleParentDto();

        assertEquals(
            "Exiting " + SampleParentDto.class.toString() + "." + methodName + " with result = " + result.toString(),
            LoggingUtils.leavingPointcutMessage(joinPoint, result)
        );
    }



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
