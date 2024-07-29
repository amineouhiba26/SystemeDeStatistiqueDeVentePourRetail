package com.satoripop.ssvr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.ssvr.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserConfTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserConf.class);
        UserConf userConf1 = new UserConf();
        userConf1.setId(UUID.randomUUID());
        UserConf userConf2 = new UserConf();
        userConf2.setId(userConf1.getId());
        assertThat(userConf1).isEqualTo(userConf2);
        userConf2.setId(UUID.randomUUID());
        assertThat(userConf1).isNotEqualTo(userConf2);
        userConf1.setId(null);
        assertThat(userConf1).isNotEqualTo(userConf2);
    }
}
