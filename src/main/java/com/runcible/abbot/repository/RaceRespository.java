package com.runcible.abbot.repository;

import java.util.Date;
import java.util.List;

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
    
    @Query("select r from Race r where r.raceSeriesId = :seriesid order by r.raceDate")
    public List<Race> findRacesOrderByDate(@Param("seriesid") Integer seriesid);
    
    @Query("select r from Race r join r.competitions c where c.id = :competitionid and r.raceSeriesId = :seriesid")
    public List<Race> findRacesInCompetition(
            @Param("seriesid") Integer seriesid,
            @Param("competitionid") Integer competitionid);

    @Query("select r from Race r where r.fleet.id = :fleetid and r.raceSeriesId = :seriesid")
    public List<Race> findRacesForFleet(
            @Param("seriesid") Integer seriesid,
            @Param("fleetid") Integer fleetid);

//    public Race findTopOneByRaceDateLessThanOrderBy
}
