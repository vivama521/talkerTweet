package it.integris.talkwalker;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.integris.report.ReportGeneration;
import it.integris.report.TWJSONParser;
import it.integris.twitter.TwitterAPI;
import it.integris.twitter.TwitterCredentials;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;

// TODO: change error messages
public class TWSearchAPI {

	private static String token = null;
	private static TwitterAPI twitterAPI = null;
	public static final Logger LOGGER = Logger.getLogger(TWSearchAPI.class);

	public static int RETURNCODE_OK = 0;
	public static int RETURNCODE_ERR = -1;

	public static void init(String token)
	{
		TWSearchAPI.token = token;
		TWSearchAPI.twitterAPI = new TwitterAPI(1000);
	}

	/**
	 * Method enrichTwitterDocuments.
	 * @param documents is the list of the documents to enrich with Twitter metadata.
	 * @return {@value #RETURNCODE_OK} in case all documents were enriched, or {@value #RETURNCODE_ERR} in case of an error while executing the API's call.
	 **/
	private static int enrichTwitterDocuments(HashMap<Long, JSONObject> documents){
		ArrayList<JSONObject> enrichedDocuments = TWSearchAPI.twitterAPI.twitterLookup(documents);
		if(enrichedDocuments == null)
			return TWSearchAPI.RETURNCODE_ERR;
		for(int i = 0; i < enrichedDocuments.size(); i++){
			if(!ReportGeneration.retrievedDocuments.containsKey(enrichedDocuments.get(i).getString("url"))){
				ReportGeneration.retrievedDocuments.put(enrichedDocuments.get(i).getString("url"), enrichedDocuments.get(i));
			} else {
				TWJSONParser.managePossibleMultipleTopicsAndTagsOnSameDocument(ReportGeneration.retrievedDocuments.get(enrichedDocuments.get(i).getString("url")), enrichedDocuments.get(i));
			}
		}
		return TWSearchAPI.RETURNCODE_OK;
	}

	private static String restExecute(String query) {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(query);
		HttpResponse response;
		JSONObject json = null;

		try {
			Thread.sleep(500*2);
			response = client.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			String stringJson = "";

			while ((line = rd.readLine()) != null) {
				stringJson += line;
			}
			return stringJson;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while executing TW query. Query =  " + query + "; TW-Response-JSON = " + json);
			return null;
		}
	}


	public static HashMap<String,String> getTopicsAndChannels(String project_id)
	{
		String urlReadResources = "https://api.talkwalker.com/api/v2/talkwalker/p/" + project_id +
				"/resources?" +
				"access_token=" + token;
		String stringJson = restExecute(urlReadResources);
		JSONObject json = new JSONObject(stringJson);
		HashMap<String, String> topicsAndChannels = new HashMap<>();

		//System.out.println("topic status message : " + json.getString("status_message"));
		if (!json.getString("status_message").equals("OK")) {
			LOGGER.error("No topics found for the given project-id.");
			return null;
		}
		if (json.has("result_resources") && json.getJSONObject("result_resources").has("projects")) {
			JSONObject jsonResults = json.getJSONObject("result_resources")
					.getJSONArray("projects")
					// TODO: check if it is ok and manage cases in which there is more than one project
					.getJSONObject(0);
			if(jsonResults.has("topics")){
				JSONArray topicsAndChannelsList  = jsonResults.getJSONArray("topics");
				for (int i = 0; i < topicsAndChannelsList.length(); i++) {
					JSONObject currentObj = topicsAndChannelsList.getJSONObject(i);
					String groupTitle = currentObj.getString("title");
					JSONArray children = currentObj.getJSONArray("nodes");
					for (Iterator<Object> it = children.iterator(); it.hasNext(); ) {
						JSONObject topic = (JSONObject) it.next();
						topicsAndChannels.put(topic.get("id").toString(), "SEARCH / " + groupTitle + " / " + topic.get("title").toString());
					}
				}
			}

			if(jsonResults.has("channels")){
				JSONArray topicsAndChannelsList = jsonResults.getJSONArray("channels");
				for (int i = 0; i < topicsAndChannelsList.length(); i++) {
					JSONObject currentObj = topicsAndChannelsList.getJSONObject(i);
					String groupTitle = currentObj.getString("title");
					JSONArray children = currentObj.getJSONArray("nodes");
					for (Iterator<Object> it = children.iterator(); it.hasNext(); ) {
						JSONObject topic = (JSONObject) it.next();
						topicsAndChannels.put(topic.get("id").toString(), "PAGE / " + groupTitle + " / " + topic.get("title").toString());
					}
				}
			}

		} else {
			LOGGER.error("An error occurred while retrieving the list of topics. TW-Response-JSON = " + json);
			return null;
		}
		return topicsAndChannels;
	}

