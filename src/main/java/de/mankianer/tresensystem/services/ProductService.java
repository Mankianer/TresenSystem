package de.mankianer.tresensystem.services;

import de.mankianer.tresensystem.entities.Product;
import de.mankianer.tresensystem.exeptions.ProductNotFoundException;
import de.mankianer.tresensystem.exeptions.order.MissingValueException;
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
   * @throws ProductNotFoundException if Product not found
   */
  public Product getProductById(Long id) throws ProductNotFoundException {
    return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found. id: " + id));
  }

  /**
   * create new Product.
   * @param product
   * @return saved Product
   * @throws MissingValueException if Product has an id
   */
  public Product createProduct(Product product) throws MissingValueException {
    if(product.getId() == null) {
      return productRepository.save(product);
    }
    throw new MissingValueException("Product must not have an id.");
  }

  /**
   * update a Product.
   * @param product
   * @return updated Product
   * @throws ProductNotFoundException if Product not found
   * @throws MissingValueException if Product has no id
   */
  public Product updateProduct(Product product)
      throws MissingValueException, ProductNotFoundException {
    if(product.getId() != null) {
      if(!productRepository.existsById(product.getId())){
        throw new ProductNotFoundException("Product not found. id: " + product.getId());
      }
      return productRepository.save(product);
    }
    throw new MissingValueException("Product must have an id.");
  }

  /**
   * delete a Product.
   * @param id
   * @throws ProductNotFoundException if Product not found
   */
  public Product deactivateProduct(Long id) throws ProductNotFoundException {
    Product product = getProductById(id);
    product.setAvailable(false);
    return productRepository.save(product);
  }
}
