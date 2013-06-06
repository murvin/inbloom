package com.inbloom.model.visitors;

import com.inbloom.model.Date;
import com.inbloom.model.Entries;

public class EntryPrevDateVisitor implements IVisitor {

    private Date date;
    private Entries tempEntries;

    public EntryPrevDateVisitor(Date date) {
        this.date = date;
    }

    public void visit(Entries entries) {
        if (entries.getDate().compareTo(date) == -1) {
            if (tempEntries == null) {
                tempEntries = entries;
            } else {
                if (entries.getDate().compareTo(tempEntries.getDate()) == 1) {
                    tempEntries = entries;
                }
            }
        }
    }

    public Entries getPrevDateEntries() {
        return tempEntries;
    }
}
