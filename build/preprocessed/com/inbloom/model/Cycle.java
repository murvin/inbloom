package com.inbloom.model;

import com.inbloom.model.visitors.DeserializationVisitor;
import com.inbloom.model.visitors.EntryPrevDateVisitor;
import com.inbloom.model.visitors.IVisitor;
import com.inbloom.model.visitors.SerializationVisitor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.util.Vector;

public class Cycle implements ISerializable {

    private Vector entriesList;
    private EntryPrevDateVisitor entryIndexVisitor;

    public Vector getEntries() {
        return this.entriesList;
    }

    public void addEntries(Entries entries) {
        if (this.entriesList == null) {
            this.entriesList = new Vector();
            this.entriesList.addElement(entries);
        } else {
            entryIndexVisitor = new EntryPrevDateVisitor(entries.getDate());
            try {
                accept(entryIndexVisitor);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Entries prevDateEntries = entryIndexVisitor.getPrevDateEntries();
            int idx = -1;
            if (prevDateEntries == null) {
                idx = 0;
            } else {
                idx = entriesList.indexOf(prevDateEntries) + 1;

            }

            this.entriesList.insertElementAt(entries, idx);
        }

    }

    public void accept(IVisitor visitor) throws IOException {
        if (entriesList != null) {
            for (int i = 0; i < entriesList.size(); i++) {
                Entries e = (Entries) entriesList.elementAt(i);
                visitor.visit(e);
            }
        }
    }

    public void serialize(DataOutputStream dos) throws IOException {
        dos.writeBoolean(entriesList != null);
        if (entriesList != null) {
            dos.write(entriesList.size());
            accept(new SerializationVisitor(dos));
        }
    }

    public void deserialize(DataInputStream dis) throws IOException {
        if (dis.readBoolean()) {
            entriesList = new Vector();
            int size = dis.read();
            for (int i = 0; i < size; i++) {
                entriesList.addElement(new Entries());
            }

            accept(new DeserializationVisitor(dis));
        }
    }
}
