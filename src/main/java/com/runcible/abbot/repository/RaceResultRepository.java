package com.runcible.abbot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.RaceResult;

@Repository
public interface RaceResultRepository extends PagingAndSortingRepository<RaceResult, Integer>
{
	@Query("select r from RaceResult r where r.raceId = :raceid")
	public Page<RaceResult> findRacesResults(@Param("raceid") Integer raceid,Pageable p);
}
