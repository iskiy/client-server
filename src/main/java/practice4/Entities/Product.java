package practice4.Entities;

import java.util.Objects;

public class Product {
    private Integer id;
    private String name;
    private double price;
    private double amount;

    public Product(Integer id, String name, double price, double amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public Product(String name) {
        this.id = null;
        this.name = name;
        this.price = 0;
        this.amount = 0;
    }

    public Product(String name, double price, double amount) {
        this.id = null;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public Product() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isValidProduct(){
        return price >= 0 && amount >= 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 && Double.compare(product.amount, amount) == 0 && Objects.equals(id, product.id) && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, amount);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }
}
