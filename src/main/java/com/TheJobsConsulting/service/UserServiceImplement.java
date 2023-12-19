package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.*;
import com.TheJobsConsulting.exception.*;
import com.TheJobsConsulting.repository.AppointmentDAO;
import com.TheJobsConsulting.repository.ConsultantDAO;
import com.TheJobsConsulting.repository.SessionDAO;
import com.TheJobsConsulting.repository.UserDAO;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.TheJobsConsulting.config.SpringDocConfig.bCryptPasswordEncoder;

@Service
public class UserServiceImplement implements UserService, Runnable {

    static Map<String, LocalDateTime> myDateTime = new LinkedHashMap<>();   //creates a static map

    @Autowired
    UserDAO userDAO;
    @Autowired
    SessionDAO sessionDAO;
    @Autowired
    ConsultantDAO consultantDAO;

    @Autowired
    AppointmentDAO appointmentDAO;

    @Override
    public User createUser(User user) throws UserException {
        User dbUser = userDAO.findByMobileNo(user.getMobileNo());  //Check is there any user with the given mobile number
        if (dbUser == null) {
            user.setUserType("User");                             //Set user type to user
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));   //encrypt the password
            userDAO.save(user);                               //save the user
            return user;
        } else {
            throw new UserException("Already Registered User by Using this Mobile Number.");
        }
    }

    @Override
    public User updateUser(User user, String key) throws UserException {
        CurrentSession loginUser = sessionDAO.findByUuid(key);   // Retrieves object from the data access layer
        if (loginUser == null) {
            throw new UserException("Please Provide the Valid Details to Update the User");
        }
        if (user.getUserId() == loginUser.getUserId()) {
            return userDAO.save(user);
        } else {
            throw new UserException("Invalid Details. Please Login Again.");
        }
    }

    @Override
    public User getUserByUuid(String uuid) throws UserException {
        CurrentSession currentUser = sessionDAO.findByUuid(uuid);  // Retrieve the current session based on the provided UUID
        Optional<User> user = userDAO.findById(currentUser.getUserId());  // Retrieve the user based on the user ID from
        // the current session
        if (user.isPresent()) {                                         // Check if the user is present in the database
            return user.get();
        } else {
            throw new UserException("No User Found on this Key" + uuid);
        }
    }

    @Override
    public User getUserDetails(String key) throws UserException {
        CurrentSession currentUserSession = sessionDAO.findByUuid(key);   // Retrieve the current session based on the
        // provided key
        Optional<User> registeredUser = userDAO.findById(currentUserSession.getUserId());  // Retrieve the registered user
        // based on the user ID from the current session
        if (registeredUser.isPresent()) {
            return registeredUser.get();
        } else {
            throw new UserException("No User Found. Please Try Again.");
        }
    }

    @Override
    public CurrentSession getCurrentUserByUuid(String uuid) throws LoginException {  //find a user session based on the provided UUID
        CurrentSession currentUserSession = sessionDAO.findByUuid(uuid);
        if (currentUserSession != null) {
            return currentUserSession;
        } else {
            throw new LoginException("No User Found From This Key.");
        }
    }

    @Override
    public List<Consultant> getAllConsultant() throws ConsultantException {    //retrieve all consultants from the database
        List<Consultant> listOfConsultant = consultantDAO.findAll();
        if (!listOfConsultant.isEmpty()) {
            listOfConsultant = listOfConsultant.stream().collect(Collectors.toList()); //If there are consultants, the list
            // is converted to a new list and returned
            return listOfConsultant;
        } else {
            throw new ConsultantException("No Registered Consultants.");
        }
    }

    @Override
    public User forgotPassword(String key, ForgotPassword forgotPassword) throws PasswordException {
        CurrentSession currentUserSession = sessionDAO.findByUuid(key);
        Optional<User> existingUser = userDAO.findById(currentUserSession.getUserId());  //Retrieves a user by calling
        // the findById method on a userDAO object
        Boolean passwordMatch = bCryptPasswordEncoder.matches
                (forgotPassword.getCurrentPassword(), existingUser.get().getPassword());  //Checks the current password
        // provided in the ForgotPassword object matches the stored password of the user obtained from the DB
        if (passwordMatch) {
            existingUser.get().setPassword(bCryptPasswordEncoder.encode(forgotPassword.getNewPassword()));  //sets the user
            // password to the hashed version of the new password
            return userDAO.save(existingUser.get());     //saves the updated user object (with the new password) to the DB
        } else {
            throw new PasswordException("Error. Password Does Not Match.");
        }
    }


    @Override
    public List<Appointment> getUserAppointment(String key) throws AppointmentException, UserException {
        CurrentSession currentUserSession = sessionDAO.findByUuid(key);   //Find the current session based on the provided key
        Optional<User> user = userDAO.findById(currentUserSession.getUserId());  //Retrieve the user of the current session
        if (user.get() != null) {
            List<Appointment> listOfAppointments = user.get().getListOfAppointments(); //Get the list of user appointments
            if (listOfAppointments.isEmpty()) {
                throw new AppointmentException("No Appointments Found.");
            } else {
                return listOfAppointments;
            }
        }else {
            throw new UserException("Please Enter Valid User Details.");
        }
    }

    public static void loadAppointmentsDates (Integer from, Integer to) throws IOException, TimeDateException{
        myDateTime.clear();  //Clears the existing content of the myDateTime data structure
        if (from == null || to == null){
            throw new TimeDateException("Please Enter Appointments Start & End Times Respectively.");
        }
        LocalDateTime currentDateTime = LocalDateTime.now();    //Obtains the current date and time
        LocalDateTime tomorrowDateTime = currentDateTime.plusDays(1); //Calculates the date and time for tomorrow
        LocalDateTime dayAfterTomorrow = currentDateTime.plusDays(2);  //calculates the date and time for day after tomorrow
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = from; i <= to; i++){  //Iterates over the range of hours for today
            String todayTimeString = null;            //Constructs date-time strings
            if (!(i >= 10)){
                todayTimeString = currentDateTime.toLocalDate() + "0" + i + ":00";
            }else {
                todayTimeString = currentDateTime.toLocalDate() + " " + i + ":00";
            }
            LocalDateTime dateTime = LocalDateTime.parse(todayTimeString, formatter);   //Parses date,time strings into
                                                                   //LocalDateTime objects using the specified formatter
            if (currentDateTime.isBefore(dateTime)) {   //Checks if the parsed date-time is in the future compared to the
                                                        // current date and time
                myDateTime.put("today"+i, dateTime);
                }
            }

        for (int i = from; i <= to; i++){ //Iterates over the range of hours for tomorrow
            String tomorrowTimeString = null;     //Constructs date-time strings
            if (!(i >= 10)){
                tomorrowTimeString = tomorrowDateTime.toLocalDate() + "0" + i + ":00";
            }else {
                tomorrowTimeString = tomorrowDateTime.toLocalDate() + " " + i + ":00";
            }
            LocalDateTime dateTime = LocalDateTime.parse(tomorrowTimeString, formatter); //Parses date,time strings into
                                                                   //LocalDateTime objects using the specified formatter
            if (currentDateTime.isBefore(dateTime)){
                myDateTime.put("tomorrow"+ i, dateTime);
            }
        }
        for (int i = from; i <= to; i++){  //Iterates over the range of hours for day after tomorrow
            String dayAfterTomorrowString = null;    //Constructs date-time strings
            if (!(i >= 10)) {
                dayAfterTomorrowString = dayAfterTomorrow.toLocalDate() + "0" + i + ":00";
            }else {
                dayAfterTomorrowString = dayAfterTomorrow.toLocalDate()+ " " + i + ":00";
            }
            LocalDateTime dateTime = LocalDateTime.parse(dayAfterTomorrowString, formatter); //Parses date,time strings into
                                                                   //LocalDateTime objects using the specified formatter
            if (currentDateTime.isBefore(dateTime)) {
                myDateTime.put("day After Tomorrow"+ i, dateTime);
            }
        }
    }

    @Override
    public Appointment bookAppointment(String key, Appointment appointment) throws AppointmentException, LoginException,
            ConsultantException, IOException, MessagingException, TimeDateException {
        CurrentSession currentAppointmentSession = sessionDAO.findByUuid(key);  //retrieving a CurrentSession object based on the provided key using a sessionDAO
        Optional<User> user = userDAO.findById(currentAppointmentSession.getUserId()); // gets a User object based on the user ID stored in the CurrentSession
        synchronized (this){
            if (user.isPresent()){
                appointment.setUser(user.get());  //sets the user for the appointment and proceeds to handle the consultant details.
                Consultant consultant = appointment.getConsultant();  //retrieves a Consultant object from the appointment
                Optional<Consultant> registerConsultant = consultantDAO.findById(consultant.getConsultantId());
                if (!registerConsultant.isEmpty()){
                    appointment.setConsultant(registerConsultant.get()); //setting consultant in appointment
                    loadAppointmentsDates(registerConsultant.get().getAppointmentFromTime(),
                            registerConsultant.get().getAppointmentToTime());   //loads appointment dates & times for the consultant
                    List<Appointment> listOfAppointment = appointment.getConsultant().getListOfAppointments();
                    Boolean flag1 = false;
                    Boolean flag2 = false;
                    for (Appointment eachAppointment : listOfAppointment){
                        if (eachAppointment.getAppointmentDateTime().isEqual(appointment.getAppointmentDateTime())){
                            flag1 = true;
                        }
                    }
                    for (String str : myDateTime.keySet()){ //check the given date
                        if (myDateTime.get(str).isEqual(appointment.getAppointmentDateTime())){
                            flag2 = true;
                        }
                    }
                    Appointment registerAppointment = null;   //checks if the requested appointment date/time is not already booked
                    if (!flag1 && flag2){
                        registerAppointment = appointmentDAO.save(appointment);
                    }
                    else {
                        throw new AppointmentException("This Time Slot was Already Booked. Please Select Different " +
                                "Time Slot." +appointment.getAppointmentDateTime());
                    }
                    registerConsultant.get().getListOfAppointments().add(registerAppointment);
                    consultantDAO.save(registerConsultant.get());
                    user.get().getListOfAppointments().add(registerAppointment);
                    userDAO.save(user.get());
                    return registerAppointment;   //returns the registered appointment
                }else {
                    throw new ConsultantException("Please Enter Valid Consultant Details" + consultant.getConsultantId());
                }
            } else {
                throw new LoginException("Please Enter Valid Key.");
            }
        }
    }



    @Override
    public Appointment deleteAppointment(Appointment appointment) throws AppointmentException, ConsultantException, Exception {
        return null;
    }

    @Override
    public void run() {

    }
}
