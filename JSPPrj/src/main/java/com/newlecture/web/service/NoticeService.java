package com.newlecture.web.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.entity.NoticeView;

public class NoticeService {
	// Admin 페이지를 위한 서비스 목록 추가
	public int removeNoticeAll(int[] ids){
		return 0;
	}
	public int pubNoticeAll(int[] ids){
		return 0;
	}

	public int insertNotice(Notice notice) {

		int result = 0;
		// 오라클에서 ID 자동으로 들어가는 시퀀스 설정해둠
		String sql = "INSERT INTO NOTICE (TITLE, CONTENT, WRITER_ID, PUB) VALUES(?, ?, ?, ?)";
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(url, "test", "720044");
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, notice.getTitle());
			st.setString(2, notice.getContent());
			st.setString(3, notice.getWriterId());
			st.setBoolean(4, notice.getPub());

			result = st.executeUpdate();

			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int deleteNotice(int id){
		return 0;
	}
	
	public int updateNotice(Notice notice){
		return 0;
	}
	
	public List<Notice> getNoticeNewestList(){
		return null;
	}
	
	// notice 페이지를 위한 서비스
	// 이름이 같은 함수는 기능이 같은 함수임 - 파라미터가 제일 많은 걸 구현해야 다른 것들도 사용
	public List<NoticeView> getNoticeList() {
		
		return getNoticeList("title", "", 1);
	}
	
	public List<NoticeView> getNoticeList(int page) {
		
		return getNoticeList("title", "", page);
	}
	
	public List<NoticeView> getNoticeList(String field/* TITLE, WRITER_ID */, String query/* A */, int page) {
		List<NoticeView> list = new ArrayList<>();
		
		String sql = "SELECT * FROM ("
				+ "    SELECT ROWNUM NUM, N.* "
				+ "    FROM (SELECT * FROM NOTICE_VIEW WHERE " + field + " LIKE ? ORDER BY REGDATE DESC) N"
				+ ") "
				+ "WHERE NUM BETWEEN ? AND ?";
		// field는 문자열 그대로 전달해줘야하기 때문에 st.setString아니고 덧셈연산자로 그대로 넣어줌
		// 시작페이지는 1, 11, 21, 31 ... -> an = 1 + (page - 1) * 10
		// 끝페이지는 10, 20, 40, 40 ... -> page * 10
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(url, "test", "720044");
			// 물음표가 포함된 쿼리문 - sql을 먼저 받아서 처리
			PreparedStatement st = con.prepareStatement(sql);
			// 몇번째 물음표에 어떤 값을 넣을건지
			st.setString(1, "%" + query + "%");
			st.setInt(2, 1 + (page - 1) * 10);
			st.setInt(3, page * 10);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID");
				String title = rs.getString("TITLE");
			  String writerId = rs.getString("WRITER_ID");
			  Date regDate = rs.getDate("REGDATE");
			  String hit = rs.getString("HIT");
			  String files = rs.getString("FILES");
			  // String content = rs.getString("CONTENT");
			  int cmtCount = rs.getInt("CMT_COUNT");
			  boolean pub = rs.getBoolean("PUB");
			  
			  NoticeView notice = new NoticeView(id, title, writerId, regDate, hit, files, pub, cmtCount);
			  // while문 돌면서 객체 만들때마다 담아줌
			  list.add(notice);
			}

			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Integer getNoticeCount() {
		
		return getNoticeCount("title", "");
	}
	
