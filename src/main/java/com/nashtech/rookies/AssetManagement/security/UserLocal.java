package com.nashtech.rookies.AssetManagement.security;

import com.nashtech.rookies.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.AssetManagement.repository.InformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserLocal {



    public String getLocalUserName(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if(userName != null)
        {
            return userName;
        }
        throw new ResourceNotFoundException("Cannot recognize user. Maybe you haven't log in");
    }


}
