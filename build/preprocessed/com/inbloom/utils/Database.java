package com.inbloom.utils;

import com.inbloom.model.ISerializable;
import com.inbloom.model.Settings;
import com.inbloom.model.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class Database {

    public static final int USER = 0x0001;
    public static final int SETTINGS = 0x0002;
    // THE RECORDSTORES //
    /** The customer record store */
    private RecordStore rsUser;
    /** The settings record store */
    private RecordStore rsSettings;
    private static Database instance;

    private Database() {
        init();
    }

    /**
     * @return The Singleton instance of this class
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private void init() {
        try {
            rsUser = RecordStore.openRecordStore("USER", true);
            rsSettings = RecordStore.openRecordStore("SETTINGS", true);
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            rsUser.closeRecordStore();
            rsSettings.closeRecordStore();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        } finally {
            rsUser = null;
            rsSettings = null;
        }
    }

    public void saveISerializable(ISerializable serializable, int id) throws Exception {
        switch (id) {
            case USER:
                save(rsUser, serializable);
                break;
            case SETTINGS:
                save(rsSettings, serializable);
                break;
        }
    }

    public ISerializable retrieveISerializable(int id) throws Exception {
        switch (id) {
            case USER:
                return retrieve(rsUser, new User());
            case SETTINGS:
                return retrieve(rsSettings, new Settings());
            default:
                return null;
        }
    }

    private void clearStore(RecordStore store) {
        try {
            if (store.getNumRecords() > 0) {
                RecordEnumeration enu = null;
                for (enu = store.enumerateRecords(null, null, false); enu.hasNextElement();) {
                    store.deleteRecord(enu.nextRecordId());
                }
            }
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

    private void save(RecordStore store, ISerializable serializable) throws IOException, RecordStoreException {
        clearStore(store);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        byte[] data = null;

        try {
            serializable.serialize(dos);
            dos.flush();
            data = baos.toByteArray();
            store.addRecord(data, 0, data.length);
        } finally {
            if (baos != null) {
                baos.close();
            }
            if (dos != null) {
                dos.close();
            }
        }
    }

    private ISerializable retrieve(RecordStore store, ISerializable serializable) throws IOException, RecordStoreException {
        ByteArrayInputStream bais = null;
        DataInputStream dis = null;

        try {
            if (store.getNumRecords() > 0) {
                byte[] data = store.getRecord(store.getNextRecordID() - 1);
                bais = new ByteArrayInputStream(data);
                dis = new DataInputStream(bais);
                serializable.deserialize(dis);
                return serializable;
            }
        } finally {
            if (dis != null) {
                dis.close();
            }
            if (bais != null) {
                bais.close();
            }
        }
        return null;
    }
}
