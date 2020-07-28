package com.example.demo.repositories;

import com.example.demo.domain.Metadata;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends CrudRepository<Metadata,Integer> {
}
