package pl.com.bottega.cymes.reservations.domain.model.model.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.LinkedList;
import java.util.List;

@Value
@AllArgsConstructor
public class Receipt {

    List<ReceiptItem> items;

    public Receipt() {
        items = new LinkedList<>();
    }

    public Receipt withItem(TicketKind ticketKind, int count, Money price) {
        List<ReceiptItem> newItems = new LinkedList<>(items);
        items.add(new ReceiptItem(ticketKind, count, price));
        return new Receipt(newItems);
    }

    @Value
    static class ReceiptItem {
        TicketKind ticketKind;
        int count;
        Money price;
    }
}
