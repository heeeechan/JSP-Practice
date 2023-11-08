package com.newlecture.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/spaghetti")
public class Spaghetti extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  int num = 0;
	  String num_ = request.getParameter("n");
	  if(num_ != null && !num_.equals("")) {
	  	num = Integer.parseInt(num_);
	  }
	  
	  String result;
	  if(num % 2 != 0) {
	  	result = "홀수";
	  } else {
	  	result = "짝수";
	  }
	  
	  // request에 result 값을 담음
	  request.setAttribute("result", result);
	  
	  String[] names = {"steve", "jobs"};
	  request.setAttribute("names", names);
	  
	  Map<String, Object> notice = new HashMap<>();
	  notice.put("id", 1);
	  notice.put("title", "좋아요!");
	  request.setAttribute("notice", notice);

	  // forward - 현재 진행중인 내용을 이어갈 때 포워딩
	  RequestDispatcher dispatcher = request.getRequestDispatcher("spaghetti.jsp");
	  dispatcher.forward(request, response);
	}
}
