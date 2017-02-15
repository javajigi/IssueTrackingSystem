package infinitefire.project.utils;

import javax.servlet.http.HttpSession;
import infinitefire.project.domain.User;

public class HttpSessionUtils {
	public static final String USER_SESSION_KEY = "sessionedUser";

	public static boolean isLoginUser(HttpSession session) {
		Object sessionedUser = session.getAttribute(USER_SESSION_KEY);
		if (sessionedUser == null) {
			return false;
		}
		return true;
	}

	// TODO 메소드를 사용하는 것이 없음. 필요 없으면 제거.
	public static User getUserFromSession(HttpSession session) {
		if (!isLoginUser(session)) {
			return null;
		}

		return (User) session.getAttribute(USER_SESSION_KEY);
	}
}
