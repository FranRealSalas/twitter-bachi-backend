package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.Role;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import com.twitter.bachi.backend.twitter_bachi_backend.model.IUser;
import com.twitter.bachi.backend.twitter_bachi_backend.model.UserRequest;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.RoleRepository;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    private UserRepository repository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepository repository, PasswordEncoder passwordEncoder, RoleRepository roleRepository){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return this.repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public User save(User user) {

        user.setRoles(getRoles(user));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return this.repository.save(user);
    }

    @Override
    @Transactional

    public Optional<User> update(UserRequest user, Long id) {
        Optional <User> userOptional = repository.findById(id);
        if (userOptional.isPresent()){
            User userDB = userOptional.get();
            userDB.setUsername(user.getUsername());
            userDB.setEmail(user.getEmail());

            userDB.setRoles(getRoles(user));

            return Optional.of((repository.save(userDB)));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    private List<Role> getRoles(IUser user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRoleUser = roleRepository.findByName("User");
        optionalRoleUser.ifPresent(roles::add);

        if (user.isAdmin()){
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("Admin");
            optionalRoleAdmin.ifPresent(roles::add);
        }
        return roles;
    }
}