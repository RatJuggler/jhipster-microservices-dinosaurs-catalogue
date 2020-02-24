package com.rj.dinosaurs.catalogue.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.rj.dinosaurs.catalogue.web.rest.TestUtil;

public class DinosaurTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dinosaur.class);
        Dinosaur dinosaur1 = new Dinosaur();
        dinosaur1.setId(1L);
        Dinosaur dinosaur2 = new Dinosaur();
        dinosaur2.setId(dinosaur1.getId());
        assertThat(dinosaur1).isEqualTo(dinosaur2);
        dinosaur2.setId(2L);
        assertThat(dinosaur1).isNotEqualTo(dinosaur2);
        dinosaur1.setId(null);
        assertThat(dinosaur1).isNotEqualTo(dinosaur2);
    }
}
