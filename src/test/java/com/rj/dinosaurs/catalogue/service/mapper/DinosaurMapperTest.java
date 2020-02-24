package com.rj.dinosaurs.catalogue.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DinosaurMapperTest {

    private DinosaurMapper dinosaurMapper;

    @BeforeEach
    public void setUp() {
        dinosaurMapper = new DinosaurMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(dinosaurMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(dinosaurMapper.fromId(null)).isNull();
    }
}
