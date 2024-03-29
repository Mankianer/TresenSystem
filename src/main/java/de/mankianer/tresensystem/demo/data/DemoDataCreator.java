package de.mankianer.tresensystem.demo.data;

import de.mankianer.tresensystem.entities.Order;
import de.mankianer.tresensystem.entities.OrderPosition;
import de.mankianer.tresensystem.entities.OrderPositionID;
import de.mankianer.tresensystem.entities.Product;
import de.mankianer.tresensystem.security.entities.Authority;
import de.mankianer.tresensystem.services.OrderService;
import de.mankianer.tresensystem.services.ProductService;
import de.mankianer.tresensystem.services.UserService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Profile("demo")
@Component
public class DemoDataCreator {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @PostConstruct
    public void init() {
        log.warn("Starting demo data creation");
        createDemoUser();
        createDemoCustomers();
        createDemoProducts();
        createDemoOrders();
    }

    /**
     * Creates demo users (User, Admin, Barkeeper, treasurer)
     */
    @SneakyThrows
    public void createDemoUser() {
        userService.createUser(userService.newUserObject("user", "user", Authority.AuthorityEnum.USER));
        userService.createUser(userService.newUserObject("admin", "admin", Authority.AuthorityEnum.ADMIN));
        userService.createUser(userService.newUserObject("barkeeper", "barkeeper", Authority.AuthorityEnum.BARKEEPER));
        userService.createUser(userService.newUserObject("treasurer", "treasurer", Authority.AuthorityEnum.TREASURER));
    }

    /**
     * Creates demo users
     */
    @SneakyThrows
    public void createDemoCustomers() {
        userService.createUser(userService.newUserObject("John Wolf", "user", Authority.AuthorityEnum.USER));
        userService.createUser(userService.newUserObject("Thomas Müller", "user", Authority.AuthorityEnum.USER));
        userService.createUser(userService.newUserObject("Hans Jürgens", "user", Authority.AuthorityEnum.USER));
        userService.createUser(userService.newUserObject("Sarah Froh", "user", Authority.AuthorityEnum.USER));
        userService.createUser(userService.newUserObject("Peter Müller", "user", Authority.AuthorityEnum.USER));
        userService.createUser(userService.newUserObject("Ute Pillow", "user", Authority.AuthorityEnum.USER));
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

    @SneakyThrows
    public void createDemoOrders() {
        Order order = new Order();
        order.setPurchaser(userService.findUser("John Wolf"));
        order.setCreatedAt(LocalDateTime.now());
        order.setCreatedByUser(false);
        OrderPosition orderPosition = new OrderPosition(new OrderPositionID(order, productService.getProductById(11L)), 2);
        order.setPositions(List.of(orderPosition));
        orderService.createOrderByBarkeeper(order);

        order = new Order();
        order.setPurchaser(userService.findUser("Sarah Froh"));
        order.setCreatedAt(LocalDateTime.now());
        order.setCreatedByUser(false);
        orderPosition = new OrderPosition(new OrderPositionID(order, productService.getProductById(11L)), 4);
        OrderPosition orderPosition2 = new OrderPosition(new OrderPositionID(order, productService.getProductById(12L)), 2);
        order.setPositions(List.of(orderPosition, orderPosition2));
        orderService.createOrderByBarkeeper(order);
    }
}
