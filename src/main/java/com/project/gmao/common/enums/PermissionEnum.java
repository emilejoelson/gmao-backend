package com.project.gmao.common.enums;

public enum PermissionEnum {
    UNAUTHORIZED("UNAUTHORIZED"),
    CREATE("CREATE"),
    VIEW("VIEW"),
    UPDATE("UPDATE"),
    DELETE("DELETE");
    
    private final String name;
    
    PermissionEnum(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
