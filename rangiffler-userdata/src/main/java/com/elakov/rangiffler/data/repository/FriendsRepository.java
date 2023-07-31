package com.elakov.rangiffler.data.repository;

import com.elakov.rangiffler.data.FriendsEntity;
import com.elakov.rangiffler.data.FriendsId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FriendsRepository extends JpaRepository<FriendsEntity, FriendsId> {

    List<FriendsEntity> findAllByUserId(UUID userId);
}
