package com.rj.dinosaurs.catalogue.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link DinosaurSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DinosaurSearchRepositoryMockConfiguration {

    @MockBean
    private DinosaurSearchRepository mockDinosaurSearchRepository;

}
