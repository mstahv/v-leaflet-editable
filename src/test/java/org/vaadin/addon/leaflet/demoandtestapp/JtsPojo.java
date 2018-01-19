/*
 * Copyright 2018 Vaadin Community.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.addon.leaflet.demoandtestapp;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.time.LocalDate;

/**
 *
 * @author mstahv
 */
public class JtsPojo {
    
    private String name;
    private LocalDate date;
    private Point point;
    private LineString lineString;
    private LinearRing linearRing;
    private Polygon polygon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public LineString getLineString() {
        return lineString;
    }

    public void setLineString(LineString lineString) {
        this.lineString = lineString;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    @Override
    public String toString() {
        return "JtsPojo [\n name=" + name + ",\n date=" + date + ",\n point=" + point + ",\n lineString=" + lineString + ",\n linearRing=" + linearRing + ",\n polygon=" + polygon + "\n]";
    }

    public LinearRing getLinearRing() {
        return linearRing;
    }

    public void setLinearRing(LinearRing linearRing) {
        this.linearRing = linearRing;
    }
    
}
