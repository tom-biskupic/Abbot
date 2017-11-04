package com.runcible.abbot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.Handicap;

@Repository
public interface HandicapRepository extends CrudRepository<Handicap, Integer>
{
    public Handicap findByBoatID(Integer boatID);
}
