package it.integris.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import it.integris.talkwalker.TWSearchAPI;
import it.integris.twitter.TwitterCredentials;

public class ReportGeneration 
{
	private static final String SEPARATOR = ",";

	public static HashMap<String, JSONObject> retrievedDocuments = null;
	public static final Logger LOGGER = Logger.getLogger(ReportGeneration.class);

	public static void main( String[] args )
	{

		//***		File propertiesFiles = new File(args[0]);
		File propertiesFiles = new File("src/test/resources/application.properties");

		//***path provvisorio per far partire l'applicazione
		String provvisoryFilename="/home/sviluppo/Documenti/progetti Java/workspaceEclipse/TalkWalkerReport/src/test/resources/";
		Path provvisoryPathToFile = Paths.get(provvisoryFilename);
		

		Properties p = new Properties();
		try {
			InputStream targetStream = new FileInputStream(propertiesFiles);
			p.load(targetStream);
		} catch (IOException e1) {
			e1.printStackTrace();
			LOGGER.error("Error: no input properties file.");
			LOGGER.error("Quitting...");
			System.exit(-1);
		}
		Properties props = System.getProperties();
		props.setProperty("log4j2.debug", "true");
		retrievedDocuments = new HashMap<>();
		String token = p.getProperty("tw_access_token");
		String projectName = p.getProperty("project_name");
		String projectId = p.getProperty("project_id");
		String basePath = p.getProperty("output_directory");
		JSONArray twitter = new JSONArray(p.getProperty("twitter_accounts"));
		Integer delay = Integer.parseInt(p.getProperty("delay"));
		long startSearchDate = 0;
		long endSearchDate = 0;

		// TalkWalker API connector initialization
		TWSearchAPI.init(token);
		// Parsing twitter accounts
		for(int i = 0; i < twitter.length(); i++){
			JSONObject obj = twitter.getJSONObject(i);
			TWSearchAPI.addTwitterCredentials(new TwitterCredentials(obj.getString("consumerKey"), obj.getString("consumerSecret"), 
					obj.getString("accessToken"), obj.getString("accessTokenSecret")));
		}
		SimpleDateFormat outputSDF = new SimpleDateFormat("ddMMyyyyHHmmss");
		SimpleDateFormat jsonOutputSDF = new SimpleDateFormat("ddMMyyyy");
		Date nowDate = new Date(System.currentTimeMillis());

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
		calendar.setTime(nowDate);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-delay);
		//		calendar.set(Calendar.DAY_OF_MONTH, 7);
		//		calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		startSearchDate = calendar.getTimeInMillis()/1000;
		LOGGER.info("Start time = " + calendar.getTime());

