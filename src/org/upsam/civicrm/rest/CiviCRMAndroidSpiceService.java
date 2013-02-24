package org.upsam.civicrm.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
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
        MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();
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
