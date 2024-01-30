package sunuser.mylife.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UrlUtil {
	public String getBaseUrl(HttpServletRequest request) {
		String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
			.replacePath(null)
			.build()
			.toUriString();

		return baseUrl;
	}
}
