package vnpt.net.syndata.api;


import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vnpt.net.syndata.model.JwtRequestMode;
import vnpt.net.syndata.utils.EJson;
import vnpt.net.syndata.component.HttpClientComponent;
import vnpt.net.syndata.jwt.JwtTokenUtil;

@RestController
@CrossOrigin
public class AuthenticateApi {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private HttpClientComponent httpClient;

	@Value("${api.system.base.url}")
	private String apiSystemBaseUrl;

	@Value("${api.sys.login.url}")
	private String apiSysLogin;

	@RequestMapping(value = "/public/aut/generate-token", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequestMode authenticationRequest,
			HttpServletResponse response) throws Exception {

		EJson result = new EJson();

		// tạo tham số đẩy lên api
		EJson param = new EJson();
		param.put("username", authenticationRequest.getUsername());
		param.put("password", authenticationRequest.getPassword());

		ResponseEntity<String> rsLogin = null;
		try {
			// rsLogin = httpClient.postDataByApi(apiSystemBaseUrl + apiSysLogin, null, param.jsonString());
			// if (rsLogin.getStatusCode() == HttpStatus.OK) {
				//result.put("user", rsLogin.getBody());d

				UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
				String token = jwtTokenUtil.generateToken(userDetails);
				result.put("token", token);
			//}
		} catch (Exception e) {
			result.addError("", "Sai tài khoản hoặc mật khẩu");
				return ResponseEntity.badRequest().body(result.jsonString());
		}
		return ResponseEntity.ok(result.jsonString());
	}
}