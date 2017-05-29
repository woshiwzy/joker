package com.common.util;

/**
 * Created by clia on 2016/7/7.
 */
public class GPSConverter {

    /**
     * returns ref for latitude which is S or N.
     *
     * @param latitude
     * @return S or N
     */
    public static String latitudeRef(double latitude) {
        return latitude < 0.0d ? "S" : "N";
    }

    public static String longitudeRef(double longitude) {
        return longitude < 0.0d ? "W" : "E";
    }

    /**
     * convert latitude into DMS (degree minute second) format. For instance<br/>
     * -79.948862 becomes<br/>
     * 79/1,56/1,55903/1000<br/>
     * It works for latitude and longitude<br/>
     *
     * @param latitude could be longitude.
     * @return
     */
    synchronized public static final double convertLat(String latitude, String latitudeRef) {
        if (latitude == null || latitudeRef == null) {
            return 0.0;
        }
        String degrees, minutes, seconds;
        double lat = 0.0;
        double d, m, s;
        double a, b;

        degrees = latitude.substring(0, latitude.indexOf(","));
        a = Double.valueOf(degrees.substring(0, degrees.indexOf("/")));
        b = Double.valueOf(degrees.substring(degrees.indexOf("/") + 1));
        d = a / b;

        minutes = latitude.substring(latitude.indexOf(",") + 1, latitude.lastIndexOf(","));
        a = Double.valueOf(minutes.substring(0, minutes.indexOf("/")));
        b = Double.valueOf(minutes.substring(minutes.indexOf("/") + 1));
        m = a / b / 60;

        seconds = latitude.substring(latitude.lastIndexOf(",") + 1);
        a = Double.valueOf(seconds.substring(0, seconds.indexOf("/")));
        b = Double.valueOf(seconds.substring(seconds.indexOf("/") + 1));
        s = a / b / 3600;

        lat = d + m + s;

        if (latitudeRef.equals("S")) {
            lat = -lat;
        }

        return lat;
    }

    synchronized public static final double convertLon(String longitude, String longitudeRef) {
        if (longitude == null || longitudeRef == null) {
            return 0.0;
        }

        String degrees, minutes, seconds;
        double lat = 0.0;
        double d, m, s;
        double a, b;

        degrees = longitude.substring(0, longitude.indexOf(","));
        a = Double.valueOf(degrees.substring(0, degrees.indexOf("/")));
        b = Double.valueOf(degrees.substring(degrees.indexOf("/") + 1));
        d = a / b;

        minutes = longitude.substring(longitude.indexOf(",") + 1, longitude.lastIndexOf(","));
        a = Double.valueOf(minutes.substring(0, minutes.indexOf("/")));
        b = Double.valueOf(minutes.substring(minutes.indexOf("/") + 1));
        m = a / b / 60;

        seconds = longitude.substring(longitude.lastIndexOf(",") + 1);
        a = Double.valueOf(seconds.substring(0, seconds.indexOf("/")));
        b = Double.valueOf(seconds.substring(seconds.indexOf("/") + 1));
        s = a / b / 3600;

        lat = d + m + s;

        if (longitudeRef.equals("W")) {
            lat = -lat;
        }
        return lat;
    }
}
