package com.rj.dinosaurs.catalogue.repository.search;

import com.rj.dinosaurs.catalogue.domain.Epoch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Epoch} entity.
 */
public interface EpochSearchRepository extends ElasticsearchRepository<Epoch, Long> {
}
