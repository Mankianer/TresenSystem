package de.mankianer.tresensystem.restcontroller;

import de.mankianer.tresensystem.entities.Product;
import de.mankianer.tresensystem.exeptions.ProductNotFoundException;
import de.mankianer.tresensystem.exeptions.order.MissingValueException;
import de.mankianer.tresensystem.services.ProductService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductApi {

  private final ProductService productService;

  public ProductApi(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("/products")
  public List<Product> getAllProducts() {
    return productService.getAllProducts();
  }

  @GetMapping("/product/{id}")
  public Product getProductById(@PathVariable long id) throws ProductNotFoundException {
    return productService.getProductById(id);
  }

  @PostMapping("/product")
  public Product createProduct(@RequestBody Product product) throws MissingValueException {
    return productService.createProduct(product);
  }

  @PutMapping("/product/{id}")
  public Product updateProduct(@PathVariable long id, @RequestBody Product product) throws ProductNotFoundException, MissingValueException {
    product.setId(id);
    return productService.updateProduct(product);
  }

  @DeleteMapping("/product/{id}")
  public Product deleteProduct(@PathVariable long id) throws ProductNotFoundException {
    return productService.deactivateProduct(id);
  }
}
