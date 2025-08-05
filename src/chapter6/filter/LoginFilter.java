package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/setting", "/edit"})
public class LoginFilter implements Filter {
	public static String DEFAULT_ENCODING = "UTF-8";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
		FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		List<String> errorMessages = new ArrayList<String>();

		//セッション領域にログインユーザ情報があるか確認
		if (session.getAttribute("loginUser") != null) {
			chain.doFilter(request, response); // サーブレットを実行
		} else {
			errorMessages.add("ログインしてください");
			session.setAttribute("errorMessages", errorMessages);
			httpResponse.sendRedirect("./login");
		}
		return;
	}

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}
}