	public Integer getNoticeCount(String field, String query) {
		
		int count = 0;
		
		String sql = "SELECT COUNT(ID) COUNT FROM ("
				+ "    SELECT ROWNUM NUM, N.* "
				+ "    FROM (SELECT * FROM NOTICE WHERE " + field + " LIKE ? ORDER BY REGDATE DESC) N"
				+ ") ";
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(url, "test", "720044");
			// 물음표가 포함된 쿼리문 - sql을 먼저 받아서 처리
			PreparedStatement st = con.prepareStatement(sql);
			// 몇번째 물음표에 어떤 값을 넣을건지
			st.setString(1, "%" + query + "%");
			ResultSet rs = st.executeQuery();

			// 정수형 데이터 하나 (갯수) sql문에서 별칭 붙인 count
			if(rs.next()) {
				count = rs.getInt("count");
			}

			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public Notice getNotice(int id) {
		Notice notice = null;

		String sql = "SELECT * FROM NOTICE WHERE ID = ?";

		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(url, "test", "720044");
			// 물음표가 포함된 쿼리문 - sql을 먼저 받아서 처리
			PreparedStatement st = con.prepareStatement(sql);
			// 몇번째 물음표에 어떤 값을 넣을건지
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			
			if (rs.next()) {
				int nid = rs.getInt("ID");
				String title = rs.getString("TITLE");
				String writerId = rs.getString("WRITER_ID");
				Date regDate = rs.getDate("REGDATE");
				String hit = rs.getString("HIT");
				String files = rs.getString("FILES");
				String content = rs.getString("CONTENT");
				boolean pub = rs.getBoolean("PUB");

				notice = new Notice(nid, title, writerId, regDate, hit, files, content, pub);
			}

			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return notice;
	}
	
	public Notice getNextNotice(int id) {
		Notice notice = null;
		
		String sql = "SELECT * FROM NOTICE "
				+ "WHERE ID = ("
				+ "    SELECT ID FROM NOTICE "
				+ "    WHERE REGDATE > (SELECT REGDATE FROM NOTICE WHERE ID = ?) "
				+ "    AND ROWNUM = 1 "
				+ ")";
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(url, "test", "720044");
			// 물음표가 포함된 쿼리문 - sql을 먼저 받아서 처리
			PreparedStatement st = con.prepareStatement(sql);
			// 몇번째 물음표에 어떤 값을 넣을건지
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			
			if (rs.next()) {
				int nid = rs.getInt("ID");
				String title = rs.getString("TITLE");
				String writerId = rs.getString("WRITER_ID");
				Date regDate = rs.getDate("REGDATE");
				String hit = rs.getString("HIT");
				String files = rs.getString("FILES");
				String content = rs.getString("CONTENT");
				boolean pub = rs.getBoolean("PUB");

				notice = new Notice(nid, title, writerId, regDate, hit, files, content, pub);
			}

			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return notice;
	}
	
	public Notice getPrevNotice(int id) {
		Notice notice = null;
		
		String sql = "SELECT ID FROM (SELECT * FROM NOTICE ORDER BY REGDATE DESC) "
				+ "    WHERE REGDATE < (SELECT REGDATE FROM NOTICE WHERE ID = ?) "
				+ "    AND ROWNUM = 1";
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(url, "test", "720044");
			// 물음표가 포함된 쿼리문 - sql을 먼저 받아서 처리
			PreparedStatement st = con.prepareStatement(sql);
			// 몇번째 물음표에 어떤 값을 넣을건지
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			
			if (rs.next()) {
				int nid = rs.getInt("ID");
				String title = rs.getString("TITLE");
				String writerId = rs.getString("WRITER_ID");
				Date regDate = rs.getDate("REGDATE");
				String hit = rs.getString("HIT");
				String files = rs.getString("FILES");
				String content = rs.getString("CONTENT");
				boolean pub = rs.getBoolean("PUB");

				notice = new Notice(nid, title, writerId, regDate, hit, files, content, pub);
			}

			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return notice;
	}
	
	public int deleteNoticeAll(int[] ids) {
		
		int result = 0;
		
		String params = "";
		
		for (int i = 0; i < ids.length; i++) {
			params += ids[i];
			if (i < ids.length - 1) {
				params += ",";
			}
		}
		
		String sql = "DELETE NOTICE WHERE ID IN (" + params + ")";
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(url, "test", "720044");
			Statement st = con.createStatement();
			
			result = st.executeUpdate(sql);
			
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
