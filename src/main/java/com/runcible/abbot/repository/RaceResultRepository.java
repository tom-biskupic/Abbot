package com.runcible.abbot.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.RaceResult;

@Repository
public interface RaceResultRepository extends PagingAndSortingRepository<RaceResult, Integer>
{
	@Query("select r from RaceResult r where r.raceId = :raceid order by r.finishTime NULLS LAST, r.status")
	public Page<RaceResult> findRaceResults(@Param("raceid") Integer raceid,Pageable p);
	
	@Query("select r from RaceResult r where r.raceId = :raceid order by r.finishTime NULLS LAST, r.status")
	public List<RaceResult> findRaceResults(@Param("raceid") Integer raceid);
	
	@Query(    "select count(rs) from RaceResult rs where "+
	           "rs.boat.id = :boatId and "+
	           "rs.handicapPlace = 1 and "+
	           "rs.raceId in "+
	               "( select r.id from Race r where "+
	               "r.raceDate < :thisDate and "+
	               "r.fleet.id = :fleetId and "+
	               "r.shortCourseRace = :shortCourse and "+
	               "r.raceSeriesId = :raceSeriesId)")
	public int getWinsForBoat(
	        @Param("raceSeriesId") Integer raceSeriesId,
	        @Param("boatId")       Integer boatid,
	        @Param("fleetId")      Integer fleetid,
	        @Param("thisDate")     Date    thisDate,
	        @Param("shortCourse")  boolean shortCourse);
}
