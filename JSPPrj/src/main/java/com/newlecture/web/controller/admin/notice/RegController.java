package com.newlecture.web.controller.admin.notice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.service.NoticeService;

// multipart/form-data 형식일때 데이터를 받을 수 있는 설정
@MultipartConfig(fileSizeThreshold = 1024 * 1204, // 1메가 - 1메가부터는 디스크에 저장
		maxFileSize = 1024 * 1024 * 50, // 50메가 - 파일 하나당 최대 크기 제한
		maxRequestSize = 1024 * 1024 * 50 * 5 // 250메가 - 전체 요청의 크기 제한
)

@WebServlet("/admin/board/notice/reg")
public class RegController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/view/admin/board/notice/reg.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String isOpen = request.getParameter("open");

		// 다중 파일 받기
		Collection<Part> parts = request.getParts();
		StringBuilder builder = new StringBuilder();
		for (Part p : parts) {
			if (!p.getName().equals("file")) continue;
			// 파일이지만 실제 데이터를 전달하지 않는 빈 데이터일 경우
			if (p.getSize() == 0) continue;
			// 파일을 받기 위한 Part 자료형
			Part filePart = p;
			String fileName = filePart.getSubmittedFileName();
			builder.append(fileName);
			builder.append(",");
			
			InputStream fis = filePart.getInputStream();
			// "/upload/" 상대경로 -> 절대경로로 바꿔야함
			String realPath = request.getServletContext().getRealPath("/upload");
			
			// 경로 없을시 만들어줌
			File path = new File(realPath);
			if(!path.exists()) { // 존재여부 boolean값으로 줌
				path.mkdirs(); // 없는 부모 디렉토리까지 다 만들어줌
			}

			String filePath = realPath + File.separator + fileName; // 경로 구분 기호 역슬래시 File.separator
			// 출력스트림
			FileOutputStream fos = new FileOutputStream(filePath);
			// fis.read() ->1바이트 데이터를 읽음(정수형) 여러 번 읽기 위해 반복문 써야함
			int size;
			byte[] buf = new byte[1024]; // 한번에 많은 파일을 읽기 위해 버퍼 정의
			while ((size = fis.read(buf)) != -1) {
				fos.write(buf, 0, size); // 사이즈 길이 만큼 0번째부터 읽음
			}
			fos.close();
			fis.close();
		}
		// 마지막 쉼표 제거
		builder.delete(builder.length() - 1, builder.length()); // end index는 포함 X
		
		boolean pub = false;

		if (isOpen != null) {
			pub = true;
		}

		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setContent(content);
		notice.setPub(pub);
		notice.setWriterId("khc");
		notice.setFiles(builder.toString());

		NoticeService service = new NoticeService();
		int result = service.insertNotice(notice);

		response.sendRedirect("list");
	}
}
