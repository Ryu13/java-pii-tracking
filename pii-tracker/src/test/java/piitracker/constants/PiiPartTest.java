package piitracker.constants;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class PiiPartTest {

    @Test
    public void testPiiPart() {
        final PiiPart part = PiiPart.CIT_IMM_STATUS;

        // should match all other part definitions minus the part itself
        Set<PiiPart> expected = PiiPart.getValuesAsSet();
        expected.remove(part);

        assertEquals(expected, part.getPairing().getPairingMap().get(PiiPartPairingType.PAIRING_OR));
    }

}
