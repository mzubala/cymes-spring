package pl.com.bottega.cymes.reservations.domain.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Value
@AllArgsConstructor
public class Receipt {

    List<ReceiptItem> items;

    public Receipt() {
        items = new LinkedList<>();
    }

    public Receipt withItem(TicketKind ticketKind, int count, Money price) {
        List<ReceiptItem> newItems = new LinkedList<>(items);
        newItems.add(new ReceiptItem(ticketKind, count, price));
        return new Receipt(newItems);
    }

    public Money getTotal() {
        return items.stream().map(ReceiptItem::getItemTotal).reduce(Money.zero(), Money::add);
    }

    @Value
    static class ReceiptItem {
        TicketKind ticketKind;
        int count;
        Money price;

        Money getItemTotal() {
            return price.times(count);
        }
    }
}
