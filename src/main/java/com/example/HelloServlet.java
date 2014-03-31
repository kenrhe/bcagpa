package com.example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class HelloServlet extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
		/*
        ServletOutputStream out = resp.getOutputStream();
        
        out.write("Hello Heroku".getBytes());
        out.flush();
        out.close();
        */
		String URL = "https://ps01.bergen.org/public/home.html";
		WebClient webClient = new WebClient();
		webClient.setThrowExceptionOnFailingStatusCode(false);
		webClient.setThrowExceptionOnScriptError(false);
		HtmlPage page = webClient.getPage(URL);
		WebResponse response = page.getWebResponse();
		String content = response.getContentAsString();

		HtmlForm form = page.getFormByName("LoginForm");
		HtmlButton button = (HtmlButton) form.getElementById("btn-enter");
		HtmlTextInput username = form.getInputByName("account");
		HtmlPasswordInput password = form.getInputByName("pw");
		username.setValueAttribute("hwarhe");
		password.setValueAttribute("9wg3Bg!");
		//HtmlPage landing = button.click();
		
		ServletOutputStream output = resp.getOutputStream();
		String testcase = "\nTest case: " + content;
		output.write(testcase.getBytes());
		output.flush();
		output.close();
		webClient.closeAllWindows();
    }
}
