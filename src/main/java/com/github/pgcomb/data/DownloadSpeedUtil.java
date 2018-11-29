package com.github.pgcomb.data;

import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Title: DownloadSpeedUtil <br>
 * Description: DownloadSpeedUtil <br>
 * Date: 2018年09月14日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class DownloadSpeedUtil {
    private final static String SIZE_UNIT_B = "B";
    private final static String SIZE_UNIT_KB = "K";
    private final static String SIZE_UNIT_MB = "M";
    private final static String SIZE_UNIT_GB = "G";

    private final static List<String> SIZE_UNITS = Arrays.asList(SIZE_UNIT_B, SIZE_UNIT_KB, SIZE_UNIT_MB, SIZE_UNIT_GB);

    private final static String SPEED_UNIT_B_S = "B/s";
    private final static String SPEED_UNIT_KB_S = "K/s";
    private final static String SPEED_UNIT_MB_S = "M/s";
    private final static String SPEED_UNIT_GB_S = "G/s";
    private final static List<String> SPEED_UNITS = Arrays.asList(SPEED_UNIT_B_S, SPEED_UNIT_KB_S, SPEED_UNIT_MB_S, SPEED_UNIT_GB_S);

    private final static String TIME_UNIT_MS = "ms";
    private final static String TIME_UNIT_S = "s";
    private final static String TIME_UNIT_M = "m";
    private final static String TIME_UNIT_H = "h";

    private final static NumberFormat nf = NumberFormat.getInstance();

    public static void main(String[] args) {
        System.out.println(timeStr(Duration.between(LocalDateTime.of(2018, 9, 14, 13, 59, 1,345),LocalDateTime.now())));
        System.out.println(Duration.between(LocalDateTime.of(2018, 6, 5, 4, 2, 1),
                LocalDateTime.of(2019, 7, 6, 6, 5, 3, 2)).getSeconds());
        System.out.println(speed(1234123412234L, Duration.ofSeconds(12)));
    }

    public static Speed speed(long sizeB, Duration duration) {

        if (duration.toMillis() == 0) {
            return new Speed(timeStr(Duration.ofSeconds(0)), joint(0, SPEED_UNIT_B_S), sizeStr(sizeB, SIZE_UNIT_B));
        }
        long speed = (sizeB * 1000) / duration.toMillis();
        return new Speed(timeStr(duration), speedStr(speed, SPEED_UNIT_B_S), sizeStr(sizeB, SIZE_UNIT_B));
    }

    public static String sizeStr(long sizeB, String sizeUnit) {
        return tranStr(sizeB, sizeUnit, SIZE_UNITS);
    }

    public static String speedStr(long speed, String speedUnit) {
        return tranStr(speed, speedUnit, SPEED_UNITS);
    }

    public static String tranStr(long size, String sizeUnit, List<String> stringList) {

        int index = stringList.indexOf(sizeUnit);
        int upSize = stringList.size() - index - 1;
        double tran = size;
        int i = 0;
        while (tran >= 1024 && upSize > i) {
            tran = tran / 1024;
            i++;
        }
        return joint(tran, stringList.get(index + i));
    }

    public static String timeStr(Duration duration) {
        long dms = duration.toMillis();
        long h = dms / 3600000;
        long dm = dms % 3600000;
        long m = dm / 60000;
        long ds = dm % 60000;
        long s = ds / 1000;
        long ms = ds % 1000;
        return (h == 0?"":joint(h,TIME_UNIT_H)) + (m == 0?"":joint(m,TIME_UNIT_M)) +
                (s == 0?"":joint(s,TIME_UNIT_S))+(ms == 0?"":joint(ms,TIME_UNIT_MS));
    }

    private static String joint(Number data, String unit) {
        nf.setMaximumFractionDigits(2);
        return nf.format(data) + unit;
    }
}

class Speed {
    private String duration;
    private String speed;
    private String size;

    public Speed(String duration, String speed, String size) {
        this.duration = duration;
        this.speed = speed;
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "[" +
                "duration='" + duration + '\'' +
                ", speed='" + speed + '\'' +
                ", size='" + size + '\'' +
                ']';
    }
}