package com.tus.anyDo.IndividualProject.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    CUSTOMER_SERVICE("Customer Service"),
    SUPPORT_ENGINEER("Support Engineer"),
    SYSTEM_ADMIN("System Administrator"),
    NETWORK_MANAGEMENT_ENGINEER("Network Management Engineer");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String getAuthority() {
    	return name(); 
    }
}