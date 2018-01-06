package com.runcible.abbot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.HandicapLimit;

@Repository
public interface HandicapLimitsRepository extends PagingAndSortingRepository<HandicapLimit, Integer>
{
    @Query("select h from HandicapLimit h where h.raceSeriesID = :seriesid and h.fleet.id = :fleetid")
    public HandicapLimit findByFleetID(
            @Param("seriesid")  Integer seriesid,
            @Param("fleetid")   Integer fleetID);
    
    @Query("select h from HandicapLimit h where h.raceSeriesID = :seriesid")    
    public Page<HandicapLimit> getHandicapLimits(@Param("seriesid") Integer raceSeriesId, Pageable page);
}
