package com.runcible.abbot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.Boat;

@Repository
public interface BoatRepository extends CrudRepository<Boat, Integer> 
{
    @Query("select b from Boat b where b.raceSeriesID = :seriesid")
    public Page<Boat> findByRaceSeries(@Param("seriesid") Integer seriesid, Pageable pageable);
}
