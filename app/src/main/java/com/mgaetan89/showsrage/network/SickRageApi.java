package com.mgaetan89.showsrage.network;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.model.Indexer;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public final class SickRageApi implements RequestInterceptor {
	private String apiKey = "";

	@NonNull
	private String apiUrl = "";

	@Nullable
	/* package */ String credentials = null;

	@NonNull
	private static final SickRageApi INSTANCE = new SickRageApi();

	private OkHttpClient okHttpClient = null;

	@NonNull
	private String path = "";

	private SickRageServices services = null;

	@NonNull
	private String webRoot = "";

	private SickRageApi() {
	}

	@NonNull
	public static SickRageApi getInstance() {
		return INSTANCE;
	}

	@NonNull
	public String getApiUrl() {
		return buildFullApiUrl(this.apiUrl, this.path, this.apiKey);
	}

	@NonNull
	public String getBannerUrl(int indexerId, @Nullable Indexer indexer) {
		if (indexer == null) {
			return this.getApiUrl() + "?cmd=show.getbanner";
		}

		return String.format("%s?cmd=show.getbanner&%s=%d", this.getApiUrl(), indexer.getParamName(), indexerId);
	}

	@NonNull
	public String getFanArtUrl(int indexerId, @Nullable Indexer indexer) {
		if (indexer == null) {
			return this.getApiUrl() + "?cmd=show.getfanart";
		}

		return String.format("%s?cmd=show.getfanart&%s=%d", this.getApiUrl(), indexer.getParamName(), indexerId);
	}

	@NonNull
	public OkHttpClient getOkHttpClient(boolean useSelfSignedCertificate) {
		if (this.okHttpClient == null) {
			this.okHttpClient = new OkHttpClient();

			this.setAuthenticator();
		}

		this.trustAllCertificates(useSelfSignedCertificate);

		return this.okHttpClient;
	}

	@NonNull
	public String getPosterUrl(int indexerId, @Nullable Indexer indexer) {
		if (indexer == null) {
			return this.getApiUrl() + "?cmd=show.getposter";
		}

		return String.format("%s?cmd=show.getposter&%s=%d", this.getApiUrl(), indexer.getParamName(), indexerId);
	}

	public SickRageServices getServices() {
		return this.services;
	}

	@NonNull
	public String getVideosUrl() {
		return this.apiUrl + "videos";
	}

	public void init(@NonNull SharedPreferences preferences) {
		boolean useHttps = preferences.getBoolean("use_https", false);
		String address = preferences.getString("server_address", "");
		String portNumber = preferences.getString("server_port_number", "");
		boolean selfSignedCertificate = preferences.getBoolean("self_signed_certificate", false);

		this.apiKey = preferences.getString("api_key", "");
		this.apiUrl = buildApiUrl(useHttps, address, portNumber);
		this.credentials = getCredentials(
				preferences.getBoolean("basic_auth", false),
				preferences.getString("server_username", null),
				preferences.getString("server_password", null)
		);
		this.path = getApiPath(preferences.getString("server_path", ""));
		this.webRoot = getWebRoot(this.path);

		RestAdapter.Builder builder = new RestAdapter.Builder();
		builder.setClient(new OkClient(this.getOkHttpClient(selfSignedCertificate)));
		builder.setEndpoint(this.apiUrl);
		builder.setRequestInterceptor(this);
		builder.setLogLevel(Constants.NETWORK_LOG_LEVEL);

		this.services = builder.build().create(SickRageServices.class);
	}

	@Override
	public void intercept(RequestFacade request) {
		request.addEncodedPathParam("api_path", this.path);
		request.addPathParam("api_key", this.apiKey);
		request.addEncodedPathParam("web_root", this.webRoot);
	}

	@NonNull
	private static String buildApiUrl(boolean useHttps, String address, String portNumber) {
		// Retrofit requires a non-empty endpoint
		// So we use the local url
		if (address.isEmpty()) {
			return "http://127.0.0.1/";
		}

		StringBuilder builder = new StringBuilder();

		if (useHttps) {
			builder.append("https://");
		} else {
			builder.append("http://");
		}

		builder.append(address);

		if (!portNumber.isEmpty()) {
			builder.append(":").append(portNumber);
		}

		builder.append("/");

		return builder.toString();
	}

	@NonNull
	private static String buildFullApiUrl(String apiUrl, String path, String apiKey) {
		StringBuilder builder = new StringBuilder();
		builder.append(apiUrl);

		if (!path.isEmpty()) {
			builder.append(path).append("/");
		}

		if (!apiKey.isEmpty()) {
			builder.append(apiKey).append("/");
		}

		return builder.toString();
	}

	@NonNull
	/* package */ static String getApiPath(String apiPath) {
		if (apiPath == null || apiPath.isEmpty()) {
			return "";
		}

		return apiPath.replaceAll("^/+|/$+", "");
	}

	@Nullable
	/* package */ static String getCredentials(boolean useBasicAuthentication, String username, String password) {
		if (!useBasicAuthentication) {
			return null;
		}

		if (username == null || username.isEmpty()) {
			return null;
		}

		if (password == null || password.isEmpty()) {
			return null;
		}

		return Credentials.basic(username, password);
	}

	@NonNull
	/* package */ static String getWebRoot(String apiPath) {
		if (apiPath == null || apiPath.isEmpty()) {
			return "";
		}

		String path = apiPath;

		// Add a leading /
		if (!path.startsWith("/")) {
			path = "/" + path;
		}

		// Add a trailing /
		if (!path.endsWith("/")) {
			path += "/";
		}

		// If the path ends with /api/, we ignore that last segment
		if (path.endsWith("/api/")) {
			return path.substring(1, path.length() - 4);
		}

		return apiPath.replaceAll("^/+|/$+", "") + "/";
	}

	private void setAuthenticator() {
		this.okHttpClient.setAuthenticator(new Authenticator() {
			@Override
			public Request authenticate(Proxy proxy, Response response) throws IOException {
				String credentials = SickRageApi.this.credentials;

				if (credentials != null && !credentials.isEmpty()) {
					return response.request().newBuilder()
							.header("Authorization", credentials)
							.build();
				}

				return null;
			}

			@Override
			public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
				return null;
			}
		});
	}

	private void trustAllCertificates(boolean useSelfSignedCertificate) {
		if (!useSelfSignedCertificate) {
			return;
		}

		TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager() {
					@Override
					public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					}

					@Override
					public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					}

					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
				}
		};

		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new SecureRandom());
		} catch (KeyManagementException | NoSuchAlgorithmException exception) {
			exception.printStackTrace();
		}

		if (sslContext != null) {
			this.okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
		}

		this.okHttpClient.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
	}
}
