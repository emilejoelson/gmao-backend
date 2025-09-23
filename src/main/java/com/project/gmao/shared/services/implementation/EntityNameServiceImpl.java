package com.project.gmao.shared.services.implementation;

import org.springframework.stereotype.Service;

import com.project.gmao.shared.services.interfaces.EntityNameService;

@Service
public class EntityNameServiceImpl implements EntityNameService {
    @Override
    public String getEntityName(Class<?> clazz) {
        return clazz.getSimpleName();
    }
}
