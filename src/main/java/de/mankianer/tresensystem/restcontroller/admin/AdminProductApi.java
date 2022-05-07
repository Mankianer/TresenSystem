package de.mankianer.tresensystem.restcontroller.admin;

import de.mankianer.tresensystem.entities.Product;
import de.mankianer.tresensystem.exeptions.ProductNotFoundException;
import de.mankianer.tresensystem.exeptions.order.MissingValueException;
import de.mankianer.tresensystem.services.ProductService;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/products")
public class AdminProductApi {

  private final ProductService productService;

  public AdminProductApi(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping()
  public List<Product> getAllProducts() {
    return productService.getAllProducts();
  }

  @GetMapping("{id}")
  public Product getProductById(@PathVariable long id) throws ProductNotFoundException {
    return productService.getProductById(id);
  }

  @PostMapping("")
  public Product createProduct(@RequestBody Product product) throws MissingValueException {
    return productService.createProduct(product);
  }

  @PutMapping("{id}")
  public Product updateProduct(@PathVariable long id, @RequestBody Product product) throws ProductNotFoundException, MissingValueException {
    product.setId(id);
    return productService.updateProduct(product);
  }

  @DeleteMapping("{id}")
  public Product deleteProduct(@PathVariable long id) throws ProductNotFoundException {
    return productService.deactivateProduct(id);
  }
}
