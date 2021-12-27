package pl.com.bottega.cymes.reservations.domain.model;

public class ReceiptExample {

    private Receipt receipt = new Receipt();

    static ReceiptExample aReceipt() {
        return new ReceiptExample();
    }

    public ReceiptExample withTicketKind(String ticketKind, int count, Money price) {
        receipt = receipt.withItem(new TicketKind(ticketKind), count, price);
        return this;
    }

    public Receipt build() {
        return receipt;
    }
}
