/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import services.AccountService;

/**
 *
 * @author wenchi
 */
public class ResetPasswordServlet extends HttpServlet {


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccountService as = new AccountService();
        // getServletContext().getRequestDispatcher("/WEB-INF/reset.jsp").forward(request, response);
        String uuid = request.getParameter("uuid");
        if (uuid != null && !(uuid.equals("")))
        {
            System.out.println("get uuid");
            // if uuid is exist direct to set new password
            // check uuid in database
            String email = as.checkUUID(uuid);
            if(!email.equals(""))
            {
                System.out.println("valid uuid");
                HttpSession session = request.getSession();
                session.setAttribute("email",email);
                System.out.println("go to reset new password page");
                getServletContext().getRequestDispatcher("/WEB-INF/resetNewPassword.jsp").forward(request, response);
                return;
            }
            System.out.println("invalid uuid");
        }
        getServletContext().getRequestDispatcher("/WEB-INF/reset.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // action: reset password (attribute email)
        String action = request.getParameter("action");
        AccountService as = new AccountService();
        if (action != null && action.equals("reset"))
        {
            String email = request.getParameter("email");
            System.out.println(email);
            if(as.checkEmail(email))
            {
                String path = getServletContext().getRealPath("/WEB-INF");
                System.out.println("find email");
                String url = request.getRequestURL().toString();
                as.resetPassword(email,path, url);
            }
            request.setAttribute("message",
                    "send reset password email to your email box (if the address is valid)");
            getServletContext().getRequestDispatcher("/WEB-INF/reset.jsp").forward(request, response);
        }
        
        // action: new, set new passowrd attribute:email/password
        if (action != null && action.equals("new"))
        {
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute("email");
            System.out.printf("email:%s update new password:%s",email,request.getParameter("password"));
            if (email != null && !(email.equals("")))
            {
                String password = request.getParameter("password");
                as.changePassword(email, password);
            }
            response.sendRedirect("login");
        }
        
        // go to login page anyway
    }
}
