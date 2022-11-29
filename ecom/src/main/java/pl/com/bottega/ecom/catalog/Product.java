package pl.com.bottega.ecom.catalog;

import jakarta.persistence.ManyToOne;

class Product {

    @ManyToOne
    Category category;

}
