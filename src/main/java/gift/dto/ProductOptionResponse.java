package gift.dto;

import gift.domain.ProductOption;

public class ProductOptionResponse {

    private Long id;
    private String name;
    private int quantity;

    public ProductOptionResponse(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public static ProductOptionResponse from(ProductOption option) {
        return new ProductOptionResponse(option.getId(), option.getName(), option.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
