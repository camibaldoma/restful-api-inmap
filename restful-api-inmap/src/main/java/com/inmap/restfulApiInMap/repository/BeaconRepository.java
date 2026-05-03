package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.entity.Beacon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeaconRepository extends JpaRepository<Beacon,Long> {
}
