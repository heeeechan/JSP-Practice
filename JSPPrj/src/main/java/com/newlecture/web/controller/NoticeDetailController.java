package com.newlecture.web.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.newlecture.web.entity.Notice;

@WebServlet("/notice/detail")
public class NoticeDetailController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 쿼리의 ID 값을 주기 위해 정의
		int id = Integer.parseInt(request.getParameter("id"));

		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String sql = "SELECT * FROM NOTICE WHERE ID = ?";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(url, "TEST", "720044");
			// 쿼리문에서 물음표 해당하는 내용을 채우기 위해 prepare하는 객체
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, id); // 첫번째 물음표에 id 값을 넣겠다
			ResultSet rs = st.executeQuery(); // 서버쪽의 실행 결과를 가져오기 위한 객체

			rs.next();

			// Model
			String title = rs.getString("TITLE");
			String writerId = rs.getString("WRITER_ID");
			Date regDate = rs.getDate("REGDATE");
			String hit = rs.getString("HIT");
			String files = rs.getString("FILES");
			String content = rs.getString("CONTENT");  
			
			Notice notice = new Notice(id, title, writerId, regDate, hit, files, content);
			request.setAttribute("notice", notice);
			
			/*
			request.setAttribute("title", title);
			request.setAttribute("writerId", writerId);
			request.setAttribute("regDate", regDate);
			request.setAttribute("hit", hit);
			request.setAttribute("files", files);
			request.setAttribute("content", content);
			*/
			
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// 리디렉션, 포워딩 중 포워딩 해야함- 포워딩하려면 dispatcher가 필요함
		request.getRequestDispatcher("/WEB-INF/view/notice/detail.jsp").forward(request, response);
	}
}
