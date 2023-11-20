package com.newlecture.web.controller.admin.notice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.entity.NoticeView;
import com.newlecture.web.service.NoticeService;

@WebServlet("/admin/board/notice/list")
public class ListController extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] openIds = request.getParameterValues("open-id");
		String[] delIds = request.getParameterValues("del-id");
		String cmd = request.getParameter("cmd");
		// 게시글 id들
		String ids_ = request.getParameter("ids");
		String[] ids = ids_.trim().split(" ");
		
		NoticeService service = new NoticeService();
		
		switch (cmd) {
		case "일괄공개":
			for(String openId : openIds) {
				System.out.printf("open id : %s\n", openId);
			}
			// 배열을 리스트로 바꿈
			List<String> oids = Arrays.asList(openIds);
			// 전체목록에서 오픈id들을 빼서 안보여줄 id들만 얻음
			List<String> cids = new ArrayList(Arrays.asList(ids)); // id들 리스트로 얻어서 변수에 담아줌
			cids.removeAll(oids);
			System.out.println(Arrays.asList(ids));
			System.out.println(oids);
			System.out.println(cids);
			
			// Transaction(업무적단위, 논리적단위)
			// open과 close를 하나의 함수로 수행되게끔
			service.pubNoticeAll(oids, cids);
			break;

		case "일괄삭제":
			// delIds를 정수형 배열로 바꿔야함
			int[] ids1 = new int[delIds.length];
			for (int i = 0; i < delIds.length; i++) {
				ids1[i] = Integer.parseInt(delIds[i]);
			}
			// 삭제 된 개수 반환하도록
			int result = service.deleteNoticeAll(ids1);
			break;
		}
		
		// 공개나 삭제 작업 후 GET요청으로 list를 보여줘야함 -> Redirect
		response.sendRedirect("list");
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// list?f=title&q=a
		// 값을 입력하는 것이 선택사항이기 때문에 입력안했을 때를 고려해 임시변수 필요함
		String field_ = request.getParameter("f");
		String query_ = request.getParameter("q");
		// int형으로 정의하면 안됨. 입력안했을때 null을 받아야 하기 때문
		String page_ = request.getParameter("p");
		
		String field = "title";
		if(field_ != null && !field_.equals("")) {
			field = field_;
		}
		
		String query = "";
		if(query_ != null && !query_.equals("")) {
			query = query_;
		}
		
		int page = 1;
		if(page_ != null && !page_.equals("")) {
			page = Integer.parseInt(page_);
		}
		
		// 모델이 목록이기 때문에 List 객체 사용
		NoticeService service = new NoticeService();
		List<NoticeView> list = service.getNoticeList(field, query, page);
		
		// 마지막 페이지를 보여주기 위해 총 레코드 수 가져오는 로직
		int count = service.getNoticeCount(field, query);
		
		request.setAttribute("list", list);
		request.setAttribute("count", count);
		
		request.getRequestDispatcher("/WEB-INF/view/admin/board/notice/list.jsp").forward(request, response);
	}
}
