package pl.com.bottega.cymes.reservations.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReceiptTest {

    private final Receipt emptyReceipt = new Receipt();

    @Test
    public void emptyReceiptHasZeroTotal() {
        // expect
        assertThat(emptyReceipt.getTotal()).isEqualTo(Money.zero());
    }

    @Test
    public void emptyReceiptHasNoItems() {
        // expect
        assertThat(emptyReceipt.getItems()).isEmpty();
    }

    @Test
    public void addsItemsToReceipt() {
        // when
        var newReceipt = emptyReceipt.withItem(new TicketKind("regular"), 2, Money.of(10));

        // then
        assertThat(emptyReceipt.getItems()).isEmpty();
        assertThat(newReceipt.getItems()).containsExactly(new Receipt.ReceiptItem(new TicketKind("regular"), 2, Money.of(10)));
    }

    @Test
    public void addsMultipleItemsToReceipt() {
        // when
        var newReceipt = emptyReceipt
            .withItem(new TicketKind("regular"), 2, Money.of(10))
            .withItem(new TicketKind("student"), 3, Money.of(20));

        // then
        assertThat(emptyReceipt.getItems()).isEmpty();
        assertThat(newReceipt.getItems()).containsExactly(
            new Receipt.ReceiptItem(new TicketKind("regular"), 2, Money.of(10)),
            new Receipt.ReceiptItem(new TicketKind("student"), 3, Money.of(20))
        );
    }

    @Test
    public void calculatesReceiptTotal() {
        // when
        var newReceipt = emptyReceipt
            .withItem(new TicketKind("regular"), 2, Money.of(10))
            .withItem(new TicketKind("student"), 3, Money.of(20));

        // then
        assertThat(emptyReceipt.getTotal()).isEqualTo(Money.zero());
        assertThat(newReceipt.getTotal()).isEqualTo(Money.of(80));
    }

    @Test
    public void calculatesReceiptItemsSubtotal() {
        // when
        var newReceipt = emptyReceipt
            .withItem(new TicketKind("regular"), 2, Money.of(10))
            .withItem(new TicketKind("student"), 3, Money.of(20));

        // then
        assertThat(newReceipt.getItems().stream().map(Receipt.ReceiptItem::getItemTotal))
            .containsExactly(Money.of(20), Money.of(60));
    }
}
