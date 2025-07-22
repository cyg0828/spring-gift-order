package gift.domain;

import jakarta.persistence.*;

@Entity
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    protected ProductOption() {
    }

    public ProductOption(Product product, String name, int quantity) {
        this.product = product;
        this.name = name;
        this.quantity = quantity;
    }

    public void subtract(int amount) {
        if (amount < 1 || amount > this.quantity) {
            throw new IllegalArgumentException("삭제할 수량이 잘못되었습니다.");
        }
        this.quantity -= amount;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void update(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}
