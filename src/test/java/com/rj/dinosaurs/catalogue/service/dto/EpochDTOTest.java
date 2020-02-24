package com.rj.dinosaurs.catalogue.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.rj.dinosaurs.catalogue.web.rest.TestUtil;

public class EpochDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EpochDTO.class);
        EpochDTO epochDTO1 = new EpochDTO();
        epochDTO1.setId(1L);
        EpochDTO epochDTO2 = new EpochDTO();
        assertThat(epochDTO1).isNotEqualTo(epochDTO2);
        epochDTO2.setId(epochDTO1.getId());
        assertThat(epochDTO1).isEqualTo(epochDTO2);
        epochDTO2.setId(2L);
        assertThat(epochDTO1).isNotEqualTo(epochDTO2);
        epochDTO1.setId(null);
        assertThat(epochDTO1).isNotEqualTo(epochDTO2);
    }
}
