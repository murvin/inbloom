package com.inbloom.model.visitors;

import com.inbloom.model.Coordinates;
import com.inbloom.model.Entries;
import com.inbloom.model.Settings;
import com.inbloom.utils.Utils;
import java.util.Vector;

public class CoordinatesVisitor implements IVisitor {
    
    private int year, month, entryId, unit;
    private Vector coordinates;
    
    public CoordinatesVisitor(int month, int year, int entryId, int unit) {
        this.month = month;
        this.year = year;
        this.entryId = entryId;
        this.unit = unit;
        coordinates = new Vector();
    }
    
    public void visit(Entries entries) {
        if (entries.getDate().getYear() == this.year) {
            if (entries.getDate().getMonth() == this.month) {
                if (entryId == Utils.ENTRY_TEMP) {
                    if (entries.getTemp() != null) {
                        if (entries.getTempUnit() == unit) {
                            addToCoordinates(new Coordinates(entries.getDate().getDay(), Float.parseFloat(entries.getTemp())));
                        } else {
                            float t = Float.parseFloat(entries.getTemp());
                            if (unit == Settings.TEMP_CEL) {
                                addToCoordinates(new Coordinates(entries.getDate().getDay(), Utils.fahToCel(t)));
                            } else {
                                addToCoordinates(new Coordinates(entries.getDate().getDay(), Utils.celToFah(t)));
                            }
                        }
                    }
                } else if (entryId == Utils.ENTRY_WEIGHT) {
                    if (entries.getWeight() != null) {
                        if (entries.getWeightUnit() == unit) {
                            addToCoordinates(new Coordinates(entries.getDate().getDay(), Float.parseFloat(entries.getWeight())));
                        } else {
                            float w = Float.parseFloat(entries.getWeight());
                            if (unit == Settings.WEIGHT_KG) {
                                addToCoordinates(new Coordinates(entries.getDate().getDay(), Utils.lbToKg(w)));
                            } else {
                                addToCoordinates(new Coordinates(entries.getDate().getDay(), Utils.kgToLb(w)));
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void addToCoordinates(Coordinates coor) {
        if (coordinates == null) {
            coordinates = new Vector();
        }
        
        coordinates.addElement(coor);
    }
    
    public Coordinates[] getCoordinates() {
        if (coordinates == null) {
            return null;
        } else {
            Coordinates[] coors = new Coordinates[coordinates.size()];
            for (int i = 0; i < coordinates.size(); i++) {
                coors[i] = (Coordinates) coordinates.elementAt(i);
            }
            return coors;
        }
    }
}
