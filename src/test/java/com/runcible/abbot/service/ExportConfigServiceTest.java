package com.runcible.abbot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.ExportConfig;
import com.runcible.abbot.repository.ExportConfigRepository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.NoSuchExportConfig;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@ExtendWith(MockitoExtension.class)
public class ExportConfigServiceTest
{
    // -------------------------------------------------------------------------
    // getAllForSeries (paged)
    // -------------------------------------------------------------------------

    @Test
    public void testGetAllForSeriesPaged() throws NoSuchUser, UserNotPermitted
    {
        setupPermissionMocks(true);
        when(exportConfigRepoMock.findByRaceSeries(TEST_RACE_SERIES_ID, pageableMock)).thenReturn(exportConfigPageMock);

        Page<ExportConfig> result = fixture.getAllForSeries(TEST_RACE_SERIES_ID, pageableMock);

        assertEquals(exportConfigPageMock, result);
    }

    @Test
    public void testGetAllForSeriesPagedUserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupPermissionMocks(false);

        assertThrows(UserNotPermitted.class, () ->
                fixture.getAllForSeries(TEST_RACE_SERIES_ID, pageableMock));
    }

    // -------------------------------------------------------------------------
    // getAllForSeries (unpaged)
    // -------------------------------------------------------------------------

    @Test
    public void testGetAllForSeriesUnpaged() throws NoSuchUser, UserNotPermitted
    {
        setupPermissionMocks(true);
        when(exportConfigRepoMock.findByRaceSeries(TEST_RACE_SERIES_ID)).thenReturn(List.of(exportConfigMock));

        List<ExportConfig> result = fixture.getAllForSeries(TEST_RACE_SERIES_ID);

        assertEquals(List.of(exportConfigMock), result);
    }

    @Test
    public void testGetAllForSeriesUnpagedUserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupPermissionMocks(false);

        assertThrows(UserNotPermitted.class, () ->
                fixture.getAllForSeries(TEST_RACE_SERIES_ID));
    }

    // -------------------------------------------------------------------------
    // getById
    // -------------------------------------------------------------------------

    @Test
    public void testGetById() throws NoSuchExportConfig, NoSuchUser, UserNotPermitted
    {
        setupPermissionMocks(true);
        when(exportConfigRepoMock.findById(TEST_CONFIG_ID)).thenReturn(Optional.of(exportConfigMock));
        when(exportConfigMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);

        ExportConfig result = fixture.getById(TEST_CONFIG_ID);

        assertEquals(exportConfigMock, result);
    }

    @Test
    public void testGetByIdNoSuchExportConfig() throws NoSuchUser, UserNotPermitted
    {
        when(exportConfigRepoMock.findById(TEST_CONFIG_ID)).thenReturn(Optional.empty());

        assertThrows(NoSuchExportConfig.class, () ->
                fixture.getById(TEST_CONFIG_ID));
    }

    @Test
    public void testGetByIdUserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupPermissionMocks(false);
        when(exportConfigRepoMock.findById(TEST_CONFIG_ID)).thenReturn(Optional.of(exportConfigMock));
        when(exportConfigMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);

        assertThrows(UserNotPermitted.class, () ->
                fixture.getById(TEST_CONFIG_ID));
    }

    // -------------------------------------------------------------------------
    // addExportConfig
    // -------------------------------------------------------------------------

    @Test
    public void testAddExportConfig() throws NoSuchUser, UserNotPermitted
    {
        setupPermissionMocks(true);
        when(exportConfigMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(exportConfigMock.getName()).thenReturn(TEST_CONFIG_NAME);

        fixture.addExportConfig(TEST_RACE_SERIES_ID, exportConfigMock);

        verify(exportConfigMock).setRaceSeriesId(TEST_RACE_SERIES_ID);
        verify(exportConfigRepoMock).save(exportConfigMock);
        verifyAuditEvent(AuditEventType.CREATED);
    }

    @Test
    public void testAddExportConfigUserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupPermissionMocks(false);

        assertThrows(UserNotPermitted.class, () ->
                fixture.addExportConfig(TEST_RACE_SERIES_ID, exportConfigMock));
    }

    // -------------------------------------------------------------------------
    // updateExportConfig
    // -------------------------------------------------------------------------

    @Test
    public void testUpdateExportConfig() throws NoSuchExportConfig, NoSuchUser, UserNotPermitted
    {
        setupPermissionMocks(true);
        when(exportConfigMock.getId()).thenReturn(TEST_CONFIG_ID);
        when(exportConfigMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(exportConfigMock.getName()).thenReturn(TEST_CONFIG_NAME);
        when(exportConfigRepoMock.findById(TEST_CONFIG_ID)).thenReturn(Optional.of(exportConfigMock));

        fixture.updateExportConfig(exportConfigMock);

        verify(exportConfigRepoMock).save(exportConfigMock);
        verifyAuditEvent(AuditEventType.UPDATED);
    }

    @Test
    public void testUpdateExportConfigNoSuchExportConfig() throws NoSuchUser, UserNotPermitted
    {
        when(exportConfigMock.getId()).thenReturn(TEST_CONFIG_ID);
        when(exportConfigRepoMock.findById(TEST_CONFIG_ID)).thenReturn(Optional.empty());

        assertThrows(NoSuchExportConfig.class, () ->
                fixture.updateExportConfig(exportConfigMock));
    }

    @Test
    public void testUpdateExportConfigUserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupPermissionMocks(false);
        when(exportConfigMock.getId()).thenReturn(TEST_CONFIG_ID);
        when(exportConfigMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(exportConfigRepoMock.findById(TEST_CONFIG_ID)).thenReturn(Optional.of(exportConfigMock));

        assertThrows(UserNotPermitted.class, () ->
                fixture.updateExportConfig(exportConfigMock));
    }

    // -------------------------------------------------------------------------
    // removeExportConfig
    // -------------------------------------------------------------------------

    @Test
    public void testRemoveExportConfig() throws NoSuchExportConfig, NoSuchUser, UserNotPermitted
    {
        setupPermissionMocks(true);
        when(exportConfigRepoMock.findById(TEST_CONFIG_ID)).thenReturn(Optional.of(exportConfigMock));
        when(exportConfigMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(exportConfigMock.getName()).thenReturn(TEST_CONFIG_NAME);

        fixture.removeExportConfig(TEST_CONFIG_ID);

        verify(exportConfigRepoMock).deleteById(TEST_CONFIG_ID);
        verifyAuditEvent(AuditEventType.DELETED);
    }

    @Test
    public void testRemoveExportConfigNoSuchExportConfig() throws NoSuchUser, UserNotPermitted
    {
        when(exportConfigRepoMock.findById(TEST_CONFIG_ID)).thenReturn(Optional.empty());

        assertThrows(NoSuchExportConfig.class, () ->
                fixture.removeExportConfig(TEST_CONFIG_ID));
    }

    @Test
    public void testRemoveExportConfigUserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupPermissionMocks(false);
        when(exportConfigRepoMock.findById(TEST_CONFIG_ID)).thenReturn(Optional.of(exportConfigMock));
        when(exportConfigMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);

        assertThrows(UserNotPermitted.class, () ->
                fixture.removeExportConfig(TEST_CONFIG_ID));
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void setupPermissionMocks(boolean permitted) throws NoSuchUser
    {
        when(raceSeriesAuthorizationServiceMock.isLoggedOnUserPermitted(TEST_RACE_SERIES_ID)).thenReturn(permitted);
    }

    private void verifyAuditEvent(AuditEventType eventType) throws NoSuchUser, UserNotPermitted
    {
        verify(auditMock).auditEvent(
                eventType,
                TEST_RACE_SERIES_ID,
                EXPORT_CONFIG_OBJECT_NAME,
                TEST_CONFIG_NAME);
    }

    private static final Integer TEST_RACE_SERIES_ID       = 9999;
    private static final Integer TEST_CONFIG_ID            = 1111;
    private static final String  TEST_CONFIG_NAME          = "Annual Results Export";
    private static final String  EXPORT_CONFIG_OBJECT_NAME = "ExportConfig";

    @Mock private ExportConfigRepository          exportConfigRepoMock;
    @Mock private RaceSeriesAuthorizationService  raceSeriesAuthorizationServiceMock;
    @Mock private AuditService                    auditMock;
    @Mock private Pageable                        pageableMock;
    @Mock private Page<ExportConfig>              exportConfigPageMock;
    @Mock private ExportConfig                    exportConfigMock;

    @InjectMocks
    private ExportConfigServiceImpl fixture;
}
