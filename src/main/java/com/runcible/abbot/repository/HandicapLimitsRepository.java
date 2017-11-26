package com.runcible.abbot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.HandicapLimit;

@Repository
public interface HandicapLimitsRepository extends CrudRepository<HandicapLimit, Integer>
{
    @Query("select h from HandicapLimit h where h.seriesid = :seriesid and h.fleet.id = :fleetid")
    public HandicapLimit findByFleetID(
            @Param("seriesid")  Integer seriesid,
            @Param("fleet")     Integer fleetID);
}
