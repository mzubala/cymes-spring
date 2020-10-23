package pl.com.bottega.cymes.reservations.domain;

// stefan.nowak+abcd@gmail.com
// stefan.nowak+efgh@gmail.com
public class Email {

    private final String normalizedAddress;
    private final String address;

    private Email(String normalizedAddress, String address) {
        this.normalizedAddress = normalizedAddress;
        this.address = address;
    }

    public static Email parse(String address) {
        return null;
    }

    public String domain() {
        return null;
    }

    public String username() {
        return null;
    }

    public String normalizedAddress() {
        return null;
    }


    public Email alias(String alias) {
        return null;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
