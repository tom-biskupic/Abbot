package com.runcible.abbot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.ExportConfig;
import com.runcible.abbot.service.exceptions.NoSuchExportConfig;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

public interface ExportConfigService
{
    /**
     * Returns a page of export configurations for the given race series.
     * @param raceSeriesId  The race series to query
     * @param pageable      Paging parameters
     * @return A page of {@link ExportConfig} records
     */
    Page<ExportConfig> getAllForSeries(Integer raceSeriesId, Pageable pageable)
            throws NoSuchUser, UserNotPermitted;

    /**
     * Returns all export configurations for the given race series as a list.
     * @param raceSeriesId  The race series to query
     * @return All {@link ExportConfig} records for the series
     */
    List<ExportConfig> getAllForSeries(Integer raceSeriesId)
            throws NoSuchUser, UserNotPermitted;

    /**
     * Returns the export configuration with the given ID.
     * @param id  The export config ID
     * @return The matching {@link ExportConfig}
     * @throws NoSuchExportConfig if no config with that ID exists
     */
    ExportConfig getById(Integer id)
            throws NoSuchExportConfig, NoSuchUser, UserNotPermitted;

    /**
     * Adds a new export configuration to the given race series.
     * @param raceSeriesId  The race series to add the config to
     * @param exportConfig  The config to persist
     */
    void addExportConfig(Integer raceSeriesId, ExportConfig exportConfig)
            throws NoSuchUser, UserNotPermitted;

    /**
     * Updates an existing export configuration.
     * @param exportConfig  The config to update (must have a valid ID)
     */
    void updateExportConfig(ExportConfig exportConfig)
            throws NoSuchExportConfig, NoSuchUser, UserNotPermitted;

    /**
     * Removes the export configuration with the given ID.
     * @param id  The export config ID to remove
     * @throws NoSuchExportConfig if no config with that ID exists
     */
    void removeExportConfig(Integer id)
            throws NoSuchExportConfig, NoSuchUser, UserNotPermitted;
}
