package gift.dto;

import gift.domain.Wish;

public class WishDto {
    private final String productName;
    private final String optionName;
    private final int optionQuantity;

    public WishDto(Wish wish) {
        this.productName = wish.getProduct().getName();
        if (wish.getOption() != null) {
            this.optionName = wish.getOption().getName();
            this.optionQuantity = wish.getOption().getQuantity();
        } else {
            this.optionName = "-";
            this.optionQuantity = 0;
        }
    }

    public String getProductName() {
        return productName;
    }

    public String getOptionName() {
        return optionName;
    }

    public int getQuantity() {
        return optionQuantity;
    }
}
