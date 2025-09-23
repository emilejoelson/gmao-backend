package com.project.gmao.common.enums;

public enum RoleEnum {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    MODERATOR("ROLE_MODERATOR");
    
    private final String authority;
    
    RoleEnum(String authority) {
        this.authority = authority;
    }
    
    public String getAuthority() {
        return authority;
    }
    
    @Override
    public String toString() {
        return authority;
    }
}
