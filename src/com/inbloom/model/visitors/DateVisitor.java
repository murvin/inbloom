package com.inbloom.model.visitors;

import com.inbloom.model.Date;
import com.inbloom.model.Entries;

public class DateVisitor implements IVisitor {

    private Entries entries;
    private Date date;

    public DateVisitor(Date date) {
        this.date = date;
    }

    public void visit(Entries entries) {
        if (entries.getDate().equals(this.date)) {
            this.entries = entries;
        }
    }

    public Entries getEntries() {
        return this.entries;
    }
}
