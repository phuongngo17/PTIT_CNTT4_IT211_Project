package org.example.badminton.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "badminton_clusters")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BadmintonCluster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100,nullable = false)
    private String name;

    @Column(length = 255,nullable = false)
    private String address;

    @Column(length = 20,nullable = false)
    private String hot_line;


}
