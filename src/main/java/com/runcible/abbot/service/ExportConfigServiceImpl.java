package com.runcible.abbot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.runcible.abbot.model.ExportConfig;
import com.runcible.abbot.repository.ExportConfigRepository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.NoSuchExportConfig;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Component
@Transactional
public class ExportConfigServiceImpl extends AuthorizedService implements ExportConfigService
{
    @Override
    public Page<ExportConfig> getAllForSeries(Integer raceSeriesId, Pageable pageable)
            throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        return exportConfigRepo.findByRaceSeries(raceSeriesId, pageable);
    }

    @Override
    public List<ExportConfig> getAllForSeries(Integer raceSeriesId)
            throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        return exportConfigRepo.findByRaceSeries(raceSeriesId);
    }

    @Override
    public ExportConfig getById(Integer id)
            throws NoSuchExportConfig, NoSuchUser, UserNotPermitted
    {
        Optional<ExportConfig> found = exportConfigRepo.findById(id);
        if (!found.isPresent())
        {
            throw new NoSuchExportConfig();
        }
        throwIfUserNotPermitted(found.get().getRaceSeriesId());
        return found.get();
    }

    @Override
    public void addExportConfig(Integer raceSeriesId, ExportConfig exportConfig)
            throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        exportConfig.setRaceSeriesId(raceSeriesId);
        exportConfigRepo.save(exportConfig);
        auditEvent(exportConfig, AuditEventType.CREATED);
    }

    @Override
    public void updateExportConfig(ExportConfig exportConfig)
            throws NoSuchExportConfig, NoSuchUser, UserNotPermitted
    {
        if (!exportConfigRepo.findById(exportConfig.getId()).isPresent())
        {
            throw new NoSuchExportConfig();
        }
        throwIfUserNotPermitted(exportConfig.getRaceSeriesId());
        exportConfigRepo.save(exportConfig);
        auditEvent(exportConfig, AuditEventType.UPDATED);
    }

    @Override
    public void removeExportConfig(Integer id)
            throws NoSuchExportConfig, NoSuchUser, UserNotPermitted
    {
        Optional<ExportConfig> found = exportConfigRepo.findById(id);
        if (!found.isPresent())
        {
            throw new NoSuchExportConfig();
        }
        throwIfUserNotPermitted(found.get().getRaceSeriesId());
        exportConfigRepo.deleteById(id);
        auditEvent(found.get(), AuditEventType.DELETED);
    }

    private void auditEvent(ExportConfig exportConfig, AuditEventType eventType)
            throws NoSuchUser, UserNotPermitted
    {
        audit.auditEvent(
                eventType,
                exportConfig.getRaceSeriesId(),
                EXPORT_CONFIG_OBJECT_NAME,
                exportConfig.getName());
    }

    private static final String EXPORT_CONFIG_OBJECT_NAME = "ExportConfig";

    @Autowired
    private ExportConfigRepository exportConfigRepo;

    @Autowired
    private AuditService audit;
}
