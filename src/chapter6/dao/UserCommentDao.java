package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.UserComment;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class UserCommentDao {
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public UserCommentDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

//	public List<UserComment> select(Connection connection, Integer id, int num)
	public List<UserComment> select(Connection connection, int num) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			//UsersテーブルとcommentsテーブルSQL組み立て
			sql.append("SELECT ");
			sql.append("    comments.id as id, ");
			sql.append("    comments.text as text, ");
			sql.append("    comments.user_id as user_id, ");
			sql.append("    comments.message_id as message_id, ");
			sql.append("    users.account as account, ");
			sql.append("    users.name as name, ");
			sql.append("    comments.created_date as created_date ");
			sql.append("FROM comments ");
			sql.append("INNER JOIN users ");
			sql.append("ON comments.user_id = users.id ");
//			if (id != null) {
//				sql.append("WHERE ");
//				sql.append("user_id = ? ");
//			}
			sql.append("ORDER BY created_date DESC limit " + num);

			ps = connection.prepareStatement(sql.toString());

//			if (id != null) {
//				ps.setInt(1, id);
//			}

			//SQLを実行、ResultSet型に入る
			ResultSet rs = ps.executeQuery();

			List<UserComment> comments = toUserComments(rs);
			return comments;
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {}.getClass().getEnclosingClass().getName() +
			" : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<UserComment> toUserComments(ResultSet rs) throws SQLException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		List<UserComment> comments = new ArrayList<UserComment>();
		try {
			//UserCommentのフィールドに値をセットしていく
			while (rs.next()) {
				UserComment comment = new UserComment();
				comment.setId(rs.getInt("id"));
				comment.setUserId(rs.getInt("user_id"));
				comment.setMessageId(rs.getInt("message_id"));
				comment.setAccount(rs.getString("account"));
				comment.setName(rs.getString("name"));
				comment.setText(rs.getString("text"));
				comment.setCreatedDate(rs.getTimestamp("created_date"));
				comments.add(comment);
			}
			return comments;
		} finally {
			close(rs);
		}
	}
}
