package com.myPg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
    name = "rooms",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"roomNumber", "hostel_id"})}
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String roomNumber;

    @Min(1)
    @Column(nullable = false)
    private int capacity;

    @Min(0)
    @Column(nullable = false)
    private int occupied;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_id", nullable = false)
    @JsonIgnore
    private Hostel hostel;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TenantAllocation> allocations;
}
