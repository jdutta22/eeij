package eeij.repository;

import eeij.domain.TrainingRegister;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TrainingRegister entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrainingRegisterRepository extends JpaRepository<TrainingRegister, Long> {
    @Query("select trainingRegister from TrainingRegister trainingRegister where trainingRegister.user.login = ?#{principal.username}")
    List<TrainingRegister> findByUserIsCurrentUser();
}
