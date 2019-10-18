/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holds;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mahmoud
 */

@WebServlet(name = "search", urlPatterns = {"/search"})
public class Search extends HttpServlet {

    private String answerQuery = "select iditem, title from item where title like ?";
    private PreparedStatement answerStmt = null;
    private Statement stmt = null;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://localhost:3306/hold?user=root&password=cmsc250";
            Connection connection = DriverManager.getConnection(url);
            stmt = connection.createStatement();
            answerStmt = connection.prepareStatement(answerQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Search for an item</title>");            
            out.println("</head>");
            out.println("<body>");
            //Here, inside the body is where we will process the request
            //And make the response page
            String item = request.getParameter("item");
            request.getSession().setAttribute("item", item);
            try {
                answerStmt.setString(1, "%" + item + "%");
                ResultSet rset = answerStmt.executeQuery();
                int i =0;
                while (rset.next()){
                    i++;
                }
                rset.first();
                if(i==0) {
                    out.println("<p>Sorry, I couldn't find any items</p>");
                    //Need to find a way now to go back to first thing
                } else if(i==1){
                    out.println("<p>Here is your search result:</p>");
                    out.println("<p>" + rset.getString(2) + "</p>");
                    out.println("<form action=\"showResults\">");
                    out.println("<input type=\"hidden\" name=\"item\" value=\""
                            + rset.getInt(1) + "\"/>");
                    out.println("<p>Enter your library card number to reserve it:</p>");
                    out.println("<input type=\"text\" name=\"patron\"/>");
                    out.println("<input type=\"submit\" value=\"Place Hold\"/>");
                    out.println("</form>");
                } else {
                    out.println("<p>Here are your search results:</p>");
                    out.println("<p>" + rset.getString(2) + "</p>");
                    while (rset.next()){
                        out.println("<p>" + rset.getString(2) + "</p>");
                    }
                    rset.first();
                    out.println("<form action=\"showResults\">");
                    out.println("<p>Choose the item you would like to reserve:</p>");
                    out.println("<select name=\"item\">");
                    out.println("<option value=\"" + rset.getInt(1) + "\">" + rset.getString(2) + "</option>");
                    while (rset.next()) {
                        out.println("<option value=\"" + rset.getInt(1) + "\">" + rset.getString(2) + "</option>");
                    }
                    out.println("</select>");
                    out.println("<p>Enter your library card number to reserve:</p>");
                    out.println("<input type=\"text\" name=\"patron\"/>");
                    out.println("<input type=\"submit\" value=\"Place Hold\"/>");
                    out.println("</form>");
                }
            } catch (SQLException ex) {
                    out.println("<p>SQL Error: " + ex.getMessage() + "</p>");
            }
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        processRequest(request, response);
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
        // Hey there :)
    }// </editor-fold>

}
