package com.example.demo.repositories;

import com.example.demo.domain.Waypoint;
import org.springframework.data.repository.CrudRepository;

public interface WaypointRepository extends CrudRepository<Waypoint, Integer> {
}
