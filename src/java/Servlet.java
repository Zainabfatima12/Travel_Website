import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            out.println("<h2>Welcome to Servlet</h2>");

            // Retrieve form parameters
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String message = request.getParameter("message");

            // Debugging output
            out.println("<h3>Debugging Output</h3>");
            out.println("<p>Username: " + (username == null ? "NULL" : username) + "</p>");
            out.println("<p>Email: " + (email == null ? "NULL" : email) + "</p>");
            out.println("<p>Phone: " + (phone == null ? "NULL" : phone) + "</p>");
            out.println("<p>Message: " + (message == null ? "NULL" : message) + "</p>");

            // Validate form parameters
            if (username == null || username.isEmpty() ||
                email == null || email.isEmpty() ||
                phone == null || phone.isEmpty() ||
                message == null || message.isEmpty()) {
                out.println("All fields are required.");
                return;
            }

            // Connect to the database and execute the query
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/traveldb", "zainab", "zainabsimba")) {
                    String sql = "INSERT INTO contact (username, email, phone, message) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, username);
                        ps.setString(2, email);
                        ps.setString(3, phone);
                        ps.setString(4, message);

                        int i = ps.executeUpdate();
                        if (i > 0) {
                            out.print("You are successfully registered....");
                        } else {
                            out.print("Registration failed, please try again.");
                        }
                    }
                }
            } catch (ClassNotFoundException | SQLException ex) {
                out.println("Registration failed due to an error: " + ex.getMessage());
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Registration Servlet";
    }
}
