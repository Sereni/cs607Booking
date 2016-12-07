package persistence;
import core.*;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Sereni on 12/3/16.
 */
class RecordNotFoundException extends Exception{}


public class DatabaseHandler {

    private String databaseName = "jdbc:sqlite:development.db";
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM");

    /**
     * Creates a database entry from a BookingEvent object. Returns booking number.
     */
    public int makeBooking(BookingEvent booking) throws SQLException {

        String sql = "insert into bookings (email, checkin, checkout, price)" +
                     String.format("values (%s, %s, %s, %d);", booking.userEmail, formatter.format(booking.checkIn),
                             formatter.format(booking.checkOut), booking.payment);
        update(sql);
        ResultSet lastId = select("SELECT last_insert_rowid()");
        lastId.next();
        int bookingNumber = lastId.getInt(1);

        for (Room r: booking.rooms) {
            sql = String.format("insert into room_booking_junction (room_id, booking_id) values (%d, %d)",
                    r.getId(), bookingNumber);
            update(sql);
        }
        return bookingNumber;
    }

    public BookingEvent getBooking(int number) throws SQLException, RecordNotFoundException, ParseException {
        String sql = String.format("select * from bookings where id=%d;", number);
        ResultSet results = select(sql);
        if (!results.next()) {
            throw new RecordNotFoundException();
        }
        int bookingId = results.getInt("id");
        String email = results.getString("email");
        Date checkin = (Date)formatter.parse(results.getString("checkin"));
        Date checkout = (Date)formatter.parse(results.getString("checkout"));
        int price = results.getInt("price");
        ArrayList<Room> rooms = new ArrayList<>();

        ResultSet roomData = select(String.format("select room_id from room_booking_junction where booking_id=%d", bookingId));
        while (roomData.next()) {
            rooms.add(getRoom(roomData.getInt("id")));
        }
        BookingEvent booking = new BookingEvent(bookingId, checkin, checkout, email, rooms, price);
        return booking;
    }

    public void cancelBooking(CancelEvent cancel) throws SQLException {
        String sql = String.format("update bookings set canceled=1 refund=%d where id=%d;", cancel.getRefund(), cancel.id);
        update(sql);
    }


    public ArrayList<Room> getRooms(String type) throws SQLException {
        String sql = String.format("select * from rooms where type=%s", type);
        ResultSet roomData = select(sql);
        ArrayList<Room> rooms = new ArrayList<>();
        Set<java.util.Date> booked;
        int roomId;
        String roomType;
        int price;

        while (roomData.next()) {
            roomId = roomData.getInt("id");
            roomType = roomData.getString("type");
            price = roomData.getInt("price");
            booked = getBookedDates(roomId);
            rooms.add(new Room(roomId, roomType, price, booked));
        }
        return rooms;
    }

    // todo we'll need some kind of cleanup, when the booking date passes, rooms should free up

    /**
     * Look up the room by its number.
     * @param id int: room number.
     * @return Room with a matching id.
     * @throws SQLException
     * @throws RecordNotFoundException if room with this number does not exist.
     */
    public Room getRoom(int id) throws SQLException, RecordNotFoundException {
        String sql = String.format("select * from rooms where id=%d", id);
        ResultSet roomData = select(sql);
        if (!roomData.next()) {
            throw new RecordNotFoundException();
        }
        int roomId = roomData.getInt("id");
        return new Room(roomId,
                roomData.getString("type"),
                roomData.getInt("price"),
                getBookedDates(roomId));
    }


    public HashSet<java.util.Date> getBookedDates(int roomId) throws SQLException {
        String sql = String.format("select (bookings.checkin, bookings.checkout, bookings.canceled) "
                + "from room_booking_junction inner join bookings on bookings.id=room_booking_junction.booking_id "
                + "where room_id=%d;", roomId);
        ResultSet dateRecords = select(sql);
        HashSet<java.util.Date> dates = new HashSet<>();
        while (dateRecords.next()) {
            if (!dateRecords.getBoolean("canceled")) {
                try {
                    java.util.Date checkin = formatter.parse(dateRecords.getString("checkin"));
                    java.util.Date checkout = formatter.parse(dateRecords.getString("checkout"));
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(checkin);
                    while (cal.getTime().before(checkout)) {
                        dates.add(cal.getTime());
                        cal.add(Calendar.DATE, 1);
                    }
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return dates;
    }


    public ArrayList<BookingPricingRule> getCalendarRules() throws SQLException, ParseException {
        String sql = "select * from calendar_rules";
        Float rate;
        HashSet<java.util.Date> dates;
        ResultSet ruleData = select(sql);
        HashMap<Float, HashSet<java.util.Date>> tempRules = new HashMap<>();
        while (ruleData.next()) {
            rate = ruleData.getFloat("rate");
            dates = tempRules.get(rate);
            if (dates == null) dates = new HashSet<>();
            dates.add(formatter.parse(ruleData.getString("date")));
            tempRules.put(rate, dates);
        }
        ArrayList<BookingPricingRule> rules = new ArrayList<>();
        for (Map.Entry<Float, HashSet<java.util.Date>> e: tempRules.entrySet()) {
            rules.add(new BookingPricingRule(e.getKey(), new ArrayList<>(e.getValue())));
        }
        return rules;
    }

    public void setDayRate(Date date, float rate){}


    /**
     * Establishes connection to the database and performs update with given SQL command.
     */
    private void update(String sql) throws SQLException {
        Connection connection;
        Statement statement;

        connection = DriverManager.getConnection(databaseName);
        connection.setAutoCommit(false);
        statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
        connection.commit();
        connection.close();

    }

    /**
     * Establishes connection to the database and performs select with given SQL command.
     * @param sql: command to execute.
     * @return ResultSet of rows matching the query.
     * @throws SQLException if any of the underlying operations fail.
     */
    private ResultSet select(String sql) throws SQLException {
        Connection connection;
        Statement statement;

        connection = DriverManager.getConnection(databaseName);
        connection.setAutoCommit(false);
        statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
        statement.close();
        connection.close();
        return result;
    }
}
