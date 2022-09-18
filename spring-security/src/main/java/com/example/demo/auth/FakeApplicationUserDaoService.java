package com.example.demo.auth;

import com.example.demo.security.ApplicationUserRole;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDAO {
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public FakeApplicationUserDaoService(
        PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(
        String username) {
        return getApplicationUsers().stream()
            .filter(applicationUser -> username.equals(
                applicationUser.getUsername()))
            .findFirst();
    }


    private Collection<ApplicationUser> getApplicationUsers() {
        return Sets.newHashSet(
            new ApplicationUser(
                ApplicationUserRole.ADMIN.grantedAuthorities(),
                passwordEncoder.encode("root"),
                "root",
                true,
                true,
                true,
                true
            ), new ApplicationUser(
                ApplicationUserRole.STUDENT.grantedAuthorities(),
                passwordEncoder.encode("max"),
                "max",
                true,
                true,
                true,
                true
            ), new ApplicationUser(
                ApplicationUserRole.ADMINTRAINEE.grantedAuthorities(),
                passwordEncoder.encode("rootTrainee"),
                "rootTrainee",
                true,
                true,
                true,
                true
            )
        );
    }
}
