package com.runcible.abbot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.Competition;

@Repository
public interface CompetitionRepository extends CrudRepository<Competition, Integer> 
{
    @Query("select c from Competition c where c.raceSeriesId = :seriesid")
    public Page<Competition> findByRaceSeries(@Param("seriesid") Integer seriesid, Pageable pageable);

    @Query("select c from Competition c where c.raceSeriesId = :seriesid")
    public List<Competition> findByRaceSeries(Integer raceSeriesId);
}
