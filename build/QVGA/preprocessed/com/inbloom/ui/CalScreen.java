package com.inbloom.ui;

import com.inbloom.InBloomController;
import com.inbloom.model.Settings;
import com.inbloom.ui.components.CalDay;
import com.inbloom.ui.components.CalMonth;
import com.inbloom.ui.components.MapKeys;
import com.inbloom.ui.components.MonthSelector;
import com.inbloom.utils.Database;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
import com.inbloom.utils.Utils;

import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.Component;
import com.uikit.coreElements.ITouchEventListener;
import com.uikit.coreElements.Panel;

import java.util.Calendar;
import java.util.Date;
import javax.microedition.lcdui.Image;

public class CalScreen extends InBloomScreen {

    private MonthSelector monthSelector;
    private CalMonth month;
    private MapKeys mapKeys;
    /** Actual month and current Month */
    private int mnth, currentMonth;
    /** Actual year and current year*/
    private int year, currentYear;
    /** Current day */
    private int day;
    private Image imgHighLight;

    public CalScreen() {
        initResources();
        initComponents();
    }

    private void initResources() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        mnth = c.get(Calendar.MONTH);
        currentMonth = mnth;
        year = c.get(Calendar.YEAR);
        currentYear = year;
        day = c.get(Calendar.DAY_OF_MONTH);

        this.imgHighLight = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HIGHLIGHT_PATCH);
    }

    private void initComponents() {
        this.monthSelector = new MonthSelector(iWidth, 9 * UiKitDisplay.getHeight() / 100, true);
        this.monthSelector.setCurrent(mnth, year);
        this.monthSelector.setEventListener(this);
        addComponent(monthSelector);

        initCalMonth();
        this.month.setEventListener(this);
        addComponent(this.month);
        this.month.x = UiKitDisplay.getWidth();
        this.month.enter();

        //#if QVGA || WQVGA || QVGA_ADS
        this.mapKeys = new MapKeys(iWidth, 40);
        //#elif WVGA
//#         this.mapKeys = new MapKeys(iWidth, 80);
        //#endif 

        addComponent(this.mapKeys);
        updateBottomOffset();

        getStyle(true).setPadding(0, 0, bottomPadding, 0);
    }

    /**
     * Gets the Zeller calculated day of the week for the
     * corresponding year, month and day
     * 
     * @return          A calibrated offset to match calendar's starting 
     *                  day being Monday instead of Saturday (Zeller value = 0)
     */
    private int getCalibratedStartOffset() {
        int startOffset = Utils.getZellerDay(year, mnth, 1);
        if (startOffset == 0) {
            startOffset = 5;
        } else if (startOffset == 1) {
            startOffset = 6;
        } else {
            startOffset -= 2;
        }
        return startOffset;
    }

    private void initCalMonth() {
        Settings settings = null;
        try {
            settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int startOffset = getCalibratedStartOffset();
        com.inbloom.model.Date lastCycleStartDate = settings.getCycleStartDay();
        com.inbloom.model.Date targetCycleStartDate = new com.inbloom.model.Date(0, mnth, year);
        int cycleLength = settings.getAve_cycle_time();
        int periodLength = settings.getAve_period_time();

        int startDay = Utils.getStartingDay(lastCycleStartDate, targetCycleStartDate, settings.getAve_cycle_time()) + startOffset;
        int nextStartDay = startDay + cycleLength;
        int prevStartDay = startDay - cycleLength;

        int ovulationDay = startDay + settings.getAve_cycle_time() - settings.getAve_luteal_time();
        int nextOvulationDay = nextStartDay + settings.getAve_cycle_time() - settings.getAve_luteal_time();
        int prevOvulationDay = prevStartDay + settings.getAve_cycle_time() - settings.getAve_luteal_time();

        int fertileDay = ovulationDay - 4;
        int nextFertileDay = nextOvulationDay - 4;
        int prevFertileDay = prevOvulationDay - 4;

        int[] periodDays = new int[periodLength * 3];
        for (int i = 0; i < periodLength; i++) {
            periodDays[i] = startDay++;
            periodDays[i + periodLength] = nextStartDay++;
            periodDays[i + periodLength + periodLength] = prevStartDay++;
        }

        month = new CalMonth(
                iWidth, 10,
                year == currentYear ? (mnth == currentMonth ? day : -1) : -1,
                Utils.getMonthLength(year, mnth),
                new int[]{
                    fertileDay++, fertileDay++, fertileDay++, fertileDay++, fertileDay++, fertileDay++,
                    nextFertileDay++, nextFertileDay++, nextFertileDay++, nextFertileDay++, nextFertileDay++, nextFertileDay++,
                    prevFertileDay++, prevFertileDay++, prevFertileDay++, prevFertileDay++, prevFertileDay++, prevFertileDay++
                },
                periodDays,
                new int[]{
                    prevOvulationDay, ovulationDay, nextOvulationDay
                }, this, this.imgHighLight, startOffset);

    }

    private void enterMonth(boolean isNext) {
        CalMonth temp = month;
        initCalMonth();
        month.x = isNext ? UiKitDisplay.getWidth() : -UiKitDisplay.getWidth();
        month.y = temp.y;
        month.isLayoutable = false;
        month.setEventListener(this);
        insertComponentAt(month, 1, false);

        if (isNext) {
            temp.exit();
        } else {
            temp.exitToRight();
        }

        month.enter();

    }

    public void onComponentEvent(Component c, int e, Object o, int p) {
        super.onComponentEvent(c, e, o, p);
        if (c == monthSelector) {
            mnth = e;
            year = p;
            enterMonth(((Boolean) o).booleanValue());
        } else if (c instanceof CalMonth) {
            if (e == CalMonth.EXIT_FINISHED) {
                ((Panel) c).removeAllComponents();
                c.isLayoutable = false;
                removeComponent(c, false);
            } else if (e == CalMonth.ENTER_FINISHED) {
                month.isLayoutable = true;
                month.setEventListener(this);
            }
        } else if (c instanceof CalDay) {
            int d = Integer.parseInt((String) o);
            if (d != -1) {
                com.inbloom.model.Date date = new com.inbloom.model.Date(d, mnth, year);
                boolean isPeriodToday = false;
                try {
                    Settings settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
                    isPeriodToday = settings.getCycleStartDay().equals(date);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                controller.navigateScreen(InBloomController.SCREEN_ENTRY, true,
                        new Object[]{Utils.getEntriesForDate(date), date, new Boolean(isPeriodToday)});
            }
        }
    }

    public boolean onDrag(int type, int iStartX, int iStartY, int iDeltaX, int iDeltaY) {
        boolean isHandled = super.onDrag(type, iStartX, iStartY, iDeltaX, iDeltaY);
        if (type == ITouchEventListener.DRAG_RELEASE) {
            if (Math.abs(iDeltaX) > 20) {
                if (iDeltaX < 0) {
                    monthSelector.next();
                    isHandled = true;
                } else if (iDeltaX > 0) {
                    monthSelector.previous();
                    isHandled = true;
                }
            }
        }

        return isHandled;
    }
}
