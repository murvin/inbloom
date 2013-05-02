package com.inbloom.model.visitors;

import com.inbloom.model.Entries;

import java.io.DataOutputStream;
import java.io.IOException;

public class SerializationVisitor implements IVisitor {

    private DataOutputStream dos;

    public SerializationVisitor(DataOutputStream dos) {
        this.dos = dos;
    }

    public void visit(Entries entries) {
        try {
            entries.serialize(dos);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
