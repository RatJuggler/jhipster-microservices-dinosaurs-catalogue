package com.rj.dinosaurs.catalogue.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.rj.dinosaurs.catalogue.web.rest.TestUtil;

public class EpochTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Epoch.class);
        Epoch epoch1 = new Epoch();
        epoch1.setId(1L);
        Epoch epoch2 = new Epoch();
        epoch2.setId(epoch1.getId());
        assertThat(epoch1).isEqualTo(epoch2);
        epoch2.setId(2L);
        assertThat(epoch1).isNotEqualTo(epoch2);
        epoch1.setId(null);
        assertThat(epoch1).isNotEqualTo(epoch2);
    }
}
