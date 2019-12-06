package com.runcible.abbot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.Handicap;

@Repository
public interface HandicapRepository extends CrudRepository<Handicap, Integer>
{
    @Query("select h from Handicap h where h.boatID = :boatId and h.raceID = :raceId")
    public Handicap findByBoatAndRace(
            @Param("boatId") Integer boatID,
            @Param("raceId") Integer raceID);
}
