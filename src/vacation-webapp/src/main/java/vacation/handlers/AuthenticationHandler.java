package vacation.handlers;

import java.io.IOException;

import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

import vacation.UserSessionBean;

public class AuthenticationHandler implements PhaseListener {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserSessionBean session;

	@Override
	public void afterPhase(PhaseEvent arg0) {
		FacesContext context = arg0.getFacesContext();
		if (isProtectedPath() && session != null && !session.isAuthenticated()) {
			String redirectLogIn = "/index";
			final NavigationHandler navigationHandler = context.getApplication().getNavigationHandler();
			// the sessionExpired request parameter should have been set by the CustomExceptionHandler
			navigationHandler.handleNavigation(context, null, redirectLogIn);
		}
	}

	@Override
	public void beforePhase(PhaseEvent arg0) {
		// do-nothing
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

	public void redirect(final String uri) throws IOException {
		redirect(uri, false);
	}

	public void redirect(final String uri, final boolean invalidateSession) throws IOException {
		ExternalContext ec = getExternalContext();
		if (invalidateSession) {
			ec.invalidateSession();
		}
		ec.redirect(uri);
	}

	public static boolean isProtectedPath() {
		String requestPath = getExternalContext().getRequestServletPath();
		// /index.xhtml is the login URL. Should be unprotected to allow basic authentication.
		return !"/index.xhtml".equals(requestPath);
	}
	
	private static ExternalContext getExternalContext() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		return ec;
	}

}
