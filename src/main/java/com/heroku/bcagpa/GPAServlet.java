package com.heroku.bcagpa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
				if (!isValid(subject, new String[]{first,second,third})) {
					continue;
				}
				builder.append("[Mods]" + mods + "[Subject]" + subject + "[Grades]" + first + "," + second + "," + third + "\n");
			}
		}
		this.output = builder.toString();
	}
	
	private double findCredits(String mods) {
		return 0.0;
	}
	
	private boolean isValid(String subject, String[] gradesList) {
		if (subject.contains("~")) {
			return false;
		}
		for (int i = 0; i < gradesList.length; i++) {
			if (gradesList[i].contains("-") || gradesList[i].contains("?") || gradesList[i].contains("P")) {
				return false;
			}
		}
		return true;
	}
	
}
