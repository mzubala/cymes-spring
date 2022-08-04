package pl.com.bottega.cymes.reservations.domain.model.model;

import pl.com.bottega.cymes.reservations.domain.model.model.model.Money;
import pl.com.bottega.cymes.reservations.domain.model.model.model.Receipt;
import pl.com.bottega.cymes.reservations.domain.model.model.model.TicketKind;

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
