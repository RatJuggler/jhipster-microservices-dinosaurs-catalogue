package com.rj.dinosaurs.catalogue.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CladeMapperTest {

    private CladeMapper cladeMapper;

    @BeforeEach
    public void setUp() {
        cladeMapper = new CladeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(cladeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(cladeMapper.fromId(null)).isNull();
    }
}
