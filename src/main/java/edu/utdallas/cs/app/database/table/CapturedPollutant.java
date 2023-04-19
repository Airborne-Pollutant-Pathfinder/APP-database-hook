package edu.utdallas.cs.app.database.table;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "captured_pollutant")
public class CapturedPollutant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "captured_pollutant_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pollutant_id")
    private Pollutant pollutant;

    @Column(name = "datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetime;

    @Column(name = "value")
    private double value;

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public void setPollutant(Pollutant pollutant) {
        this.pollutant = pollutant;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public void setValue(double value) {
        this.value = value;
    }
}