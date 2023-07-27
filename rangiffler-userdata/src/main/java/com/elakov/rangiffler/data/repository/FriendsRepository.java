package com.elakov.rangiffler.data.repository;

import com.elakov.rangiffler.data.FriendsEntity;
import com.elakov.rangiffler.data.FriendsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsRepository extends JpaRepository<FriendsEntity, FriendsId> {


}
