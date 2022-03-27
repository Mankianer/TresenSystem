package de.mankianer.tresensystem.services;

import de.mankianer.tresensystem.entities.Product;
import de.mankianer.tresensystem.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  /**
   * Get all products.
   * @return
   */
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  /**
   * Get product by id.
   * @param id
   * @return
   */
  public Product getProductById(Long id) {
    return productRepository.findById(id).orElse(null);
  }

  /**
   * create new Product.
   * @param product
   * @return saved Product
   */
  public Product createProduct(Product product) {
    if(product.getId() == null) {
      return productRepository.save(product);
    }
    throw new IllegalArgumentException("Product must not have an id.");
  }

  /**
   * update a Product.
   * @param product
   * @return updated Product
   */
  public Product updateProduct(Product product) {
    if(product.getId() != null) {
      return productRepository.save(product);
    }
    throw new IllegalArgumentException("Product must have an id.");
  }
}
