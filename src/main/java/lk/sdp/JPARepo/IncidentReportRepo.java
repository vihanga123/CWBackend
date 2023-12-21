package lk.sdp.JPARepo;

import lk.sdp.Entity.IncidentReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Component
public interface IncidentReportRepo extends JpaRepository<IncidentReport,Long> {

      //Optional<IncidentReport> findIncidentById(Long incidentId);
      @Query("SELECT e FROM IncidentReport e")
      Page<IncidentReport> findInquiries(PageRequest pageRequest);

      @Query("SELECT e FROM IncidentReport e WHERE e.username = :username ORDER BY e.createdDateTime DESC")
      Page<IncidentReport> findLatestIncidentByUsername(String username, Pageable pageable);

      @Query("SELECT e FROM IncidentReport e WHERE e.incidentType.id = 1")
      Page<IncidentReport> findWildlifeIncidents(PageRequest pageRequest);

      @Query("SELECT e FROM IncidentReport e WHERE e.incidentType.id = 2")
      Page<IncidentReport> findForestryIncidents(PageRequest pageRequest);

      @Query("SELECT e FROM IncidentReport e WHERE e.status IN ('IN PROGRESS', 'COMPLETE', 'COMPLETED')")
      Page<IncidentReport>findInquiriesInProgress(PageRequest pageRequest);

      @Query("SELECT image1 FROM IncidentReport WHERE incidentId = :incidentId")
      List<byte[]> findImage1ByIncidentId(Long incidentId);

      @Query("SELECT image2 FROM IncidentReport WHERE incidentId = :incidentId")
      List<byte[]> findImage2ByIncidentId(Long incidentId);

      @Query("SELECT image3 FROM IncidentReport WHERE incidentId = :incidentId")
      List<byte[]> findImage3ByIncidentId(Long incidentId);

      @Query("SELECT COUNT(u) FROM IncidentReport u WHERE u.status IN ('PENDING')")
      Long incidentCount();
}


