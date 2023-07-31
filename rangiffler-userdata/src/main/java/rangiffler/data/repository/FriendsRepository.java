package rangiffler.data.repository;

import rangiffler.data.FriendsEntity;
import rangiffler.data.FriendsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsRepository extends JpaRepository<FriendsEntity, FriendsId> {


}
