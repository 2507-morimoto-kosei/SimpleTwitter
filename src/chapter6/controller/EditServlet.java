package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public EditServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
		" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<String>();

		String messageId = request.getParameter("messageId");

		//messageIDが「空」または「数字でない」ということを許容しない
		if (StringUtils.isBlank(messageId) || !messageId.matches("^[0-9]+$")) {
			errorMessages.add("不正なパラメータが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		Message message = new MessageService().select(Integer.parseInt(messageId));

		//存在しないメッセージIDを許容しない
		if (message == null) {
			errorMessages.add("不正なパラメータが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		//更新前のメッセージをDBから拾ってjspに渡す
		request.setAttribute("message", message);
		request.getRequestDispatcher("edit.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
		" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		List<String> errorMessages = new ArrayList<String>();

		Message message = new Message();
		message.setId(Integer.parseInt(request.getParameter("editTextId")));
		message.setText(request.getParameter("editText"));

		//更新後のメッセージ内容に問題がないか確認する
		if (!isValid(message, errorMessages)) {
			request.setAttribute("errorMessages", errorMessages);
			//書き途中のメッセージを、再度リクエストにセット、jspに渡す
			request.setAttribute("message", message);
			request.getRequestDispatcher("edit.jsp").forward(request, response);
			return;
		}
		new MessageService().update(message);
		response.sendRedirect("./");
	}

	private boolean isValid(Message message, List<String> errorMessages) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
		" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		String text = message.getText();

		if (StringUtils.isBlank(text)) {
			errorMessages.add("メッセージを入力してください");
		} else if (140 < text.length()) {
			errorMessages.add("140文字以下で入力してください");
		}
		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}
}