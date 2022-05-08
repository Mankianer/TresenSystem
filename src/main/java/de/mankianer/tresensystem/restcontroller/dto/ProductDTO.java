package de.mankianer.tresensystem.restcontroller.dto;

import de.mankianer.tresensystem.entities.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
public class ProductDTO {

    private Long id;
    @NonNull
    private String name;
    //price in cents
    @NonNull
    private Integer price;
    //is product available
    private boolean available = true;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.available = product.isAvailable();
    }

    public Product toProduct() {
        return new Product(this.id, this.name, this.price, this.available);
    }
}
