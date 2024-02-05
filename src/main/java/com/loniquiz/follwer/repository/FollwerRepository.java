package com.loniquiz.follwer.repository;

import com.loniquiz.follwer.entity.Follower;
import com.loniquiz.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollwerRepository extends JpaRepository<Follower, String> {
    List<Follower> findByUser(User user);


}
