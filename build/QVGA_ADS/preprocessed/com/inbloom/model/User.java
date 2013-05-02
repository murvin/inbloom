package com.inbloom.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class User implements ISerializable {

    private Cycle cycle;

    public Cycle getCycle() {
        return this.cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public void serialize(DataOutputStream dos) throws IOException {
        dos.writeBoolean(cycle != null);
        if (cycle != null) {
            cycle.serialize(dos);
        }
    }

    public void deserialize(DataInputStream dis) throws IOException {
        if (dis.readBoolean()) {
            cycle = new Cycle();
            cycle.deserialize(dis);
        }
    }
}
