package com.example;

import java.io.IOException;
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

public class HelloServlet extends HttpServlet {
	private boolean maintenance = true;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		*/
		String HOME_URL = "https://ps01.bergen.org/public/home.html", GRADES_URL = "https://ps01.bergen.org/guardian/home.html";
		Connection.Response respo = Jsoup.connect(HOME_URL)
				.method(Connection.Method.GET).execute();
		String pstoken = respo.parse().body()
				.getElementsByAttributeValue("name", "pstoken").val();
		String contextData = respo.parse().body()
				.getElementsByAttributeValue("name", "contextData").val();
		String serviceName = "PS+Parent+Portal", credentialType = "User+Id+and+Password+Credential", pcasServerUrl = "/";
		String username = "hwarhe";
		String password = "9wg3Bg!";
		respo = Jsoup
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
				.cookies(respo.cookies()).userAgent("Mozilla")
				.method(Connection.Method.POST).execute();
		Document page = respo.parse();
		
		StringBuilder builder = new StringBuilder();
		Element table = page.select("table").first();
		
		Element t = page.select("table").first();
		Iterator<Element> iterator = t.select("tr").iterator();
		while (iterator.hasNext()) {
			builder.append(iterator.next() + "\n");
		}
		
		for (Element row : table.select("tr")) {
			builder.append("[new row]");
			for (Element column : row.select("td")) {
				builder.append(column + "<<");
			}
		}
		
		builder.append("<br><br>");
		builder.append("[The name is Stinky Bob]");
		for (int i = 0; i < table.select("tr").size(); i++) {
			Element cRow = table.select("tr").get(i);
			Elements subject = cRow.select("td");
			if (subject.size() > 10) {
				builder.append("<br>" + subject.get(11).toString() + " GRADES:" + subject.get(12).text() + "," + subject.get(13).text() + "," + subject.get(14).text());
			}
			
		}
		String contents = builder.toString();
		
		ServletOutputStream out = resp.getOutputStream();
		out.write(page.toString().getBytes());
		out.write("\n<p><b>Hello!</b></p>".getBytes());
		out.write(contents.getBytes());
		out.flush();
		out.close();
		//hello!
	}

	private void calculate() {
		
	}
	
	protected void display() {
		
	}
	
	private double getGPA(String grade) {
		if (grade.equals("A")) return 4.0;
		else if (grade.equals("A-")) return 3.8;
		else if (grade.equals("B+")) return 3.33;
		else if (grade.equals("B")) return 3.0;
		else if (grade.equals("B-")) return 2.8;
		else if (grade.equals("C+")) return 2.33;
		else if (grade.equals("C")) return 2.0;
		else if (grade.equals("C-")) return 1.8;
		else if (grade.equals("D+")) return 1.33;
		else if (grade.equals("D")) return 1.0;
		else return 0.0;
	}
}
