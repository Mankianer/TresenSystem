package de.mankianer.tresensystem.restcontroller.barkeeper;

import de.mankianer.tresensystem.exeptions.ProductNotFoundException;
import de.mankianer.tresensystem.restcontroller.dto.ProductDTO;
import de.mankianer.tresensystem.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("bar/product/")
public class BarkeeperProductApi {

  private final ProductService productService;

  public BarkeeperProductApi(ProductService productService) {
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
}
