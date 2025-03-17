package com.tus.anyDo.IndividualProject.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_PROJECT_MANAGER,
    ROLE_TEAMWORKER;
    
    @Override
    public String getAuthority() {
    	return name(); 
    }
}