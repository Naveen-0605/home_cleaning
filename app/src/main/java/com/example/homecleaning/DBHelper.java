package com.example.homecleaning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.homecleaning.models.Booking;
import com.example.homecleaning.models.Service;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "cleanhome.db";
    private static final int DB_VERSION = 2;

    // Users
    public static final String TABLE_USERS = "users";
    public static final String U_ID = "id";
    public static final String U_NAME = "name";
    public static final String U_EMAIL = "email";
    public static final String U_PASSWORD = "password";

    // Services
    public static final String TABLE_SERVICES = "services";
    public static final String S_ID = "id";
    public static final String S_TITLE = "title";
    public static final String S_DESC = "description";
    public static final String S_PRICE = "price";

    // Bookings
    public static final String TABLE_BOOKINGS = "bookings";
    public static final String B_ID = "id";
    public static final String B_USER_ID = "user_id";
    public static final String B_SERVICE_ID = "service_id";
    public static final String B_ADDRESS = "address";
    public static final String B_DATE = "bdate";   // ✅ correct name
    public static final String B_TIME = "btime";   // ✅ correct name
    public static final String B_STATUS = "status";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" + U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + U_NAME + " TEXT, " + U_EMAIL + " TEXT UNIQUE, " + U_PASSWORD + " TEXT)";

        String createServices = "CREATE TABLE " + TABLE_SERVICES + " (" + S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + S_TITLE + " TEXT, " + S_DESC + " TEXT, " + S_PRICE + " TEXT)";

        String createBookings = "CREATE TABLE " + TABLE_BOOKINGS + " (" + B_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + B_USER_ID + " INTEGER, " + B_SERVICE_ID + " INTEGER, " + B_ADDRESS + " TEXT, "
                + B_DATE + " TEXT, " + B_TIME + " TEXT, " + B_STATUS + " TEXT)";

        db.execSQL(createUsers);
        db.execSQL(createServices);
        db.execSQL(createBookings);

        ContentValues cv = new ContentValues();
        cv.put(S_TITLE, "Basic Home Cleaning");
        cv.put(S_DESC, "Dusting, sweeping, mopping of floors, basic surface cleaning.");
        cv.put(S_PRICE, "499");
        db.insert(TABLE_SERVICES, null, cv);

        cv = new ContentValues();
        cv.put(S_TITLE, "Deep Cleaning");
        cv.put(S_DESC, "Deep cleaning of kitchen, bathrooms, corners, steam cleaning where needed.");
        cv.put(S_PRICE, "1299");
        db.insert(TABLE_SERVICES, null, cv);

        cv = new ContentValues();
        cv.put(S_TITLE, "Sofa & Carpet Cleaning");
        cv.put(S_DESC, "Upholstery and carpet steam cleaning to remove stains and dust.");
        cv.put(S_PRICE, "899");
        db.insert(TABLE_SERVICES, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }

    // User methods
    public boolean addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(U_NAME, name);
        cv.put(U_EMAIL, email);
        cv.put(U_PASSWORD, password);
        long res = db.insert(TABLE_USERS, null, cv);
        db.close();
        return res != -1;
    }
    public String getUserNameById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + U_NAME + " FROM " + TABLE_USERS + " WHERE " + U_ID + "=?",
                new String[]{String.valueOf(userId)});
        String name = "";
        if (c != null && c.moveToFirst()) {
            name = c.getString(0);
        }
        if (c != null) c.close();
        return name;
    }

    public int checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + U_ID + " FROM " + TABLE_USERS + " WHERE " + U_EMAIL + "=? AND " + U_PASSWORD + "=?",
                new String[]{email, password});
        int id = -1;
        if (c != null && c.moveToFirst()) {
            id = c.getInt(0);
        }
        if (c != null) c.close();
        return id;
    }

    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + U_ID + " FROM " + TABLE_USERS + " WHERE " + U_EMAIL + "=?",
                new String[]{email});
        int id = -1;
        if (c != null && c.moveToFirst()) {
            id = c.getInt(0);
        }
        if (c != null) c.close();
        return id;
    }

    // Services methods
    public List<Service> getAllServices() {
        List<Service> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_SERVICES, null);
        if (c.moveToFirst()) {
            do {
                Service s = new Service();
                s.setId(c.getInt(c.getColumnIndexOrThrow(S_ID)));
                s.setTitle(c.getString(c.getColumnIndexOrThrow(S_TITLE)));
                s.setDescription(c.getString(c.getColumnIndexOrThrow(S_DESC)));
                s.setPrice(c.getString(c.getColumnIndexOrThrow(S_PRICE)));
                list.add(s);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public Service getServiceById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_SERVICES + " WHERE " + S_ID + "=?", new String[]{String.valueOf(id)});
        if (c != null && c.moveToFirst()) {
            Service s = new Service();
            s.setId(c.getInt(c.getColumnIndexOrThrow(S_ID)));
            s.setTitle(c.getString(c.getColumnIndexOrThrow(S_TITLE)));
            s.setDescription(c.getString(c.getColumnIndexOrThrow(S_DESC)));
            s.setPrice(c.getString(c.getColumnIndexOrThrow(S_PRICE)));
            c.close();
            return s;
        }
        if (c != null) c.close();
        return null;
    }

    // ✅ Bookings methods (fixed)
    public boolean addBooking(Booking b) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(B_USER_ID, b.getUserId());
        cv.put(B_SERVICE_ID, b.getServiceId());
        cv.put(B_ADDRESS, b.getAddress());
        cv.put(B_DATE, b.getDate());   // ✅ maps to "bdate"
        cv.put(B_TIME, b.getTime());   // ✅ maps to "btime"
        cv.put(B_STATUS, b.getStatus());
        long res = db.insert(TABLE_BOOKINGS, null, cv);
        Log.d("DBHelper", "Adding booking for user: " + b.getUserId() + " service: " + b.getServiceId());
        db.close();
        return res != -1;
    }

    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT b." + B_ID + ", b." + B_SERVICE_ID + ", b." + B_ADDRESS + ", b." + B_DATE + ", b." + B_TIME + ", b." + B_STATUS + ", s." + S_TITLE +
                        " FROM " + TABLE_BOOKINGS + " b LEFT JOIN " + TABLE_SERVICES + " s ON b." + B_SERVICE_ID + "=s." + S_ID +
                        " WHERE b." + B_USER_ID + "=? ORDER BY b." + B_ID + " DESC",
                new String[]{String.valueOf(userId)});

        if (c.moveToFirst()) {
            do {
                Booking bk = new Booking();
                bk.setId(c.getInt(0));
                bk.setServiceId(c.getInt(1));
                bk.setAddress(c.getString(2));
                bk.setDate(c.getString(3));
                bk.setTime(c.getString(4));
                bk.setStatus(c.getString(5));
                bk.setServiceTitle(c.getString(6));
                list.add(bk);
            } while (c.moveToNext());
        }
        Log.d("DBHelper", "Querying bookings for user: " + userId);
        c.close();
        return list;
    }
}
