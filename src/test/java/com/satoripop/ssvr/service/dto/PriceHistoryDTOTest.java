package com.satoripop.ssvr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.ssvr.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PriceHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PriceHistoryDTO.class);
        PriceHistoryDTO priceHistoryDTO1 = new PriceHistoryDTO();
        priceHistoryDTO1.setId(UUID.randomUUID());
        PriceHistoryDTO priceHistoryDTO2 = new PriceHistoryDTO();
        assertThat(priceHistoryDTO1).isNotEqualTo(priceHistoryDTO2);
        priceHistoryDTO2.setId(priceHistoryDTO1.getId());
        assertThat(priceHistoryDTO1).isEqualTo(priceHistoryDTO2);
        priceHistoryDTO2.setId(UUID.randomUUID());
        assertThat(priceHistoryDTO1).isNotEqualTo(priceHistoryDTO2);
        priceHistoryDTO1.setId(null);
        assertThat(priceHistoryDTO1).isNotEqualTo(priceHistoryDTO2);
    }
}
