package org.upsam.civicrm.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;

public class CiviCRMAndroidSpiceService extends
		JacksonSpringAndroidSpiceService {

	/* (non-Javadoc)
	 * @see com.octo.android.robospice.JacksonSpringAndroidSpiceService#createRestTemplate()
	 */
	@Override
	public RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // web services support json responses
        MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter() {

			/* (non-Javadoc)
			 * @see org.springframework.http.converter.json.MappingJacksonHttpMessageConverter#readInternal(java.lang.Class, org.springframework.http.HttpInputMessage)
			 */
			@Override
			protected Object readInternal(Class<?> clazz,
					final HttpInputMessage inputMessage) throws IOException,
					HttpMessageNotReadableException {
				HttpInputMessage hackedInputMessage = new HttpInputMessage() {
					
					@Override
					public HttpHeaders getHeaders() {
						return inputMessage.getHeaders();
					}
					
					@Override
					public InputStream getBody() throws IOException {
						return new ByteArrayInputStream(sanifyResponse(inputMessage));
					}
					// esto es para limpiar lo que devuelve CiviCRM...
					private byte[] sanifyResponse(final HttpInputMessage inputMessage)
							throws IOException {
						InputStream is = inputMessage.getBody();
						byte[] bytes = FileCopyUtils.copyToByteArray(is);
						String s = new String(bytes);
						String substring = s.substring(s.indexOf("{\"is_error\""));
						return substring.getBytes();
					}
				};
				return super.readInternal(clazz, hackedInputMessage);
			}
        	
        };
        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>(2);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(new MediaType("text", "javascript"));
        jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
        final List<HttpMessageConverter<?>> listHttpMessageConverters = restTemplate
            .getMessageConverters();

        listHttpMessageConverters.add(jsonConverter);
        restTemplate.setMessageConverters(listHttpMessageConverters);
        return restTemplate;
	}

}
