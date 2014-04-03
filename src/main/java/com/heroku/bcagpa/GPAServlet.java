package com.heroku.bcagpa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
	private ArrayList<Grade> currentYear = new ArrayList<Grade>();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		try {
			parse(username, password);
		} catch (IOException ioe) {
			
		}
		calculate();
		
		ServletOutputStream out = resp.getOutputStream();
		//out.write(page.toString().getBytes());
		out.write(output.getBytes());
		out.flush();
		out.close();
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
				
				builder.append("[Credits] " + credits + "[Mods]" + mods + "[Subject]" + subject + "[Grades]" + first + "," + second + "," + third + "\n");
			}
		}
		this.output = builder.toString();
	}
	
	private double findCredits(String mods) {
		double numberOfMods = 0.0;
		double numberOfTimes = 0.0;
		String modsPattern = "\\d\\d-\\d\\d";
		String daysPattern = "\\((.*)\\)";
		
		String modsMatch = match(modsPattern, mods);
		if (modsMatch.equals("ERROR")) {
			modsMatch = "10";
		}
		String daysMatch = match(daysPattern, mods);
		daysMatch = daysMatch.substring(1, daysMatch.length()-1);
		if (modsMatch.length() == 2) {
			numberOfMods = 1;
		} else {
			double modsBegin = Double.parseDouble(modsMatch.split("-")[0]);
			double modsEnd = Double.parseDouble(modsMatch.split("-")[1]);
			
			numberOfMods = (modsEnd-modsBegin)+1.0;
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
				numberOfTimes += Double.parseDouble(day.split("-")[1]) - Double.parseDouble(day.split("-")[0]) + 1.0;
			} else {
				//error;
			}
		}
		return (numberOfMods*numberOfTimes)/2.0;
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
		if (grade.contains("P") || grade.contains("-") || grade.contains("?")) {
			return false;
		}
		return true;
	}
	
}
