package com.loniquiz.side.repository;

import com.loniquiz.side.entity.Side;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SideRepository extends JpaRepository<Side, Integer> {

    @Query("SELECT COUNT(*) FROM Side s")
    int countSideMenuImagesByAll();

    Optional<Side> findByImgId(int id);

}
