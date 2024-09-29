package com.example.inventory02;

import com.example.inventory02.model.Product;
import com.example.inventory02.repository.ProductRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class ProductView extends VerticalLayout {

    private final ProductRepository productRepository;
    private Grid<Product> grid = new Grid<>(Product.class);
    private TextField name = new TextField("Product Name");
    private TextField category = new TextField("Category");
    private TextField price = new TextField("Price");
    private TextField quantity = new TextField("Quantity");

    @Autowired
    public ProductView(ProductRepository productRepository) {
        this.productRepository = productRepository;

        grid.setColumns("id", "name", "category", "price", "quantity");

        Button addButton = new Button("Add Product", event -> addProduct());
        Button deleteButton = new Button("Delete Product", event -> deleteProduct());

        HorizontalLayout form = new HorizontalLayout(name, category, price, quantity, addButton, deleteButton);
        add(grid, form);

        updateGrid();
    }

    private void addProduct() {
        Product product = new Product();
        product.setName(name.getValue());
        product.setCategory(category.getValue());
        product.setPrice(Double.parseDouble(price.getValue()));
        product.setQuantity(Integer.parseInt(quantity.getValue()));

        productRepository.save(product);
        updateGrid();
        Notification.show("Product added successfully.");
    }

    private void deleteProduct() {
        Product selectedProduct = grid.asSingleSelect().getValue();
        if (selectedProduct != null) {
            productRepository.delete(selectedProduct);
            updateGrid();
            Notification.show("Product deleted successfully.");
        } else {
            Notification.show("Select a product to delete.");
        }
    }

    private void updateGrid() {
        grid.setItems(productRepository.findAll());
    }
}
