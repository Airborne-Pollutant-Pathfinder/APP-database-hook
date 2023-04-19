package edu.utdallas.cs.app.database.table;

import jakarta.persistence.*;

@Entity
@Table(name = "pollutant")
public class Pollutant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pollutant_id")
    private int id;

    @Column(name = "abbreviation")
    private String abbreviation;

    @Column(name = "units")
    private String units;

    @Column(name = "full_name")
    private String fullName;
}