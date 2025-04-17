package com.coding.car_rental_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
//@EntityListeners(AuditingEntityListener.class)
@Table(name = "cars")
@Data
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String color;
    private String name;
    private String type;
    private String transmission;
    private String description;
    private Long price;
    private Date modelYear;
    @CreationTimestamp //hibernate annotation to insert create date automatically
    @Column(updatable = false)
    private LocalDateTime dateCreated;
    @UpdateTimestamp //hibernate annotation to insert update date automatically
    private LocalDateTime lastUpdated;
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] image;
}
