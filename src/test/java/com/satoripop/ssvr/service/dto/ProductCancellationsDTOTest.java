package com.satoripop.ssvr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.ssvr.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProductCancellationsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductCancellationsDTO.class);
        ProductCancellationsDTO productCancellationsDTO1 = new ProductCancellationsDTO();
        productCancellationsDTO1.setId(UUID.randomUUID());
        ProductCancellationsDTO productCancellationsDTO2 = new ProductCancellationsDTO();
        assertThat(productCancellationsDTO1).isNotEqualTo(productCancellationsDTO2);
        productCancellationsDTO2.setId(productCancellationsDTO1.getId());
        assertThat(productCancellationsDTO1).isEqualTo(productCancellationsDTO2);
        productCancellationsDTO2.setId(UUID.randomUUID());
        assertThat(productCancellationsDTO1).isNotEqualTo(productCancellationsDTO2);
        productCancellationsDTO1.setId(null);
        assertThat(productCancellationsDTO1).isNotEqualTo(productCancellationsDTO2);
    }
}
