package com.project.gmao.features.authentication.services.implementation;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.gmao.common.enums.RoleEnum;
import com.project.gmao.core.exception.ElementAlreadyExistsException;
import com.project.gmao.core.exception.ElementNotFoundException;
import com.project.gmao.features.authentication.dto.request.SignupRequest;
import com.project.gmao.features.authentication.dto.request.UserRoleAssignmentRequest;
import com.project.gmao.features.authentication.dto.response.UserResponse;
import com.project.gmao.features.authentication.entity.User;
import com.project.gmao.features.authentication.mapper.UserMapper;
import com.project.gmao.features.authentication.repository.UserRepository;
import com.project.gmao.features.authentication.services.interfaces.UserService;
import com.project.gmao.features.manage_role.entity.Role;
import com.project.gmao.features.manage_role.repository.RoleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("User not found"));
        return userMapper.userToUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse getUserByUuid(UUID uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ElementNotFoundException("User not found"));
        return userMapper.userToUserResponse(user);
    }

    @Override
    @Transactional
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUser(UUID uuid, SignupRequest request) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ElementNotFoundException("User not found"));

        if (!user.getEmail().equals(request.email()) && 
            userRepository.findByEmailIgnoreCase(request.email()).isPresent()) {
            throw new ElementAlreadyExistsException("Email already exists");
        }

        user.setPrenom(request.prenom().trim());
        user.setNom(request.nom().trim());
        user.setEmail(request.email().toLowerCase().trim());
        user.setTelephone(request.telephone() != null ? request.telephone().trim() : null);

        if (!request.motDePasse().equals(request.confirmerMotDePasse())) {
            throw new IllegalArgumentException("Password mismatch");
        }
        user.setPassword(passwordEncoder.encode(request.motDePasse()));

        User updatedUser = userRepository.save(user);
        return userMapper.userToUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(UUID uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ElementNotFoundException("User not found"));
        refreshTokenService.deleteUserRefreshTokens(user);
        userRepository.delete(user);
    }

    @Override
    public UserResponse assignRolesToUser(UserRoleAssignmentRequest request) {
        User user = userRepository.findByUuid(request.userUuid())
                .orElseThrow(() -> new ElementNotFoundException("User not found"));

        Set<Role> roles = roleRepository.findByNameIn(request.roles())
                .stream().collect(Collectors.toSet());

        user.getRoles().addAll(roles);
        User updatedUser = userRepository.save(user);

        refreshTokenService.deleteUserRefreshTokens(user);

        return userMapper.userToUserResponse(updatedUser);
    }

    @Override
    public UserResponse removeRolesFromUser(UUID userUuid, Set<String> roles) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ElementNotFoundException("User not found"));

        Set<Role> rolesToRemove = roleRepository.findByNameIn(roles)
                .stream().collect(Collectors.toSet());

        user.getRoles().removeAll(rolesToRemove);
        User updatedUser = userRepository.save(user);

        refreshTokenService.deleteUserRefreshTokens(user);

        return userMapper.userToUserResponse(updatedUser);
    }
}
