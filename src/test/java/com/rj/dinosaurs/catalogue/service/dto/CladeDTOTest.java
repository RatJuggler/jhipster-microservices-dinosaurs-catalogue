package com.rj.dinosaurs.catalogue.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.rj.dinosaurs.catalogue.web.rest.TestUtil;

public class CladeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CladeDTO.class);
        CladeDTO cladeDTO1 = new CladeDTO();
        cladeDTO1.setId(1L);
        CladeDTO cladeDTO2 = new CladeDTO();
        assertThat(cladeDTO1).isNotEqualTo(cladeDTO2);
        cladeDTO2.setId(cladeDTO1.getId());
        assertThat(cladeDTO1).isEqualTo(cladeDTO2);
        cladeDTO2.setId(2L);
        assertThat(cladeDTO1).isNotEqualTo(cladeDTO2);
        cladeDTO1.setId(null);
        assertThat(cladeDTO1).isNotEqualTo(cladeDTO2);
    }
}
