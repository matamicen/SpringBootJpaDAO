package com.example.captton55.Controller;


import com.example.captton55.model.User;
import com.example.captton55.model.UserDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.ArrayList;
import java.util.Enumeration;



/**
 * A class to test interactions with the MySQL database using the UserDao class.
 *
 * @author netgloo
 */
@Controller
public class UserController {
    Logger log = LoggerFactory.getLogger(this.getClass());

    HttpSession session;
   ArrayList<User> ListaUsuarios;


    // ------------------------
    // PUBLIC METHODS
    // ------------------------
    @RequestMapping(value="/create", method= RequestMethod.GET)
    public String customerForm(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        session = request.getSession();
        model.addAttribute("ListaUsers", ListaUsuarios);


        return "create";
    }

    @RequestMapping(value="/create", method=RequestMethod.POST)
    public String customerSubmit(@ModelAttribute User user, Model model, HttpServletRequest request) {

        User user2 = null;
        try {
            user2 = new User(user.getEmail(), user.getName());
            userDao.save(user);
            session = request.getSession();
            session.setAttribute("mySessionAttribute", user.getName());
            //ListaUsuarios = (ArrayList<User>) session.getAttribute("ListaUsers");
            if (ListaUsuarios==null) ListaUsuarios = new ArrayList<User>();
            ListaUsuarios.add(user);
           // model.addAttribute("ListaUsers", ListaUsuarios);
           // session.setAttribute("ListaUsers", ListaUsuarios);
        }
        catch (Exception ex) {
            return "Error creating the user: " + ex.toString();
        }


        model.addAttribute("user", user);
        String info = String.format("Customer Submission: id = %d, firstname = %s, lastname = %s",
                user.getId(), user.getEmail(), user.getName());
        log.info(info);

        return "result";
    }
    /**
     * /create  --> Create a new user and save it in the database.
     *
     * @param email User's email
     * @param name User's name
     * @return A string describing if the user is succesfully created or not.
     */
  /*  @RequestMapping("/create")
    @ResponseBody
    public String create(String email, String name) {
        User user = null;
        try {
            user = new User(email, name);
            userDao.save(user);
        }
        catch (Exception ex) {
            return "Error creating the user: " + ex.toString();
        }
        return "User succesfully created! (id = " + user.getId() + ")";
    }
*/

    /**
     * /delete  --> Delete the user having the passed id.
     *
     * @param id The id of the user to delete
     * @return A string describing if the user is succesfully deleted or not.
     */
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(long id) {
        try {
            User user = new User(id);
            userDao.delete(user);
        }
        catch (Exception ex) {
            return "Error deleting the user: " + ex.toString();
        }
        return "User succesfully deleted!";
    }

    /**
     * /get-by-email  --> Return the id for the user having the passed email.
     *
     * @param email The email to search in the database.
     * @return The user id or a message error if the user is not found.
     */
    @RequestMapping("/get-by-email")
    @ResponseBody
    public String getByEmail(String email) {
        String userId;
        try {
            User user = userDao.findByEmail(email);
            userId = String.valueOf(user.getId());
        }
        catch (Exception ex) {
            return "User not found";
        }
        return "The user id is: " + userId;
    }


    @RequestMapping("/getbyname")
    @ResponseBody
    public String getByPepe(String name) {
        String userId;
        try {
            User user = userDao.findByName(name);
            userId = String.valueOf(user.getId());
        }
        catch (Exception ex) {
            return "User not found";
        }
        return "The user id is: " + userId;
    }

    /**
     * /update  --> Update the email and the name for the user in the database
     * having the passed id.
     *
     * @param id The id for the user to update.
     * @param email The new email.
     * @param name The new name.
     * @return A string describing if the user is succesfully updated or not.
     */
    @RequestMapping("/update")
    @ResponseBody
    public String updateUser(long id, String email, String name) {
        try {
            User user = userDao.findOne(id);
            user.setEmail(email);
            user.setName(name);
            userDao.save(user);
        }
        catch (Exception ex) {
            return "Error updating the user: " + ex.toString();
        }
        return "User succesfully updated!";
    }



    // ------------------------
    // PRIVATE FIELDS
    // ------------------------

    @Autowired
    private UserDao userDao;

} // class UserController