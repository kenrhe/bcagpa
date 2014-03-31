package com.example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class HelloServlet extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
		String HOME_URL = "https://ps01.bergen.org/public/home.html", GRADES_URL = "https://ps01.bergen.org/guardian/home.html";
		Connection.Response respo = Jsoup.connect(HOME_URL).method(Connection.Method.GET).execute();
		String pstoken = respo.parse().body().getElementsByAttributeValue("name", "pstoken").val();
		String contextData = respo.parse().body().getElementsByAttributeValue("name", "contextData").val();
		String serviceName="PS+Parent+Portal", credentialType="User+Id+and+Password+Credential", pcasServerUrl="/";
		String username = "hwarhe";
		String password = "9wg3Bg!";
		respo = Jsoup.connect(GRADES_URL)
				.data("pstoken",pstoken)
				.data("contextData",contextData)
				.data("serviceName",serviceName)
				.data("pcasServerUrl",pcasServerUrl)
				.data("credentialType",credentialType)
				.data("account",username)
				.data("ldappassword",password)
				.data("pw",Base64.sStringToHMACMD5(contextData, Base64.encodeBytes(Base64.MD5("password").getBytes())))
				.cookies(respo.cookies())
				.userAgent("Mozilla")
				.method(Connection.Method.POST)
				.execute();
		Document page = respo.parse();
        ServletOutputStream out = resp.getOutputStream();
        out.write(page.toString().getBytes());
        out.flush();
        out.close();
    }
}
