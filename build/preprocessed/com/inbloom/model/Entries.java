package com.inbloom.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Entries implements ISerializable {

    private Date date;
    private Entry moodEntry;
    private Entry intimacyEntry;
    private Entry spottingEntry;
    private Entry flowEntry;
    private String weight, temp, note;
    private int[] symptomsIndices;
    private int tempUnit, weightUnit;

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Entry getFlowEntry() {
        return flowEntry;
    }

    public Entry getIntimacyEntry() {
        return intimacyEntry;
    }

    public Entry getMoodEntry() {
        return moodEntry;
    }

    public Entry getSpottingEntry() {
        return spottingEntry;
    }

    public String getTemp() {
        return temp;
    }

    public String getWeight() {
        return weight;
    }

    public int[] getSymptomsIndices() {
        return symptomsIndices;
    }

    public void setFlowEntry(Entry flowEntry) {
        this.flowEntry = flowEntry;
    }

    public void setIntimacyEntry(Entry intimacyEntry) {
        this.intimacyEntry = intimacyEntry;
    }

    public void setMoodEntry(Entry moodEntry) {
        this.moodEntry = moodEntry;
    }

    public void setSpottingEntry(Entry spottingEntry) {
        this.spottingEntry = spottingEntry;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setSymptomsIndices(int[] symptomsIndices) {
        this.symptomsIndices = symptomsIndices;
    }

    public void serialize(DataOutputStream dos) throws IOException {
        dos.writeBoolean(date != null);
        if (date != null) {
            date.serialize(dos);
        }

        dos.writeBoolean(moodEntry != null);
        if (moodEntry != null) {
            moodEntry.serialize(dos);
        }

        dos.writeBoolean(intimacyEntry != null);
        if (intimacyEntry != null) {
            intimacyEntry.serialize(dos);
        }

        dos.writeBoolean(spottingEntry != null);
        if (spottingEntry != null) {
            spottingEntry.serialize(dos);
        }

        dos.writeBoolean(flowEntry != null);
        if (flowEntry != null) {
            flowEntry.serialize(dos);
        }

        dos.writeBoolean(weight != null);
        if (weight != null) {
            dos.writeUTF(weight);
            dos.write(weightUnit);
        }

        dos.writeBoolean(temp != null);
        if (temp != null) {
            dos.writeUTF(temp);
            dos.write(tempUnit);
        }

        dos.writeBoolean(symptomsIndices != null);
        if (symptomsIndices != null) {
            dos.writeInt(symptomsIndices.length);
            for (int i = 0; i < symptomsIndices.length; i++) {
                int j = symptomsIndices[i];
                dos.writeInt(j);
            }
        }

        dos.writeBoolean(note != null);
        if (note != null) {
            dos.writeUTF(note);
        }

    }

    public void deserialize(DataInputStream dis) throws IOException {
        if (dis.readBoolean()) {
            date = new Date();
            date.deserialize(dis);
        }

        if (dis.readBoolean()) {
            moodEntry = new Entry();
            moodEntry.deserialize(dis);
        }

        if (dis.readBoolean()) {
            intimacyEntry = new Entry();
            intimacyEntry.deserialize(dis);
        }

        if (dis.readBoolean()) {
            spottingEntry = new Entry();
            spottingEntry.deserialize(dis);
        }

        if (dis.readBoolean()) {
            flowEntry = new Entry();
            flowEntry.deserialize(dis);
        }

        if (dis.readBoolean()) {
            weight = dis.readUTF();
            weightUnit = dis.read();
        }

        if (dis.readBoolean()) {
            temp = dis.readUTF();
            tempUnit = dis.read();
        }

        if (dis.readBoolean()) {
            int size = dis.readInt();
            symptomsIndices = new int[size];
            for (int i = 0; i < symptomsIndices.length; i++) {
                symptomsIndices[i] = dis.readInt();
            }
        }

        if (dis.readBoolean()) {
            note = dis.readUTF();
        }
    }

    public int getTempUnit() {
        return tempUnit;
    }

    public int getWeightUnit() {
        return weightUnit;
    }

    public void setTempUnit(int tempUnit) {
        this.tempUnit = tempUnit;
    }

    public void setWeightUnit(int weightUnit) {
        this.weightUnit = weightUnit;
    }
    
}
