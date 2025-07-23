package gift.repository;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class WishRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishRepository wishRepository;

    @Test
    @DisplayName("회원과 상품으로 위시 생성 및 조회")
    void saveAndFindWish() {
        Member member = memberRepository.save(new Member("wish@test.com", "pass123"));
        Product product = productRepository.save(new Product("상품", 10000, "url"));
        Wish wish = wishRepository.save(new Wish(member, product));

        Optional<Wish> result = wishRepository.findByMemberAndProduct(member, product);

        assertThat(result).isPresent();
        assertThat(result.get().getMember().getEmail()).isEqualTo(member.getEmail());
        assertThat(result.get().getProduct().getName()).isEqualTo(product.getName());
    }

    @Test
    @DisplayName("회원의 위시리스트 전체 조회")
    void findByMember() {
        Member member = memberRepository.save(new Member("list@test.com", "pw"));
        Product p1 = productRepository.save(new Product("p1", 1, "url1"));
        Product p2 = productRepository.save(new Product("p2", 2, "url2"));
        wishRepository.save(new Wish(member, p1));
        wishRepository.save(new Wish(member, p2));

        List<Wish> wishes = wishRepository.findByMember(member);

        assertThat(wishes).hasSize(2);
    }
}
