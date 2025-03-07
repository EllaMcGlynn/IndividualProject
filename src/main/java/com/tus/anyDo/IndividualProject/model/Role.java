package com.tus.anyDo.IndividualProject.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    PROJECT_MANAGER,
    TEAMWORKER;
    
    @Override
    public String getAuthority() {
    	return name(); 
    }
}