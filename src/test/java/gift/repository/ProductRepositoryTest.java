package gift.repository;

import gift.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 저장 및 조회")
    void saveAndFind() {
        Product product = new Product("선물세트", 15000, "https://image.url/item.jpg");
        Product saved = productRepository.save(product);

        Product found = productRepository.findById(saved.getId()).orElseThrow();

        assertThat(found.getName()).isEqualTo(product.getName());
        assertThat(found.getPrice()).isEqualTo(product.getPrice());
        assertThat(found.getImageUrl()).isEqualTo(product.getImageUrl());
    }
}
