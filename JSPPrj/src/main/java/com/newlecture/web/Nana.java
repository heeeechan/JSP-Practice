package com.newlecture.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hi")
public class Nana extends HttpServlet {
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 서버에서 request 내용을 UTF-8로 읽겠다
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		// null이 아닌지 확인하기 위해 임시변수 사용
		String cnt_ = request.getParameter("cnt");

		// null이거나 빈 문자열이면 기본 값으로 100번 인사 반환
		int cnt = 100;
		if (cnt_ != null && !cnt_.equals("")) {
			cnt = Integer.parseInt(cnt_);
		}

		for (int i = 0; i < cnt; i++) {
			out.println((i + 1) + ": 안녕 Servlet!!</br>");
		}
	}
}