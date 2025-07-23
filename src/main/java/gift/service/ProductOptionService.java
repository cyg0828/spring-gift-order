package gift.service;

import gift.domain.Product;
import gift.domain.ProductOption;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
        ProductOption option = new ProductOption(product, name, quantity);
        product.addOption(option);
        return optionRepository.save(option);
    }





}
