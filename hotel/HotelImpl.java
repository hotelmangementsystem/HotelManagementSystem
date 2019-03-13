// This package means we are working in the same file called hotel
package hotel;

// These are all the imports necessary for a few functions
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import java.text.*;

/**
 * The main class HotelImpl that implements the Hotel.java interface.
 *
 * @author Joshua du Parc Braham and Lucas Martin Calderon M.
 * @version 26/02/2019
 *
 */

// The public class HotelImpl has a <contract> to follow, that is, it has to declare and initialize all Hotel"s methods.
public class HotelImpl implements Hotel {

    public static ArrayList<Room> roomList;
    public static ArrayList<Booking> bookingList;
    public static ArrayList<Guest> guestList;
    public static ArrayList<VIPGuest> vipGuestList;
    public static ArrayList<Payment> paymentList;
    public static final SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Imports all data from the rooms, guests, boookings and payments text files.
     *
     * @param roomsTxtFileName   the rooms txt file
     * @param guestsTxtFileName     the guests txt file
     * @param bookingsTxtFileName        the bookings txt file
     * @param paymentsTxtFileName     the payments txt file
     * @return             void (This means it returns nothing)
     */

    // This is the constructor for the HotelImpl class. It will be set on public, so everyone has access to it

    public HotelImpl(String roomsTxtFileName, String guestsTxtFileName,
                           String bookingsTxtFileName, String paymentsTxtFileName){
        importAllData(roomsTxtFileName, guestsTxtFileName, bookingsTxtFileName, paymentsTxtFileName); // This is where the magic happens
    }

    /**
     * Removes a room according to the toom number
     *
     * @param roomNumber   long Integer representing the room number
     * @return             boolean-type parameter representing true if the room was successfully removed or false if it was not
     */

