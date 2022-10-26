package pl.com.bottega.ecom.catalog;

import javax.persistence.ManyToOne;

class Product {

    @ManyToOne
    Category category;

}
