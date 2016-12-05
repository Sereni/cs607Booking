package persistence;

import java.sql.*;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;

import java.util.Random;


/**
 * Created by Sereni on 12/4/16.
 * One-off demo class to create database schema and populate it with initial values.
 */
public class DatabaseSetup {
    public static void main(String[] args) {

        // connect to database or create a new one
        String url = "jdbc:sqlite:../development.db";
        try {
            Connection conn = DriverManager.getConnection(url);
            createSchema(conn);
            populateRooms(conn);
            populateRules(conn);
            populateCancellationRules(conn);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void createSchema(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        ArrayList<String> statements = new ArrayList<>();

        // create table bookings
        statements.add(
                "CREATE TABLE IF NOT EXISTS bookings (\n"
                        + "id integer PRIMARY KEY,\n"
                        + "email text NOT NULL,\n"
                        + "checkin text NOT NULL,\n"
                        + "checkout text NOT NULL,\n"
                        + "price integer NOT NULL,\n"
                        + "refund integer NOT NULL DEFAULT 0,\n"
                        + "canceled integer NOT NULL DEFAULT 0"
                        + ");"
        );

        // create room type table
//        statements.add(
//                "CREATE TABLE IF NOT EXISTS room_type (\n"
//                        + "type text PRIMARY KEY,\n"
//                        + "multiplier real NOT NULL\n"
//                        + ");"
//        );

        // create table rooms
        // todo when you get rooms from database, go to room-booking, then to individual bookings to collect unavailable dates
        statements.add(
                "CREATE TABLE IF NOT EXISTS rooms (\n"
                        + "id integer PRIMARY KEY,\n"
                        + "price integer NOT NULL,\n" // fixme this is double in Room class, should be integer?
                        + "type text NOT NULL"
                        + ");"
        );

        // create room-booking junction table
        statements.add(
                "CREATE TABLE IF NOT EXISTS room_booking_junction (\n"
                        + "id integer PRIMARY KEY,\n"
                        + "room_id integer,"
                        + "booking_id integer,"
                        + "FOREIGN KEY (room_id) REFERENCES room (id),\n"
                        + "FOREIGN KEY (booking_id) REFERENCES booking (id)\n"
                        + ");"
        );

        // create calendar rule table
        statements.add(
                "CREATE TABLE IF NOT EXISTS calendar_rules (\n"
                        + "id integer PRIMARY KEY,\n"
                        + "date text NOT NULL,\n"
                        + "rate real NOT NULL DEFAULT 1.0\n"
                        + ");"
        );

        statements.add(
                "CREATE TABLE IF NOT EXISTS cancel_rules (\n"
                        + "id integer PRIMARY KEY,\n"
                        + "lowbound integer NOT NULL,\n"
                        + "upbound integer NOT NULL,\n"
                        + "rate real NOT NULL DEFAULT 1.0\n"
                        + ");"
        );


        for (String sql: statements) {
            stmt.execute(sql);
        }
        stmt.close();
    }

    private static void populateRooms(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO rooms (type, price) VALUES ");
        sb.append("('single', 100),");
        sb.append("('double', 200),");
        sb.append("('studio', 150),");
        sb.append("('suite', 220),");
        sb.deleteCharAt(sb.length()-1);
        sb.append(';');
        stmt.execute(sb.toString());
        stmt.close();
    }

    private static void populateRules(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO calendar_rules (date, rate) VALUES");
        float rate;
        Random rateRandom = new Random();


        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM");
        GregorianCalendar cal = new GregorianCalendar();

        for (int i=1; i<367; i++) {
            cal.set(Calendar.DAY_OF_YEAR, i);
            Date date=cal.getTime();

            // randomize rate for the date
            rate = rateRandom.nextFloat();
            if (rate > 0.7) {
                rate = 1+rate;
            } else rate=1;

            sb.append(String.format("(%s, %f),", formatter.format(date), rate));
        }

        sb.deleteCharAt(sb.length()-1);
        sb.append(';');
        stmt.execute(sb.toString());
        stmt.close();
    }

    private static void populateCancellationRules(Connection c) throws SQLException{
        Statement stmt = c.createStatement();
        String sql = "INSERT INTO cancel_rules (lowbound, upbound, rate) VALUES"
                + "(0, 1, 0.0),"
                + "(2, 7, 0.3),"
                + "(8, 14, 0.5);"
        ;
        stmt.execute(sql);
        stmt.close();
    }

}