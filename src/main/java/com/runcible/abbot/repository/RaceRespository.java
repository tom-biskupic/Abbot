package com.runcible.abbot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.Race;

@Repository
public interface RaceRespository extends PagingAndSortingRepository<Race, Integer>
{
    @Query("select r from Race r where r.raceSeriesId = :seriesid")
    public Page<Race> findByRaceSeries(@Param("seriesid") Integer seriesid, Pageable pageable);
}
