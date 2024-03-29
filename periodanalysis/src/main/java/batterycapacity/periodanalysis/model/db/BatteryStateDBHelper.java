package batterycapacity.periodanalysis.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import batterycapacity.periodanalysis.entities.BatteryState;
import batterycapacity.periodanalysis.model.Preferences;

public class BatteryStateDBHelper extends BatteryCapacityDBHelper {

    enum Columns {
        VOLTAGE,
        CURRENT,
        LEVEL,
        PERIOD
    }

    private static final String TABLE_NAME = "BATTERY_STATE";

    private boolean empty = statesCount() == 0;

    public boolean isEmpty() {
        return empty;
    }

    public void insert(BatteryState batteryState) {
        ContentValues stateValues = new ContentValues();
        stateValues.put(Columns.VOLTAGE.name(), batteryState.getVoltage());
        stateValues.put(Columns.CURRENT.name(), batteryState.getCurrent());
        stateValues.put(Columns.LEVEL.name(), batteryState.getLevel());
        stateValues.put(Columns.PERIOD.name(), batteryState.getPeriod());
        getWritableDatabase().insert(TABLE_NAME, null, stateValues);
        empty = false;
    }

    public void deleteAll() {
        getWritableDatabase().execSQL("delete from " + TABLE_NAME);
        empty = true;

    }

    public int statesCount() {
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase()
                    .rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME,
                            null);
            cursor.moveToFirst();
            return cursor.getInt(0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    //    public MeteringResult getMeteringResult() {
    public BatteryStateDBHelper(Context context) {
        super(context);
    }

    //    }
    static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Columns.VOLTAGE + " REAL, "
                + Columns.CURRENT + " REAL, "
                + Columns.LEVEL + " INTEGER, "
                + Columns.PERIOD + " INTEGER);");
    }

    public List<BatteryState> getByPeriod(int period) {
        Cursor cursor = getReadableDatabase().query(TABLE_NAME,
                new String[]{Columns.VOLTAGE.name(), Columns.CURRENT.name(), Columns.LEVEL.name()},
                Columns.PERIOD.name() + " = ?", new String[]{String.valueOf(period)}, null, null,
                null, null);
        List<BatteryState> states = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                states.add(new BatteryState(cursor.getDouble(0),
                        cursor.getDouble(1),
                        cursor.getInt(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return states;
    }

    //        }
    //        Cursor cursor = null;
    //        try {
    //            cursor = getReadableDatabase().query(TABLE_NAME,
    //                    new String[]{Columns.CURRENT.name(), Columns.VOLTAGE.name(),  Columns.LEVEL.name()},
    //                    null, null, null, null,
    //                    null, null);
    //            cursor.moveToFirst();
    //            double sumOfPowers = 0;
    //            double sumOfVoltages = 0;
    //            int startLevel = cursor.getInt(2);
    //            do{
    //                double voltage = cursor.getDouble(1);
    //                sumOfPowers += cursor.getDouble(0)*voltage;
    //                sumOfVoltages += voltage;
    //            }while (cursor.moveToNext());
    //            cursor.moveToLast();
    //            int finishLevel = cursor.getInt(2);
    //            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+startLevel + "   " + finishLevel + "   "+ cursor.getCount() + "   v "+cursor.getDouble(1));
    //            return new MeteringResult(sumOfPowers, sumOfVoltages / cursor.getCount(), startLevel, finishLevel);
    //        } finally {
    //            if (cursor != null) {
    //                cursor.close();
    //    public List<BatteryState> getLast(int limit) {
    //        Cursor cursor = getReadableDatabase().query(TABLE_NAME,
    //                new String[]{Columns.VOLTAGE.name(), Columns.CURRENT.name(), Columns.LEVEL.name()},
    //                null, null, null, null,
    //                "_id DESC", String.valueOf(limit));
    //        ArrayList<BatteryState> states = new ArrayList<>();
    //        if (cursor.moveToLast()) {
    //            do {
    //                states.add(new BatteryState(cursor.getDouble(0),
    //                        cursor.getDouble(1),
    //                        cursor.getInt(2)));
    //            }while (cursor.moveToPrevious());
    //        }
    //        cursor.close();
    //        return states;
    //    }
    //
        public List<BatteryState> getAll() {
            Cursor cursor = getReadableDatabase().query(TABLE_NAME,
                    new String[]{Columns.VOLTAGE.name(), Columns.CURRENT.name(), Columns.LEVEL.name(), Columns.PERIOD.name()},
                    null, null, null, null,
                    null, null);
            ArrayList<BatteryState> states = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    states.add(new BatteryState(cursor.getDouble(0),
                            cursor.getDouble(1),
                            cursor.getInt(2),
                            cursor.getInt(3)));
                }while (cursor.moveToNext());
            }
            cursor.close();
            return states;
        }
    //
    //    public double getAvgVoltage() {
    //        Cursor cursor = null;
    //        try {
    //            cursor = getReadableDatabase()
    //                    .rawQuery("SELECT AVG(" + Columns.VOLTAGE + ") FROM " + TABLE_NAME,
    //                            null);
    //            cursor.moveToFirst();
    //            return cursor.getDouble(0);
    //        } finally {
    //            if (cursor != null) {
    //                cursor.close();
    //            }
    //        }
    //    }
    //            }
}
