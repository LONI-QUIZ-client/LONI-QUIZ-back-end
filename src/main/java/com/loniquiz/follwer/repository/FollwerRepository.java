package com.loniquiz.follwer.repository;

import com.loniquiz.follwer.entity.Follower;
import com.loniquiz.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollwerRepository extends JpaRepository<Follower, String> {
    List<Follower> findByUser(User user);

    boolean existsByFollwerId(String fid);

    void deleteByFollwerId(String fid);

    boolean existsByFollwerIdAndUser(String find, User user);
}
