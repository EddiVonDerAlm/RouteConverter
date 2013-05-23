/*
 *
 *     This file is part of RouteConverter.
 *
 *     RouteConverter is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     RouteConverter is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with RouteConverter; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *     Copyright (C) 2007 Christian Pesch. All Rights Reserved.
 * /
 */

package slash.navigation.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static slash.common.io.Transfer.parseDouble;
import static slash.common.io.Transfer.trim;

/**
 * A basic position for common navigation usage
 *
 * @author Christian Pesch
 */

public class BasicPosition {
    private static final String NUMBER = "[[-|+]|\\d|\\.|E]";
    private static final String SEPARATOR = "[\\s|,]+";
    private static final Pattern POSITION_PATTERN = Pattern.compile("(\\s*" + NUMBER + "*\\s*),(\\s*" + NUMBER + "*\\s*)(,\\s*" + NUMBER + "+\\s*)?\\s*");
    private static final Pattern EXTENSION_POSITION_PATTERN = Pattern.compile("\\s*(" + NUMBER + "+)" + SEPARATOR + "(" + NUMBER + "+)" + SEPARATOR + "(" + NUMBER + "+)\\s*");

    private Double longitude, latitude, elevation;
    private String location;

    public BasicPosition(Double longitude, Double latitude, Double elevation, String location) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
        this.location = location;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getElevation() {
        return elevation;
    }

    public String getComment() {
        return location;
    }

    public static boolean isPosition(String coordinates) {
        Matcher matcher = POSITION_PATTERN.matcher(coordinates);
        return matcher.matches();
    }

    public static BasicPosition parsePosition(String coordinates, String comment) {
        Matcher matcher = POSITION_PATTERN.matcher(coordinates);
        if (!matcher.matches())
            throw new IllegalArgumentException("'" + coordinates + "' does not match");
        String longitude = matcher.group(1);
        String latitude = matcher.group(2);
        String elevation = matcher.group(3);
        if(elevation != null && elevation.startsWith(","))
            elevation = elevation.substring(1);
        return new BasicPosition(parseDouble(longitude), parseDouble(latitude), parseDouble(elevation), trim(comment));
    }

    public static List<BasicPosition> parsePositions(String listOfCoordinates) {
        List<BasicPosition> result = new ArrayList<BasicPosition>();
        Matcher matcher = POSITION_PATTERN.matcher(listOfCoordinates);
        while (matcher.find()) {
            result.add(parsePosition(matcher.group(0), null));
        }
        return result;
    }

    public static List<BasicPosition> parseExtensionPositions(String listOfCoordinates) {
        List<BasicPosition> result = new ArrayList<BasicPosition>();
        Matcher matcher = EXTENSION_POSITION_PATTERN.matcher(listOfCoordinates);
        while (matcher.find()) {
            String longitude = matcher.group(1);
            String latitude = matcher.group(2);
            String elevation = matcher.group(3);
            result.add(new BasicPosition(parseDouble(longitude), parseDouble(latitude), parseDouble(elevation), null));
        }
        return result;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicPosition that = (BasicPosition) o;

        return !(elevation != null ? !elevation.equals(that.elevation) : that.elevation != null) &&
                !(latitude != null ? !latitude.equals(that.latitude) : that.latitude != null) &&
                !(location != null ? !location.equals(that.location) : that.location != null) &&
                !(longitude != null ? !longitude.equals(that.longitude) : that.longitude != null);
    }

    public int hashCode() {
        int result = longitude != null ? longitude.hashCode() : 0;
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (elevation != null ? elevation.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}
