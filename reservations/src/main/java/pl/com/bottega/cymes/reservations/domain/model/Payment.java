package pl.com.bottega.cymes.reservations.domain.model;

public abstract class Payment {

    private Payment() {
    }

    static Payment of(PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case ONLINE:
                return new OnlinePayment();
            case OFFLINE:
                return new OfflinePayment();
            default:
                throw new IllegalArgumentException();
        }
    }

    public abstract PaymentMethod getMethod();

    static final class OfflinePayment extends Payment {

        @Override
        public PaymentMethod getMethod() {
            return PaymentMethod.OFFLINE;
        }
    }

    static final class OnlinePayment extends Payment {

        @Override
        public PaymentMethod getMethod() {
            return PaymentMethod.ONLINE;
        }
    }
}
