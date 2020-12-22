import java.io.*;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/CategoryADA")


public class CategoryADA extends HttpServlet {

  private String msg = null;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
    Utilities utility = new Utilities(request, pw);

    String action = request.getParameter("action");
    String super_cat = request.getParameter("super_category");
    String sub_cat = request.getParameter("sub_category");

    if(action.equalsIgnoreCase("add")){

      boolean ret = MySqlDataStoreUtilities.insertCategory(super_cat, sub_cat);
      if(ret){
        this.msg = "Successfully added new category.";
      }else{
        this.msg = "error adding new category.";
      }

    }else if(action.equalsIgnoreCase("delete")){

      boolean ret = MySqlDataStoreUtilities.deleteCategory(super_cat, sub_cat);
      if(ret){
        this.msg = "Successfully deleted category.";
      }else{
        this.msg = "error deleting category.";
      }
    }
    displayCategoryADAPage(request, response, pw);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
    Utilities utility = new Utilities(request, pw);

    if(utility.isLoggedin() && utility.user_type().equals("administrator")) {
			displayCategoryADAPage(request, response, pw);
    }else{
			response.sendRedirect("Login");
      return;
		}
	}

	protected void displayCategoryADAPage(HttpServletRequest request, HttpServletResponse response, PrintWriter pw)throws ServletException, IOException {

    Utilities utility = new Utilities(request, pw);
    HttpSession session = request.getSession(true);

    utility.printHtml("Head.html");
    utility.printAHeader();
    utility.printALeftNav();
	pw.print("<div class='container-fluid text-center'>");
			pw.print("<div class='row content'>");
    pw.print("<div class='w3-card-4'>");
		pw.print("<div class='w3-container'>");
		pw.print("<h2 style='background-color: #2f2f2f; color: white'>Add/Delete Category</h2>");
		if(this.msg != null){
		  pw.print("<h3>"+this.msg+"</h3>");
		  this.msg = null;
		}
		pw.print("</div>");
    pw.print("<form style='text-align: left 'class='w3-container' method='post' action='CategoryADA'>");
    pw.print("<p>");
    pw.print("<label><b>Super Category : </b></label>");
    pw.print("<input class='w3-input w3-round w3-border w3-light-grey ' style='width:50%' name='super_category' type='text' required></p>");
    pw.print("<p>");
    pw.print("<label ><b>Sub Category : </b></label>");
    pw.print("<input class='w3-input w3-round w3-border w3-light-grey' style='width:50%' name='sub_category' type='text' required></p>");
    pw.print("<p>");
	pw.print("<br>");
    pw.print("<input style='width: 150px' type='submit' class='w3-button w3-green w3-wide' name='action' value='Add'>");
    pw.print("<input style='width: 150px; margin-left:10px;' type='submit' class='w3-button w3-red w3-wide' name='action' value='Delete'>");
    pw.print("</p>");
    pw.print("</form>");
    pw.print("</div>");
 pw.print("</div>");
  pw.print("</div>");
	utility.printHtml("Footer.html");

	}

}
