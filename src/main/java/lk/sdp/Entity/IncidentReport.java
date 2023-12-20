package lk.sdp.Entity;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="incident")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "incidentType"})
public class IncidentReport implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="incident_id", unique = true, nullable = false)
    private Long incidentId;

    @Column(name="username")
    private String username;

    @Column(name="name")
    private String name;

    @Column(name="location")
    private String location;

    @Column(name="incident")
    private String incident;

    @Column(name="startdate")
    private LocalDate startdate;

    @Column(name ="timeframe")
    private String timeframe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_type_id")
    //@Column(name="incident_type")
    private IncidentType incidentType;

    @Column(name ="progress")
    private String progress;

    @Column(name="comment")
    private String comment;

    @Lob
    @Column(name = "image1")
    private byte[] image1;

    @Lob
    @Column(name = "image2")
    private byte[] image2;

    @Lob
    @Column(name = "image3")
    private byte[] image3;

    @Column(name="status")
    private String status;

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    public void addImage(byte[] imageBytes) {
            this.image1 = imageBytes;
            this.image2 = imageBytes;
            this.image3 = imageBytes;
    }
}
