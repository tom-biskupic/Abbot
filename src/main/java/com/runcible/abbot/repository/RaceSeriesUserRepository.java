package com.runcible.abbot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.RaceSeriesUser;

@Repository
public interface RaceSeriesUserRepository extends CrudRepository<RaceSeriesUser, Integer>
{
    /**
     * Gets the number of user entries for the series and user ID specified. Will only ever be
     * 0 or 1 but this is used to determine if the user is authorized (saves fetching the user
     * record for nothing)
     * @param series The id of the series we are checking
     * @param userId The id of the user we are checking in that series
     * @return 0 if the user is not authorized and 1 otherwise.
     */
    @Query("select count(r) from RaceSeriesUser r where r.raceSeries.id = :seriesId and r.user.id = :userId")
    Long countUserEntriesForRaceSeries(
            @Param("seriesId") Integer series,
            @Param("userId") Integer userId );
    
    /**
     * Returns the count of all user authorizations for a series. Used to test if a series
     * is new (unowned).
     * @param series The id of the series in question
     * @return the number of authorization records.
     */
    @Query("select count(r) from RaceSeriesUser r where r.raceSeries.id = :seriesId")
    Long countUserEntriesForRaceSeries(
            @Param("seriesId") Integer series );

    @Query("select r from RaceSeriesUser r where r.raceSeries.id = :seriesId")
    Page<RaceSeriesUser> getByRaceSeriesId(@Param("seriesId") Integer seriesId, Pageable page);

    @Modifying
    @Query("delete from RaceSeriesUser r where r.raceSeries.id = :seriesId and r.user.id = :userId")
    void removeUser(
            @Param("seriesId") Integer series,
            @Param("userId") Integer userId );
}