		calendar.setTime(nowDate);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-delay);
		//		calendar.set(Calendar.DAY_OF_MONTH, 7);
		//		calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		endSearchDate = calendar.getTimeInMillis()/1000;

		LOGGER.info("End time = " + calendar.getTime());

		try {
			Path JSONOutputFolder = null;
			//jsonOutputSDF.format(new Date(startSearchDate * 1000))
			//****			if(!Files.exists(Paths.get(basePath + "/JSON")))
			if(!Files.exists(provvisoryPathToFile))
				//***				JSONOutputFolder = Files.createDirectory(Paths.get(basePath + "/JSON"));
				JSONOutputFolder = Files.createDirectory(Paths.get(provvisoryPathToFile+ "/JSON"));
			else
			//***	JSONOutputFolder = Paths.get(basePath + "/JSON");
				JSONOutputFolder = Paths.get(provvisoryPathToFile + "/JSON");

//***			if(!Files.exists(Paths.get(basePath + "/CSV")))
//***				Files.createDirectory(Paths.get(basePath + "/CSV"));
			if(!Files.exists(Paths.get(provvisoryPathToFile + "/CSV")))
				Files.createDirectory(Paths.get(provvisoryPathToFile + "/CSV"));
			//			else
			//				JSONOutputFolder = Paths.get(basePath + "/exportJSON-" + jsonOutputSDF.format(new Date(startSearchDate * 1000)));
//***			PrintWriter writer = new PrintWriter(basePath + "/CSV/NewPost_" + projectName + "_" + outputSDF.format(new Date(startSearchDate * 1000)) + "_" + outputSDF.format(new Date(endSearchDate * 1000)) + ".csv", "UTF-8");
			PrintWriter writer = new PrintWriter(provvisoryPathToFile + "/CSV/NewPost_" + projectName + "_" + outputSDF.format(new Date(startSearchDate * 1000)) + "_" + outputSDF.format(new Date(endSearchDate * 1000)) + ".csv", "UTF-8");

			// Write down the first line
			writer.println("url" + SEPARATOR + "indexed" + SEPARATOR + "published" + SEPARATOR + "search_indexed" + SEPARATOR + "title_snippet" + SEPARATOR + "content_snippet" + SEPARATOR + "title" + SEPARATOR + 
					"content" + SEPARATOR + "root_url" + SEPARATOR + "domain_url" + SEPARATOR + "host_url" + SEPARATOR + "parent_url" + SEPARATOR + "lang" + SEPARATOR + "porn_level" + SEPARATOR + 
					"fluency_level" + SEPARATOR + "spam_level" + SEPARATOR + "sentiment" + SEPARATOR + "source_type" + SEPARATOR + "post_type" + SEPARATOR + "cluster_id" + SEPARATOR + "meta_cluster_id" + SEPARATOR + 
					"tags_internal" + SEPARATOR + "tags_marking" + SEPARATOR + "tags_customer" + SEPARATOR + "entity_urls" + SEPARATOR + "images.url" + SEPARATOR + "images.width" + SEPARATOR + "images.height" + SEPARATOR + 
					"images.legend" + SEPARATOR + "videos.url" + SEPARATOR + "videos.width" + SEPARATOR + "videos.height" + SEPARATOR + 
					"videos.legend" + SEPARATOR + "pagemonitoring_sitemon_siteid" + SEPARATOR + "matched_profile" + SEPARATOR + "article_extended_attributes.facebook_shares" + SEPARATOR + 
					"article_extended_attributes.facebook_likes" + SEPARATOR + "article_extended_attributes.twitter_retweets" + SEPARATOR + "article_extended_attributes.url_views" + SEPARATOR + 
					"article_extended_attributes.pinterest_likes" + SEPARATOR + "article_extended_attributes.pinterest_pins" + SEPARATOR + "article_extended_attributes.pinterest_repins" + SEPARATOR + 
					"article_extended_attributes.youtube_views" + SEPARATOR + "article_extended_attributes.youtube_likes" + SEPARATOR + "article_extended_attributes.youtube_dislikes" + SEPARATOR + 
					"article_extended_attributes.instagram_likes" + SEPARATOR + "article_extended_attributes.twitter_shares" + SEPARATOR + "article_extended_attributes.num_comments" + SEPARATOR + 
					"source_extended_attributes.alexa_pageviews" + SEPARATOR + "source_extended_attributes.facebook_followers" + SEPARATOR + "source_extended_attributes.twitter_followers" + SEPARATOR + 
					"source_extended_attributes.instagram_followers" + SEPARATOR + "source_extended_attributes.pinterest_followers" + SEPARATOR + "extra_article_attributes.world_data.continent" + SEPARATOR + 
					"extra_article_attributes.world_data.country" + SEPARATOR + "extra_article_attributes.world_data.country_code" + SEPARATOR + "extra_article_attributes.world_data.region" + SEPARATOR + 
					"extra_article_attributes.world_data.city" + SEPARATOR + "extra_article_attributes.world_data.longitude" + SEPARATOR + "extra_article_attributes.world_data.latitude" + SEPARATOR + 
					"extra_author_attributes.id" + SEPARATOR + "extra_author_attributes.type" + SEPARATOR + "extra_author_attributes.name" + SEPARATOR + "extra_author_attributes.birthdate.date" + SEPARATOR + 
					"extra_author_attributes.birthdate.resolution" + SEPARATOR + "extra_author_attributes.gender" + SEPARATOR + "extra_author_attributes.image_url" + SEPARATOR + 
					"extra_author_attributes.short_name" + SEPARATOR + "extra_author_attributes.url" + SEPARATOR + "extra_author_attributes.world_data.continent" + SEPARATOR + 
					"extra_author_attributes.world_data.country" + SEPARATOR + "extra_author_attributes.world_data.country_code" + SEPARATOR + "extra_author_attributes.world_data.region" + SEPARATOR + 
					"extra_author_attributes.world_data.city" + SEPARATOR + "extra_author_attributes.world_data.longitude" + SEPARATOR + "extra_author_attributes.world_data.latitude" + SEPARATOR + 
					"extra_source_attributes.world_data.continent" + SEPARATOR + "extra_source_attributes.world_data.country" + SEPARATOR + "extra_source_attributes.world_data.country_code" + SEPARATOR + 
					"extra_source_attributes.world_data.region" + SEPARATOR + "extra_source_attributes.world_data.city" + SEPARATOR + "extra_source_attributes.world_data.longitude" + SEPARATOR + 
					"extra_source_attributes.world_data.latitude" + SEPARATOR + "engagement" + SEPARATOR + "reach" + SEPARATOR + "provider" + SEPARATOR + "generator.type" + SEPARATOR + 
					"source_extended_attributes.alexa_unique_visitors" + SEPARATOR + "article_extended_attributes.twitter_likes" + SEPARATOR + "extra_author_attributes.description" + SEPARATOR + 
					"article_extended_attributes.linkedin_shares" + SEPARATOR + "extra_source_attributes.name" + SEPARATOR + "word_count" + SEPARATOR + "project_name");


			// Get the set of topics available on TalkWalker
			HashMap<String, String> topics = TWSearchAPI.getTopicsAndChannels(projectId);
			if(topics == null){
				LOGGER.error("An error occurred while retrieving topic's list. Try again later.");
				System.exit(-1);
			}

			//			HashMap<String, String> topics = new HashMap<>();
			//			topics.put("j9881o71_5imm9ixp6dzt", "WIND/INFOSTRADA");

			TWJSONParser.setTopics(topics);

			for (String topicID :topics.keySet()) {
				LOGGER.info("Retrieving posts from topic: "+topics.get(topicID));
				int returnCode = TWSearchAPI.retrieveDocumentsFromTopicAndChannel(projectId, topicID, startSearchDate, endSearchDate, JSONOutputFolder.toString(), jsonOutputSDF.format(new Date(startSearchDate * 1000)));
				if(returnCode == TWSearchAPI.RETURNCODE_ERR){
					LOGGER.error("An error occurred while retrieving the documents for topic name: " + topics.get(topicID) +  ". This topic will be skipped!");	
					continue;
				} 
			}
			for(JSONObject doc: ReportGeneration.retrievedDocuments.values()){
				TWJSONParser.parseDocumentMetadata(projectName, doc, writer);
			}

			writer.close();
			LOGGER.info("Finished!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("ERROR: an error occurred while saving output file. Abort!");
			System.exit(-1);
		}

	}
}
