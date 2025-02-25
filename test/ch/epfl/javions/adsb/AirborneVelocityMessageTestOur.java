package ch.epfl.javions.adsb;

import ch.epfl.javions.ByteString;
import ch.epfl.javions.Units;
import ch.epfl.javions.demodulation.AdsbDemodulator;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class AirborneVelocityMessageTestOur {

    @Test
    void of() {
        String f = "resources/samples_20230304_1442.bin";
        try (InputStream s = new FileInputStream(f)) {
            AdsbDemodulator d = new AdsbDemodulator(s);
            RawMessage m;
            int count = 0;

            while ((m = d.nextMessage()) != null) {
                if(m.typeCode() == 19) {
                    AirborneVelocityMessage message = AirborneVelocityMessage.of(m);
                    if(message != null) {
                        System.out.println(message);
                    }
                    ++count;
                }
            }

            System.out.println(count);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void timeStampNs() {
        String f = "resources/samples_20230304_1442.bin";
        try (InputStream s = new FileInputStream(f)) {
            AdsbDemodulator d = new AdsbDemodulator(s);
            RawMessage m;
            int count = 0;

            while ((m = d.nextMessage()) != null){
                if(m.typeCode() == 19) {
                    System.out.println(m.payload());
                }
            }

            System.out.println(count);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void TestMessageB(){
        byte[] bytes= {(byte)0x8D, (byte)0xA0, (byte)0x5F, (byte)0x21, (byte)0x9B, (byte)0x06, (byte)0xB6, (byte)0xAF, (byte)0x18, (byte)0x94, (byte)0x00, (byte)0xCB, (byte)0xC3, (byte)0x3F};
        RawMessage message = new RawMessage(1, new ByteString(bytes));
        System.out.println(AirborneVelocityMessage.of(message));
    }

    @Test
    void icaoAddress() {
    }

    @Test
    void speed() {
        double speed = Math.hypot(354, 230);
        System.out.println(speed);
        speed = Units.convert(speed, Units.Speed.KNOT, Units.Speed.KILOMETER_PER_HOUR)/(3.6);

        System.out.println(speed);
    }


    String actual = "AirborneVelocityMessage[timeStampNs=100775400, icaoAddress=IcaoAddress[string=39D300], speed=217.1759987875795, trackOrHeading=5.707008696317668]\n" +
            "AirborneVelocityMessage[timeStampNs=146689300, icaoAddress=IcaoAddress[string=440237], speed=227.75426436901594, trackOrHeading=4.1068443167797195]\n" +
            "AirborneVelocityMessage[timeStampNs=208341000, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=210521800, icaoAddress=IcaoAddress[string=01024C], speed=228.01904908511267, trackOrHeading=5.311655187675027]\n" +
            "AirborneVelocityMessage[timeStampNs=232125000, icaoAddress=IcaoAddress[string=4B17E5], speed=114.64264880353804, trackOrHeading=5.335246702497837]\n" +
            "AirborneVelocityMessage[timeStampNs=235839800, icaoAddress=IcaoAddress[string=495299], speed=225.4352845765249, trackOrHeading=4.159837003030189]\n" +
            "AirborneVelocityMessage[timeStampNs=270464000, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=288802400, icaoAddress=IcaoAddress[string=4B1900], speed=239.0617299923047, trackOrHeading=4.4689760779824566]\n" +
            "AirborneVelocityMessage[timeStampNs=316898700, icaoAddress=IcaoAddress[string=4241A9], speed=78.93831084937837, trackOrHeading=0.7807899010873279]\n" +
            "AirborneVelocityMessage[timeStampNs=349526700, icaoAddress=IcaoAddress[string=4B1A00], speed=143.01277974924108, trackOrHeading=4.031468341546198]\n" +
            "AirborneVelocityMessage[timeStampNs=408163200, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=429622400, icaoAddress=IcaoAddress[string=4BCDE9], speed=224.56192397369023, trackOrHeading=5.188331051863648]\n" +
            "AirborneVelocityMessage[timeStampNs=636533100, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=645795900, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=705844800, icaoAddress=IcaoAddress[string=495299], speed=225.4352845765249, trackOrHeading=4.159837003030189]\n" +
            "AirborneVelocityMessage[timeStampNs=833094200, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=858094400, icaoAddress=IcaoAddress[string=39CEAA], speed=225.14630401936483, trackOrHeading=6.1086454708443]\n" +
            "AirborneVelocityMessage[timeStampNs=1065855100, icaoAddress=IcaoAddress[string=A4F239], speed=252.5057681873847, trackOrHeading=5.1231661989825374]\n" +
            "AirborneVelocityMessage[timeStampNs=1164431700, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=1184955200, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=1218171500, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=1219038000, icaoAddress=IcaoAddress[string=394C13], speed=227.36873123183696, trackOrHeading=2.610098023059783]\n" +
            "AirborneVelocityMessage[timeStampNs=1233094200, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=1280289300, icaoAddress=IcaoAddress[string=4B17E5], speed=114.51907720744369, trackOrHeading=5.363976166307547]\n" +
            "AirborneVelocityMessage[timeStampNs=1288753400, icaoAddress=IcaoAddress[string=495299], speed=225.4352845765249, trackOrHeading=4.159837003030189]\n" +
            "AirborneVelocityMessage[timeStampNs=1422737500, icaoAddress=IcaoAddress[string=4BCDE9], speed=224.56192397369023, trackOrHeading=5.188331051863648]\n" +
            "AirborneVelocityMessage[timeStampNs=1437921600, icaoAddress=IcaoAddress[string=39CEAA], speed=225.14630401936483, trackOrHeading=6.1086454708443]\n" +
            "AirborneVelocityMessage[timeStampNs=1447383100, icaoAddress=IcaoAddress[string=4D0221], speed=100.41027825998151, trackOrHeading=0.7998899024629483]\n" +
            "AirborneVelocityMessage[timeStampNs=1472930400, icaoAddress=IcaoAddress[string=4241A9], speed=78.93831084937837, trackOrHeading=0.7807899010873279]\n" +
            "AirborneVelocityMessage[timeStampNs=1541942000, icaoAddress=IcaoAddress[string=4CA2BF], speed=201.580839587717, trackOrHeading=3.3419912074156715]\n" +
            "AirborneVelocityMessage[timeStampNs=1605989900, icaoAddress=IcaoAddress[string=39D300], speed=217.1759987875795, trackOrHeading=5.707008696317668]\n" +
            "AirborneVelocityMessage[timeStampNs=1722323400, icaoAddress=IcaoAddress[string=4951CE], speed=182.59589393041378, trackOrHeading=0.8471956239020995]\n" +
            "AirborneVelocityMessage[timeStampNs=1731065400, icaoAddress=IcaoAddress[string=495299], speed=225.4352845765249, trackOrHeading=4.159837003030189]\n" +
            "AirborneVelocityMessage[timeStampNs=1823092600, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=1859535300, icaoAddress=IcaoAddress[string=4B1A00], speed=142.2892327767549, trackOrHeading=4.032001570784253]\n" +
            "AirborneVelocityMessage[timeStampNs=1872922900, icaoAddress=IcaoAddress[string=39CEAA], speed=225.14630401936483, trackOrHeading=6.1086454708443]\n" +
            "AirborneVelocityMessage[timeStampNs=1905870900, icaoAddress=IcaoAddress[string=A4F239], speed=252.5057681873847, trackOrHeading=5.1231661989825374]\n" +
            "AirborneVelocityMessage[timeStampNs=2012822900, icaoAddress=IcaoAddress[string=4D0221], speed=100.41027825998151, trackOrHeading=0.7998899024629483]\n" +
            "AirborneVelocityMessage[timeStampNs=2031706700, icaoAddress=IcaoAddress[string=440237], speed=227.75426436901594, trackOrHeading=4.1068443167797195]\n" +
            "AirborneVelocityMessage[timeStampNs=2076126500, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=2176455400, icaoAddress=IcaoAddress[string=4B17E5], speed=114.09074196651163, trackOrHeading=5.3955804256748845]\n" +
            "AirborneVelocityMessage[timeStampNs=2181294100, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=2186476200, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=2266325300, icaoAddress=IcaoAddress[string=495299], speed=225.4352845765249, trackOrHeading=4.159837003030189]\n" +
            "AirborneVelocityMessage[timeStampNs=2288212800, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=2289602000, icaoAddress=IcaoAddress[string=4B1A00], speed=142.2892327767549, trackOrHeading=4.032001570784253]\n" +
            "AirborneVelocityMessage[timeStampNs=2367925700, icaoAddress=IcaoAddress[string=39CEAA], speed=225.14630401936483, trackOrHeading=6.1086454708443]\n" +
            "AirborneVelocityMessage[timeStampNs=2386931400, icaoAddress=IcaoAddress[string=4BCDE9], speed=224.56192397369023, trackOrHeading=5.188331051863648]\n" +
            "AirborneVelocityMessage[timeStampNs=2496301600, icaoAddress=IcaoAddress[string=4CA2BF], speed=201.5900295949648, trackOrHeading=3.3289785377449017]\n" +
            "AirborneVelocityMessage[timeStampNs=2516450900, icaoAddress=IcaoAddress[string=4241A9], speed=78.93831084937837, trackOrHeading=0.7807899010873279]\n" +
            "AirborneVelocityMessage[timeStampNs=2558471700, icaoAddress=IcaoAddress[string=4B17E1], speed=197.00199797194514, trackOrHeading=2.470929992181367]\n" +
            "AirborneVelocityMessage[timeStampNs=2597642100, icaoAddress=IcaoAddress[string=4D0221], speed=99.68282090067501, trackOrHeading=0.7999956666085085]\n" +
            "AirborneVelocityMessage[timeStampNs=2662676700, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=2693625500, icaoAddress=IcaoAddress[string=495299], speed=225.4352845765249, trackOrHeading=4.159837003030189]\n" +
            "AirborneVelocityMessage[timeStampNs=2707170900, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=2729532100, icaoAddress=IcaoAddress[string=4B1A00], speed=142.2892327767549, trackOrHeading=4.032001570784253]\n" +
            "AirborneVelocityMessage[timeStampNs=2883205900, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=2912934400, icaoAddress=IcaoAddress[string=39CEAA], speed=225.14630401936483, trackOrHeading=6.1086454708443]\n" +
            "AirborneVelocityMessage[timeStampNs=3066745000, icaoAddress=IcaoAddress[string=440237], speed=227.75426436901594, trackOrHeading=4.1068443167797195]\n" +
            "AirborneVelocityMessage[timeStampNs=3113140100, icaoAddress=IcaoAddress[string=4D0221], speed=99.68282090067501, trackOrHeading=0.7999956666085085]\n" +
            "AirborneVelocityMessage[timeStampNs=3148679700, icaoAddress=IcaoAddress[string=495299], speed=225.87333483094972, trackOrHeading=4.161032416229856]\n" +
            "AirborneVelocityMessage[timeStampNs=3171165200, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=3179549600, icaoAddress=IcaoAddress[string=4B1A00], speed=142.2892327767549, trackOrHeading=4.032001570784253]\n" +
            "AirborneVelocityMessage[timeStampNs=3210517700, icaoAddress=IcaoAddress[string=01024C], speed=228.01904908511267, trackOrHeading=5.311655187675027]\n" +
            "AirborneVelocityMessage[timeStampNs=3303948100, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=3322936000, icaoAddress=IcaoAddress[string=39CEAA], speed=225.14630401936483, trackOrHeading=6.1086454708443]\n" +
            "AirborneVelocityMessage[timeStampNs=3413090900, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=3486858100, icaoAddress=IcaoAddress[string=4BCDE9], speed=224.56192397369023, trackOrHeading=5.188331051863648]\n" +
            "AirborneVelocityMessage[timeStampNs=3532024100, icaoAddress=IcaoAddress[string=4241A9], speed=78.93831084937837, trackOrHeading=0.7807899010873279]\n" +
            "AirborneVelocityMessage[timeStampNs=3577785100, icaoAddress=IcaoAddress[string=4D0221], speed=99.32508798117995, trackOrHeading=0.8037111342619165]\n" +
            "AirborneVelocityMessage[timeStampNs=3588444200, icaoAddress=IcaoAddress[string=4B17E1], speed=196.68268095815884, trackOrHeading=2.472979085661972]\n" +
            "AirborneVelocityMessage[timeStampNs=3708566200, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=3719514700, icaoAddress=IcaoAddress[string=4B1A00], speed=141.56572667540618, trackOrHeading=4.032540250572385]\n" +
            "AirborneVelocityMessage[timeStampNs=3727943400, icaoAddress=IcaoAddress[string=39CEAA], speed=225.14630401936483, trackOrHeading=6.1086454708443]\n" +
            "AirborneVelocityMessage[timeStampNs=3758671100, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=3933091000, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=4077709100, icaoAddress=IcaoAddress[string=4D0221], speed=99.32508798117995, trackOrHeading=0.8037111342619165]\n" +
            "AirborneVelocityMessage[timeStampNs=4171624600, icaoAddress=IcaoAddress[string=4CA2BF], speed=201.28653781654862, trackOrHeading=3.305900995067771]\n" +
            "AirborneVelocityMessage[timeStampNs=4173289500, icaoAddress=IcaoAddress[string=4D029F], speed=160.78672859980443, trackOrHeading=3.9315156729836973]\n" +
            "AirborneVelocityMessage[timeStampNs=4176740300, icaoAddress=IcaoAddress[string=4B17E5], speed=113.14620519214384, trackOrHeading=5.481711358333747]\n" +
            "AirborneVelocityMessage[timeStampNs=4200509200, icaoAddress=IcaoAddress[string=01024C], speed=228.01904908511267, trackOrHeading=5.311655187675027]\n" +
            "AirborneVelocityMessage[timeStampNs=4211726900, icaoAddress=IcaoAddress[string=440237], speed=227.75426436901594, trackOrHeading=4.1068443167797195]\n" +
            "AirborneVelocityMessage[timeStampNs=4213719800, icaoAddress=IcaoAddress[string=495299], speed=225.87333483094972, trackOrHeading=4.161032416229856]\n" +
            "AirborneVelocityMessage[timeStampNs=4223029400, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=4228002900, icaoAddress=IcaoAddress[string=39CEAA], speed=225.23620964623444, trackOrHeading=6.106396149174445]\n" +
            "AirborneVelocityMessage[timeStampNs=4229530200, icaoAddress=IcaoAddress[string=4B1A00], speed=141.56572667540618, trackOrHeading=4.032540250572385]\n" +
            "AirborneVelocityMessage[timeStampNs=4261388900, icaoAddress=IcaoAddress[string=4CAC87], speed=229.16868847054272, trackOrHeading=3.767583203845613]\n" +
            "AirborneVelocityMessage[timeStampNs=4294540900, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=4368110200, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=4535927800, icaoAddress=IcaoAddress[string=A4F239], speed=252.5057681873847, trackOrHeading=5.1231661989825374]\n" +
            "AirborneVelocityMessage[timeStampNs=4568559900, icaoAddress=IcaoAddress[string=3C6545], speed=224.6019904378454, trackOrHeading=0.486184776110018]\n" +
            "AirborneVelocityMessage[timeStampNs=4570410500, icaoAddress=IcaoAddress[string=4BCDE9], speed=224.56192397369023, trackOrHeading=5.188331051863648]\n" +
            "AirborneVelocityMessage[timeStampNs=4581253000, icaoAddress=IcaoAddress[string=4D029F], speed=160.78672859980443, trackOrHeading=3.9315156729836973]\n" +
            "AirborneVelocityMessage[timeStampNs=4582695900, icaoAddress=IcaoAddress[string=4D0221], speed=98.5976765621624, trackOrHeading=0.8038462547886048]\n" +
            "AirborneVelocityMessage[timeStampNs=4623269100, icaoAddress=IcaoAddress[string=4CA2BF], speed=201.28653781654862, trackOrHeading=3.305900995067771]\n" +
            "AirborneVelocityMessage[timeStampNs=4633873700, icaoAddress=IcaoAddress[string=495299], speed=225.87333483094972, trackOrHeading=4.161032416229856]\n" +
            "AirborneVelocityMessage[timeStampNs=4669537000, icaoAddress=IcaoAddress[string=4B1A00], speed=141.56572667540618, trackOrHeading=4.032540250572385]\n" +
            "AirborneVelocityMessage[timeStampNs=4722963600, icaoAddress=IcaoAddress[string=39CEAA], speed=225.23620964623444, trackOrHeading=6.106396149174445]\n" +
            "AirborneVelocityMessage[timeStampNs=4730938900, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=5021984900, icaoAddress=IcaoAddress[string=4D029F], speed=160.78672859980443, trackOrHeading=3.9315156729836973]\n" +
            "AirborneVelocityMessage[timeStampNs=5127926700, icaoAddress=IcaoAddress[string=4D0221], speed=98.24138005726282, trackOrHeading=0.8076167287241673]\n" +
            "AirborneVelocityMessage[timeStampNs=5159521200, icaoAddress=IcaoAddress[string=4B1A00], speed=141.56572667540618, trackOrHeading=4.032540250572385]\n" +
            "AirborneVelocityMessage[timeStampNs=5232903200, icaoAddress=IcaoAddress[string=4B17E5], speed=112.45171907188535, trackOrHeading=5.5269051252891765]\n" +
            "AirborneVelocityMessage[timeStampNs=5260147800, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=5316792000, icaoAddress=IcaoAddress[string=440237], speed=227.75426436901594, trackOrHeading=4.1068443167797195]\n" +
            "AirborneVelocityMessage[timeStampNs=5416696100, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=5480739200, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=5526691500, icaoAddress=IcaoAddress[string=4CA2BF], speed=201.3903808373855, trackOrHeading=3.292882495543511]\n" +
            "AirborneVelocityMessage[timeStampNs=5544778400, icaoAddress=IcaoAddress[string=4BB279], speed=196.72573493678897, trackOrHeading=5.226345390979916]\n" +
            "AirborneVelocityMessage[timeStampNs=5607405000, icaoAddress=IcaoAddress[string=4D0221], speed=98.24138005726282, trackOrHeading=0.8076167287241673]\n" +
            "AirborneVelocityMessage[timeStampNs=5619520400, icaoAddress=IcaoAddress[string=4B1A00], speed=141.56572667540618, trackOrHeading=4.032540250572385]\n" +
            "AirborneVelocityMessage[timeStampNs=5705933900, icaoAddress=IcaoAddress[string=A4F239], speed=252.5057681873847, trackOrHeading=5.1231661989825374]\n" +
            "AirborneVelocityMessage[timeStampNs=5715627900, icaoAddress=IcaoAddress[string=4B2964], speed=78.3341972686795, trackOrHeading=0.8970810252373478]\n" +
            "AirborneVelocityMessage[timeStampNs=5722728800, icaoAddress=IcaoAddress[string=4951CE], speed=182.59589393041378, trackOrHeading=0.8471956239020995]\n" +
            "AirborneVelocityMessage[timeStampNs=5723088700, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=5847966700, icaoAddress=IcaoAddress[string=39CEAA], speed=225.23620964623444, trackOrHeading=6.106396149174445]\n" +
            "AirborneVelocityMessage[timeStampNs=5947403200, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=5998476400, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=6117960100, icaoAddress=IcaoAddress[string=4CA2BF], speed=201.01879638481574, trackOrHeading=3.2802323907239512]\n" +
            "AirborneVelocityMessage[timeStampNs=6163110900, icaoAddress=IcaoAddress[string=4D0221], speed=97.87026697005646, trackOrHeading=0.803983383853622]\n" +
            "AirborneVelocityMessage[timeStampNs=6286746200, icaoAddress=IcaoAddress[string=440237], speed=227.75426436901594, trackOrHeading=4.1068443167797195]\n" +
            "AirborneVelocityMessage[timeStampNs=6288172900, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=6297064200, icaoAddress=IcaoAddress[string=4B17E5], speed=112.37991494902734, trackOrHeading=5.575552083384871]\n" +
            "AirborneVelocityMessage[timeStampNs=6380505800, icaoAddress=IcaoAddress[string=01024C], speed=227.7292797502235, trackOrHeading=5.309789803459042]\n" +
            "AirborneVelocityMessage[timeStampNs=6432972300, icaoAddress=IcaoAddress[string=39CEAA], speed=225.23620964623444, trackOrHeading=6.106396149174445]\n" +
            "AirborneVelocityMessage[timeStampNs=6507943600, icaoAddress=IcaoAddress[string=4BCDE9], speed=224.56192397369023, trackOrHeading=5.188331051863648]\n" +
            "AirborneVelocityMessage[timeStampNs=6540789400, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=6565584200, icaoAddress=IcaoAddress[string=4D0221], speed=97.51402665802357, trackOrHeading=0.8077824837367386]\n" +
            "AirborneVelocityMessage[timeStampNs=6569525800, icaoAddress=IcaoAddress[string=4B1A00], speed=140.84226207505955, trackOrHeading=4.033084464593893]\n" +
            "AirborneVelocityMessage[timeStampNs=6609334700, icaoAddress=IcaoAddress[string=4CA2BF], speed=201.01879638481574, trackOrHeading=3.2802323907239512]\n" +
            "AirborneVelocityMessage[timeStampNs=6638424800, icaoAddress=IcaoAddress[string=4B17E1], speed=196.54201712058205, trackOrHeading=2.4823860299653697]\n" +
            "AirborneVelocityMessage[timeStampNs=6703087200, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=6739638900, icaoAddress=IcaoAddress[string=4B2964], speed=78.3341972686795, trackOrHeading=0.8970810252373478]\n" +
            "AirborneVelocityMessage[timeStampNs=6796662500, icaoAddress=IcaoAddress[string=4B1A1E], speed=208.44651536817736, trackOrHeading=2.364920272217771]\n" +
            "AirborneVelocityMessage[timeStampNs=6858084900, icaoAddress=IcaoAddress[string=39CEAA], speed=225.23620964623444, trackOrHeading=6.106396149174445]\n" +
            "AirborneVelocityMessage[timeStampNs=6946551100, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=6965499200, icaoAddress=IcaoAddress[string=4D0221], speed=97.51402665802357, trackOrHeading=0.8077824837367386]\n" +
            "AirborneVelocityMessage[timeStampNs=7019523400, icaoAddress=IcaoAddress[string=4B1A00], speed=140.84226207505955, trackOrHeading=4.033084464593893]\n" +
            "AirborneVelocityMessage[timeStampNs=7060165000, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=7201532100, icaoAddress=IcaoAddress[string=495299], speed=225.87333483094972, trackOrHeading=4.161032416229856]\n" +
            "AirborneVelocityMessage[timeStampNs=7208226400, icaoAddress=IcaoAddress[string=4B2964], speed=78.65618349013998, trackOrHeading=0.8919696054683265]\n" +
            "AirborneVelocityMessage[timeStampNs=7253088900, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=7257254700, icaoAddress=IcaoAddress[string=4B17E5], speed=111.85233727696972, trackOrHeading=5.624965850065693]\n" +
            "AirborneVelocityMessage[timeStampNs=7380502000, icaoAddress=IcaoAddress[string=01024C], speed=227.7292797502235, trackOrHeading=5.309789803459042]\n" +
            "AirborneVelocityMessage[timeStampNs=7382974700, icaoAddress=IcaoAddress[string=4D0221], speed=97.14285924590205, trackOrHeading=0.8041225665717969]\n" +
            "AirborneVelocityMessage[timeStampNs=7400051800, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=7403217000, icaoAddress=IcaoAddress[string=39CEAA], speed=225.23620964623444, trackOrHeading=6.106396149174445]\n" +
            "147";

    String expectedBangBangTheBest = "AirborneVelocityMessage[timeStampNs=100775400, icaoAddress=IcaoAddress[string=39D300], speed=217.1759987875795, trackOrHeading=5.707008696317668]\n" +
            "AirborneVelocityMessage[timeStampNs=146689300, icaoAddress=IcaoAddress[string=440237], speed=227.75426436901594, trackOrHeading=4.1068443167797195]\n" +
            "AirborneVelocityMessage[timeStampNs=208341000, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=210521800, icaoAddress=IcaoAddress[string=01024C], speed=228.01904908511267, trackOrHeading=5.311655187675027]\n" +
            "AirborneVelocityMessage[timeStampNs=232125000, icaoAddress=IcaoAddress[string=4B17E5], speed=114.64264880353804, trackOrHeading=5.335246702497837]\n" +
            "AirborneVelocityMessage[timeStampNs=235839800, icaoAddress=IcaoAddress[string=495299], speed=225.4352845765249, trackOrHeading=4.159837003030189]\n" +
            "AirborneVelocityMessage[timeStampNs=270464000, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=288802400, icaoAddress=IcaoAddress[string=4B1900], speed=239.0617299923047, trackOrHeading=4.4689760779824566]\n" +
            "AirborneVelocityMessage[timeStampNs=316898700, icaoAddress=IcaoAddress[string=4241A9], speed=78.93831084937837, trackOrHeading=0.7807899010873279]\n" +
            "AirborneVelocityMessage[timeStampNs=349526700, icaoAddress=IcaoAddress[string=4B1A00], speed=143.01277974924108, trackOrHeading=4.031468341546198]\n" +
            "AirborneVelocityMessage[timeStampNs=408163200, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=429622400, icaoAddress=IcaoAddress[string=4BCDE9], speed=224.56192397369023, trackOrHeading=5.188331051863648]\n" +
            "AirborneVelocityMessage[timeStampNs=636533100, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=645795900, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=705844800, icaoAddress=IcaoAddress[string=495299], speed=225.4352845765249, trackOrHeading=4.159837003030189]\n" +
            "AirborneVelocityMessage[timeStampNs=833094200, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=858094400, icaoAddress=IcaoAddress[string=39CEAA], speed=225.14630401936483, trackOrHeading=6.1086454708443]\n" +
            "AirborneVelocityMessage[timeStampNs=1065855100, icaoAddress=IcaoAddress[string=A4F239], speed=252.5057681873847, trackOrHeading=5.1231661989825374]\n" +
            "AirborneVelocityMessage[timeStampNs=1164431700, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=1184955200, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=1218171500, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=1219038000, icaoAddress=IcaoAddress[string=394C13], speed=227.36873123183696, trackOrHeading=2.610098023059783]\n" +
            "AirborneVelocityMessage[timeStampNs=1233094200, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=1280289300, icaoAddress=IcaoAddress[string=4B17E5], speed=114.51907720744369, trackOrHeading=5.363976166307547]\n" +
            "AirborneVelocityMessage[timeStampNs=1288753400, icaoAddress=IcaoAddress[string=495299], speed=225.4352845765249, trackOrHeading=4.159837003030189]\n" +
            "AirborneVelocityMessage[timeStampNs=1422737500, icaoAddress=IcaoAddress[string=4BCDE9], speed=224.56192397369023, trackOrHeading=5.188331051863648]\n" +
            "AirborneVelocityMessage[timeStampNs=1437921600, icaoAddress=IcaoAddress[string=39CEAA], speed=225.14630401936483, trackOrHeading=6.1086454708443]\n" +
            "AirborneVelocityMessage[timeStampNs=1447383100, icaoAddress=IcaoAddress[string=4D0221], speed=100.41027825998151, trackOrHeading=0.7998899024629483]\n" +
            "AirborneVelocityMessage[timeStampNs=1472930400, icaoAddress=IcaoAddress[string=4241A9], speed=78.93831084937837, trackOrHeading=0.7807899010873279]\n" +
            "AirborneVelocityMessage[timeStampNs=1541942000, icaoAddress=IcaoAddress[string=4CA2BF], speed=201.580839587717, trackOrHeading=3.3419912074156715]\n" +
            "AirborneVelocityMessage[timeStampNs=1605989900, icaoAddress=IcaoAddress[string=39D300], speed=217.1759987875795, trackOrHeading=5.707008696317668]\n" +
            "AirborneVelocityMessage[timeStampNs=1722323400, icaoAddress=IcaoAddress[string=4951CE], speed=182.59589393041378, trackOrHeading=0.8471956239020995]\n" +
            "AirborneVelocityMessage[timeStampNs=1731065400, icaoAddress=IcaoAddress[string=495299], speed=225.4352845765249, trackOrHeading=4.159837003030189]\n" +
            "AirborneVelocityMessage[timeStampNs=1823092600, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=1859535300, icaoAddress=IcaoAddress[string=4B1A00], speed=142.2892327767549, trackOrHeading=4.032001570784253]\n" +
            "AirborneVelocityMessage[timeStampNs=1872922900, icaoAddress=IcaoAddress[string=39CEAA], speed=225.14630401936483, trackOrHeading=6.1086454708443]\n" +
            "AirborneVelocityMessage[timeStampNs=1905870900, icaoAddress=IcaoAddress[string=A4F239], speed=252.5057681873847, trackOrHeading=5.1231661989825374]\n" +
            "AirborneVelocityMessage[timeStampNs=2012822900, icaoAddress=IcaoAddress[string=4D0221], speed=100.41027825998151, trackOrHeading=0.7998899024629483]\n" +
            "AirborneVelocityMessage[timeStampNs=2031706700, icaoAddress=IcaoAddress[string=440237], speed=227.75426436901594, trackOrHeading=4.1068443167797195]\n" +
            "AirborneVelocityMessage[timeStampNs=2076126500, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=2176455400, icaoAddress=IcaoAddress[string=4B17E5], speed=114.09074196651163, trackOrHeading=5.3955804256748845]\n" +
            "AirborneVelocityMessage[timeStampNs=2181294100, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=2186476200, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=2266325300, icaoAddress=IcaoAddress[string=495299], speed=225.4352845765249, trackOrHeading=4.159837003030189]\n" +
            "AirborneVelocityMessage[timeStampNs=2288212800, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=2289602000, icaoAddress=IcaoAddress[string=4B1A00], speed=142.2892327767549, trackOrHeading=4.032001570784253]\n" +
            "AirborneVelocityMessage[timeStampNs=2367925700, icaoAddress=IcaoAddress[string=39CEAA], speed=225.14630401936483, trackOrHeading=6.1086454708443]\n" +
            "AirborneVelocityMessage[timeStampNs=2386931400, icaoAddress=IcaoAddress[string=4BCDE9], speed=224.56192397369023, trackOrHeading=5.188331051863648]\n" +
            "AirborneVelocityMessage[timeStampNs=2496301600, icaoAddress=IcaoAddress[string=4CA2BF], speed=201.5900295949648, trackOrHeading=3.3289785377449017]\n" +
            "AirborneVelocityMessage[timeStampNs=2516450900, icaoAddress=IcaoAddress[string=4241A9], speed=78.93831084937837, trackOrHeading=0.7807899010873279]\n" +
            "AirborneVelocityMessage[timeStampNs=2558471700, icaoAddress=IcaoAddress[string=4B17E1], speed=197.00199797194514, trackOrHeading=2.470929992181367]\n" +
            "AirborneVelocityMessage[timeStampNs=2597642100, icaoAddress=IcaoAddress[string=4D0221], speed=99.68282090067501, trackOrHeading=0.7999956666085085]\n" +
            "AirborneVelocityMessage[timeStampNs=2662676700, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=2693625500, icaoAddress=IcaoAddress[string=495299], speed=225.4352845765249, trackOrHeading=4.159837003030189]\n" +
            "AirborneVelocityMessage[timeStampNs=2707170900, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=2729532100, icaoAddress=IcaoAddress[string=4B1A00], speed=142.2892327767549, trackOrHeading=4.032001570784253]\n" +
            "AirborneVelocityMessage[timeStampNs=2883205900, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=2912934400, icaoAddress=IcaoAddress[string=39CEAA], speed=225.14630401936483, trackOrHeading=6.1086454708443]\n" +
            "AirborneVelocityMessage[timeStampNs=3066745000, icaoAddress=IcaoAddress[string=440237], speed=227.75426436901594, trackOrHeading=4.1068443167797195]\n" +
            "AirborneVelocityMessage[timeStampNs=3113140100, icaoAddress=IcaoAddress[string=4D0221], speed=99.68282090067501, trackOrHeading=0.7999956666085085]\n" +
            "AirborneVelocityMessage[timeStampNs=3148679700, icaoAddress=IcaoAddress[string=495299], speed=225.87333483094972, trackOrHeading=4.161032416229856]\n" +
            "AirborneVelocityMessage[timeStampNs=3171165200, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=3179549600, icaoAddress=IcaoAddress[string=4B1A00], speed=142.2892327767549, trackOrHeading=4.032001570784253]\n" +
            "AirborneVelocityMessage[timeStampNs=3210517700, icaoAddress=IcaoAddress[string=01024C], speed=228.01904908511267, trackOrHeading=5.311655187675027]\n" +
            "AirborneVelocityMessage[timeStampNs=3303948100, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=3322936000, icaoAddress=IcaoAddress[string=39CEAA], speed=225.14630401936483, trackOrHeading=6.1086454708443]\n" +
            "AirborneVelocityMessage[timeStampNs=3413090900, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=3486858100, icaoAddress=IcaoAddress[string=4BCDE9], speed=224.56192397369023, trackOrHeading=5.188331051863648]\n" +
            "AirborneVelocityMessage[timeStampNs=3532024100, icaoAddress=IcaoAddress[string=4241A9], speed=78.93831084937837, trackOrHeading=0.7807899010873279]\n" +
            "AirborneVelocityMessage[timeStampNs=3577785100, icaoAddress=IcaoAddress[string=4D0221], speed=99.32508798117995, trackOrHeading=0.8037111342619165]\n" +
            "AirborneVelocityMessage[timeStampNs=3588444200, icaoAddress=IcaoAddress[string=4B17E1], speed=196.68268095815884, trackOrHeading=2.472979085661972]\n" +
            "AirborneVelocityMessage[timeStampNs=3708566200, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=3719514700, icaoAddress=IcaoAddress[string=4B1A00], speed=141.56572667540618, trackOrHeading=4.032540250572385]\n" +
            "AirborneVelocityMessage[timeStampNs=3727943400, icaoAddress=IcaoAddress[string=39CEAA], speed=225.14630401936483, trackOrHeading=6.1086454708443]\n" +
            "AirborneVelocityMessage[timeStampNs=3758671100, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=3933091000, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=4077709100, icaoAddress=IcaoAddress[string=4D0221], speed=99.32508798117995, trackOrHeading=0.8037111342619165]\n" +
            "AirborneVelocityMessage[timeStampNs=4171624600, icaoAddress=IcaoAddress[string=4CA2BF], speed=201.28653781654862, trackOrHeading=3.305900995067771]\n" +
            "AirborneVelocityMessage[timeStampNs=4173289500, icaoAddress=IcaoAddress[string=4D029F], speed=160.78672859980443, trackOrHeading=3.9315156729836973]\n" +
            "AirborneVelocityMessage[timeStampNs=4176740300, icaoAddress=IcaoAddress[string=4B17E5], speed=113.14620519214384, trackOrHeading=5.481711358333747]\n" +
            "AirborneVelocityMessage[timeStampNs=4200509200, icaoAddress=IcaoAddress[string=01024C], speed=228.01904908511267, trackOrHeading=5.311655187675027]\n" +
            "AirborneVelocityMessage[timeStampNs=4211726900, icaoAddress=IcaoAddress[string=440237], speed=227.75426436901594, trackOrHeading=4.1068443167797195]\n" +
            "AirborneVelocityMessage[timeStampNs=4213719800, icaoAddress=IcaoAddress[string=495299], speed=225.87333483094972, trackOrHeading=4.161032416229856]\n" +
            "AirborneVelocityMessage[timeStampNs=4223029400, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=4228002900, icaoAddress=IcaoAddress[string=39CEAA], speed=225.23620964623444, trackOrHeading=6.106396149174445]\n" +
            "AirborneVelocityMessage[timeStampNs=4229530200, icaoAddress=IcaoAddress[string=4B1A00], speed=141.56572667540618, trackOrHeading=4.032540250572385]\n" +
            "AirborneVelocityMessage[timeStampNs=4261388900, icaoAddress=IcaoAddress[string=4CAC87], speed=229.16868847054272, trackOrHeading=3.767583203845613]\n" +
            "AirborneVelocityMessage[timeStampNs=4294540900, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=4368110200, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=4535927800, icaoAddress=IcaoAddress[string=A4F239], speed=252.5057681873847, trackOrHeading=5.1231661989825374]\n" +
            "AirborneVelocityMessage[timeStampNs=4568559900, icaoAddress=IcaoAddress[string=3C6545], speed=224.6019904378454, trackOrHeading=0.486184776110018]\n" +
            "AirborneVelocityMessage[timeStampNs=4570410500, icaoAddress=IcaoAddress[string=4BCDE9], speed=224.56192397369023, trackOrHeading=5.188331051863648]\n" +
            "AirborneVelocityMessage[timeStampNs=4581253000, icaoAddress=IcaoAddress[string=4D029F], speed=160.78672859980443, trackOrHeading=3.9315156729836973]\n" +
            "AirborneVelocityMessage[timeStampNs=4582695900, icaoAddress=IcaoAddress[string=4D0221], speed=98.5976765621624, trackOrHeading=0.8038462547886048]\n" +
            "AirborneVelocityMessage[timeStampNs=4623269100, icaoAddress=IcaoAddress[string=4CA2BF], speed=201.28653781654862, trackOrHeading=3.305900995067771]\n" +
            "AirborneVelocityMessage[timeStampNs=4633873700, icaoAddress=IcaoAddress[string=495299], speed=225.87333483094972, trackOrHeading=4.161032416229856]\n" +
            "AirborneVelocityMessage[timeStampNs=4669537000, icaoAddress=IcaoAddress[string=4B1A00], speed=141.56572667540618, trackOrHeading=4.032540250572385]\n" +
            "AirborneVelocityMessage[timeStampNs=4722963600, icaoAddress=IcaoAddress[string=39CEAA], speed=225.23620964623444, trackOrHeading=6.106396149174445]\n" +
            "AirborneVelocityMessage[timeStampNs=4730938900, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=5021984900, icaoAddress=IcaoAddress[string=4D029F], speed=160.78672859980443, trackOrHeading=3.9315156729836973]\n" +
            "AirborneVelocityMessage[timeStampNs=5127926700, icaoAddress=IcaoAddress[string=4D0221], speed=98.24138005726282, trackOrHeading=0.8076167287241673]\n" +
            "AirborneVelocityMessage[timeStampNs=5159521200, icaoAddress=IcaoAddress[string=4B1A00], speed=141.56572667540618, trackOrHeading=4.032540250572385]\n" +
            "AirborneVelocityMessage[timeStampNs=5232903200, icaoAddress=IcaoAddress[string=4B17E5], speed=112.45171907188535, trackOrHeading=5.5269051252891765]\n" +
            "AirborneVelocityMessage[timeStampNs=5260147800, icaoAddress=IcaoAddress[string=4B2964], speed=77.93281516952223, trackOrHeading=0.8929626109272769]\n" +
            "AirborneVelocityMessage[timeStampNs=5316792000, icaoAddress=IcaoAddress[string=440237], speed=227.75426436901594, trackOrHeading=4.1068443167797195]\n" +
            "AirborneVelocityMessage[timeStampNs=5416696100, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=5480739200, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=5526691500, icaoAddress=IcaoAddress[string=4CA2BF], speed=201.3903808373855, trackOrHeading=3.292882495543511]\n" +
            "AirborneVelocityMessage[timeStampNs=5544778400, icaoAddress=IcaoAddress[string=4BB279], speed=196.72573493678897, trackOrHeading=5.226345390979916]\n" +
            "AirborneVelocityMessage[timeStampNs=5607405000, icaoAddress=IcaoAddress[string=4D0221], speed=98.24138005726282, trackOrHeading=0.8076167287241673]\n" +
            "AirborneVelocityMessage[timeStampNs=5619520400, icaoAddress=IcaoAddress[string=4B1A00], speed=141.56572667540618, trackOrHeading=4.032540250572385]\n" +
            "AirborneVelocityMessage[timeStampNs=5705933900, icaoAddress=IcaoAddress[string=A4F239], speed=252.5057681873847, trackOrHeading=5.1231661989825374]\n" +
            "AirborneVelocityMessage[timeStampNs=5715627900, icaoAddress=IcaoAddress[string=4B2964], speed=78.3341972686795, trackOrHeading=0.8970810252373478]\n" +
            "AirborneVelocityMessage[timeStampNs=5722728800, icaoAddress=IcaoAddress[string=4951CE], speed=182.59589393041378, trackOrHeading=0.8471956239020995]\n" +
            "AirborneVelocityMessage[timeStampNs=5723088700, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=5847966700, icaoAddress=IcaoAddress[string=39CEAA], speed=225.23620964623444, trackOrHeading=6.106396149174445]\n" +
            "AirborneVelocityMessage[timeStampNs=5947403200, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=5998476400, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=6117960100, icaoAddress=IcaoAddress[string=4CA2BF], speed=201.01879638481574, trackOrHeading=3.2802323907239512]\n" +
            "AirborneVelocityMessage[timeStampNs=6163110900, icaoAddress=IcaoAddress[string=4D0221], speed=97.87026697005646, trackOrHeading=0.803983383853622]\n" +
            "AirborneVelocityMessage[timeStampNs=6286746200, icaoAddress=IcaoAddress[string=440237], speed=227.75426436901594, trackOrHeading=4.1068443167797195]\n" +
            "AirborneVelocityMessage[timeStampNs=6288172900, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=6297064200, icaoAddress=IcaoAddress[string=4B17E5], speed=112.37991494902734, trackOrHeading=5.575552083384871]\n" +
            "AirborneVelocityMessage[timeStampNs=6380505800, icaoAddress=IcaoAddress[string=01024C], speed=227.7292797502235, trackOrHeading=5.309789803459042]\n" +
            "AirborneVelocityMessage[timeStampNs=6432972300, icaoAddress=IcaoAddress[string=39CEAA], speed=225.23620964623444, trackOrHeading=6.106396149174445]\n" +
            "AirborneVelocityMessage[timeStampNs=6507943600, icaoAddress=IcaoAddress[string=4BCDE9], speed=224.56192397369023, trackOrHeading=5.188331051863648]\n" +
            "AirborneVelocityMessage[timeStampNs=6540789400, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=6565584200, icaoAddress=IcaoAddress[string=4D0221], speed=97.51402665802357, trackOrHeading=0.8077824837367386]\n" +
            "AirborneVelocityMessage[timeStampNs=6569525800, icaoAddress=IcaoAddress[string=4B1A00], speed=140.84226207505955, trackOrHeading=4.033084464593893]\n" +
            "AirborneVelocityMessage[timeStampNs=6609334700, icaoAddress=IcaoAddress[string=4CA2BF], speed=201.01879638481574, trackOrHeading=3.2802323907239512]\n" +
            "AirborneVelocityMessage[timeStampNs=6638424800, icaoAddress=IcaoAddress[string=4B17E1], speed=196.54201712058205, trackOrHeading=2.4823860299653697]\n" +
            "AirborneVelocityMessage[timeStampNs=6703087200, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=6739638900, icaoAddress=IcaoAddress[string=4B2964], speed=78.3341972686795, trackOrHeading=0.8970810252373478]\n" +
            "AirborneVelocityMessage[timeStampNs=6796662500, icaoAddress=IcaoAddress[string=4B1A1E], speed=208.44651536817736, trackOrHeading=2.364920272217771]\n" +
            "AirborneVelocityMessage[timeStampNs=6858084900, icaoAddress=IcaoAddress[string=39CEAA], speed=225.23620964623444, trackOrHeading=6.106396149174445]\n" +
            "AirborneVelocityMessage[timeStampNs=6946551100, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=6965499200, icaoAddress=IcaoAddress[string=4D0221], speed=97.51402665802357, trackOrHeading=0.8077824837367386]\n" +
            "AirborneVelocityMessage[timeStampNs=7019523400, icaoAddress=IcaoAddress[string=4B1A00], speed=140.84226207505955, trackOrHeading=4.033084464593893]\n" +
            "AirborneVelocityMessage[timeStampNs=7060165000, icaoAddress=IcaoAddress[string=4D029F], speed=161.15254486753832, trackOrHeading=3.9337627224977503]\n" +
            "AirborneVelocityMessage[timeStampNs=7201532100, icaoAddress=IcaoAddress[string=495299], speed=225.87333483094972, trackOrHeading=4.161032416229856]\n" +
            "AirborneVelocityMessage[timeStampNs=7208226400, icaoAddress=IcaoAddress[string=4B2964], speed=78.65618349013998, trackOrHeading=0.8919696054683265]\n" +
            "AirborneVelocityMessage[timeStampNs=7253088900, icaoAddress=IcaoAddress[string=3C6481], speed=225.6042721691849, trackOrHeading=0.5494141736110284]\n" +
            "AirborneVelocityMessage[timeStampNs=7257254700, icaoAddress=IcaoAddress[string=4B17E5], speed=111.85233727696972, trackOrHeading=5.624965850065693]\n" +
            "AirborneVelocityMessage[timeStampNs=7380502000, icaoAddress=IcaoAddress[string=01024C], speed=227.7292797502235, trackOrHeading=5.309789803459042]\n" +
            "AirborneVelocityMessage[timeStampNs=7382974700, icaoAddress=IcaoAddress[string=4D0221], speed=97.14285924590205, trackOrHeading=0.8041225665717969]\n" +
            "AirborneVelocityMessage[timeStampNs=7400051800, icaoAddress=IcaoAddress[string=4D2228], speed=209.59113184813597, trackOrHeading=0.9194406912631904]\n" +
            "AirborneVelocityMessage[timeStampNs=7403217000, icaoAddress=IcaoAddress[string=39CEAA], speed=225.23620964623444, trackOrHeading=6.106396149174445]\n" +
            "147";
    @Test
    void BangBangTest() {
        System.out.println("BangBang verify: ");
        System.out.println(actual.equals(expectedBangBangTheBest));
        assertEquals(actual, expectedBangBangTheBest);
    }

    @Test
    void TestRandomMessageEd() {
        String expected = "AirborneVelocityMessage[timeStampNs=0, icaoAddress=IcaoAddress[string=4B1A00], speed=1061.4503686262444, trackOrHeading=4.221861463749146]";
        byte[] bytes = {(byte)0x8D, (byte)0x4B, (byte)0x1A, (byte)0x00, (byte)0xEA, (byte)0x0D, (byte)0xC8, (byte)0x9E, (byte)0x8F, (byte)0x7C, (byte)0x08, (byte)0x57, (byte)0xD5, (byte)0xF5};
        RawMessage message = new RawMessage(0, new ByteString(bytes));
        System.out.println(AirborneVelocityMessage.of(message));
        assertEquals(expected, AirborneVelocityMessage.of(message).toString());
    }

    @Test
    void TestRandomMessageEd1() {
        String expected = "AirborneVelocityMessage[timeStampNs=0, icaoAddress=IcaoAddress[string=485020], speed=81.90013721178154, trackOrHeading=3.1918647255875205]";
        byte[] bytes = {(byte) 0x8D, (byte)0x48, (byte)0x50, (byte)0x20, (byte)0x99, (byte)0x44, (byte)0x09, (byte)0x94, (byte)0x08, (byte)0x38, (byte)0x17, (byte)0x5B, (byte)0x28, (byte)0x4F};
        RawMessage message = new RawMessage(0, new ByteString(bytes));
        System.out.println(AirborneVelocityMessage.of(message));
        assertEquals(expected, AirborneVelocityMessage.of(message).toString());
    }
}