package com.satoripop.ssvr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.ssvr.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PermissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Permission.class);
        Permission permission1 = new Permission();
        permission1.setId(UUID.randomUUID());
        Permission permission2 = new Permission();
        permission2.setId(permission1.getId());
        assertThat(permission1).isEqualTo(permission2);
        permission2.setId(UUID.randomUUID());
        assertThat(permission1).isNotEqualTo(permission2);
        permission1.setId(null);
        assertThat(permission1).isNotEqualTo(permission2);
    }
}
