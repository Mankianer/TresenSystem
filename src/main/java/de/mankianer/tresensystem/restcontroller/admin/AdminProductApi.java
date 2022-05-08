package de.mankianer.tresensystem.restcontroller.admin;

import de.mankianer.tresensystem.exeptions.ProductNotAvailableException;
import de.mankianer.tresensystem.exeptions.ProductNotFoundException;
import de.mankianer.tresensystem.exeptions.order.MissingValueException;
import de.mankianer.tresensystem.restcontroller.dto.ProductDTO;
import de.mankianer.tresensystem.services.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/product/")
public class AdminProductApi {

  private final ProductService productService;

  public AdminProductApi(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping()
  public List<ProductDTO> getAllProducts() {
    return productService.getAllProducts().stream().map(ProductDTO::new).toList();
  }

  @GetMapping("{id}")
  public ProductDTO getProductById(@PathVariable long id) throws ProductNotFoundException {
    return new ProductDTO(productService.getProductById(id));
  }

  @PostMapping("")
  public ProductDTO createProduct(@RequestBody ProductDTO product) throws MissingValueException {
    return new ProductDTO(productService.createProduct(product.toProduct()));
  }

  @PutMapping("{id}")
  public ProductDTO updateProduct(@PathVariable long id, @RequestBody ProductDTO product) throws ProductNotFoundException, MissingValueException, ProductNotAvailableException {
    product.setId(id);
    return new ProductDTO(productService.updateProduct(product.toProduct()));
  }

  @DeleteMapping("{id}")
  public ProductDTO deleteProduct(@PathVariable long id) throws ProductNotFoundException {
    return new ProductDTO(productService.deactivateProduct(id));
  }
}
