package com.runcible.abbot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.Fleet;

@Repository
public interface BoatRepository extends CrudRepository<Boat, Integer> 
{
    @Query("select b from Boat b where b.raceSeriesID = :seriesid")
    public Page<Boat> findByRaceSeries(@Param("seriesid") Integer seriesid, Pageable pageable);
     
    @Query("select b from Boat b where b.raceSeriesID = :seriesid and b.boatClass.id = :boatclassid and b.division.id = :divisionid")
    public List<Boat> findBoatByClassAndDivision(
    		@Param("seriesid") Integer seriesId,
    		@Param("boatclassid") Integer boatClassId,
    		@Param("divisionid") Integer divisionId );
    
    @Query("select b from Boat b where b.raceSeriesID = :seriesid and b.boatClass.id = :boatclassid")
    public List<Boat> findBoatByClass(
    		@Param("seriesid") Integer seriesId,
    		@Param("boatclassid") Integer boatClassId );

}
