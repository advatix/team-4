package com.advatix.partner.commons.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.advatix.partner.commons.logger.Logger;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

/**
 * <h1>Examples</h1>
 * <p/>
 * <span style="color:yellow">Find by id</span>
 * <p>
 * UserDto userDto = restTemplateHelper.getForEntity(UserDto.class,
 * "http://localhost:8080/users/{id}", id);
 * </p>
 *
 * <span style="color:yellow">Find all</span>
 * <p>
 * List<T> userDto = restTemplateHelper.getForList(UserDto.class, "http://localhost:8080/users");
 * </p>
 * 
 * <span style="color:yellow">Save</span>
 * <p>
 * UserDto userDto = restTemplateHelper.postForEntity(UserDto.class, "http://localhost:8080/users",
 * userDto);
 * </p>
 * 
 * <span style="color:yellow">Update</span>
 * <p>
 * UserDto userDto = restTemplateHelper.putForEntity(UserDto.class,
 * "http://localhost:8080/users/{id}", userDto, id);
 * </p>
 * 
 * <span style="color:yellow">Delete</span>
 * <p>
 * restTemplateHelper.delete("http://localhost:8080/users/{id}", id);
 * </p>
 *
 */
@Component
public class RestTemplateHelper {

  private static final Logger LOGGER = Logger.getLogger(RestTemplateHelper.class);

  private RestTemplate restTemplate;

  private ObjectMapper objectMapper;

  private static final String NOT_DATA_FOUND_LOG_FORMAT = "No data found {}";

  private Supplier<ClientHttpRequestFactory> requestFactorySupplier =
      () -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());

  @Autowired
  public RestTemplateHelper(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
    restTemplateBuilder.requestFactory(requestFactorySupplier);
    this.restTemplate = restTemplateBuilder.build();
    // this.restTemplate
    // .setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
    this.objectMapper = objectMapper;
  }

  public <T> T getForEntity(Class<T> clazz, String url, Object... uriVariables) {
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, uriVariables);
      JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
      return readValue(response, javaType);
    } catch (HttpClientErrorException exception) {
      if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
        LOGGER.info(NOT_DATA_FOUND_LOG_FORMAT, url);
      } else {
        LOGGER.info("rest client exception", exception.getMessage());
      }
    }
    return null;
  }

  public <T> List<T> getForList(Class<T> clazz, String url, Object... uriVariables) {
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, uriVariables);
      CollectionType collectionType =
          objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
      return readValue(response, collectionType);
    } catch (HttpClientErrorException exception) {
      if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
        LOGGER.info(NOT_DATA_FOUND_LOG_FORMAT, url);
      } else {
        LOGGER.info("rest client exception", exception.getMessage());
      }
    }
    return new ArrayList<>();
  }

  public <T, R> T postForEntity(Class<T> clazz, String url, R body, Object... uriVariables) {
    HttpEntity<R> request = new HttpEntity<>(body);
    ResponseEntity<String> response =
        restTemplate.postForEntity(url, request, String.class, uriVariables);
    JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
    return readValue(response, javaType);
  }

  public <T, R> T postForEntityWithHeaders(Class<T> clazz, String url, MultiValueMap<String, String> headers,
      R body, Object... uriVariables) {
    HttpEntity<R> request = new HttpEntity<>(body, headers);
    ResponseEntity<String> response =
        restTemplate.postForEntity(url, request, String.class, uriVariables);
    JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
    return readValue(response, javaType);
  }

  @Async
  public <T, R> T asyncPostForEntity(Class<T> clazz, String url, R body, Object... uriVariables) {
    HttpEntity<R> request = new HttpEntity<>(body);
    ResponseEntity<String> response =
        restTemplate.postForEntity(url, request, String.class, uriVariables);
    JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
    return readValue(response, javaType);
  }

  public <T> T postForUrlEncodedEntity(Class<T> clazz, String url,
      MultiValueMap<String, String> headers, MultiValueMap<String, String> values,
      Object... uriVariables) {
    HttpHeaders header = new HttpHeaders();
    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    if (Objects.nonNull(headers) && Boolean.FALSE.equals(headers.isEmpty()))
      header.addAll(headers);
    LOGGER.info("{}", header);
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(values, header);
    LOGGER.info("{}, {}", url, request.getBody());
    ResponseEntity<String> response =
        restTemplate.postForEntity(url, request, String.class, uriVariables);
    JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
    return readValue(response, javaType);
  }

  public ResponseEntity<String> postForUrlEncodedEntity(String url,
      MultiValueMap<String, String> headers, MultiValueMap<String, String> values,
      Object... uriVariables) {
    HttpHeaders header = new HttpHeaders();
    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    if (Objects.nonNull(headers) && Boolean.FALSE.equals(headers.isEmpty()))
      header.addAll(headers);
    LOGGER.info("{}", header);
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(values, header);
    LOGGER.info("{}, {}", url, request.getBody());
    return restTemplate.postForEntity(url, request, String.class, uriVariables);
  }

	/*
	 * public <T> T postForUrlEncodedEntityBasicAuth(Class<T> clazz, String url,
	 * String username, String password, MultiValueMap<String, String> headers,
	 * MultiValueMap<String, String> values, Object... uriVariables) { HttpHeaders
	 * header = new HttpHeaders();
	 * header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	 * header.addAll(headers); LOGGER.info("{}", header);
	 * HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(values,
	 * header); HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
	 * new HttpComponentsClientHttpRequestFactory();
	 * clientHttpRequestFactory.setHttpClient(httpClient(username, password));
	 * LOGGER.info("{}, {}", url, request.getBody()); RestTemplate template = new
	 * RestTemplate(clientHttpRequestFactory);
	 * template.setInterceptors(Collections.singletonList(new
	 * RequestResponseLoggingInterceptor())); ResponseEntity<String> response =
	 * template.postForEntity(url, request, String.class, uriVariables); JavaType
	 * javaType = objectMapper.getTypeFactory().constructType(clazz); return
	 * readValue(response, javaType); }
	 */

  public <T, R> T putForEntity(Class<T> clazz, String url, R body, Object... uriVariables) {
    HttpEntity<R> request = new HttpEntity<>(body);
    ResponseEntity<String> response =
        restTemplate.exchange(url, HttpMethod.PUT, request, String.class, uriVariables);
    JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
    return readValue(response, javaType);
  }

  public void delete(String url, Object... uriVariables) {
    try {
      restTemplate.delete(url, uriVariables);
    } catch (RestClientException exception) {
      LOGGER.info(exception.getMessage());
    }
  }

  private <T> T readValue(ResponseEntity<String> response, JavaType javaType) {
    T result = null;
    if (response.getStatusCode() == HttpStatus.OK
        || response.getStatusCode() == HttpStatus.CREATED) {
      try {
        result = objectMapper.readValue(response.getBody(), javaType);
      } catch (IOException e) {
        LOGGER.info(e.getMessage());
      }
    } else {
      LOGGER.info(NOT_DATA_FOUND_LOG_FORMAT, response.getStatusCode());
    }
    return result;
  }
  
  
  
  
  

	/*
	 * private HttpClient httpClient(String username, String password) {
	 * CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	 * 
	 * credentialsProvider.setCredentials(AuthScope.ANY, new
	 * UsernamePasswordCredentials(username, password));
	 * 
	 * return
	 * HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
	 * .build(); }
	 */

}
