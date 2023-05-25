package nl.inholland.codegeneration.models;

public enum AccountType {
    CURRENT(0),
    SAVINGS(1);

    private final int value;

    private AccountType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
