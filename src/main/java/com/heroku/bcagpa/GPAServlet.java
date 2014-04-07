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
	private Document page;
	private String output;
	private ArrayList<Grade> tri1 = new ArrayList<Grade>();
	private ArrayList<Grade> tri2 = new ArrayList<Grade>();
	private ArrayList<Grade> tri3 = new ArrayList<Grade>();
	private double tri1GPA, tri2GPA, tri3GPA;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("/");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		tri1 = new ArrayList<Grade>();
		tri2 = new ArrayList<Grade>();
		tri3 = new ArrayList<Grade>();
		try {
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			parse(username, password);
			calculate();

			req.setAttribute("tri1GPA", round(tri1GPA, 3));
			req.setAttribute("tri2GPA", round(tri2GPA, 3));
			req.setAttribute("tri3GPA", round(tri3GPA, 3));
			req.setAttribute("yearGPA", round(findYearGPA(), 3));
			req.getRequestDispatcher("gpa.jsp").forward(req, resp);
		} catch (Exception e) {
			req.setAttribute("error", "Error: Please check your username and password. If your username/password is correct, please contact Kenneth hwarhe@bergen.org or message me on facebook.");
			req.getRequestDispatcher("index.jsp").forward(req,resp);
		}
	}

	private void parse(String username, String password) throws IOException {
		String HOME_URL = "https://ps01.bergen.org/public/home.html", GRADES_URL = "https://ps01.bergen.org/guardian/home.html";
		String serviceName = "PS+Parent+Portal", credentialType = "User+Id+and+Password+Credential", pcasServerUrl = "/";
		Connection.Response r = Jsoup.connect(HOME_URL)
				.method(Connection.Method.GET).execute();
		String pstoken = r.parse().body()
				.getElementsByAttributeValue("name", "pstoken").val();
		String contextData = r.parse().body()
				.getElementsByAttributeValue("name", "contextData").val();
		r = Jsoup
				.connect(GRADES_URL)
				.data("pstoken", pstoken)
				.data("contextData", contextData)
				.data("serviceName", serviceName)
				.data("pcasServerUrl", pcasServerUrl)
				.data("credentialType", credentialType)
				.data("account", username)
				.data("ldappassword", password)
				.data("pw",
						Base64.sStringToHMACMD5(contextData, Base64
								.encodeBytes(Base64.MD5("password").getBytes())))
				.cookies(r.cookies()).userAgent("Mozilla")
				.method(Connection.Method.POST).execute();
		this.page = r.parse();
	}

	private void calculate() {
		StringBuilder builder = new StringBuilder();
		// builder.append("<html>");
		builder.append("[Start]\n");
		Element table = page.select("table").first();
		for (int i = 0; i < table.select("tr").size(); i++) {
			Element row = table.select("tr").get(i);
			Elements column = row.select("td");
			if (column.size() > 10) {
				String mods = column.get(0).text();
				String subject = column.get(11).text();
				String first = column.get(12).text();
				String second = column.get(13).text();
				String third = column.get(14).text();
				double credits = 0.0;
				if (subject.contains("~")) {
					continue;
				}
				String[] modsArray = mods.split(" ");
				if (modsArray.length > 1) {
					for (int j = 0; j < modsArray.length; j++) {
						credits += findCredits(modsArray[j]);
					}
				} else {
					credits = findCredits(mods);
				}
				if (isGradeValid(first)) {
					tri1.add(new Grade(subject.split("\u00a0")[0], getGPA(first
							.split(" ")[0]), credits));
				}
				if (isGradeValid(second)) {
					tri2.add(new Grade(subject.split("\u00a0")[0],
							getGPA(second.split(" ")[0]), credits));
				}
				if (isGradeValid(third)) {
					tri3.add(new Grade(subject.split("\u00a0")[0], getGPA(third
							.split(" ")[0]), credits));
				}
				builder.append("[Credits]" + credits + "[Mods]" + mods
						+ "[Subject]" + subject + "[Grades]" + first + ","
						+ second + "," + third + "\n");
			}
		}
		builder.append("\n\ntri1:");
		for (int i = 0; i < tri1.size(); i++) {
			builder.append("\n" + tri1.get(i));
		}
		builder.append("\n\ntri2:");
		for (int i = 0; i < tri2.size(); i++) {
			builder.append("\n" + tri2.get(i));
		}
		builder.append("\n\ntri3:");
		for (int i = 0; i < tri3.size(); i++) {
			builder.append("\n" + tri3.get(i));
		}
		// builder.append("</html>");
		this.tri1GPA = findGPA(this.tri1);
		this.tri2GPA = findGPA(this.tri2);
		this.tri3GPA = findGPA(this.tri3);
		builder.append("\nTrimester 1 GPA: " + tri1GPA);
		builder.append("\nTrimester 2 GPA: " + tri2GPA);
		builder.append("\nTrimester 3 GPA: " + tri3GPA);

		this.output = builder.toString();
	}

	private double findGPA(ArrayList<Grade> gradeList) {
		double credits = 0.0;
		double equivalent = 0.0;
		for (int i = 0; i < gradeList.size(); i++) {
			Grade current = gradeList.get(i);
			credits += current.getCredits();
			equivalent += current.getCredits() * current.getGrade();
		}
		return equivalent / credits;
	}
	
	private double findYearGPA() {
		double credits = 0.0;
		double equivalent = 0.0;
		for (int i = 0; i < tri1.size(); i++) {
			Grade current = tri1.get(i);
			credits += current.getCredits();
			equivalent += current.getCredits() * current.getGrade();
		}
		for (int i = 0; i < tri2.size(); i++) {
			Grade current = tri2.get(i);
			credits += current.getCredits();
			equivalent += current.getCredits() * current.getGrade();
		}
		for (int i = 0; i < tri3.size(); i++) {
			Grade current = tri3.get(i);
			credits += current.getCredits();
			equivalent += current.getCredits() * current.getGrade();
		}
		
		return equivalent/credits;
	}

	private double findCredits(String mods) {
		if (mods.equals("25-27(M,R)") || mods.equals("25-27(T,F)")
				|| mods.equals("04-09(W)")) {
			return 1.0;
		}
		double numberOfMods = 0.0;
		double numberOfTimes = 0.0;
		String modsPattern = "\\d\\d-\\d\\d";
		String daysPattern = "\\((.*)\\)";

		String modsMatch = match(modsPattern, mods);
		if (modsMatch.equals("ERROR")) {
			modsMatch = "10";
		}
		String daysMatch = match(daysPattern, mods);
		daysMatch = daysMatch.substring(1, daysMatch.length() - 1);
		if (modsMatch.length() == 2) {
			numberOfMods = 1;
		} else {
			double modsBegin = Double.parseDouble(modsMatch.split("-")[0]);
			double modsEnd = Double.parseDouble(modsMatch.split("-")[1]);

			numberOfMods = (modsEnd - modsBegin) + 1.0;
		}

		String[] days = daysMatch.split(",");
		for (int i = 0; i < days.length; i++) {
			if (days[i].length() == 1) {
				numberOfTimes += 1.0;
			} else if (days[i].length() == 3) {
				String day = days[i];
				day = day.replaceAll("M", "01");
				day = day.replaceAll("T", "02");
				day = day.replaceAll("W", "03");
				day = day.replaceAll("R", "04");
				day = day.replaceAll("F", "05");
				numberOfTimes += Double.parseDouble(day.split("-")[1])
						- Double.parseDouble(day.split("-")[0]) + 1.0;
			} else {
				// error;
			}
		}
		return (numberOfMods * numberOfTimes) / 2.0;
	}

	private String match(String pattern, String line) {
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(line);
		if (matcher.find()) {
			return matcher.group(0);
		}
		return "ERROR";
	}

	private boolean isGradeValid(String grade) {
		if (grade.contains("P") || grade.contains("--")
				|| grade.contains("\u00a0")) {
			return false;
		}
		return true;
	}

	private double getGPA(String grade) {
		if (grade.equals("A"))
			return 4.0;
		else if (grade.equals("A-"))
			return 3.8;
		else if (grade.equals("B+"))
			return 3.33;
		else if (grade.equals("B"))
			return 3.0;
		else if (grade.equals("B-"))
			return 2.8;
		else if (grade.equals("C+"))
			return 2.33;
		else if (grade.equals("C"))
			return 2.0;
		else if (grade.equals("C-"))
			return 1.8;
		else if (grade.equals("D+"))
			return 1.33;
		else if (grade.equals("D"))
			return 1.0;
		else
			return 0.0;
	}
	
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

}
