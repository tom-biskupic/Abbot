package com.runcible.abbot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.BoatClass;
import com.runcible.abbot.model.BoatDivision;
import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.repository.BoatClassRepository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.DuplicateDivision;
import com.runcible.abbot.service.exceptions.NoSuchBoatClass;
import com.runcible.abbot.service.exceptions.NoSuchDivision;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@ExtendWith(MockitoExtension.class)
public class BoatClassServiceTest
{
    @Test
    public void testGetAllBoatClassesForSeries() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        when(boatClassRepoMock.findByRaceSeries(TEST_RACE_SERIES_ID, pageableMock)).thenReturn(boatClassPageMock);
        Page<BoatClass> returned = fixture.getAllBoatClassesForSeries(TEST_RACE_SERIES_ID, pageableMock);
        assertEquals(boatClassPageMock,returned);
    }
    
    @Test
    public void getAllBoatClassesForSeries2() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(boatClassRepoMock.findByRaceSeries(TEST_RACE_SERIES_ID)).thenReturn(mockBoatList);
        List<BoatClass> returned = fixture.getAllBoatClassesForSeries(TEST_RACE_SERIES_ID);
        assertEquals(mockBoatList,returned);
    }

    @Test
    public void getAllBoatClassesForSeries2UserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.getAllBoatClassesForSeries(TEST_RACE_SERIES_ID);
        });
    }

    @Test
    public void testAddBoatClass() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        when(boatClassMock.getName()).thenReturn(TEST_BOAT_CLASS_NAME);
        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        fixture.addBoatClass(TEST_RACE_SERIES_ID, boatClassMock);
        verify(boatClassMock).setRaceSeriesId(TEST_RACE_SERIES_ID);
        verify(boatClassRepoMock).save(boatClassMock);
        verify(auditServiceMock).auditEvent(
                AuditEventType.CREATED, 
                TEST_RACE_SERIES_ID, 
                BOAT_CLASS_OBJECT_NAME, 
                TEST_BOAT_CLASS_NAME);
    }

    @Test
    public void testAddBoatClassUserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.addBoatClass(TEST_RACE_SERIES_ID, boatClassMock);
        });
    }

    @Test
    public void testUpdateBoatClass() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(boatClassMock.getName()).thenReturn(TEST_BOAT_CLASS_NAME);
        
        fixture.updateBoatClass(boatClassMock);
        verify(boatClassRepoMock).save(boatClassMock);
        
        verify(auditServiceMock).auditEvent(
                AuditEventType.UPDATED, 
                TEST_RACE_SERIES_ID, 
                BOAT_CLASS_OBJECT_NAME, 
                TEST_BOAT_CLASS_NAME);
    }

    @Test
    public void testUpdateBoatClassUserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.updateBoatClass(boatClassMock);
        });
    }
    
    @Test
    public void testRemoveBoatClass() throws NoSuchUser, NoSuchBoatClass, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        
        when(boatClassRepoMock.findById(TEST_BOAT_CLASS_ID)).thenReturn(Optional.of(boatClassMock));

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(boatClassMock.getName()).thenReturn(TEST_BOAT_CLASS_NAME);
        
        fixture.removeBoatClass(TEST_BOAT_CLASS_ID);
        verify(boatClassRepoMock).deleteById(TEST_BOAT_CLASS_ID);
        
        verify(auditServiceMock).auditEvent(
                AuditEventType.DELETED, 
                TEST_RACE_SERIES_ID, 
                BOAT_CLASS_OBJECT_NAME, 
                TEST_BOAT_CLASS_NAME);
    }

    @Test
    public void testRemoveBoatClassInvalidBoatClass() throws NoSuchUser, NoSuchBoatClass, UserNotPermitted
    {
        when(boatClassRepoMock.findById(TEST_BOAT_CLASS_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchBoatClass.class, () -> {
            fixture.removeBoatClass(TEST_BOAT_CLASS_ID);
        });
    }

    @Test
    public void testRemoveBoatClassUserNotPermitted() throws NoSuchUser, NoSuchBoatClass, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        
        when(boatClassRepoMock.findById(TEST_BOAT_CLASS_ID)).thenReturn(Optional.of(boatClassMock));

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.removeBoatClass(TEST_BOAT_CLASS_ID);
        });
    }

    @Test
    public void testgetBoatClassById() throws NoSuchUser, NoSuchBoatClass, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(boatClassRepoMock.findById(TEST_BOAT_CLASS_ID)).thenReturn(Optional.of(boatClassMock));

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        
        BoatClass returned = fixture.getBoatClassByID(TEST_BOAT_CLASS_ID);
        assertEquals(boatClassMock,returned);
    }

    @Test
    public void testgetBoatClassByIdNoSuchBoatClass() throws NoSuchUser, NoSuchBoatClass, UserNotPermitted
    {
        when(boatClassRepoMock.findById(TEST_BOAT_CLASS_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchBoatClass.class, () -> {
            fixture.getBoatClassByID(TEST_BOAT_CLASS_ID);
        });
    }

    @Test
    public void testgetBoatClassByIdUserNotPermitted() throws NoSuchUser, NoSuchBoatClass, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        when(boatClassRepoMock.findById(TEST_BOAT_CLASS_ID)).thenReturn(Optional.of(boatClassMock));

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        
        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.getBoatClassByID(TEST_BOAT_CLASS_ID);
        });
    }

    @Test
    public void testAddDivision() throws DuplicateDivision, NoSuchUser, UserNotPermitted, NoSuchBoatClass
    {
        setupCheckPermissionsMocks(true);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        
        when(boatClassRepoMock.findById(TEST_ID)).thenReturn(Optional.of(boatClassMock));
        when(boatClassMock.getName()).thenReturn(TEST_BOAT_CLASS_NAME);
        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(divisionMock.getName()).thenReturn(TEST_NAME);
        when(boatClassMock.getDivision(TEST_NAME)).thenReturn(null);
        fixture.addDivision(TEST_ID, divisionMock);
        verify(boatClassRepoMock).save(boatClassMock);
        verify(boatClassMock).addDivision(divisionMock);
        
        verify(auditServiceMock).auditEvent(
                AuditEventType.CREATED, 
                TEST_RACE_SERIES_ID, 
                DIVISION_OBJECT_NAME, 
                TEST_BOAT_CLASS_NAME,
                TEST_NAME);
    }

    @Test
    public void testAddDivisionDuplicate() throws DuplicateDivision, NoSuchUser, UserNotPermitted, NoSuchBoatClass
    {
        when(boatClassRepoMock.findById(TEST_ID)).thenReturn(Optional.of(boatClassMock));
        when(divisionMock.getName()).thenReturn(TEST_NAME);
        when(boatClassMock.getDivision(TEST_NAME)).thenReturn(divisionMock2);
        
        Assertions.assertThrows(DuplicateDivision.class, () -> {
            fixture.addDivision(TEST_ID, divisionMock);
        });
    }

    @Test
    public void testAddDivisionNotPermitted() throws DuplicateDivision, NoSuchUser, UserNotPermitted, NoSuchBoatClass
    {
        setupCheckPermissionsMocks(false);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        
        when(boatClassRepoMock.findById(TEST_ID)).thenReturn(Optional.of(boatClassMock));
        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.addDivision(TEST_ID, divisionMock);
        });
    }

    @Test
    public void testAddDivisionNoSuchBoatClass() throws DuplicateDivision, NoSuchUser, UserNotPermitted, NoSuchBoatClass
    {
        when(boatClassRepoMock.findById(TEST_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchBoatClass.class, () -> {
            fixture.addDivision(TEST_ID, divisionMock);
        });
    }
    
    @Test
    public void testUpdateDivision() throws DuplicateDivision, NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);

        when(boatClassRepoMock.findById(TEST_ID)).thenReturn(Optional.of(boatClassMock));
        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);

        when(boatClassMock.getName()).thenReturn(TEST_BOAT_CLASS_NAME);
        when(divisionMock.getName()).thenReturn(TEST_NAME);
        when(boatClassMock.getDivision(TEST_NAME)).thenReturn(divisionMock2);
        when(divisionMock.getId()).thenReturn(TEST_ID);
        when(divisionMock2.getId()).thenReturn(TEST_ID);
        when(boatClassMock.getDivision(TEST_ID)).thenReturn(divisionMock2);
        
        fixture.updateDivision(TEST_ID, divisionMock);
        verify(boatClassMock).removeDivision(divisionMock2);
        verify(boatClassMock).addDivision(divisionMock);
        verify(boatClassRepoMock).save(boatClassMock);
        
        verify(auditServiceMock).auditEvent(
                AuditEventType.UPDATED, 
                TEST_RACE_SERIES_ID, 
                DIVISION_OBJECT_NAME, 
                TEST_BOAT_CLASS_NAME,
                TEST_NAME);

    }

    @Test
    public void testUpdateDivisionDuplicate() throws DuplicateDivision, NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);

        when(boatClassRepoMock.findById(TEST_ID)).thenReturn(Optional.of(boatClassMock));
        when(divisionMock.getName()).thenReturn(TEST_NAME);
        when(boatClassMock.getDivision(TEST_NAME)).thenReturn(divisionMock2);
        when(divisionMock.getId()).thenReturn(TEST_ID);
        when(divisionMock2.getId()).thenReturn(99);
        
        Assertions.assertThrows(DuplicateDivision.class, () -> {
            fixture.updateDivision(TEST_ID, divisionMock);
        });
    }

    @Test
    public void testUpdateDivisionNoDivision() throws DuplicateDivision, NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(boatClassRepoMock.findById(TEST_ID)).thenReturn(Optional.of(boatClassMock));

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        
        when(divisionMock.getName()).thenReturn(TEST_NAME);
        when(boatClassMock.getDivision(TEST_NAME)).thenReturn(null);
        
        Assertions.assertThrows(NoSuchDivision.class, () -> {
            fixture.updateDivision(TEST_ID, divisionMock);
        });
    }

    @Test
    public void testUpdateDivisionNotPermitted() throws DuplicateDivision, NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(boatClassRepoMock.findById(TEST_ID)).thenReturn(Optional.of(boatClassMock));
        
        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.updateDivision(TEST_ID, divisionMock);
        });
    }

    @Test
    public void testUpdateDivisionNoSuchBoatClass() throws DuplicateDivision, NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        when(boatClassRepoMock.findById(TEST_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchBoatClass.class, () -> {
            fixture.updateDivision(TEST_ID, divisionMock);
        });
    }

    @Test
    public void testRemoveDivision() throws NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        
        when(boatClassRepoMock.findById(TEST_BOAT_CLASS_ID)).thenReturn(Optional.of(boatClassMock));
        
        when(boatClassMock.getName()).thenReturn(TEST_BOAT_CLASS_NAME);
        when(boatClassMock.getDivision(TEST_ID)).thenReturn(divisionMock);
        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(divisionMock.getName()).thenReturn(TEST_NAME);
        fixture.removeDivision(TEST_BOAT_CLASS_ID, TEST_ID);
        verify(boatClassMock).removeDivision(divisionMock);
        verify(boatClassRepoMock).save(boatClassMock);
        
        verify(auditServiceMock).auditEvent(
                AuditEventType.DELETED, 
                TEST_RACE_SERIES_ID, 
                DIVISION_OBJECT_NAME, 
                TEST_BOAT_CLASS_NAME,
                TEST_NAME);

    }

    @Test
    public void testRemoveDivisionNoSuchBoatClass() throws NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        when(boatClassRepoMock.findById(TEST_BOAT_CLASS_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchBoatClass.class, () -> {
            fixture.removeDivision(TEST_BOAT_CLASS_ID, TEST_ID);
        });
    }

    @Test
    public void testRemoveDivisionNoSuchDivision() throws NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        
        when(boatClassRepoMock.findById(TEST_BOAT_CLASS_ID)).thenReturn(Optional.of(boatClassMock));
        
        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);

        when(boatClassMock.getDivision(TEST_ID)).thenReturn(null);
        Assertions.assertThrows(NoSuchDivision.class, () -> {
            fixture.removeDivision(TEST_BOAT_CLASS_ID, TEST_ID);
        });
    }

    @Test
    public void testRemoveDivisionUserNotPermitted() throws NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        
        when(boatClassRepoMock.findById(TEST_BOAT_CLASS_ID)).thenReturn(Optional.of(boatClassMock));
        
        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);

        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.removeDivision(TEST_BOAT_CLASS_ID, TEST_ID);
        });
    }
    
    private void setupCheckPermissionsMocks(boolean permitted) throws NoSuchUser
    {
        when(raceSeriesAuthorizationServiceMock.isLoggedOnUserPermitted(TEST_RACE_SERIES_ID)).thenReturn(permitted);
    }

    public static final Integer TEST_ID=1234;
    public static final String  TEST_NAME="Radial";
    public static final Integer TEST_BOAT_CLASS_ID=6969;
    public static final Integer TEST_RACE_SERIES_ID=999;
    public static final String  BOAT_CLASS_OBJECT_NAME = "Boat class"; 
    public static final String  TEST_BOAT_CLASS_NAME = "Sabot";
    public static final String  DIVISION_OBJECT_NAME = "Division";
            
    @Mock private Page<BoatClass>                   boatClassPageMock;
    @Mock private List<BoatClass>                   mockBoatList;
    @Mock private BoatClass                         boatClassMock;
    @Mock private BoatClassRepository               boatClassRepoMock;
    @Mock private RaceSeries                        raceSeriesMock;
    @Mock private BoatDivision                      divisionMock;
    @Mock private BoatDivision                      divisionMock2;
    @Mock private RaceSeriesAuthorizationService    raceSeriesAuthorizationServiceMock;
    @Mock private Pageable                          pageableMock;
    @Mock private RaceSeriesService                 raceSeriesServiceMock;
    @Mock private AuditService                      auditServiceMock;
    
    @InjectMocks
    BoatClassServiceImpl fixture;
}
