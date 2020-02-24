package com.rj.dinosaurs.catalogue.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EpochMapperTest {

    private EpochMapper epochMapper;

    @BeforeEach
    public void setUp() {
        epochMapper = new EpochMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(epochMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(epochMapper.fromId(null)).isNull();
    }
}
