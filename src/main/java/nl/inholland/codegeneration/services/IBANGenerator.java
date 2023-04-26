package nl.inholland.codegeneration.services;

import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializerBase;
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
    private final long RND_SEED = 126L;
    private final Random rnd = new Random(RND_SEED);
    private final String COUNTRY_CODE = "NL";
    private final String BANK_CODE = "INHO0";
    private final int IBAN_LENGTH = 18;
    private final int MODULO_OPERATOR = 97;

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String accountNumber = String.format("%09d", rnd.nextInt(2, 1000000000)); // Replace with finals
//        System.out.println("1: " + accountNumber);
        String IBAN = COUNTRY_CODE + "00" + BANK_CODE + accountNumber;
//        System.out.println("2: " + IBAN);
        int checkDigits = calculateCheckDigits(IBAN);
        IBAN = IBAN.substring(0, COUNTRY_CODE.length()) + String.format("%02d", checkDigits) + IBAN.substring(2 + COUNTRY_CODE.length());
        return IBAN;
    }

    private int calculateCheckDigits(String IBAN) throws IdentifierGenerationException {
        BigInteger IBANInteger = this.calculateIBANInteger(IBAN);
        int remainder = IBANInteger.mod(BigInteger.valueOf(MODULO_OPERATOR)).intValue();
        return Math.abs(remainder - (MODULO_OPERATOR + 1));
    }

    private BigInteger calculateIBANInteger(String IBAN) throws IdentifierGenerationException {
        if (IBAN.length() != IBAN_LENGTH) {
            throw new IdentifierGenerationException("Something went wrong while generating a new IBAN");
        }

        // DIT HOEFT MISSCHIEN NIET ALS HET BEREKENEN VAN DE IBANINTEGER MET CHECKDIGITS MODULO 97 UITKOMT OP 1!!!!!
//        if (!IBAN.substring(COUNTRY_CODE.length(), COUNTRY_CODE.length() + 2).equals("00")) {
//            IBAN = IBAN.substring(0, COUNTRY_CODE.length()) + "00" + IBAN.substring(2 + COUNTRY_CODE.length());
//        }

        IBAN = IBAN.substring(4) + IBAN.substring(0, 4);
//        System.out.println("3: " + IBAN);

        for(int i = 0; i < IBAN.length(); i++) {
            if (!Character.isDigit(IBAN.charAt(i))) {
                IBAN = IBAN.substring(0, i) + Character.getNumericValue(IBAN.charAt(i)) + IBAN.substring(i + 1);
            }
        }
//        System.out.println("4: " + IBAN);

        return new BigInteger(IBAN);
    }

    public boolean validateIBAN(String IBAN) {
        BigInteger IBANInteger = this.calculateIBANInteger(IBAN);
        return IBANInteger.mod(BigInteger.valueOf(MODULO_OPERATOR)).intValue() == 1;
    }
}
