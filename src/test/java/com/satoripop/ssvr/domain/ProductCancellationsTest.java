package com.satoripop.ssvr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.ssvr.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProductCancellationsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductCancellations.class);
        ProductCancellations productCancellations1 = new ProductCancellations();
        productCancellations1.setId(UUID.randomUUID());
        ProductCancellations productCancellations2 = new ProductCancellations();
        productCancellations2.setId(productCancellations1.getId());
        assertThat(productCancellations1).isEqualTo(productCancellations2);
        productCancellations2.setId(UUID.randomUUID());
        assertThat(productCancellations1).isNotEqualTo(productCancellations2);
        productCancellations1.setId(null);
        assertThat(productCancellations1).isNotEqualTo(productCancellations2);
    }
}
