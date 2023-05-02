package edu.utdallas.cs.app.database.util;

public final class ConversionUtil {
    private static final double MOLECULAR_WEIGHT_OF_CO = 28.01;
    private static final double MOLECULAR_WEIGHT_OF_NO2 = 46.0055;
    private static final double MOLECULAR_WEIGHT_OF_O3 = 48.0;
    private static final double MOLECULAR_WEIGHT_OF_SO2 = 64.06;
    private static final double STANDARD_TEMPERATURE_IN_KELVIN = 273.15;
    private static final double STANDARD_PRESSURE_IN_ATMOSPHERES = 1.0;
    private static final double GAS_CONSTANT = 0.08206;

    private ConversionUtil() {
    }

    public static double convertCO(double concentrationInUgPerCubicMeter) {
        return convertUgPerCubicMeterToPpm(concentrationInUgPerCubicMeter, MOLECULAR_WEIGHT_OF_CO);
    }

    public static double convertNO2(double concentrationInUgPerCubicMeter) {
        return convertUgPerCubicMeterToPpb(concentrationInUgPerCubicMeter, MOLECULAR_WEIGHT_OF_NO2);
    }

    public static double convertO3(double concentrationInUgPerCubicMeter) {
        return convertUgPerCubicMeterToPpb(concentrationInUgPerCubicMeter, MOLECULAR_WEIGHT_OF_O3);
    }

    public static double convertSO2(double concentrationInUgPerCubicMeter) {
        return convertUgPerCubicMeterToPpb(concentrationInUgPerCubicMeter, MOLECULAR_WEIGHT_OF_SO2);
    }

    private static double convertUgPerCubicMeterToPpm(double concentrationInUgPerCubicMeter, double molecularWeight) {
        double concentrationInGramsPerCubicMeter = concentrationInUgPerCubicMeter / 1000000.0;
        double moles = (STANDARD_PRESSURE_IN_ATMOSPHERES * 100000) * (1.0 / GAS_CONSTANT) *
                (1.0 / STANDARD_TEMPERATURE_IN_KELVIN) * 1.0;
        double mass = moles * molecularWeight;
        double concentrationInPpm = (concentrationInGramsPerCubicMeter / mass) * 1000000.0;
        return concentrationInPpm;
    }

    private static double convertUgPerCubicMeterToPpb(double concentrationInUgPerCubicMeter, double molecularWeight) {
        double concentrationInGramsPerCubicMeter = concentrationInUgPerCubicMeter / 1000000.0;
        double moles = (STANDARD_PRESSURE_IN_ATMOSPHERES * 100000) * (1.0 / GAS_CONSTANT) *
                (1.0 / STANDARD_TEMPERATURE_IN_KELVIN) * 1.0;
        double volumeOfAir = 22.4;
        double mass = moles * molecularWeight;
        double concentrationInPpb = (concentrationInGramsPerCubicMeter / mass) * volumeOfAir * 1000.0;
        return concentrationInPpb;
    }
}