    public boolean removeRoom(long roomNumber) {
        for (Booking book : bookingList ) {
            Date d1 = new Date();
            if (book.getRoomNumber() == roomNumber && d1.after(book.getCheckOutDate())) { // The if condition makes sure that the checkout date is before the current day
                bookingList.remove(book);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a guest, either a VIP guest in the VIP guest list or a regular guest in the hotel
     *
     * @param fName   String representing the first name of the guest
     * @param lName     String representing the last name of the guest
     * @param vipState        boolean-type parameter to indicate if the guest is VIP or not
     * @return             true if the guest was successfully added or false if it was not
     */

    public boolean addGuest(String fName, String lName, boolean vipState) {
        
        // We make sure that the addGuest method has all necessary parameters and the arguements are passed accordingly

        assert fName != null && lName != null
                && (Boolean)vipState instanceof Boolean: "Please enter the right input for this method";

        boolean unique = true;
        long guestID;
        try{
            if(vipState==true){
                while(true){
                    guestID = new Random().nextLong() & 0xffffffffL;
                    for(VIPGuest guest: vipGuestList){
                        if(guest.getGuestID() == guestID){unique = false;}
                    }
                    if(unique == true){break;}
                }

                // The next 5 lines work out when the following year from the current day will be
                Calendar end_Date = Calendar.getInstance();
                end_Date.setTime(new Date());
                end_Date.add(Calendar.YEAR,1);
                Date date_expire = new Date();
                date_expire.setTime(end_Date.getTimeInMillis());
                VIPGuest vipGuest = new VIPGuest(guestID, fName, lName, new Date(), new Date(), date_expire);
                Payment payment = new Payment(new Date(), vipGuest.getGuestID(), 50.00, "VIPmembership"); // And here we initilize the object using the defined constructor

                paymentList.add(payment);
                vipGuestList.add(vipGuest);
            }else{ // Else if the guest is not VIP
                while(true){
                    guestID = new Random().nextLong() & 0xffffffffL;
                    for(Guest guest: guestList){
                        if(guest.getGuestID() == guestID){unique = false;}
                    }
                    if(unique == true){break;}
                }
                Guest guest = new Guest(guestID, fName, lName, new Date());
                guestList.add(guest);

            }
        }catch(Exception e){
            System.out.print("An Error Has Occured adding a Guest... ");
            System.out.print(e + "\n");
            return false;
        }
        return true;
       }

    /**
     * Removes a guest from the hotel, regardless if they are VIP or not. We first have to make sure that
     * the said guest does not have any future bookings at the time of the removal. If so, it will not be deleted
     * from the list and the function will return false as clarified below:
     *
     * @param guestID   Long integer representing the guest ID number
     * @return          Boolean type parameter that is true if the guest was successfully removed or false if it was not
     */

    public boolean removeGuest(long guestID) {

        // We assert that the guestID has been provided
        assert (Long)guestID instanceof Long : "Please enter the guestID correctly";

        try{
            for(Booking book: bookingList){
                if(book.getGuestID() == guestID && new Date().after(book.getCheckOutDate())){ // Checks the guest ID and then the corresponding date of future bookings
                    for(Guest guest : guestList){
                        if(guest.getGuestID() == guestID){
                            guestList.remove(guest); // This is where the regular guest is removed from the list.
                        }
                    }
                    for(Guest guest : vipGuestList) {
                        if (guest.getGuestID() == guestID) {
                            vipGuestList.remove(guest); // This is where the VIP guest is removed from the list.
                        }
                    }
                    return true;
                }
            }return false;

        }catch(Exception e){ //If there is an error, we print it out to notify our client that an error has occurred
            System.out.print("An Error Has Occured... ");
            System.out.print(e + "\n");
            return false;
        }

        /**
         * Adds a room in the hotel with certain characteristics
         *
         * @param roomNumber   Long integer representing the romm ID number
         * @param roomType     String representing the room type
         * @param roomPrice    Double representing the room price
         * @param capacity     Integer representing the capacity
         * @param facilities   String representing the facilities needed
         * @return             boolean type parameter that is true if the room was successfully added or false if it was not
         */

    }

    public boolean addRoom(long roomNumber, String roomType, double roomPrice, int capacity, String facilities) {
        
        //We assert that all the parameters have their corresponding arguments.

        assert (Long)roomNumber instanceof Long && roomType != null && (Double)roomPrice instanceof Double && (Integer)capacity instanceof Integer
                && facilities != null : "Please provide the correct information for the addRoom method";

        for(Room room : roomList) {
            if(room.getRoomNumber() == roomNumber){return false;}
        }
        Room room = new Room(roomNumber, roomType, roomPrice, capacity, facilities); // Adds a rooms using the constructor of Room defined in the Room class.
        roomList.add(room);
        return true;
    }

    /**
     * This method checks if the room is available or not, and it returns a boolean type value corresponding to the success of the operation
     *
     * @param roomNumber   Long integer representing the romm ID number
     * @param checkInDate  Date object representing the date when the guest checks in
     * @param checkOutDate Date object representing the date when the guest checks out
     * @return             boolean type parameter that is true if the room was successfully checked available or false if it was not
     */

    public boolean checkRoomAvailable(long roomNumber, Date checkInDate, Date checkOutDate){
        for (Booking book : bookingList){
            // The following line checks if room ID number and if the checkin date is before the guest"s check-out date and the booking info is correct in time
            if(book.getRoomNumber() == roomNumber && checkInDate.before(book.getCheckOutDate()) && checkInDate.after(book.getCheckInDate())){
                return false;
            }
        }
        return true;
    }

    /**
     * This method returns an array of available rooms
     *
     * @param roomNumber   Long integer representing the romm ID number
     * @param checkInDate  Date object representing the date when the guest checks in
     * @param checkOutDate Date object representing the date when the guest checks out
     * @return             This method returns an array of available rooms
     */

    public ArrayList<Long> findAvailableRooms(String roomType, Date checkInDate, Date checkOutDate){
        ArrayList<Long> availableRooms = new ArrayList<Long>();
        for(Room room: roomList){
            if(room.getRoomType() == roomType && checkRoomAvailable(room.getRoomNumber(), checkInDate, checkOutDate)){ //Checks the room type as well as the availability between the check in dates and check out dates
                availableRooms.add(room.getRoomNumber());
            }
        }
        if (availableRooms.size() == 0) {
            System.out.println("There are no available rooms!");
            return null;
        } else {
            return availableRooms; // returns an array with all the room numbers that are avilable,
        }
    }

    /**
     * This method makes a booking
     *
     * @param roomType     String representing the romm type
     * @param guestID      Long integer representing the ID of the guest
     * @param checkOutDate Date object representing the date when the guest checks in
     * @param checkOutDate Date object representing the date when the guest checks out
     * @return             This method returns a boolean-type true if the booking was successful or false if not
     */

    public boolean makeBooking(String roomType, long guestID, Date checkInDate, Date checkOutDate) {
        
        // We make sure that the makeBooking method has all necessary parameters and the arguements are passed accordingly

        assert roomType != null && (Long)guestID instanceof Long && (Date)checkInDate instanceof Date && (Date)checkOutDate instanceof Date : "Please enter the right input for this method";

        boolean guestExists = false;
        for(Guest guest: guestList){
            if(guest.getGuestID() == guestID){guestExists = true;}} // This checks if the user exists
        if(guestExists == false){return false;}


        if(new Date().after(checkInDate)){return false;} // This checks if the check in date is before today

        long roomNumber;
        ArrayList<Long> availableRooms = findAvailableRooms(roomType, checkInDate, checkOutDate);
        try{ // With a try and catch we can catch an error if there is any
            roomNumber = availableRooms.get(new Random().nextInt(availableRooms.size()));
        }catch(Exception e){
            System.out.println("Error occured while making a booking");
            return false;
        }

        boolean unique = true;
        long bookingID;
        Room bookedRoom = null;
        while(true){
            bookingID = new Random().nextLong() & 0xffffffffL;
            for(Booking book: bookingList){
                if(book.getBookingID() == bookingID){unique = false;}
            }
            if(unique == true){break;}
        }

        for(Room room: roomList){
            if(room.getRoomNumber() == roomNumber){bookedRoom = room;}
        }

        long diff = checkOutDate.getTime() - checkInDate.getTime();
        long daysDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        double totalAmount = daysDiff*bookedRoom.getRoomPrice();

        for(VIPGuest guest: vipGuestList){
            if(guest.getGuestID() == guestID && checkOutDate.before(guest.getVIPExpiryDate())){
                totalAmount = totalAmount * 0.9;
            }
        }
        Booking booking = new Booking(bookingID, guestID, roomNumber, new Date(), checkInDate, checkOutDate, totalAmount); // We initialize it using a constructor from the class method at the bottom
        Payment payment = new Payment(new Date(), guestID, totalAmount, "booking");

        paymentList.add(payment);
        bookingList.add(booking);
        return true; // Returns a boolean

    }

    /**
     * This method imports all data
     *
     * @param roomsTxtFileName   the rooms txt file
     * @param guestsTxtFileName     the guests txt file
     * @param bookingsTxtFileName        the bookings txt file
     * @param paymentsTxtFileName     the payments txt file
     * @return             This method returns a boolean-type true if the data import process was successful, false if not
     */

    public boolean importAllData(String roomsTxtFileName, String guestsTxtFileName, String bookingsTxtFileName, String paymentsTxtFileName){
        try{
            importRoomsData(roomsTxtFileName);
            importGuestsData(guestsTxtFileName);
            importBookingsData(bookingsTxtFileName);
            importPaymentsData(paymentsTxtFileName);
            return true; // Boolean return type
        }catch(Exception e){
            System.out.println("ERROR: an issue occured importing data");
            System.out.println(e); // Here is when we print the error
            return false;
        }
    }

    /**
     * This method imports all room data
     *
     * @param roomsTxtFileName   The rooms txt file
     * @return                   This method returns a boolean-type true if the data import process was successful, false if not
     */

    public boolean importRoomsData(String roomsTxtFileName) {
        
        // We make sure that the makeBooking method has all necessary parameters and the arguements are passed accordingly

        assert roomsTxtFileName != null : "Please enter the right input for this method";

        try
        {
            File file = new File(roomsTxtFileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            roomList = new ArrayList<Room>();

            while ((st = br.readLine()) != null)
            {
                String[] room_info = st.split(",");
                Room room = new Room(Long.valueOf(room_info[0]), room_info[1], Double.valueOf(room_info[2]), Integer.valueOf(room_info[3]), room_info[4]); // We initialize the object using its own-defined constructor
                roomList.add(room);
            }
            br.close();    
            return true;
        }
        catch(Exception e) // We can the error here
        {
            System.out.println("Error Occured when reading rooms data...");
            return false;
        }
    }

    /**
     * This method imports all guests data
     *
     * @param guestsTxtFileName     the guests txt file
     * @return             This method returns a boolean-type true if the guest data import process was successful, false if not
     */

    public boolean importGuestsData(String guestsTxtFileName){
        try{
            File file = new File(guestsTxtFileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            guestList = new ArrayList<Guest>();
            vipGuestList = new ArrayList<VIPGuest>();
            while ((st = br.readLine()) != null) {
                String[] guest_info = st.split(",");
                if(guest_info.length > 4){
                    VIPGuest vipGuest = new VIPGuest(Long.valueOf(guest_info[0]), guest_info[1], guest_info[2], ft.parse(guest_info[3]), ft.parse(guest_info[4]), ft.parse(guest_info[5])); // We use its own-defined constructor
                    vipGuestList.add(vipGuest);
                }else{
                    Guest guest = new Guest(Long.valueOf(guest_info[0]), guest_info[1], guest_info[2], ft.parse(guest_info[3]));
                    guestList.add(guest);
                }

            }
            br.close();
            return true;
        }catch(Exception e){ // We catch an error if there is ever an error
            System.out.println("Error Occured when reading Guests data...");
            return false;
        }
    }

    /**
     * This method imports all booking data
     *
     * @param bookingsTxtFileName         The bookings txt file
     * @return                            This method returns a boolean-type true if the booking data import process was successful, false if not
     */

    public boolean importBookingsData(String bookingsTxtFileName){
        try{
            File file = new File(bookingsTxtFileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            bookingList = new ArrayList<Booking>();
            while((st = br.readLine()) != null){
                String[] booking_info = st.split(",");
                Booking booking = new Booking(Long.valueOf(booking_info[0]), Long.valueOf(booking_info[1]),
                    Long.valueOf(booking_info[2]),ft.parse(booking_info[3]),
                    ft.parse(booking_info[4]), ft.parse(booking_info[5]),
                    Double.valueOf(booking_info[6])); // This initialized the object booking calling the constructor in the class Booking
                bookingList.add(booking);
            }
            br.close(); // fclose() is  a file method that closes the previously opened and created file
            return true;
        }
        catch(Exception e){ // This catches an error, if any
            System.out.println("Error Occured when reading booking data...");
            return false;
        }
    }

    /**
     * This method imports all data
     *
     * @param paymentsTxtFileName     the payments txt file
     * @return             This method returns a boolean-type true if the payment data import process was successful, false if not
     */

    public boolean importPaymentsData(String paymentsTxtFileName){
        try{
            File file = new File(paymentsTxtFileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
            paymentList = new ArrayList<Payment>();
            while ((st = br.readLine()) != null){
                String[] payment_info = st.split(",");
                Payment payment = new Payment(ft.parse(payment_info[0]), Long.valueOf(payment_info[1]),
                    Double.valueOf(payment_info[2]), payment_info[3]); // This initialized the object payment using its constructor
                paymentList.add(payment);
            }
            br.close(); // This closes the file if there are no errors
            return true;
        }catch(Exception e){ // Catches an error if any
            System.out.println("Error Occured when reading payment data...");
            return false;
        }
    }

    /**
     * This method checks out
     *
     * @param bookingID This is a long integer representing the booking ID number
     * @return             This method returns a boolean-type true if check out was successful, false if not
     */

    public boolean checkOut(long bookingID){
        Booking booking = null;
        try{
            for(Booking book: bookingList){
                if(book.getBookingID()==bookingID){booking=book;}
            }

            if(new Date().after(booking.getCheckOutDate())|| new Date().before(booking.getCheckInDate())){return false;} //This makes sure the checkinDate if after the current date

            bookingList.remove(booking); // Removes the element from the bookings list
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method seaches guests and returns a string of the long integers of the values of their IDs
     *
     * @param firstName    String representing the first name of the guest
     * @param lastName     String representing the second name of the guestthe guests txt file
     * @return             This method seaches guests and returns a string of the long integers of the values of their IDs
     */

    public ArrayList<Long> searchGuest(String firstName, String lastName) {
        ArrayList<Long> result = new ArrayList<Long>();
        for(Guest guest : guestList) {
            if (guest.getfName().toLowerCase().equals(firstName.toLowerCase()) && guest.getlName().toLowerCase().equals(lastName.toLowerCase())) {
                result.add(guest.getGuestID());
            }
        }
        for(VIPGuest guest : vipGuestList) {
            if (guest.getfName().toLowerCase().equals(firstName.toLowerCase()) && guest.getlName().toLowerCase().equals(lastName.toLowerCase())) {
                result.add(guest.getGuestID());
            }
        }
        return result; // This is an array of long integers
    }

    /**
     * This method seaches guests by ID and returns the object with representing that guest
     *
     * @param guestID Long integer representing the guest"s ID number
     * @return             This method seaches guests and returns an object of the guest.
     */

    public Guest searchGuestByID(long guestID) {
        for(Guest guest : guestList) {
            if(guest.getGuestID() == guestID) {
                return guest;
            }
        }
        for(VIPGuest guest : vipGuestList) { // It also searches as exprected through the vipGuestList
            if(guest.getGuestID() == guestID) {
                return guest;
            }
        }

        return null;
    }

    /**
     * This method saves the data of a room
     *
     * @param roomsTxtFileName    String representing the rooms txt file
     * @return                    This returns a boolean value if the room was saved
     */

    public boolean saveRoomsData(String roomsTxtFileName) {
        File fnew = new File(roomsTxtFileName);
        try {
            FileWriter roomsFile = new FileWriter(fnew, false);
            PrintWriter roomsWriter = new PrintWriter(roomsFile);
            for(Room room: roomList){roomsWriter.println(room.getRoomNumber()+","+room.getRoomType()+","+room.getRoomPrice()+","+room.getCapacity()+","+room.getFacilities());}
            roomsFile.close();
            return true;
        }catch (IOException e) { // This catches an exception and stacks it.
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method saves the data of the guests
     *
     * @param roomsTxtFileName    String representing the rooms txt file
     * @return                    This returns a boolean value if the guest data was saved
     */

    public boolean saveGuestsData(String guestsTxtFileName) {
        File fnew = new File(guestsTxtFileName);
        try {
            FileWriter guestsFile = new FileWriter(fnew, false);
            PrintWriter guestsWriter = new PrintWriter(guestsFile);
            for(Guest guest: guestList){guestsWriter.println(guest.getGuestID() +","+guest.getfName()+","+guest.getlName()+","+guest.getDateJoin());} // This is how the file is modified to the new source data
            for(VIPGuest guest: vipGuestList){guestsWriter.println(guest.getGuestID() +","+guest.getfName()+","+guest.getlName()+","+guest.getDateJoin()+","+guest.getVIPStartDate()+","+guest.getVIPExpiryDate());}
            guestsFile.close(); // This is how we close the file that we opened before
            return true;
        }
        catch (IOException e) { // This catches an expection, if any
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method saves the data of the booking
     *
     * @param bookingsTxtFileName    String representing the bookings txt file
     * @return                    This returns a boolean value if the booking data was saved
     */

    public boolean saveBookingsData(String bookingsTxtFileName) {
        File fnew = new File(bookingsTxtFileName);
        try {
            FileWriter bookingsFile = new FileWriter(fnew, false);
            PrintWriter bookingsWriter = new PrintWriter(bookingsFile);
            for(Booking booking: bookingList){bookingsWriter.println(booking.getBookingID()+","+booking.getGuestID()+","+booking.getRoomNumber()+","+booking.getBookingDate()+","+booking.getCheckInDate()+","+booking.getCheckOutDate()+","+booking.getTotalAmount());} // This opens and then closes in the next line the file that we created
            bookingsFile.close();
            return true;
        }
        catch (IOException e) { // This catches an error, if any
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method saves the data of the payments
     *
     * @param paymentsTxtFileName    String representing the payments txt file
     * @return                       This returns a boolean value if the payment data was saved
     */

    public boolean savePaymentsData(String paymentsTxtFileName) {
        File fnew = new File(paymentsTxtFileName);
        try {
            FileWriter paymentsFile = new FileWriter(fnew, false); // These next 3 lines represent how we create, open, modify and close a file with new data source
            PrintWriter paymentsWriter = new PrintWriter(paymentsFile);
            for(Payment payment: paymentList){paymentsWriter.println(payment.getDate()+","+payment.getGuestID()+","+payment.getAmount()+","+payment.getPayReason());}
            paymentsFile.close();
            return true;
        }
        catch (IOException e) { // This catches an error, if any
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveAllData(String roomsTxtFileName, String guestsTxtFileName,
                           String bookingsTxtFileName, String paymentsTxtFileName){
        try{
            savePaymentsData(paymentsTxtFileName);
            saveRoomsData(roomsTxtFileName);
            saveBookingsData(bookingsTxtFileName);
            saveGuestsData(guestsTxtFileName);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This cancels a booking, making sure of the time and date limitations mentioned in the instructions of the coursework
     *
     * @param bookingID    Long integer representing a booking ID
     * @return             This returns a boolean value if the cancellation was cancelled
     */

    public boolean cancelBooking(long bookingID){
        try{
            Booking booking=null;
            for(Booking book : bookingList){
                if(book.getBookingID() == bookingID){booking = book;}
            }
            
            long diff = new Date().getTime() - booking.getCheckInDate().getTime();
            long daysDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            if(daysDiff>2){ // If there is a difference of at least 2 days, all the money will be reimbursed
                Payment refund = new Payment(new Date(), booking.getGuestID(), booking.getTotalAmount()*(-1), "refund");
                paymentList.add(refund);
            }
            bookingList.remove(booking); // After, we remove the booking from the list, once we have verified the time constrainst
            return true;
        }catch(Exception e){ // This catches an error, if any
            System.out.print("An error occured while canceling a booking....");
            System.out.print(e + "\n");
            return false;
        }
    }

    /**
     * This method displays Bookings On a Certain Date
     *
     * @param date    Date object representing the date that we want to look for
     * @return        void (Nothing)
     */

    public void displayBookingsOnDate(Date date){
        Room booked_room=null;
        for(Booking book: bookingList){
            if(book.getCheckOutDate().before(date) && book.getCheckInDate().after(date)){ // These conditions check that the check out date is after the actual check in date and that everything follows the instructions on the coursework
                Guest guest = searchGuestByID(book.getGuestID());
                for(Room room: roomList){
                    if(room.getRoomNumber()==book.getRoomNumber()){booked_room = room;}
                }
                System.out.print("bookingID: " + book.getBookingID() + " Name: " + guest.getlName() +" " + guest.getfName() + " Room Number" + book.getRoomNumber() + " Room Type: " + booked_room.getRoomType() + " Room Price: " + booked_room.getRoomPrice() + " Payment Price: " + book.getTotalAmount() +"\n");

            }
        }
    }

    /**
     * This method displays Payments On a Certain Date
     *
     * @param date    Date object representing the date that we want to look for
     * @return        void (Nothing)
     */

    public void displayPaymentsOnDate(Date date){
        for(Payment payment: paymentList){ // We iterate through the whole payment array objects
            if(payment.getDate() == date){
                System.out.print("Guest ID: "+ payment.getGuestID() + " Payment Ammount: " +payment.getAmount() + " Payment Reason:" + payment.getPayReason());

            }
        }
    }
    /**
     * This method displays all of a given guests booking infomation
     *
     * @param   guestID a unique guest ID number
     * @return  void (Nothing)
    */
    public void displayGuestBooking(long guestID){
        try{
            Room booked_room=null;
            Guest guest = searchGuestByID(guestID);
            System.out.println("Displaying all bookings by "+guest.getfName() + " " +guest.getlName());
            for(Booking book: bookingList){
                if(book.getGuestID() == guest.getGuestID()){
                    for(Room room: roomList){if(room.getRoomNumber()==book.getRoomNumber()){booked_room = room;}}
                    System.out.println("bookingID: " + book.getBookingID() + " Name: " + guest.getlName() +" " + guest.getfName() + " Room Number: " + book.getRoomNumber() + " Room Type: " + booked_room.getRoomType() + " Room Price: " + booked_room.getRoomPrice() + " Payment Price: " + book.getTotalAmount());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }    /**
     * This method displays all guests
     *
     * @param         None
     * @return        void (Nothing)
     */
    public void displayAllGuests(){
        System.out.println("Displaying Guests: ");
        for(Guest guest: guestList){
            System.out.println("Guest ID: " +guest.getGuestID() +" Guest Name: " +guest.getlName() +" " +guest.getfName() + " Date Joined: " + guest.getDateJoin());
        }
        System.out.println("Displaying VIP Guests: ");
        for(VIPGuest guest: vipGuestList){
            System.out.println("Guest ID: "+ guest.getGuestID() + " Guest Name: "+ guest.getlName() + " " +guest.getfName() + " Date Joined: " + guest.getDateJoin() + " VIP Start Date: " + guest.getVIPStartDate() + " VIP End Date: " + guest.getVIPExpiryDate());
        }
    }

    /**
     * This method displays all rooms
     *
     * @param         None
     * @return        void (Nothing)
     */

    public void displayAllRooms(){
        System.out.println("Displaing Rooms: ");
        for(Room room: roomList){
            // The following line prints all the info about a room, after having traversed the whole array of objects that represent each and every individual room
            System.out.println("Room Number: " +room.getRoomNumber() + " Room Type: " + room.getRoomType() + " Room Price: " + room.getRoomPrice() + " Room Capacity: " + room.getCapacity() + " Facilities: " +room.getFacilities());
        }
    }

    /**
     * This method displays all bookings
     *
     * @param         None
     * @return        void (Nothing)
     */

    public void displayAllBookings(){
        System.out.println("Displaying Bookings: ");
        Room booked_room=null;
        for(Booking book: bookingList){
            Guest guest = searchGuestByID(book.getGuestID());
            for(Room room: roomList){
                if(room.getRoomNumber()==book.getRoomNumber()){booked_room = room;}}
            System.out.println("bookingID: " + book.getBookingID() + " Name: " + guest.getlName() +" " + guest.getfName() + " Room Number" + book.getRoomNumber() + " Room Type: " + booked_room.getRoomType() + " Room Price: " + booked_room.getRoomPrice() + " Payment Price: " + book.getTotalAmount()); 
        }
    }

    /**
     * This method displays all payments
     *
     * @param         None
     * @return        void (Nothing)
     */

    public void displayAllPayments(){
        System.out.println("Displaying Payments: ");
        for(Payment payment: paymentList){
            System.out.println("Guest ID: "+ payment.getGuestID() + " Payment Ammount: " +payment.getAmount() + " Payment Reason:" + payment.getPayReason());

        }
    }

    /**
     * This class is a static class named after the Room object that it represents. As taught during the lectures, all the attributes (static variables) are private, so they can only be accessed
     * within the class itself.
     *
     * There is also a constructor that creates an object with all the individual parameters.
     *
     * In order to access all attributes on the Room object, we have developed several public methods that can ideed print all the information needed
     */

    static class Room {
        private long roomNumber;
        private String roomType;
        private double roomPrice;
        private int capacity;
        private String facilities;

        // This is the constructor

        public Room(long roomNumber, String roomType, double roomPrice, int capacity, String facilities) {
            this.roomNumber = roomNumber;
            this.roomType = roomType;
            this.roomPrice = roomPrice;
            this.capacity = capacity;
            this.facilities = facilities;
        }

        // These are the public methods that can access when used outside the inner, private attributes of the class
        public long getRoomNumber() {return this.roomNumber;}
        public String getRoomType(){return this.roomType;}
        public double getRoomPrice(){return this.roomPrice;}
        public int getCapacity(){return this.capacity;}
        public String getFacilities(){return this.facilities;}
    }

    /**
     * This class is a static class named after the Booking object that it represents. As taught during the lectures, all the attributes (static variables) are private, so they can only be accessed
     * within the class itself.
     *
     * There is also a constructor that creates an object with all the individual parameters.
     *
     * In order to access all attributes on the Room object, we have developed several public methods that can ideed print all the information needed
     */

    static class Booking{
        private long bookingID;
        private long guestID;
        private long roomNumber;
        private Date bookingDate;
        private Date checkInDate;
        private Date checkOutDate;
        private double totalAmount;

        // This is the constructor we have used to create a new object tailored to its own properties

        public Booking(long bookingID, long guestID, long roomNumber, Date bookingDate, Date checkInDate, Date checkOutDate, double totalAmount) {
            this.bookingID = bookingID;
            this.guestID = guestID;
            this.roomNumber = roomNumber;
            this.bookingDate = bookingDate;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.totalAmount = totalAmount;
        }

        // These are the public methods used to access from outside the inner, private attributes of this class

        public long getBookingID(){return this.bookingID;}
        public long getGuestID(){return this.guestID;}
        public long getRoomNumber(){return this.roomNumber;}
        public Date getBookingDate(){return this.bookingDate;}
        public Date getCheckInDate(){return this.checkInDate;}
        public Date getCheckOutDate(){return this.checkOutDate;}
        public double getTotalAmount(){return this.totalAmount;}
    }

    /**
     * This class is a static class named after the Guest object that it represents. As taught during the lectures, all the attributes (static variables) are private, so they can only be accessed
     * within the class itself.
     *
     * There is also a constructor that creates an object with all the individual parameters.
     *
     * In order to access all attributes on the Room object, we have developed several public methods that can ideed print all the information needed
     */

    class Guest {
        private long guestID;
        private String fName;
        private String lName;
        private Date dateJoin;

        // This is the constructor used to create more objects with individual attributes that may or not differ among all Guest objects
        public Guest(long guestID, String fName, String lName, Date dateJoin) {
            this.guestID = guestID;
            this.fName = fName;
            this.lName = lName;
            this.dateJoin = dateJoin;
        }

        // These are the public methods used to access from outside the inner, private attributes of this class

        public long getGuestID() {return this.guestID;}
        public String getfName() {return this.fName;}
        public String getlName() {return this.lName;}
        public Date getDateJoin() {return this.dateJoin;}
    }

    /**
     * This class is a static class named after the VIPGuest object that it represents. As taught during the lectures, all the attributes (static variables) are private, so they can only be accessed
     * within the class itself.
     *
     * There is also a constructor that creates an object with all the individual parameters.
     *
     * In order to access all attributes on the Room object, we have developed several public methods that can ideed print all the information needed
     */

    class VIPGuest extends Guest {
        private Date VIPStartDate;
        private Date VIPExpiryDate;

        // This is the constructor used to create more objects with individual attributes that may or not differ among all Guest objects

        public VIPGuest(long guestID, String fName, String lName, Date dateJoin, Date VIPStartDate,
                Date VIPExpiryDate) {

            // <super> keyword calls the superclass's constructor within the subclass's constructor

            super(guestID, fName, lName, dateJoin);
            this.VIPStartDate = VIPStartDate;
            this.VIPExpiryDate = VIPExpiryDate;
        }

        public void setVIP(Date VIPStartDate, Date VIPExpiryDate) {
            this.VIPStartDate = VIPStartDate;
            this.VIPExpiryDate = VIPExpiryDate;
        }

        // These are the public methods used to access from outside the inner, private attributes of this class

        public Date getVIPStartDate() { return this.VIPStartDate; }
        public Date getVIPExpiryDate() { return this.VIPExpiryDate; }

    }

    /**
     * This class is a static class named after the Payment object that it represents. As taught during the lectures, all the attributes (static variables) are private, so they can only be accessed
     * within the class itself.
     *
     * There is also a constructor that creates an object with all the individual parameters.
     *
     * In order to access all attributes on the Room object, we have developed several public methods that can ideed print all the information needed
     */

    static class Payment {
        private Date date;
        private long guestID;
        private double amount;
        private String payReason;

        // This is the constructor used to create more objects with individual attributes that may or not differ among all Guest objects

        public Payment(Date date, long guestID, double amount, String payReason) {
            this.date = date;
            this.guestID = guestID;
            this.amount = amount;
            this.payReason = payReason;
        }

        // These are the public methods used to access from outside the inner, private attributes of this class

        public Date getDate() {return this.date;}
        public long getGuestID() {return this.guestID;}
        public double getAmount() {return this.amount;}
        public String getPayReason() {return this.payReason;}
    }
}