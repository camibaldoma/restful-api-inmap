package com.inmap.restfulApiInMap.classes;

import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.List;

public class GeoJsonHelper {
    public static Object convertToCoordinates(Geometry geometry) {
        if (geometry instanceof org.locationtech.jts.geom.Point p) {
            return new double[]{p.getX(), p.getY()};
        } else if (geometry instanceof org.locationtech.jts.geom.LineString line) {
            return coordsToList(line.getCoordinates());
        } else if (geometry instanceof org.locationtech.jts.geom.MultiLineString mLine) {
            List<List<double[]>> lines = new ArrayList<>();
            for (int i = 0; i < mLine.getNumGeometries(); i++) {
                lines.add(coordsToList(mLine.getGeometryN(i).getCoordinates()));
            }
            return lines;
        } else if (geometry instanceof org.locationtech.jts.geom.Polygon poly) {
            return extractCoordsFromPolygon(poly);
        } else if (geometry instanceof org.locationtech.jts.geom.MultiPolygon mPoly) {
            List<Object> coords = new ArrayList<>();
            for (int i = 0; i < mPoly.getNumGeometries(); i++) {
                coords.add(extractCoordsFromPolygon((org.locationtech.jts.geom.Polygon) mPoly.getGeometryN(i)));
            }
            return coords;
        }
        return null;
    }

    private static List<List<double[]>> extractCoordsFromPolygon(org.locationtech.jts.geom.Polygon poly) {
        List<List<double[]>> rings = new ArrayList<>();
        // Exterior ring
        rings.add(coordsToList(poly.getExteriorRing().getCoordinates()));
        // Interior rings (huecos)
        for (int i = 0; i < poly.getNumInteriorRing(); i++) {
            rings.add(coordsToList(poly.getInteriorRingN(i).getCoordinates()));
        }
        return rings;
    }

    private static List<double[]> coordsToList(org.locationtech.jts.geom.Coordinate[] coords) {
        List<double[]> list = new ArrayList<>();
        for (org.locationtech.jts.geom.Coordinate c : coords) {
            list.add(new double[]{c.x, c.y});
        }
        return list;
    }
}
