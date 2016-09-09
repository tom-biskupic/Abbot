package com.runcible.abbot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.Fleet;

@Repository
public interface FleetRespository extends CrudRepository<Fleet, Integer> 
{
    @Query("select f from Fleet f where f.raceSeriesId = :seriesid")
    public Page<Fleet> findByRaceSeries(@Param("seriesid") Integer seriesid, Pageable pageable);

    @Query("select f from Fleet f where f.raceSeriesId = :seriesid")
	public List<Fleet> findByRaceSeries(@Param("seriesid") Integer raceSeriesId);
}
