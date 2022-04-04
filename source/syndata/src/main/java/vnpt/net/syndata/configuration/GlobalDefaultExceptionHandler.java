package vnpt.net.syndata.configuration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

// import vnpt.net.springmvc.service.sys.LogService;
// import vnpt.net.springmvc.utils.CustomException;
// import vnpt.net.springmvc.utils.EJson;
// import vnpt.net.springmvc.utils.RequestBodyJson;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
	public static final String DEFAULT_ERROR_VIEW = "error";

	// Handler Error Server Global
	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
		ModelAndView mav = new ModelAndView();
		if (e instanceof CustomException) {
			// Lỗi khi được trả về khi lỗi nghiệp vụ từ service
			CustomException errorCustom = (CustomException) e;
			if (errorCustom.getErrorJson() != null) {
				mav.addObject("content", errorCustom.getErrorJson());
				mav.setViewName(DEFAULT_ERROR_VIEW);
				return mav;
			}
		}

		// api return json
		if (request.getRequestURI().startsWith("/api/") || request.getRequestURI().startsWith("/public/")) {
			// return json to client
			mav.addObject("content", e.getLocalizedMessage());
			mav.setViewName(DEFAULT_ERROR_VIEW);
			return mav;
		}

		// trình duyệt
		mav.addObject("content", e.getLocalizedMessage());
		mav.setViewName(DEFAULT_ERROR_VIEW);
		return mav;
	}
}