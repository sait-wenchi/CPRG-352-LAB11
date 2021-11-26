package services;

import dataaccess.UserDB;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.User;

public class AccountService {
    
    public User login(String email, String password, String path) {
        UserDB userDB = new UserDB();
        
        try {
            User user = userDB.get(email);
            if (password.equals(user.getPassword())) {
                //Logger.getLogger(AccountService.class.getName()).log(Level.INFO, "Successful login by {0}", email);
                
                //GmailService.sendMail(email,"Notes App Login", "A login has been made to your notes app db.", true);

                String to = user.getEmail();
                String subject = "Notes App Login";
                String template = path + "/emailtemplates/login.html";
                
                HashMap<String, String> tags = new HashMap<>();
                tags.put("firstname", user.getFirstName());
                tags.put("lastname", user.getLastName());
                tags.put("date", (new java.util.Date()).toString());
                
                GmailService.sendMail(to, subject, template, tags);

                return user;
            }
        } catch (Exception e) {
        }
        
        return null;
    }
    public String checkUUID(String uuid)
    {
        String email = "";
        UserDB userDB = new UserDB();
        List<User> users = userDB.getAll();
        for(int i=0;i<users.size();i++)
        {
            User user = users.get(i);
            if(user.getPasswordUuid() != null && user.getPasswordUuid().equals(uuid))
            {
                return user.getEmail();
            }
        }
        return email;
    }
    public boolean checkEmail(String email)
    {
        UserDB userDB = new UserDB();
        List<User> users = userDB.getAll();
        for(int i=0;i<users.size();i++)
        {
            User user = users.get(i);
            if(user.getEmail().equals(email))
            {
                return true;
            }
        }
        return false;
    }
    public void resetPassword(String email,String path, String url)
    {
        UserDB userDB = new UserDB();
        String uuid = UUID.randomUUID().toString();
        String link = url+ "?uuid=" + uuid;
        User user = userDB.get(email);
        user.setPasswordUuid(uuid);
        userDB.update(user);
        
        String template = path + "/emailtemplates/resetpassword.html";
        HashMap<String, String> tags = new HashMap<>();
        tags.put("firstname", user.getFirstName());
        tags.put("lastname", user.getLastName());
        tags.put("date", (new java.util.Date()).toString());
        tags.put("link", link);
                
        try {
            GmailService.sendMail(email, "Notes App Reset Email", template, tags);
        } catch (Exception ex) {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void changePassword(String email,String passowrd)
    {
        UserDB userDB = new UserDB();
        User user = userDB.get(email);
        user.setPassword(passowrd);
        user.setPasswordUuid(null);
        userDB.update(user);
    }
}
