package com.inbloom.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Entry implements ISerializable {

    /** To identify entry type */
    private int id;
    /** Index of current selected value */
    private int selectedIndex;
    private Date date;

    public Entry() {
    }

    public Entry(int id, int selectedIndex) {
        this.id = id;
        this.selectedIndex = selectedIndex;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
    
    public void setDate(Date date){
        this.date = date;
    }
    
    public Date getDate(){
        return this.date;
    }

    public void serialize(DataOutputStream dos) throws IOException {
        dos.writeInt(id);
        dos.writeInt(selectedIndex);

    }

    public void deserialize(DataInputStream dis) throws IOException {
        id = dis.readInt();
        selectedIndex = dis.readInt();
    }
}
