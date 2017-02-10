package infinitefire.project.helpers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Options.Buffer;

import infinitefire.project.domain.Ownerable;
import infinitefire.project.domain.User;
import infinitefire.project.utils.HttpSessionUtils;

@Component
public class OwnerHelper implements Helper<Object> {
	private static final Logger log = LoggerFactory.getLogger(OwnerHelper.class);

	public static final String NAME = "owner";

	@Override
	public Object apply(final Object context, final Options options) throws IOException {
		Buffer buffer = options.buffer();
		User loginUser = getLoginUser(options);
		if (loginUser.isGuestUser()) {
		    buffer.append(options.inverse());
		    return buffer;
		}
		
		log.debug("context : {}", context);
		Ownerable ownerable = (Ownerable)context;
		if (ownerable.isOwner(loginUser)) {
			buffer.append(options.fn());
		} else {
			buffer.append(options.inverse());
		}
		return buffer;
	}

    private User getLoginUser(final Options options) {
        Object loginUser = options.get(HttpSessionUtils.USER_SESSION_KEY);
		if (loginUser == null) {
		    return User.GUEST_USER;
		}
		return (User)loginUser;
    }
}
