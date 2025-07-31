package gift.service;

import gift.domain.Order;
import gift.domain.ProductOption;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.repository.OrderRepository;
import gift.repository.ProductOptionRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductOptionRepository productOptionRepository;
    private final WishRepository wishRepository;
    private final KakaoMessageService kakaoMessageService;

    public OrderService(OrderRepository orderRepository,
                        ProductOptionRepository productOptionRepository,
                        WishRepository wishRepository,
                        KakaoMessageService kakaoMessageService) {
        this.orderRepository = orderRepository;
        this.productOptionRepository = productOptionRepository;
        this.wishRepository = wishRepository;
        this.kakaoMessageService = kakaoMessageService;
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        ProductOption option = productOptionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("해당 옵션이 존재하지 않습니다."));

        option.decreaseQuantity(request.getQuantity());

        Order order = new Order(option, request.getQuantity(), LocalDateTime.now(), request.getMessage());
        orderRepository.save(order);

        wishRepository.deleteByOptionId(option.getId());

        return OrderResponse.from(order);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다. ID: " + id));
    }

    @Transactional
    public void orderandMessage(Long optionId, int quantity, String message, String kakaoAccessToken) {
        ProductOption option = productOptionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 옵션이 존재하지 않습니다."));

        option.decreaseQuantity(quantity);

        Order order = new Order(option, quantity, LocalDateTime.now(), message);
        orderRepository.save(order);

        wishRepository.deleteByOptionId(optionId);

        kakaoMessageService.sendOrderMessage(kakaoAccessToken, order);
    }

}
