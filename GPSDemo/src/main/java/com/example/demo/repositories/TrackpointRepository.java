package com.example.demo.repositories;

import com.example.demo.domain.Trackpoint;
import org.springframework.data.repository.CrudRepository;

public interface TrackpointRepository extends CrudRepository<Trackpoint, Integer> {
}
