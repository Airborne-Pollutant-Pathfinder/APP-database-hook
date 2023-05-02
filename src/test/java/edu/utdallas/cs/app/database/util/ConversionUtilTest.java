package edu.utdallas.cs.app.database.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConversionUtilTest {

    private static final double DELTA_PPM = 0.001;
    private static final double DELTA_PPB = 0.1;

    @Test
    public void Should_ConvertToPpm_When_ConvertCO() {
        double concentration = 500.0;
        double ppb = ConversionUtil.convertCO(concentration);
        Assertions.assertEquals(0.436649, ppb, DELTA_PPM);
    }

    @Test
    public void Should_ConvertToPpb_When_ConvertNO2() {
        double concentration = 100.0;
        double ppb = ConversionUtil.convertNO2(concentration);
        Assertions.assertEquals(53.165, ppb, DELTA_PPB);
    }

    @Test
    public void Should_ConvertToPpb_When_ConvertO3() {
        double concentration = 70.0;
        double ppb = ConversionUtil.convertO3(concentration);
        Assertions.assertEquals(35.672, ppb, DELTA_PPB);
    }

    @Test
    public void Should_ConvertToPpb_When_ConvertSO2() {
        double concentration = 200.0;
        double ppb = ConversionUtil.convertSO2(concentration);
        Assertions.assertEquals(76.357, ppb, DELTA_PPB);
    }
}
