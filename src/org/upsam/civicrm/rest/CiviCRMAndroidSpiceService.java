package org.upsam.civicrm.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;

public class CiviCRMAndroidSpiceService extends
		JacksonSpringAndroidSpiceService {
	/**
	 * Sobreescribimos el número de tareas simultáneas a ejecutar
	 */
	private final int NUM_THREADS_MAX = 3;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.octo.android.robospice.JacksonSpringAndroidSpiceService#
	 * createRestTemplate()
	 */
	@Override
	public RestTemplate createRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		// web services support json responses
		MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>(2);
		supportedMediaTypes.add(MediaType.APPLICATION_JSON);
		supportedMediaTypes.add(new MediaType("text", "javascript"));
		supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
		jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
		// binary (eg: images)
		ByteArrayHttpMessageConverter bArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
		FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
		final List<HttpMessageConverter<?>> listHttpMessageConverters = restTemplate
				.getMessageConverters();

		listHttpMessageConverters.add(formHttpMessageConverter);
		listHttpMessageConverters.add(jsonConverter);
		listHttpMessageConverters.add(bArrayHttpMessageConverter);
		restTemplate.setMessageConverters(listHttpMessageConverters);
		return restTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.octo.android.robospice.SpiceService#getThreadCount()
	 */
	@Override
	public int getThreadCount() {
		return this.NUM_THREADS_MAX;
	}

}
