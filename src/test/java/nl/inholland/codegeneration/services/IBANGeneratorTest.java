package nl.inholland.codegeneration.services;

import nl.inholland.codegeneration.services.IBANGenerator;
import org.hibernate.id.IdentifierGenerationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IBANGeneratorTest {

    private IBANGenerator ibanGenerator;

    @BeforeEach
    public void setUp() {
        ibanGenerator = new IBANGenerator();
    }

    @Test
    public void testGenerate() {
        String iban = (String) ibanGenerator.generate(null, null);
        assertNotNull(iban, "IBAN should not be null");
        assertEquals(18, iban.length(), "IBAN length should be 18");
        assertTrue(ibanGenerator.validateIBAN(iban), "Generated IBAN should be valid");
    }

    @Test
    public void testCalculateMod97() {
        String iban = "NL91INHO0123456789"; // a sample valid IBAN
        int mod97 = ibanGenerator.calculateMod97(iban);
        assertEquals(1, mod97, "Modulo 97 of a valid IBAN should be 1");
    }

    @Test
    public void testValidateIBAN() {
        String validIban = "NL91INHO0123456789"; // a sample valid IBAN
        String invalidIban = "NL00INHO0123456789"; // a sample invalid IBAN

        assertTrue(ibanGenerator.validateIBAN(validIban), "Should return true for a valid IBAN");
        assertFalse(ibanGenerator.validateIBAN(invalidIban), "Should return false for an invalid IBAN");
    }

    // @Test
    // public void testValidateIBAN() {
    // String validIban = "NL02ABNA0123456789"; // a sample valid IBAN
    // String invalidIban = "NL00INHO0123456789"; // a sample invalid IBAN

    // assertTrue(ibanGenerator.validateIBAN(validIban), "Should return true for a
    // valid IBAN");
    // assertFalse(ibanGenerator.validateIBAN(invalidIban), "Should return false for
    // an invalid IBAN");
    // }

    @Test
    public void testValidateIBANWithException() {
        String invalidIban = "NL91INHO0123456789ABC"; // a sample invalid IBAN

        assertThrows(IdentifierGenerationException.class, () -> {
            ibanGenerator.validateIBAN(invalidIban);
        }, "Should throw IdentifierGenerationException for an invalid IBAN");
    }
}