package com.runcible.abbot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.model.User;

@Repository
public interface RaceSeriesRepository extends CrudRepository<RaceSeries, Integer>
{
    @Query("select r from RaceSeries r, RaceSeriesUser ru where ru.raceSeries.id = r.id and ru.user.id = :userId")
    public Page<RaceSeries> findByPermittedUsers(@Param("userId") Integer userId ,Pageable pageable);
}