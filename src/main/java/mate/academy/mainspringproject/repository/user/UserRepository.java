package mate.academy.mainspringproject.repository.user;

import mate.academy.mainspringproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
