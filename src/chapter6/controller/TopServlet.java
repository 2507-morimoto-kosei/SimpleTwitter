package chapter6.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.Comment;
import chapter6.beans.User;
import chapter6.beans.UserComment;
import chapter6.beans.UserMessage;
import chapter6.logging.InitApplication;
import chapter6.service.CommentService;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/index.jsp" })
public class TopServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public TopServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		//ログインしているか確認。いればtrueを返しログインしている画面を表示するように準備
		boolean isShowMessageForm = false;
		User user = (User) request.getSession().getAttribute("loginUser");
		if (user != null) {
			isShowMessageForm = true;
		}

		//最初にトップが表示された時はnull、アカウント名のリンクが踏まれたらそのuserIDが来る
		String userId = request.getParameter("user_id");
		String start = request.getParameter("start");
		String end = request.getParameter("end");

		//ログインしているユーザーのつぶやき一覧を取得
		List<UserMessage> messages = new MessageService().select(userId, start, end);

		//DBから返信一覧を取得
		Comment comment = new Comment();
		List<UserComment> comments = new CommentService().select((comment.getMessageId()));

		//UserMessageをリクエストスコープにセット
		request.setAttribute("messages", messages);
		request.setAttribute("isShowMessageForm", isShowMessageForm);
		request.setAttribute("comments", comments);

		if (start != null && end != null) {
			request.setAttribute("start", start);
			request.setAttribute("end", end);
		}

		request.getRequestDispatcher("/top.jsp").forward(request, response);
	}
}