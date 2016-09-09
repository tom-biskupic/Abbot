package com.runcible.abbot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.BoatClass;

@Repository
public interface BoatClassRepository extends PagingAndSortingRepository<BoatClass, Integer>
{
    @Query("select b from BoatClass b where b.raceSeriesId = :seriesid")
    public Page<BoatClass> findByRaceSeries(@Param("seriesid") Integer seriesid, Pageable pageable);
    
    @Query("select b from BoatClass b where b.raceSeriesId = :seriesid")
    public List<BoatClass> findByRaceSeries(@Param("seriesid") Integer seriesid);
}