	private static String computeQuery(String project_id, String topic, long startTime, long endTime)
	{
		String query = null;
		try {
			query = "https://api.talkwalker.com/api/v1/search/p/" + project_id +
					"/results?" +
					"access_token=" + token +
					"&topic=" + topic +
					"&pretty=true&sort_by=search_indexed&sort_order=asc&hpp=500" +
					"&q=" +  URLEncoder.encode("lang:it AND searchindexed:>" + startTime + " AND searchindexed:<" + endTime + " AND published:>1504216800", StandardCharsets.UTF_8.toString());
			//			"&q=" +  URLEncoder.encode("lang:it AND published:>" + startTime + " AND published:<" + endTime, StandardCharsets.UTF_8.toString());
			//System.out.println("Executing query = " + query);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return query;
	}

	//	public static void retrieveDocuments2(JSONObject responseJSON){
	//		JSONArray listDocuments = responseJSON.getJSONArray("data");
	//		HashMap<Long, JSONObject> twitterDocumentsToEnrich = new HashMap<>();
	//		for(int i = 0; i < listDocuments.length(); i++){
	//
	//			JSONObject currentDoc = listDocuments.getJSONObject(i).getJSONObject("data");
	//			if(!ReportGeneration.retrievedDocuments.containsKey(currentDoc.getString("url"))){
	//				//System.out.println("Parsing document url = " + currentDoc.getString("url"));
	//				JSONArray sources = currentDoc.getJSONArray("source_type");
	//				int j = 0;
	//				boolean found = false;
	//				while(!found && j < sources.length()){
	//					found = sources.get(j).equals("SOCIALMEDIA_TWITTER");
	//					j++;
	//				}
	//				if(found){ 
	//					String url = currentDoc.getString("url");
	//					twitterDocumentsToEnrich.put(Long.parseLong(url.substring(url.lastIndexOf('/') + 1, url.length())), currentDoc);
	//					if(twitterDocumentsToEnrich.size() == 10){
	//						// Enrich retrieved Twitter documents
	//						int returnCode = TWSearchAPI.enrichTwitterDocuments(twitterDocumentsToEnrich);
	//						if(returnCode == TWSearchAPI.RETURNCODE_ERR){
	//							System.err.print("An error occurred during Twitter documents enrichment. Retrying...");
	//							returnCode = TWSearchAPI.enrichTwitterDocuments(twitterDocumentsToEnrich);
	//							if(returnCode == TWSearchAPI.RETURNCODE_ERR){
	//								LOGGER.error("TWITTER ERROR: The system isn't able to enrich the given documents. Documents = " + twitterDocumentsToEnrich);
	//							}
	//						}
	//						twitterDocumentsToEnrich.clear();
	//						twitterDocumentsToEnrich = new HashMap<>();
	//					}
	//				} else {
	//					ReportGeneration.retrievedDocuments.put(currentDoc.getString("url"), currentDoc);
	//				}
	//			} else {
	//				JSONObject existingDoc = ReportGeneration.retrievedDocuments.get(currentDoc.getString("url"));
	//				// Manage multiple topics and tags for the same doc
	//				TWJSONParser.managePossibleMultipleTopicsAndTagsOnSameDocument(existingDoc, currentDoc);
	//
	//			}
	//		}
	//
	//		if(twitterDocumentsToEnrich.size() > 0){
	//			int returnCode = TWSearchAPI.enrichTwitterDocuments(twitterDocumentsToEnrich);
	//			if(returnCode == TWSearchAPI.RETURNCODE_ERR){
	//				System.err.print("An error occurred during Twitter documents enrichment. Retrying...");
	//				returnCode = TWSearchAPI.enrichTwitterDocuments(twitterDocumentsToEnrich);
	//				if(returnCode == TWSearchAPI.RETURNCODE_ERR){
	//					LOGGER.error("TWITTER ERROR: The system isn't able to enrich the given documents. Documents = " + twitterDocumentsToEnrich);
	//				}
	//			} else {
	//				twitterDocumentsToEnrich.clear();
	//			}
	//		}
	//
	//	}
	//
	//	public static void retrieveDocuments3(JSONObject currentDoc){
	//		HashMap<Long, JSONObject> twitterDocumentsToEnrich = new HashMap<>();
	//
	//		if(!ReportGeneration.retrievedDocuments.containsKey(currentDoc.getString("url"))){
	//			//System.out.println("Parsing document url = " + currentDoc.getString("url"));
	//			JSONArray sources = currentDoc.getJSONArray("source_type");
	//			int j = 0;
	//			boolean found = false;
	//			while(!found && j < sources.length()){
	//				found = sources.get(j).equals("SOCIALMEDIA_TWITTER");
	//				j++;
	//			}
	//			if(found){ 
	//				String url = currentDoc.getString("url");
	//				twitterDocumentsToEnrich.put(Long.parseLong(url.substring(url.lastIndexOf('/') + 1, url.length())), currentDoc);
	//				if(twitterDocumentsToEnrich.size() == 10){
	//					// Enrich retrieved Twitter documents
	//					int returnCode = TWSearchAPI.enrichTwitterDocuments(twitterDocumentsToEnrich);
	//					if(returnCode == TWSearchAPI.RETURNCODE_ERR){
	//						System.err.print("An error occurred during Twitter documents enrichment. Retrying...");
	//						returnCode = TWSearchAPI.enrichTwitterDocuments(twitterDocumentsToEnrich);
	//						if(returnCode == TWSearchAPI.RETURNCODE_ERR){
	//							LOGGER.error("The system isn't able to enrich the given documents. Documents = " + twitterDocumentsToEnrich);
	//						} else {
	//							LOGGER.error("done.");
	//						}
	//					}
	//					twitterDocumentsToEnrich.clear();
	//					twitterDocumentsToEnrich = new HashMap<>();
	//				}
	//			} else {
	//				ReportGeneration.retrievedDocuments.put(currentDoc.getString("url"), currentDoc);
	//			}
	//		} else {
	//			JSONObject existingDoc = ReportGeneration.retrievedDocuments.get(currentDoc.getString("url"));
	//			// Manage multiple topics and tags for the same doc
	//			TWJSONParser.managePossibleMultipleTopicsAndTagsOnSameDocument(existingDoc, currentDoc);
	//
	//		}
	//
	//	}

	public static int retrieveDocumentsFromTopicAndChannel(String project_id, String topicID, long startTime, long endTime, String outputJSONPath, String outputJSONDatePattern) throws FileNotFoundException{
		//HashMap<String, JSONObject> retrievedDocuments = new HashMap<>();
		String q = TWSearchAPI.computeQuery(project_id, topicID, startTime, endTime);
		LOGGER.debug(q);
		String response = TWSearchAPI.restExecute(q);
		ArrayList<JSONObject> totalDocsPerTopic = new ArrayList<>();
		if (!response.equals("")) {
			JSONObject talkWalkerResponseJSON = null;
			try {
				talkWalkerResponseJSON = new JSONObject(response);
			} catch (JSONException e){
				e.printStackTrace();
				LOGGER.error("Error: TalkWalker API's output wasn't recognized.");
				return TWSearchAPI.RETURNCODE_ERR;
			}
			if (!talkWalkerResponseJSON.getString("status_message").equals("OK")) {
				LOGGER.error("An error occurred while calling TalkWalker API. 'status_message' = " + talkWalkerResponseJSON.getString("status_message"));
				LOGGER.debug( talkWalkerResponseJSON );
				return TWSearchAPI.RETURNCODE_ERR;
			} else {
				JSONObject responseJSON = talkWalkerResponseJSON.getJSONObject("result_content");
				if(responseJSON.has("data")){
					//System.out.println(responseJSON.toString());
					JSONArray listDocuments = responseJSON.getJSONArray("data");
					HashMap<Long, JSONObject> twitterDocumentsToEnrich = new HashMap<>();
					boolean pagination = true;
					String currentDocUrl="";
					String url="";
					JSONObject currentDoc = null;
					while(pagination){
						for(int i = 0; i < listDocuments.length(); i++){
							currentDoc=listDocuments.getJSONObject(i).getJSONObject("data");
							//metto il contenuto dell' url presente nella variabile currentDoc JSON in una variabile String
							currentDocUrl=currentDoc.getString("url");
							
							try {
								if(currentDoc.getString("external_id")!=null)
									url="https://twitter.com/statuses/"+currentDoc.getString("external_id");
							} catch(JSONException e){
								LOGGER.error("The JSONObject currentDoc dosen't contains the external_id...");
							}

							totalDocsPerTopic.add(currentDoc);
							if(!ReportGeneration.retrievedDocuments.containsKey(currentDocUrl)){
								//System.out.println("Parsing document url = " + currentDocUrl);
								JSONArray sources = currentDoc.getJSONArray("source_type");
								int j = 0;
								boolean found = false;
								while(!found && j < sources.length()){
									found = sources.get(j).equals("SOCIALMEDIA_TWITTER");
									j++;
								}

								if(found){ 

									twitterDocumentsToEnrich.put(Long.parseLong(url.substring(url.lastIndexOf('/') + 1, url.length())), currentDoc);
									if(twitterDocumentsToEnrich.size() == 10){
										// Enrich retrieved Twitter documents
										int returnCode = TWSearchAPI.enrichTwitterDocuments(twitterDocumentsToEnrich);
										if(returnCode == TWSearchAPI.RETURNCODE_ERR){
											LOGGER.error("An error occurred during Twitter documents enrichment. Retrying...");
											returnCode = TWSearchAPI.enrichTwitterDocuments(twitterDocumentsToEnrich);
											if(returnCode == TWSearchAPI.RETURNCODE_ERR){
												LOGGER.error("TWITTER ERROR: The system isn't able to enrich the given documents. Documents = " + twitterDocumentsToEnrich);
											}
										}
										twitterDocumentsToEnrich.clear();
										twitterDocumentsToEnrich = new HashMap<>();
									}
								} else {
									ReportGeneration.retrievedDocuments.put(currentDocUrl, currentDoc);
								}
							} else {
								JSONObject existingDoc = ReportGeneration.retrievedDocuments.get(currentDocUrl);
								// Manage multiple topics and tags for the same doc
								TWJSONParser.managePossibleMultipleTopicsAndTagsOnSameDocument(existingDoc, currentDoc);

							}
						}
						if(talkWalkerResponseJSON.getJSONObject("pagination").has("next")){
							String nextQuery = talkWalkerResponseJSON.getJSONObject("pagination").getString("next").replace("GET ", "https://api.talkwalker.com");
							response = TWSearchAPI.restExecute(nextQuery);
							if (!response.equals("")) {
								talkWalkerResponseJSON = new JSONObject(response);
								if (!talkWalkerResponseJSON.getString("status_message").equals("OK")) {
									LOGGER.error("An error occurred while calling TalkWalker API. 'status_message' = " + talkWalkerResponseJSON.getString("status_message"));
									pagination = false;
									if(twitterDocumentsToEnrich.size() > 0){
										int returnCode = TWSearchAPI.enrichTwitterDocuments(twitterDocumentsToEnrich);
										if(returnCode == TWSearchAPI.RETURNCODE_ERR){
											System.err.print("An error occurred during Twitter documents enrichment. Retrying...");
											returnCode = TWSearchAPI.enrichTwitterDocuments(twitterDocumentsToEnrich);
											if(returnCode == TWSearchAPI.RETURNCODE_ERR){
												LOGGER.error("TWITTER ERROR: The system isn't able to enrich the given documents. Documents = " + twitterDocumentsToEnrich);
											}
										}
									}
									// TODO: manage this error in a better way (maybe with new error value?)
									return TWSearchAPI.RETURNCODE_ERR;
								} else {
									listDocuments = talkWalkerResponseJSON.getJSONObject("result_content").getJSONArray("data");
								}
							} else {
								LOGGER.error("Error: empty response from TalkWalker. Continue...");
								pagination = false;
							}
						} else 
							pagination = false;
					}
					if(twitterDocumentsToEnrich.size() > 0){
						int returnCode = TWSearchAPI.enrichTwitterDocuments(twitterDocumentsToEnrich);
						if(returnCode == TWSearchAPI.RETURNCODE_ERR){
							System.err.print("An error occurred during Twitter documents enrichment. Retrying...");
							returnCode = TWSearchAPI.enrichTwitterDocuments(twitterDocumentsToEnrich);
							if(returnCode == TWSearchAPI.RETURNCODE_ERR){
								LOGGER.error("TWITTER ERROR: The system isn't able to enrich the given documents. Documents = " + twitterDocumentsToEnrich);
							}
						} else {
							twitterDocumentsToEnrich.clear();
						}
					}
				}
			}
		} else
			return TWSearchAPI.RETURNCODE_ERR;
		// Save docs per topic
		try {
			if(totalDocsPerTopic.size() > 0){
				PrintWriter writer = new PrintWriter(outputJSONPath + "/" + TWJSONParser.getTopicName(topicID).replaceAll(" ", "").replaceAll("[/]", "_").replaceAll("[|]", "-")+"_"+outputJSONDatePattern+".json"); 
				for(JSONObject str: totalDocsPerTopic) {
					writer.println(str.toString());
				}
				writer.close();
			}
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}
		return TWSearchAPI.RETURNCODE_OK;
	}

	public static void addTwitterCredentials(TwitterCredentials twitterCredentials) {
		TWSearchAPI.twitterAPI.addTwitterCredentials(twitterCredentials);
	}

}
