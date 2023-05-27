package com.main.catchy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Appointment")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@appointmentId")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment implements Serializable {
    private static final long serialVersionUID = -6123703288245602356L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointmentId")
    private long appointmentId;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Contact mentor;
    // @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "mantee_id")
    private Contact mentee;
    @Column(name = "status")
    private String status;
    @Column(name = "creationDate")
    private LocalDateTime creationDate;
    @Column(name = "scheduledtime")
    private String scheduledtime;
    @Column(name = "scheduledDate")
    private LocalDateTime scheduledDate;
}
