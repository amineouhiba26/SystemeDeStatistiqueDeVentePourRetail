package com.satoripop.ssvr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.ssvr.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PriceHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PriceHistory.class);
        PriceHistory priceHistory1 = new PriceHistory();
        priceHistory1.setId(UUID.randomUUID());
        PriceHistory priceHistory2 = new PriceHistory();
        priceHistory2.setId(priceHistory1.getId());
        assertThat(priceHistory1).isEqualTo(priceHistory2);
        priceHistory2.setId(UUID.randomUUID());
        assertThat(priceHistory1).isNotEqualTo(priceHistory2);
        priceHistory1.setId(null);
        assertThat(priceHistory1).isNotEqualTo(priceHistory2);
    }
}
