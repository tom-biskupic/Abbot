package com.runcible.abbot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.ExportConfig;

@Repository
public interface ExportConfigRepository extends CrudRepository<ExportConfig, Integer>
{
    @Query("select e from ExportConfig e where e.raceSeriesId = :raceSeriesId")
    Page<ExportConfig> findByRaceSeries(@Param("raceSeriesId") Integer raceSeriesId, Pageable pageable);

    @Query("select e from ExportConfig e where e.raceSeriesId = :raceSeriesId")
    List<ExportConfig> findByRaceSeries(@Param("raceSeriesId") Integer raceSeriesId);
}
