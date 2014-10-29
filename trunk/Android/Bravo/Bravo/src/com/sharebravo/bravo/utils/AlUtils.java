package com.sharebravo.bravo.utils;

import java.util.ArrayList;

public class AlUtils {
    public static int ccw(GPoint p0, GPoint p1, GPoint p2) {
        double dx1, dx2, dy1, dy2;
        dx1 = p1.lat;
        dx2 = p2.lat;
        dy1 = p1.lon - p0.lon;
        dy2 = p2.lon - p0.lon;
        if (dx1 * dy2 > dy1 * dx2)
            return 1;
        else if (dx1 * dy2 < dy1 * dx2)
            return -1;
        else
        {
            if (dx1 * dx2 < 0 || dy1 * dy2 < 0)
                return -1;
            else if ((dx1 * dx1 + dy1 * dy1) >= (dx2 * dx2 + dy2 * dy2))
                return 0;
            else
                return 1;
        }
    }

    public static double theta(GPoint p1, GPoint p2) {
        double dx, dy, ax, ay;
        double t;
        dx = p2.lat - p1.lat;
        dy = p2.lon - p1.lon;
        ax = Math.abs(dx);
        ay = Math.abs(dy);
        if (dx == 0 && dy == 0)
            t = 0;
        else
            t = dy / (ax + ay);
        if (dx < 0)
            t = 2 - t;
        else if (dy < 0)
            t = 4 + t;
        return t * 90.0;
    }

    public static double grahamscan(ArrayList<GPoint> p) {
        int min = 0;
        for (int i = 1; i < p.size(); i++)
            if (p.get(i).lon < p.get(min).lon)
                min = i;
        for (int i = 0; i < p.size(); i++)
            if (p.get(i).lon == p.get(min).lon && p.get(i).lat == p.get(min).lat)
                min = i;
        GPoint t =p.get(1);
        p.set(1, p.get(min));
        p.set(min, t);
        return 0;
    }
}

class GPoint {
    public double lat;
    public double lon;
}
