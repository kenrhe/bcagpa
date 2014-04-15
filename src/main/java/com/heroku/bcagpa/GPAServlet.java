package com.heroku.bcagpa;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GPAServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("/");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			GPA user = new GPA(username, password);
			req.setAttribute("tri1GPA", round(user.getTriOneGPA(), 3));
			req.setAttribute("tri2GPA", round(user.getTriTwoGPA(), 3));
			req.setAttribute("tri3GPA", round(user.getTriThreeGPA(), 3));
			req.setAttribute("yearGPA", round(user.getYearGPA(), 3));
			req.getRequestDispatcher("gpa.jsp").forward(req, resp);
			System.out.println("Fulfilled request for user: " + username);
		} catch (Exception e) {
			req.setAttribute("error", "Error: Please check your username and password.");
			req.getRequestDispatcher("index.jsp").forward(req,resp);
		}
	}
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

}
