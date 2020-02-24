package com.rj.dinosaurs.catalogue.repository.search;

import com.rj.dinosaurs.catalogue.domain.Dinosaur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Dinosaur} entity.
 */
public interface DinosaurSearchRepository extends ElasticsearchRepository<Dinosaur, Long> {
}
