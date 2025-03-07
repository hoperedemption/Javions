package ch.epfl.javions.adsb;

import org.junit.jupiter.api.Test;

class AircraftStateAccumulatorTest {

        @Test
        void testString(){
            {
            String actual = "position : (5.620176717638969°, 45.71530147455633°)\n" +
                    "position : (5.621292097494006°, 45.715926848351955°)\n" +
                    "indicatif : CallSign[string=RYR7JD]\n" +
                    "position : (5.62225341796875°, 45.71644593961537°)\n" +
                    "position : (5.623420681804419°, 45.71704415604472°)\n" +
                    "position : (5.624397089704871°, 45.71759032085538°)\n" +
                    "position : (5.625617997720838°, 45.71820789948106°)\n" +
                    "position : (5.626741759479046°, 45.718826316297054°)\n" +
                    "position : (5.627952609211206°, 45.71946484968066°)\n" +
                    "position : (5.629119873046875°, 45.72007002308965°)\n" +
                    "position : (5.630081193521619°, 45.7205820735544°)\n" +
                    "position : (5.631163045763969°, 45.72120669297874°)\n" +
                    "indicatif : CallSign[string=RYR7JD]\n" +
                    "position : (5.633909627795219°, 45.722671514377°)\n" +
                    "position : (5.634819064289331°, 45.72314249351621°)";

            String expected = "position : (5.620176717638969°, 45.71530147455633°)\n" +
                    "position : (5.621292097494006°, 45.715926848351955°)\n" +
                    "indicatif : CallSign[string=RYR7JD]\n" +
                    "position : (5.62225341796875°, 45.71644593961537°)\n" +
                    "position : (5.623420681804419°, 45.71704415604472°)\n" +
                    "position : (5.624397089704871°, 45.71759032085538°)\n" +
                    "position : (5.625617997720838°, 45.71820789948106°)\n" +
                    "position : (5.626741759479046°, 45.718826316297054°)\n" +
                    "position : (5.627952609211206°, 45.71946484968066°)\n" +
                    "position : (5.629119873046875°, 45.72007002308965°)\n" +
                    "position : (5.630081193521619°, 45.7205820735544°)\n" +
                    "position : (5.631163045763969°, 45.72120669297874°)\n" +
                    "indicatif : CallSign[string=RYR7JD]\n" +
                    "position : (5.633909627795219°, 45.722671514377°)\n" +
                    "position : (5.634819064289331°, 45.72314249351621°)";

            System.out.println(actual.equals(expected));
        }

    }
}