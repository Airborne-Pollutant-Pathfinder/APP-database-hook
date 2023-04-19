package edu.utdallas.cs.app.database;

public enum PollutantType {
    CO(1),
    NO2(2),
    O3(3),
    PM2_5(4),
    PM10(5),
    SO2(6),

    ;

    /**
     * The ID of the pollutant in the database.
     */
    private final int pollutantId;

    PollutantType(int pollutantId) {
        this.pollutantId = pollutantId;
    }

    public int getPollutantId() {
        return pollutantId;
    }
}
