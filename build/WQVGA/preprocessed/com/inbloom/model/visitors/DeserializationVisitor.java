package com.inbloom.model.visitors;

import com.inbloom.model.Entries;
import java.io.DataInputStream;
import java.io.IOException;

public class DeserializationVisitor implements IVisitor {

    private DataInputStream dis;

    public DeserializationVisitor(DataInputStream dis) {
        this.dis = dis;
    }

    public void visit(Entries entries) {
        try {
            entries.deserialize(dis);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
