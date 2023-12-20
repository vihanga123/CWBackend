package lk.sdp.JPARepo;

import lk.sdp.Entity.IncidentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IncidentTypeRepo extends JpaRepository<IncidentType, Long> {

    Optional<IncidentType> findTypeById(Long incidentTypeId);

}
