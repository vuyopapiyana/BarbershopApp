
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vuyopapiyana
 */
public class ReadInClass {

    private String bRef = "";
    private User userArr[] = new User[100];
    private boolean overbooked;
    private int noAppointments;
    private Barber barberArr[] = new Barber[100];
    private int userCounter = 0;
    private int barberCounter = 0;
    private DBConnect dbObj = new DBConnect();
    private User signedInUser;

    //Constructor used to read in objects for the userArr and the barberArr. Values acquired from the respective tables
    //in the database.
    public ReadInClass() {
        try {
            ResultSet uRs = dbObj.query("Select * from Users");
            int user_id;
            String user_name;
            String user_surname;
            String username;
            String pswd;
            int cell;
            String email;

            //While loop to read in objects of User into the arrays
            // using details from ResultSet uRs
            while (uRs.next()) {
                user_id = uRs.getInt("UserID");
                user_name = uRs.getString("Name");
                user_surname = uRs.getString("Surname");
                username = uRs.getString("Username");
                pswd = uRs.getString("Password");
                cell = uRs.getInt("CellPhone");
                email = uRs.getString("Email");

                userArr[userCounter] = new User(user_id, user_name, user_surname, username, pswd, cell, email);
                userCounter++;

            }

            uRs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReadInClass.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error in populating users: " + ex);
        }

        //Try statement to read in barber array with values from bRs
        try {
            ResultSet bRs = dbObj.query("Select * from Barber");
            String name = "";
            String surname = "";
            int id = 0;
            int haircutCount = 0;

            while (bRs.next()) {
                name = bRs.getString("Name");
                id = bRs.getInt("BarberID");
                surname = bRs.getString("Surname");

                barberArr[barberCounter] = new Barber(name, surname, id);
                barberCounter++;

            }
            bRs.close();

            String haircuts[] = new String[20];
            int trackRecords = 0;
            double cost[] = new double[20];
            int countCut = 0;
            int barberID = 1;

            while (trackRecords < barberCounter) {
                ResultSet haircutResultSet = dbObj.query("select* from barberCut inner join Haircut on Haircut.Haircut = "
                        + "barberCut.Haircut where barberID = " + barberID);

                // While loop which populates a haircut and cost array. Arrays are sent into the barber objects using
                // setHaircut(String[]h) and setCost (double[]c)
                while (haircutResultSet.next()) {
                    haircuts[countCut] = haircutResultSet.getString("Haircut");
                    cost[countCut] = haircutResultSet.getDouble("Cost");
                    
                    barberArr[trackRecords].setHaircut(haircuts[countCut], countCut);
                    barberArr[trackRecords].setCost(cost[countCut], countCut);
                
                    countCut++;

                }

                barberArr[trackRecords].setAmountCuts(countCut);

                // System.out.println(barberArr[trackRecords]);
                //intialising count to zero so that the haircut and cost values will start at zero when the next barber's
                // haircuts and costs are being read in.
                countCut = 0;
                barberID = barberID + 1;
                trackRecords++;

            }

        } catch (SQLException ex) {
            Logger.getLogger(ReadInClass.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error in populating barbers: " + ex);
        }

    }

    //Method to confirm whether the user has entered the correct username and password for an account
    //Takes String paramters for username and password and returns a boolean value of true if 
    //the credentials are correct and false if the credentials are incorrect.
    public boolean confirmLogin(String user, String pswd) {
        boolean validPswd = false;
        boolean validUser = false;
        int i = 0;

        while (!(validPswd || validUser) && i < userCounter) {
            if (userArr[i].getUsername().equals(user.trim())) {
                validUser = true;
                //User is in the database. The program will now check if the passsword is correct
                if (userArr[i].getPassword().equals(pswd)) {
                    // User has entered the corect credentials for the account so program is about to switch screens.
                    validPswd = true;
                    signedInUser = userArr[i];
                    JOptionPane.showMessageDialog(null, "Welcome, " + userArr[i].getUsername());
                }

            }

            i++;
        }// end of while

        //Used to display whether the username is unknown or if the user typed the incorrect password
        if (!validUser) {

            JOptionPane.showMessageDialog(null, "Unknown Username");

        } else {
            if (!validPswd) {

                JOptionPane.showMessageDialog(null, "Incorrect Password ");

            }
        }

        return validPswd && validUser;
    }

    /*
    Used to check the validity of a name and surname.
    Method takes in the name or surname as a String in the paramters.
    Method returns true if the name is valid and false if the name is invalid.
    CRITICAL METHOD
     */
    public boolean nameValidity(String n) {
        boolean valid = true;
        n.trim();
        // for loop to loop through each character of the String and check if any of the characters are NOT letters.
        // If char is not letter, Valid is set to false
        for (int x = 0; x < n.length(); x++) {
            if (!(Character.isLetter(n.charAt(x)))) {
                valid = false;
            }

        }
        return valid;
    }

    //Method for emailValidity
    //Receives a String to represent the email.
    //Returns true if the email is valid and false if the email is invalid
    //Email is valid when it has an "@" and a "." within it.
    public boolean emailValidity(String email) {
        boolean valid = false;

        //if statements to check for the presence of the "@" and "."
        if (email.indexOf('@') > 0) {
            if (email.indexOf('.') > -1) {

                int posAt = email.indexOf('@');
                int posPeriod = email.indexOf('.');
                int posPeriodLast = email.lastIndexOf('.');
                //checks that "@" is not immediately before "." and "@" is not immediately after "."
                valid = (posAt != (posPeriod--)) && (posAt != posPeriod++);
            }
        }

        return valid;
    }

    /*
    Method to check the password validity.
    Receives a String represesnting the pasword for the account.
    Returns true if the password is valid and false if the password is invalid.
    Password is only valid when it is longer than 7 letters, has at least one capital letter and at least one digit
     */
    public boolean passwordValidity(String pword) {
        boolean valid;
        boolean isNumber = false;
        int count = 0;
        int countCaps = 0;
        boolean lengthCheck;

        if (pword.length() > 7) {
            lengthCheck = true;

            // While loop to check if the password has a capital letter or a digit.
            while (countCaps < 1 || count < pword.length() || !isNumber) {

                char character = pword.charAt(count);
                if (Character.isUpperCase(character)) {
                    countCaps++;
                }

                if (Character.isDigit(character)) {
                    isNumber = true;
                }

                count++;

            }

        } else {
            JOptionPane.showMessageDialog(null, "Password must be longer than 7 letters");
            lengthCheck = false;
        }

        valid = isNumber && lengthCheck && countCaps > 0;
        return valid;
    }

    //Method to check whether the username does not exist already.
    //Takes username as a parameters as a String and returns true if the username does not exist.
    //and false if the username already exists.
    public boolean usernameValidity(String u) {
        boolean valid = true;
        int count = 0;
        while (count < userCounter && valid) {
            if (userArr[count].equals(u)) {
                valid = false;
                JOptionPane.showMessageDialog(null, "Username already exists. Please pick a different username");
            }
            count++;

        }

        return valid;
    }

    /* 
    Cell phone validity checks if the entered cell phone number is valid
    Receives an int representing the cell phone. Returns true if the cell is valid and false if the cell phone is invalid.
    Cell is only valid when it has 9 digits and no letters
    
    Due to integers trancating a "0" that is at the beginning of an int, all cell phones should have 9 digits.
    The "0" at the beginning of standard South African cell numbers is then manually added later in programs when 
    it needs to be displayed.
     */
    public boolean cellValidity(int cellPhone) {
        String cellLength = "" + cellPhone;
        int countChar = 0;
        boolean length;
        boolean charCheck;

        if (cellLength.length() == 9) {
            length = true;

            // loop to check each character in the cell phone. If the cell is not a digit,  countChar increments.
            //When countCharr>0 then the cell is invalid
            int count = 0;
            while (count < 9 && countChar == 0) {
                char c = cellLength.charAt(count);

                if (!Character.isDigit(c)) {
                    countChar++;
                    JOptionPane.showMessageDialog(null, "Cell may not contain leters or special characters");
                }
                count++;

            }

        } else {
            length = false;
            JOptionPane.showMessageDialog(null, "Cell Phone is not long enough");
        }

        charCheck = countChar == 0;

        return length && charCheck;
    }

    //Method used to run sql statements into the databse
    //Accepts the sql as a String and has no return values.
    public void runUpdateQuery(String sql) {
        try {
            dbObj.update(sql);
        } catch (SQLException ex) {
            Logger.getLogger(ReadInClass.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error with run update query: " + ex);
        }

    }

    
    // Method used to switch the displayed screen.
    // receives panel to switch to and the card panel it is displayed on
    // as paramters.
    public void switchPanels(JPanel panel, JPanel card) {
        card.removeAll();
        card.add(panel);
        card.repaint();
        card.revalidate();

    }

    // Method to check if the chosen date is acceptable.
    // Date is acceptable if it is not more than a month away or a date earlier than the current day
    public boolean confirmBookingDate(Date selected_Date)  {

        boolean confirm = false;

        //Puts todays date in the form dd-MM-yyyy without including the timestamp
        Date current_date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            current_date = format.parse(format.format(current_date));
        } catch (ParseException ex) {
            Logger.getLogger(ReadInClass.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Comparisons to check if the date is before today's current date.
        if (!(selected_Date.before(current_date)) || selected_Date.equals(current_date)) {

            
            if (selected_Date.getYear() == current_date.getYear() && !(selected_Date.before(current_date))) {
                //Checks that the selected date is not more than one month ahead of the current date.
                if ((selected_Date.getMonth() + 1 == current_date.getMonth() + 1)
                        || (current_date.getMonth() + 2 == selected_Date.getMonth() + 1)) {

                    confirm = true;

                } else {
                    JOptionPane.showMessageDialog(null, "The earliest appointment you may book is a month in advance. Please repick your month. ");
                }

            } else {
                JOptionPane.showMessageDialog(null, "You may not book appointments years in advance. ");
            }

        } else {
            JOptionPane.showMessageDialog(null, "You may not book an appointment for a past date ");
        }

        return confirm;
    }

    // Creates booking reference by using the date, barber name and haircut of the client
    // Method returns a boolean. True if the booking was successfully created and added to database. False if the booking
    // was not added to the database.
    // The method receives String b [as barber name], Date d[as the appointment date], String h[as the haircut]
    // Uses the date of the booking as the first 4 characters of the reference (DD/MM)
    // followed by the first letter of the chosen haircut. The initials of the barber
    // are then added to the reference with the amount of bookings the barber has on that day
    // and a randomly generated integer between 1 and 100
    //---------------------------------
    // e.g 2208PMD298
    //---------------------------------
    public boolean createBRef(String b, Date d, String h, int barberIndex) {
        int counter = 0;
        //calls checkBarberOverbooked to check if the barber is fully booked and initialise boolean overbooked
        checkBarberOverbooking(barberIndex, d);
        boolean booked = false;
        if (overbooked) {
            JOptionPane.showMessageDialog(null, "Barber is over booked. Please pick another day for the appointment");
        } else {
            Barber barber = barberArr[barberIndex];

            //Format date so that it is always a four character long string 
            //Checks if any if the dates are less than 10 and puts a 0 ahead of them
            //e.g 0906
            //    1008
            //    3109
            String day = d.getDate() + "";
            String month = "" + (d.getMonth() + 1);

            if (Integer.parseInt(day) < 10) {
                day = "0" + day;
                if (Integer.parseInt(month) < 10) {
                    month = "0" + month;
                }
            } else {
                if (Integer.parseInt(month) < 10) {
                    month = "0" + month;
                }
            }

            String barberName = barber.getName();

            String dateAppointment = day + month;
            String barberInitials = b.charAt(0) + "";
            barberInitials += b.charAt(3);
            String hair = h.charAt(0) + "";
            int ranNum = (int) (Math.round(Math.random() * 100));
            bRef = dateAppointment + hair + barberInitials + noAppointments + ranNum;

            //Displays the booking details and asks user to confirm the details.
            //If the user presses "no", then the program will not attempt to insert the booking into the database.
            //Should the user press "yes", his booking will be added to the database
            int decision = JOptionPane.showConfirmDialog(null, "Are the details below correct:\n\n"
                    + "Barber: " + barber.getName() + "\nDate: " + dateAppointment + "\nHaircut: " + h + "\nCost: R" + getCostOfHaircut(h),
                    "Appointment", JOptionPane.YES_NO_OPTION);

            if (decision == 0) {
                try {
                    dbObj.update("Insert Into BReference "
                            + "(BookingRef, UserID, BarberID, Haircut, Date, Cost)"
                            + " Values (\"" + bRef + "\", " + signedInUser.getUserID() + " , " + barber.getBarberID() + " , \"" + h
                            + "\", \"" + dateAppointment + "\" , " + getCostOfHaircut(h) + ")");
                } catch (SQLException ex) {
                    Logger.getLogger(ReadInClass.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "Error with the create booking sql");
                }
                booked = true;
            }

        }// end of else statement

        return booked;
    }// end of creatBRef

    //Void return method to check if barber is fully booked. Barber is fully booked when he has 3 appointments on the same day.
    //Sets the class' field 'overbooked' to true if barber is fully booked and false if barber still has open slots.
    //Receives the barber as a String and the date as a Date in parameters.
    private void checkBarberOverbooking(int barberIndex, Date d) {
        String day = d.getDate() + "";
        String month = "" + (d.getMonth() + 1);

        // if statements checks if the day or month is less than 10. If that condition is true, the program adds a 0.
        //This puts the date in the correct format to compare it with the value found in the database.
        if (Integer.parseInt(day) < 10) {
            day = "0" + day;
            if (Integer.parseInt(month) < 10) {
                month = "0" + month;
            }
        } else if (Integer.parseInt(month) < 10) {
            month = "0" + month;

        }

        String date = day + month;

        //Try statement to extract the amount of appointments on a given day with a specific barber.
        try {

            int count = 0;

            ResultSet rs = dbObj.query("Select count(barberID) as Bookings"
                    + " From BReference where BarberID = " + barberArr[barberIndex].getBarberID() + " and Date = \"" + date + "\"");

            noAppointments = rs.getInt(1);

            overbooked = noAppointments >= 3;

        } catch (SQLException ex) {
            Logger.getLogger(ReadInClass.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error with the overbooking method\n" + ex);
        }

    }// End of check overbooked

    //Boolean to check if signed in user currently has a booking.
    //@returns true if the user currently has a booking and false if user does not have a booking.
    public boolean isThereABooking() {
        int count_bookings = 0;

        ResultSet rs;
        try {
            rs = dbObj.query("Select count(userID) as Bookings from BReference where userID = " + signedInUser.getUserID());
            count_bookings = rs.getInt("Bookings");

        } catch (SQLException ex) {
            Logger.getLogger(ReadInClass.class.getName()).log(Level.SEVERE, null, ex);
        }

        return count_bookings > 0;
    }

    // Method to get the cost of a specific haircut. Receives the hairstyle as a parameters
    // and returns the cost of the haircut as a double.
    public double getCostOfHaircut(String cut) {
        double cost = 0;
        int bCount = 0;
        int hairCount = 0;
        boolean found = false;
        boolean hairFound = false;
        // Nested while loop which loops through the barbers and their haircuts. First while loops from
        //0 all the way to 'barberCounter' which is the total amount of barbers in the database.
        while (bCount < barberCounter && !found) {

            //Loops from zero all the way to the total amount of haircuts the specific barber has.
            while (!hairFound && hairCount < barberArr[bCount].getAmountOfCuts()) {
                if (barberArr[bCount].getHaircut(hairCount).equals(cut)) {
                    found = true;
                    hairFound = true;
                    cost = barberArr[bCount].getCost(hairCount);
                }
                hairCount++;
            }

            hairCount = 0;
            bCount++;
        }

        return cost;
    }

    // Boolean return method with no paramters which returns true if the booking has been deleted and false if the booking has
    // not been deleted.
    public boolean deleteBooking() {
        boolean deleted = false;
        //Confirm Dialog is used to ask the user if he really wants to delete the booking
        //Decision is stored in "int decision" and used to decide if the program should delete the booking or not
        int decision = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the scheduled booking?",
                "Appointment", JOptionPane.YES_NO_OPTION);

        if (decision == 0) {
            try {
                dbObj.update("Delete from BReference where userID = " + signedInUser.getUserID());
                deleted = true;
                JOptionPane.showMessageDialog(null, "Booking has been successfully deleted !");
            } catch (SQLException ex) {
                Logger.getLogger(ReadInClass.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error with the create booking sql");
            }

        }

        return deleted;
    }

    //Method to extract the contents for the about page from its text file.
    //Has no paramters and returns a String representing the contents in the 'About' text file to be stored on the about screen.
    public String getTextAbout() {
        String line = "";
        try {
            Scanner scFile = new Scanner(new File("BarberPatAbout.txt"));

            while (scFile.hasNext()) {
                line = line + scFile.nextLine() + "\n";

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadInClass.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error in retrieving the \"About\" text file");
        }
        return line;
    }

    /**
     *
     * @param index of the help text you want to receive. E.g Instruction 4 is index 4.
     *
     * @return String representing the help instructions.
     */
    public String getHelpText(int index) {

        Scanner scFile = null;
        try {
            scFile = new Scanner(new File("HelpGuide.txt"));
        } catch (FileNotFoundException ex) {
            System.out.println("Error retrieving the help text file");
            Logger.getLogger(ReadInClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String line = "";
        String result = "";
        Scanner scLine;
        String empty = "";
        int lineCount = 1;
        int charCount = 0;

        //loops past the instructions which are a lower index than the desired instructions
        for (int x = 0; x < index; x++) {
            line = scFile.nextLine();
        }

        scLine = new Scanner(line).useDelimiter(" ");

        while (scLine.hasNext()) {
            charCount = 0;

            //Loop iterates through the result and checks if the line has 23 characters.
            //When line has 23 characters, the loop adds a new line and adds that to result.
            for (int x = 0; x < result.length(); x++) {
                if (charCount == (lineCount * 23)) {
                    lineCount++;
                    result = result + "\n";
                }
                charCount++;
            }

            result = result + scLine.next() + " ";

        }
        return result;
    }

    /**
     * @param index of the help screen image you want to switch to
     * @return Image at the given index.
     */
    public Image getHelpImage(int index) {

        File file = new File(getClass().getResource("/img").getFile());
        String[] imageList = file.list();

        String imgName = imageList[index];
        ImageIcon icon = new ImageIcon(getClass().getResource("/img/" + imgName));
        System.out.println(imgName);
        Image image = icon.getImage();
        return image;

    }
    
    // Method to display the name and surname and haircuts a barber can do with their respective costs. 
    //Receives an int as the barber's index as the paramaters.
    //Returns a String of the barber's full name and all his haircuts in a column format. 
        public String spaceDisplayedInfo(int bIndex) {
       
        String barberFullName = barberArr[bIndex].getName()+" "+barberArr[bIndex].getSurname();
        
        int count = 0;
        String formattedResult = barberFullName;

         String barberHeader = "Barber";
         String cutHeader = "Haircut";
        int barberSpace = barberHeader.length() + 21;
        int cutSpace = cutHeader.length() + 24;

        String space = "";
        // Loop to determine amount of spaces between name and haircut
        for (int x = 0; x < (barberSpace - barberFullName.length()); x++) {
            space = space + " ";
        }

        formattedResult = formattedResult + space + barberArr[bIndex].getHaircut(0);
        space = "";

        //For loop to determine amount of spaces between haircut and cost
        //This is not within the while loop like the rest of the lines because the first line has to include the full name of
        //the barber while the next lines fo not.
        for (int x = 0; x < (cutSpace - barberArr[bIndex].getHaircut(0).length()); x++) {
            space = space + " ";
        }

        formattedResult = formattedResult + space + "R " + barberArr[bIndex].getCost(0) + "\n";

        //Loop to determine create String of next columns in the correct alignment 
        int countRows = 1;
        String nextLines = "";
        while (countRows < barberArr[bIndex].getAmountOfCuts()) {
            space = "";
            for (int x = 0; x < barberSpace + 8; x++) {
                space = space + " ";
            }
            nextLines = nextLines + space + barberArr[bIndex].getHaircut(countRows);
            space = "";

            for (int x = 0; x < (cutSpace - barberArr[bIndex].getHaircut(countRows).length()); x++) {
                space = space + " ";
            }
            nextLines = nextLines + space + "R " + barberArr[bIndex].getCost(countRows) + "\n";

            countRows++;
        }

        formattedResult = formattedResult + nextLines;

        return formattedResult;
    }
    
    //Method used to return the user with the specific username
    //Receives a string representing the user's username and returns a user object
    public User searchForUser(String un) {
        int count = 0;
        User temp = null;
        boolean found = false;
        while (count < userCounter && found == false) {
            if (un.equals(userArr[count].getUsername())) {
                found = true;
                temp = userArr[count];
            }
            count++;
        }
        return temp;
    }

    //Void method to update all the user's fields.
    //Receives the name, surname, email address, username, password and cell phone number of the user.
    public void setAllUserFields(String n, String s, String e, String u, String p, int c) {
        getUser(signedInUser.getUserID()).setAllFields(n, s, u, p, e, c);
        try {
            dbObj.update("Update Users Name = \""+n+"\", Surname = \""+s+"\", email = \""+e+"\", username = \""+u+"\""+
                    " , password = \""+p+"\", cellphone = "+c);
        } catch (SQLException ex) {
            Logger.getLogger(ReadInClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User getSignedInUser() {
        return signedInUser;
    }

    /**
     * •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
     * Series of getters and setters for all the fields of the class
     * •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
     */
    public int getUserCount() {
        return userCounter;
    }

    public int getBarberCount() {
        return barberCounter;
    }

    public ResultSet getBRef() {
        ResultSet rs = null;
        try {
            rs = dbObj.query("Select* from BReference where userID = " + signedInUser.getUserID());
        } catch (SQLException ex) {
            System.out.println("Error with the getBRef method");
            Logger.getLogger(ReadInClass.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    public User getUser(int index) {
        return userArr[index];
    }

    public Barber getBarber(int index) {
        return barberArr[index];
    }

    public void setUsers(User user) {
        userCounter++;
        userArr[userCounter] = user;
    }

    public void setSignedInUser(User user) {
        signedInUser = user;
    }

}// End of class
