package persistence;
import core.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

/**
 * Created by Sereni on 12/3/16.
 */
@SuppressWarnings("serial")
class RecordNotFoundException extends Exception{}


public class DatabaseHandler {

	private static String databaseName = "jdbc:sqlite:development.db";
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	/**
	 * Creates a database entry from a BookingEvent object. Returns booking number.
	 * @throws Exception 
	 */
	public int makeBooking(BookingEvent booking) {
		
		Connection connection = null;
		Statement statement = null;
		try {
			connection = DriverManager.getConnection(databaseName);
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			
			String sql = "insert into bookings (email, checkin, checkout, price) " +
					String.format("values ('%s', '%s', '%s', %d);", booking.userEmail, formatter.format(booking.checkIn),
							formatter.format(booking.checkOut), booking.payment);
			statement.executeUpdate(sql);
		      System.out.println("Inserted records into the table..."+sql);
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
//			try {
//				connection.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
		}
		
		ResultSet lastId = null;
		int bookingNumber = 0;
		Statement statement2 = null;
		try {
//			connection = DriverManager.getConnection(databaseName);
//			connection.setAutoCommit(false);
			statement2 = connection.createStatement();
			String sql = "SELECT last_insert_rowid() FROM bookings";
			lastId = statement2.executeQuery(sql);
			
			lastId.next();
			bookingNumber = lastId.getInt(1);
		      System.out.println("last id..."+sql+"    "+bookingNumber);

		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
			try {
				lastId.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				statement2.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				connection.commit();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
//
//		Statement statement3 = null;
//		for (Room r: booking.rooms) {
//			try {
//				connection = DriverManager.getConnection(databaseName);
//				connection.setAutoCommit(false);
//				statement3 = connection.createStatement();
//				
//				String sql = String.format("insert into room_booking_junction (room_id, booking_id) values (%d, %d)",
//						r.getId(), bookingNumber);
//				statement3.executeUpdate(sql);
//			} catch (Exception e) {
//			    e.printStackTrace();
//			} finally {
//				try {
//					statement.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//				try {
//					connection.commit();
//					connection.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		return bookingNumber;
	}

	public CancelEvent getCancelingBooking(int number) throws Exception {
		Connection connection = null;
		Statement statement = null;
		ResultSet results = null;
		
		int bookingId = 0;
		String email = null;
		Date checkin = null;
		Date checkout = null;
		int price = 0;
		
		try {
			connection = DriverManager.getConnection(databaseName);
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			
			String sql = String.format("select * from bookings where id=%d;", number);
			results = statement.executeQuery(sql);
			if (!results.next()) {
				throw new RecordNotFoundException();
			}
			
			bookingId = results.getInt("id");
			email = results.getString("email");
			checkin = (Date)formatter.parse(results.getString("checkin"));
			checkout = (Date)formatter.parse(results.getString("checkout"));
			price = results.getInt("price");
			
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
			try {
				results.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		ArrayList<Room> rooms = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(databaseName);
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			
			String sql = String.format("select room_id from room_booking_junction where booking_id=%d", bookingId);
			results = statement.executeQuery(sql);
			
			while (results.next()) {
				rooms.add(getRoom(results.getInt("id")));
			}
			
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
			try {
				results.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		CancelEvent cancel = new CancelEvent(bookingId, checkin, checkout, email, rooms, price, 0);
		
		return cancel;
	}

	public void cancelBooking(CancelEvent cancel) throws SQLException {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = DriverManager.getConnection(databaseName);
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			String sql = String.format("update bookings set canceled=1 and refund=%d where id=%d;", cancel.getRefund(), cancel.id);
			statement.executeUpdate(sql);
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	public ArrayList<Room> getRooms(String type) {
		Connection connection = null;
		Statement statement = null;
		ResultSet roomData = null;
		ArrayList<Room> rooms = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(databaseName);
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			String sql = String.format("select * from rooms where type='%s'", type);

			roomData = statement.executeQuery(sql); 
			
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
		} catch (Exception e) {
		    System.out.println("ERROR CREATING TABLE ARRAY");
		    e.printStackTrace();
		    return null;
		} finally {
			try {
				roomData.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return rooms;
	}

	// todo we'll need some kind of cleanup, when the booking date passes, rooms should free up

	/**
	 * Look up the room by its number.
	 * @param id int: room number.
	 * @return Room with a matching id.
	 * @throws Exception 
	 */
	public Room getRoom(int id) throws Exception {
		Connection connection = null;
		Statement statement = null;
		ResultSet roomData = null;
		Room room = null;
		try {
			connection = DriverManager.getConnection(databaseName);
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			String sql = String.format("select * from rooms where id=%d", id);
			roomData = statement.executeQuery(sql);
			if (!roomData.next()) {
				throw new RecordNotFoundException();
			}
			
			room = new Room(id,
					roomData.getString("type"),
					roomData.getInt("price"),
					new HashSet<java.util.Date>());
			
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
			try {
				roomData.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return room;
	}


	public HashSet<java.util.Date> getBookedDates(int roomId) throws Exception {
		Connection connection = null;
		Statement statement = null;
		ResultSet dateRecords = null;
		HashSet<java.util.Date> dates = new HashSet<>();
		try {
			connection = DriverManager.getConnection(databaseName);
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			String sql = String.format("select * "
					+ "from room_booking_junction inner join bookings on bookings.id=room_booking_junction.booking_id "
					+ "where room_id=%d;", roomId);
			dateRecords = statement.executeQuery(sql);
			dates = new HashSet<>();
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
	
			
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
			try {
				dateRecords.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return dates;
	}


	public ArrayList<BookingPricingRule> getCalendarRules() throws Exception {
		Connection connection = null;
		Statement statement = null;
		ResultSet ruleData = null;

		Float rate;
		HashSet<java.util.Date> dates;
		HashMap<Float, HashSet<java.util.Date>> tempRules = new HashMap<>();
		ArrayList<BookingPricingRule> rules = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(databaseName);
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			String sql = "select * from calendar_rules";
			ruleData = statement.executeQuery(sql);
			dates = new HashSet<>();
			while (ruleData.next()) {
				rate = ruleData.getFloat("rate");
				dates = tempRules.get(rate);
				if (dates == null) dates = new HashSet<>();
				dates.add(formatter.parse(ruleData.getString("date")));
				tempRules.put(rate, dates);
			}
			for (Map.Entry<Float, HashSet<java.util.Date>> e: tempRules.entrySet()) {
				rules.add(new BookingPricingRule(e.getKey(), new ArrayList<>(e.getValue())));
			}
	
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
			try {
				ruleData.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rules;
	}

	public void setDayRate(Date date, float rate){}

}
