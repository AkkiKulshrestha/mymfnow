package com.indocosmic.mymfnow.webservices;

public class RestClient
	{
		private static API REST_CLIENT;

		public static String ROOT = "http://13.126.98.215:8081/finpinapi/api/v3";

		public static String URLProd = "https://www.finpin.in";

		public static String URLDev = "http://13.126.98.215:8081";

		public static String GraphPath = URLProd;


		public static String Development = "http://13.126.98.215:8081/finpinapimaster/api/v2";
		/*static
			{
				setupRestClient();
			}*/


		private RestClient()
			{
			}

		public static API get()
			{
				return REST_CLIENT;
			}

		/*private static void setupRestClient()
			{
				RestAdapter.Builder builder = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(ROOT).setClient(new OkClient(new OkHttpClient()));
				RestAdapter restAdapter = builder.build();
				REST_CLIENT = restAdapter.create(API.class);
			}*/
	}
