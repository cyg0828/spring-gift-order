package gift.controller;

import gift.dto.ProductOptionResponse;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<ProductResponse> getAll(Pageable pageable) {
        return productService.findAll(pageable);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse addProduct(@Valid @RequestBody ProductRequest request) {
        return productService.save(request);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@Valid @PathVariable Long id, @RequestBody ProductRequest request) {
        return productService.update(id, request, null, null, null, null, null);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.delete(id);
    }
  
    @GetMapping("/{id}/options")
    public List<ProductOptionResponse> getOptions(@PathVariable Long id) {
        return productService.findOptionsByProductId(id);
    }
}

