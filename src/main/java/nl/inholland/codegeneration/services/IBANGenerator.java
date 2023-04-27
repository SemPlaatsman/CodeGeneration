package nl.inholland.codegeneration.services;

import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializerBase;
import nl.inholland.codegeneration.repositories.AccountRepository;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerationException;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;
import java.util.stream.Stream;

// IBAN Generator class based on: https://en.wikipedia.org/wiki/International_Bank_Account_Number#Algorithms
@Component
public class IBANGenerator implements IdentifierGenerator {
    private final Random RND = new Random(126); // Possibly replace with seed from e.g. config file
    private final int RND_ORIGIN = 2; // 0 is an invalid IBAN account number and 1 is reserved for the bank
    private final int RND_BOUND = 1000000000; // Makes sure IBAN account numbers won't have more than 9 characters
    private final String COUNTRY_CODE = "NL";
    private final String BANK_CODE = "INHO0"; // Trails with zero based on IBAN standard provided in project guide
    private final int IBAN_LENGTH = 18; // (Country code) + (Check digits) + (Bank code) + (Account number) = 18
    private final int MODULO_OPERATOR = 97; // Standard modulo operator used in IBAN calculation algorithms

    // Generate a random IBAN
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String accountNumber = String.format("%09d", RND.nextInt(RND_ORIGIN, RND_BOUND));
        String IBAN = COUNTRY_CODE + "00" + BANK_CODE + accountNumber;
        int checkDigits = calculateCheckDigits(IBAN);
        IBAN = IBAN.substring(0, COUNTRY_CODE.length()) + String.format("%02d", checkDigits) + IBAN.substring(2 + COUNTRY_CODE.length());
        System.out.println(IBAN);
        return IBAN;
    }

    // Calculate the check digits for a given IBAN
    private int calculateCheckDigits(String IBAN) throws IdentifierGenerationException {
        return Math.abs(this.calculateMod97(IBAN) - (MODULO_OPERATOR + 1));
    }

    // Convert an IBAN into a remainder int using the method provided in https://en.wikipedia.org/wiki/International_Bank_Account_Number#Algorithms
    private int calculateMod97(String IBAN) throws IdentifierGenerationException {
        if (IBAN.length() != IBAN_LENGTH) {
            throw new IdentifierGenerationException("Something went wrong while generating a new IBAN");
        }

        IBAN = IBAN.substring(4) + IBAN.substring(0, 4);
        for(int i = 0; i < IBAN.length(); i++) {
            if (!Character.isDigit(IBAN.charAt(i))) {
                IBAN = IBAN.substring(0, i) + Character.getNumericValue(IBAN.charAt(i)) + IBAN.substring(i + 1);
            }
        }

        BigInteger IBANInteger = new BigInteger(IBAN);
        return IBANInteger.mod(BigInteger.valueOf(MODULO_OPERATOR)).intValue();
    }

    // Validate an IBAN by recalculating the check digits and using the mod-97 method to check if the remainder is 1.
    // If the remainder is 1, the IBAN is valid. More information is provided at https://en.wikipedia.org/wiki/International_Bank_Account_Number#Algorithms
    public boolean validateIBAN(String IBAN) throws HibernateException {
        return calculateMod97(IBAN) == 1;
    }
}
