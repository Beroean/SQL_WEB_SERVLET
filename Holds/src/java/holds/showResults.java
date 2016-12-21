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
@WebServlet(name = "showResults", urlPatterns = {"/showResults"})
public class showResults extends HttpServlet {
    
    
    private String newHold="insert into hold (patron,item) values(?,?)";
    private PreparedStatement newHoldStmt;
    private Statement stmt = null;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://localhost:3306/hold?user=root&password=cmsc250";
            Connection connection = DriverManager.getConnection(url);
            stmt = connection.createStatement();
            newHoldStmt = connection.prepareStatement(newHold);
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
            out.println("<title>Hold Place Results</title>");            
            out.println("</head>");
            out.println("<body>");
            try{
                String patronLibNum = request.getParameter("patron");
                int itemID = Integer.parseInt(request.getParameter("item"));
                String query = "select idpatron from patron where card =\'" + 
                        patronLibNum + "\'";
                ResultSet rset = stmt.executeQuery(query);
                int patronID = 0;
                if (rset.next()){
                    patronID = rset.getInt(1);
                }
                newHoldStmt.setInt(1, patronID);
                newHoldStmt.setInt(2, itemID);
                newHoldStmt.executeUpdate();
                out.println("<p>You hold was placed successfully</p>");
                String query1 = "select * from hold where item =" + itemID;
                ResultSet rset1 = stmt.executeQuery(query1);
                int i=0;
                while(rset1.next()){
                    i++;
                }
                out.println("<p>There are currently " + i + " holds placed "
                        + "on this item including the one just made.");
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
    }// </editor-fold>

}
