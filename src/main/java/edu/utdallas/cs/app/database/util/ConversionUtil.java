package edu.utdallas.cs.app.database.util;

public final class ConversionUtil {
    private static final double CO_MOLECULAR_WEIGHT = 28.01;
    private static final double NO2_MOLECULAR_WEIGHT = 46.01;
    private static final double O3_MOLECULAR_WEIGHT = 48.0;
    private static final double SO2_MOLECULAR_WEIGHT = 64.07;

    private ConversionUtil() {
    }

    public static double convertCO(double concentrationUgPerCubicMeter) {
        return ugPerCubicMeterToPpm(concentrationUgPerCubicMeter, CO_MOLECULAR_WEIGHT);
    }

    public static double convertNO2(double concentrationUgPerCubicMeter) {
        return ugPerCubicMeterToPpb(concentrationUgPerCubicMeter, NO2_MOLECULAR_WEIGHT);
    }

    public static double convertO3(double concentrationUgPerCubicMeter) {
        return ugPerCubicMeterToPpb(concentrationUgPerCubicMeter, O3_MOLECULAR_WEIGHT);
    }

    public static double convertSO2(double concentrationUgPerCubicMeter) {
        return ugPerCubicMeterToPpb(concentrationUgPerCubicMeter, SO2_MOLECULAR_WEIGHT);
    }

    private static double ugPerCubicMeterToPpb(double concentration, double molecularWeight) {
        return 24.45 * concentration / molecularWeight;
    }

    private static double ugPerCubicMeterToPpm(double concentration, double molecularWeight) {
        return ugPerCubicMeterToPpb(concentration, molecularWeight) / 1000;
    }
}
