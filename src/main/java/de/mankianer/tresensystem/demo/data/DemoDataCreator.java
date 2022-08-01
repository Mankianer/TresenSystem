package de.mankianer.tresensystem.demo.data;

import de.mankianer.tresensystem.entities.Product;
import de.mankianer.tresensystem.security.entities.Authority;
import de.mankianer.tresensystem.services.ProductService;
import de.mankianer.tresensystem.services.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Profile("demo")
@Component
public class DemoDataCreator {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @PostConstruct
    public void init() {
        createDemoUser();
        createDemoCustomers();
        createDemoProducts();
    }

    /**
     * Creates demo users (User, Admin, Barkeeper, treasurer)
     */
    public void createDemoUser() {
        userService.newUser("user", "user", Authority.AuthorityEnum.USER);
        userService.newUser("admin", "admin", Authority.AuthorityEnum.ADMIN);
        userService.newUser("barkeeper", "barkeeper", Authority.AuthorityEnum.BARKEEPER);
        userService.newUser("treasurer", "treasurer", Authority.AuthorityEnum.TREASURER);
    }

    /**
     * Creates demo users
     */
    public void createDemoCustomers() {
        userService.newUser("John Wolf", "user", Authority.AuthorityEnum.USER);
        userService.newUser("Thomas Müller", "user", Authority.AuthorityEnum.USER);
        userService.newUser("Hans Jürgens", "user", Authority.AuthorityEnum.USER);
        userService.newUser("Sarah Froh", "user", Authority.AuthorityEnum.USER);
        userService.newUser("Peter Müller", "user", Authority.AuthorityEnum.USER);
        userService.newUser("Ute Pillow", "user", Authority.AuthorityEnum.USER);
    }

    /**
     * Create demo Products
     */
    @SneakyThrows
    public void createDemoProducts() {
        productService.createProduct(new Product(null, "Bier", 140, true));
        productService.createProduct(new Product(null, "Schneewittchen", 140, true));
        productService.createProduct(new Product(null, "Cola", 120, true));
        productService.createProduct(new Product(null, "Fanta", 120, true));
        productService.createProduct(new Product(null, "Sprite", 120, true));
        productService.createProduct(new Product(null, "Flasche", 250, true));
        productService.createProduct(new Product(null, "Flasche-alt", 200, false));
    }
}
