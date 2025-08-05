package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.beans.UserMessage;
import chapter6.dao.MessageDao;
import chapter6.dao.UserMessageDao;
import chapter6.logging.InitApplication;

public class MessageService {
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public MessageService() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	public void insert(Message message) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			connection = getConnection();
			new MessageDao().insert(connection, message);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {}.getClass().getEnclosingClass().getName() +
			" : " + e.toString(), e);
			throw e;
		} finally {
			close(connection);
		}
	}

	public void delete(int id) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			connection = getConnection();
			new MessageDao().delete(connection, id);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {}.getClass().getEnclosingClass().getName() +
			" : " + e.toString(), e);
			throw e;
		} finally {
			close(connection);
		}
	}

	public Message select(int messageId) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			connection = getConnection();
			Message message = new MessageDao().select(connection, messageId);
			commit(connection);
			return message;
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {}.getClass().getEnclosingClass().getName() +
			" : " + e.toString(), e);
			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {}.getClass().getEnclosingClass().getName() +
			" : " + e.toString(), e);
			throw e;
		} finally {
			close(connection);
		}
	}
	//ログインしているユーザーのメッセージ一覧を拾うためにDBと橋渡し
	public List<UserMessage> select(String userId, String start, String end) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		final int LIMIT_NUM = 1000;

		Connection connection = null;

		Date nowDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formatNowDate = sdf.format(nowDate);

		try {
			connection = getConnection();
			Integer id = null;

			//ユーザーIDが渡されたか(アカウント名のリンク踏んで来たか)確認
			if(!StringUtils.isEmpty(userId)) {
				id = Integer.parseInt(userId);
			}

			//開始日の入力有無によって分岐
			if(!StringUtils.isBlank(start)) {
				start += " 00:00:00";
			} else {
				start = "2020-01-01 00:00:00";
			}
			//終了日の入力有無によって分岐
			if(!StringUtils.isBlank(end)) {
				end += " 23:59:59";
			}
			else {
				end = formatNowDate;
			}
			//つぶやき一覧を条件に合わせてDBから拾ってくる
			List<UserMessage> messages = new UserMessageDao().select(connection, id, start, end, LIMIT_NUM);
			commit(connection);
			return messages;
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {}.getClass().getEnclosingClass().getName() +
			" : " + e.toString(), e);
			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {}.getClass().getEnclosingClass().getName() +
			" : " + e.toString(), e);
			throw e;
		} finally {
			close(connection);
		}
	}

	//更新したいメッセージをMessageDaoに送るためのメソッド
	public void update(Message message) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			connection = getConnection();
			new MessageDao().update(connection, message);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {}.getClass().getEnclosingClass().getName() +
			" : " + e.toString(), e);
			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {}.getClass().getEnclosingClass().getName() +
			" : " + e.toString(), e);
			throw e;
		} finally {
			close(connection);
		}
	}
}