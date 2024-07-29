package com.satoripop.ssvr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductCancellationsMapperTest {

    private ProductCancellationsMapper productCancellationsMapper;

    @BeforeEach
    public void setUp() {
        productCancellationsMapper = new ProductCancellationsMapperImpl();
    }
}
