package com.inbloom.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.util.Calendar;

public class Settings implements ISerializable {

    public static final int TEMP_CEL = 0x011;
    public static final int TEMP_FAH = 0x012;
    public static final int WEIGHT_KG = 0x013;
    public static final int WEIGHT_POUNDS = 0x014;
    private int ave_cycle_time;
    private int ave_luteal_time;
    private int ave_period_time;
    private Date cycle_start_date;
    private boolean isPregnant;
    private int basal_temp_unit;
    private int weight_unit;
    private Profile profile;
    private String currentLocale;
    private int currentTheme;
    private boolean hasShownTOSOnStartUp;

    public void initDefault() {
        ave_cycle_time = 28;
        ave_luteal_time = 14;
        ave_period_time = 7;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date());
        cycle_start_date = new Date(cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.YEAR));
        isPregnant = false;
        basal_temp_unit = TEMP_CEL;
        weight_unit = WEIGHT_KG;

        profile = new Profile();
        profile.setName("Your name");
        profile.setPassword(1234);
        profile.setForum_user_name("Forum user name");
        profile.setForum_password("1234");

        currentLocale = "en-GB";
        currentTheme = 0;
    }

    public void serialize(DataOutputStream dis) throws IOException {
        dis.write(this.ave_cycle_time);
        dis.write(this.ave_luteal_time);
        dis.write(this.ave_period_time);
        dis.writeBoolean(this.cycle_start_date != null);
        if (this.cycle_start_date != null) {
            cycle_start_date.serialize(dis);
        }
        dis.writeBoolean(isPregnant);
        dis.write(this.basal_temp_unit);
        dis.writeInt(this.weight_unit);

        dis.writeBoolean(profile != null);
        if (profile != null) {
            profile.serialize(dis);
        }

        dis.writeUTF(currentLocale);
        dis.write(currentTheme);
        dis.writeBoolean(hasShownTOSOnStartUp);
    }

    public void deserialize(DataInputStream dos) throws IOException {
        ave_cycle_time = dos.read();
        ave_luteal_time = dos.read();
        ave_period_time = dos.read();
        if (dos.readBoolean()) {
            cycle_start_date = new Date();
            cycle_start_date.deserialize(dos);
        }
        isPregnant = dos.readBoolean();
        basal_temp_unit = dos.read();
        weight_unit = dos.readInt();

        if (dos.readBoolean()) {
            profile = new Profile();
            profile.deserialize(dos);
        }

        currentLocale = dos.readUTF();
        currentTheme = dos.read();
        hasShownTOSOnStartUp = dos.readBoolean();
    }

    public int getAve_cycle_time() {
        return ave_cycle_time;
    }

    public int getAve_luteal_time() {
        return ave_luteal_time;
    }

    public int getAve_period_time() {
        return this.ave_period_time;
    }

    public Date getCycleStartDay() {
        return cycle_start_date;
    }

    public int getBasal_temp_unit() {
        return basal_temp_unit;
    }

    public boolean isIsPregnant() {
        return isPregnant;
    }

    public Profile getProfile() {
        return profile;
    }

    public int getWeight_unit() {
        return weight_unit;
    }

    public void setAve_cycle_time(int ave_cycle_time) {
        this.ave_cycle_time = ave_cycle_time;
    }

    public void setAve_luteal_time(int ave_luteal_time) {
        this.ave_luteal_time = ave_luteal_time;
    }

    public void setAve_period_time(int ave_period_time) {
        this.ave_period_time = ave_period_time;
    }

    public void setCycle_start_date(Date cycle_start_date) {
        this.cycle_start_date = cycle_start_date;
    }

    public void setBasal_temp_unit(int basal_temp_unit) {
        if (basal_temp_unit != TEMP_CEL && basal_temp_unit != TEMP_FAH) {
            throw new IllegalArgumentException();
        }

        this.basal_temp_unit = basal_temp_unit;
    }

    public void setIsPregnant(boolean isPregnant) {
        this.isPregnant = isPregnant;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setWeight_unit(int weight_unit) {
        if (weight_unit != WEIGHT_KG && weight_unit != WEIGHT_POUNDS) {
            throw new IllegalArgumentException();
        }
        this.weight_unit = weight_unit;
    }

    public void setCurrentLocale(String currentLocale) {
        this.currentLocale = currentLocale;
    }

    public String getCurrentLocale() {
        return this.currentLocale;
    }

    public void setCurrentTheme(int currentTheme) {
        this.currentTheme = currentTheme;
    }

    public int getCurrentTheme() {
        return this.currentTheme;
    }

    public boolean isHasShownTOSOnStartUp() {
        return hasShownTOSOnStartUp;
    }

    public void setHasShownTOSOnStartUp(boolean hasShownTOSOnStartUp) {
        this.hasShownTOSOnStartUp = hasShownTOSOnStartUp;
    }
}
