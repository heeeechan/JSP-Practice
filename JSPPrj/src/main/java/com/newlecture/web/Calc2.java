package com.newlecture.web;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/calc2")
public class Calc2 extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 어플리케이션 객체 생성
		ServletContext application = request.getServletContext();
		// 세션 객체 생성
		HttpSession session = request.getSession();
		// 쿠키
		Cookie[] cookies = request.getCookies();

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		String v_ = request.getParameter("v");
		String op = request.getParameter("operator");

		int v = 0;
		if (!v_.equals("")) {
			v = Integer.parseInt(v_);
		}

		if (op.equals("=")) { // 계산
			// int x = (Integer) application.getAttribute("value");
			// int x = (Integer) session.getAttribute("value");
			int x = 0;
			for (Cookie c : cookies) {
				if (c.getName().equals("value")) {
					x = Integer.parseInt(c.getValue());
					break;
				}
			}

			int y = v;
			// String operator = (String) application.getAttribute("op");
			// String operator = (String) session.getAttribute("op");
			String operator = "";

			for (Cookie c : cookies) {
				if (c.getName().equals("op")) {
					operator = c.getValue();
					break;
				}
			}
			int result = 0;

			if (operator.equals("+")) {
				result = x + y;
			} else {
				result = x - y;
			}
			response.getWriter().printf("result is %d\n", result);
		} else { // 저장
			// application.setAttribute("value", v);
			// application.setAttribute("op", op);

			// session.setAttribute("value", v);
			// session.setAttribute("op", op);

			// 쿠키값은 반드시 URL로 사용할 수 있는 문자형으로 보내야함
			Cookie valueCookie = new Cookie("value", String.valueOf(v));
			Cookie opCookie = new Cookie("op", op);
			// 해당 URL만 쿠키를
			valueCookie.setPath("/calc2");
			valueCookie.setMaxAge(24 * 60 * 60); // 24시간 동안 쿠키 유지
			opCookie.setPath("/calc2");
			response.addCookie(valueCookie);
			response.addCookie(opCookie);

			response.sendRedirect("calc2.html");
		}
	}
}