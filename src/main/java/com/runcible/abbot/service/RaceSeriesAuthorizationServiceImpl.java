package com.runcible.abbot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.model.RaceSeriesUser;
import com.runcible.abbot.model.User;
import com.runcible.abbot.model.UserSummary;
import com.runcible.abbot.repository.RaceSeriesRepository;
import com.runcible.abbot.repository.RaceSeriesUserRepository;
import com.runcible.abbot.service.exceptions.CannotDeAuthorizeLastUser;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Component
@Transactional
public class RaceSeriesAuthorizationServiceImpl implements RaceSeriesAuthorizationService
{

    @Override
    public boolean isLoggedOnUserPermitted(Integer raceSeriesID) throws NoSuchUser
    {
        User loggedOnUser = loggedOnUserService.getLoggedOnUser();
        
        return ( raceSeriesUserRepo.countUserEntriesForRaceSeries(raceSeriesID, loggedOnUser.getId()) != 0);
    }

    public void authorizeUserForRaceSeries(Integer raceSeriesId, String emailAddress) 
            throws UserNotPermitted, NoSuchUser, NoSuchRaceSeries
	{
    	authorizeUserForRaceSeries(
    			raceSeriesService.findByID(raceSeriesId), 
    			userService.findByEmail(emailAddress));
	}

    public void authorizeUserForRaceSeries(RaceSeries series, User user) throws UserNotPermitted, NoSuchUser
    {
        Integer seriesId = series.getId();
        
        if (    isOwned(seriesId)  
                && 
                ! isLoggedOnUserPermitted(seriesId) )
        {
            throw new UserNotPermitted();
        }
        
        raceSeriesUserRepo.save(new RaceSeriesUser(series,user));
    }
    
    public void deAuthorizeUserForRaceSeries(Integer raceSeriesId, Integer userId) throws NoSuchUser, UserNotPermitted, CannotDeAuthorizeLastUser
    {
        if (! isLoggedOnUserPermitted(raceSeriesId) )
        {
            throw new UserNotPermitted();
        }
        
        if ( raceSeriesUserRepo.countUserEntriesForRaceSeries(raceSeriesId) == 1)
        {
            throw new CannotDeAuthorizeLastUser();
        }
        
        raceSeriesUserRepo.removeUser(raceSeriesId,userId);
    }
    
    public Page<UserSummary> getAuthorizedUsers(Integer raceSeriesID, Pageable page) throws NoSuchUser, UserNotPermitted
    {
        if ( ! isLoggedOnUserPermitted(raceSeriesID) )
        {
            throw new UserNotPermitted();
        }
        
        User loggedOnUser = loggedOnUserService.getLoggedOnUser();
        
        List<UserSummary> userList = new ArrayList<UserSummary>();
        Page<RaceSeriesUser> usersPage = raceSeriesUserRepo.getByRaceSeriesId(raceSeriesID,page);
        for(RaceSeriesUser rsu : usersPage.getContent())
        {
            boolean currentlyLoggedOn = loggedOnUser.getId()==rsu.getUser().getId();
            
			userList.add(new UserSummary(rsu.getUser(),currentlyLoggedOn));
        }
        
        Pageable newPage = new PageRequest(usersPage.getNumber(),usersPage.getSize());
        Page<UserSummary> result = new PageImpl<UserSummary>(userList,newPage,usersPage.getTotalPages());
        return result;
    }
    
    private boolean isOwned(Integer seriesId)
    {
        return raceSeriesUserRepo.countUserEntriesForRaceSeries(seriesId) != 0;
    }
    
    @Autowired
    private RaceSeriesUserRepository raceSeriesUserRepo;
    
    @Autowired
    private LoggedOnUserService loggedOnUserService;
    
    @Autowired
    private RaceSeriesService raceSeriesService;
    
    @Autowired
    private UserService userService;
}
