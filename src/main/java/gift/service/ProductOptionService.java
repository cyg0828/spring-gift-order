package gift.service;

import gift.domain.Product;
import gift.domain.ProductOption;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class ProductOptionService {

    private final ProductOptionRepository optionRepository;
    private final ProductRepository productRepository;

    public ProductOptionService(ProductOptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<ProductOption> getOptionsByProduct(Long productId) {
        return optionRepository.findByProductId(productId);
    }

    public void subtractQuantity(Long optionId, int amount) {
        ProductOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("해당 옵션을 찾을 수 없습니다."));
        option.subtract(amount);
    }

    public ProductOption createOption(Long productId, String name, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다."));

        validateName(name);
        validateQuantity(quantity);
        validateDuplicateName(product, name);

        ProductOption option = new ProductOption(product, name, quantity);
        product.addOption(option);
        return optionRepository.save(option);
    }

    private static final Pattern VALID_NAME_PATTERN =
            Pattern.compile("^[\\w가-힣\\s\\(\\)\\[\\]\\+\\-\\&\\/]{1,50}$");


    public void validateName(String name) {
        if (name == null || !VALID_NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("옵션 이름이 잘못되었습니다.");
        }
    }

    public void validateQuantity(int quantity) {
        if (quantity < 1 || quantity >= 100_000_000) {
            throw new IllegalArgumentException("옵션 수량은 1 이상 1억 미만이어야 합니다.");
        }
    }

    public boolean isDuplicateName(Product product, String newName) {
        return product.getOptions().stream()
                .anyMatch(option -> option.getName().equals(newName));
    }

    public void validateDuplicateName(Product product, String newName) {
        if (isDuplicateName(product, newName)) {
            throw new IllegalArgumentException("해당 상품에 동일한 옵션 이름이 존재합니다.");
        }
    }
}
