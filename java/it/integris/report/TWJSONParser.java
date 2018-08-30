package it.integris.report;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TWJSONParser {
	private static final String SEPARATOR = ",";
	private static SimpleDateFormat outputSDF = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	private static HashMap<String, String> topics = null;

	public static void setTopics(HashMap<String, String> topics) {
		TWJSONParser.topics = topics;
	}
	
	public static String getTopicName(String topicID){
		if(!topics.containsKey(topicID))
			return null;
		return topics.get(topicID);
	}

	public static void parseDocumentMetadata(String projectName, JSONObject doc, PrintWriter writer) throws UnsupportedEncodingException, JSONException{
		if(doc.has("url")) 
			writer.print(StringEscapeUtils.escapeCsv(doc.getString("url")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("indexed"))
			writer.print(StringEscapeUtils.escapeCsv(outputSDF.format(new Date(doc.getLong("indexed")))) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("published"))
			writer.print(StringEscapeUtils.escapeCsv(outputSDF.format(new Date(doc.getLong("published")))) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("search_indexed"))
			writer.print(StringEscapeUtils.escapeCsv(outputSDF.format(new Date(doc.getLong("search_indexed")))) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("title_snippet"))
			writer.print(StringEscapeUtils.escapeCsv(StringEscapeUtils.unescapeHtml3(new String(doc.getString("title_snippet")
					.replaceAll("\\n", "")
					.replaceAll("\\r\\n", "")
					.replace("\\", "\\\\")
					.replace("\u0096", "")
					.replace("\u0088", "")
					.replace("\\x88", "")
					.replace("\\xE2", "")
					.replace("\\x96", "")
					.replace("█", "")
					.getBytes("UTF-8")))) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("content_snippet"))
			writer.print(StringEscapeUtils.escapeCsv(StringEscapeUtils.unescapeHtml3(new String(doc.getString("content_snippet")
					.replaceAll("\\n", "")
					.replaceAll("\\r\\n", "")
					.replace("\\", "\\\\")
					.replace("\u0096", "")
					.replace("\u0088", "")
					.replace("\\x88", "")
					.replace("\\xE2", "")
					.replace("\\x96", "")
					.replace("█", "")
					.getBytes("UTF-8")))) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("title"))
			writer.print(StringEscapeUtils.escapeCsv(StringEscapeUtils.unescapeHtml3(new String(doc.getString("title")
					.replaceAll("\\n", "")
					.replaceAll("\\r\\n", "")
					.replace("\\", "\\\\")
					.replace("\u0096", "")
					.replace("\u0088", "")
					.replace("\\x88", "")
					.replace("\\xE2", "")
					.replace("\\x96", "")
					.replace("█", "")
					.getBytes("UTF-8")))) + SEPARATOR);
		else
			writer.print(SEPARATOR);
		
		if(doc.has("content"))
			writer.print(StringEscapeUtils.escapeCsv(StringEscapeUtils.unescapeHtml3(new String(doc.getString("content")
					.replaceAll("\\n", "")
					.replaceAll("\\r\\n", "")
					.replace("\\", "\\\\")
					.replace("\u0096", "")
					.replace("\u0088", "")
					.replace("\\x88", "")
					.replace("\\xE2", "")
					.replace("\\x96", "")
					.replace("█", "")
					.getBytes("UTF-8")))) + SEPARATOR);
		
		else
			writer.print(SEPARATOR);

		if(doc.has("root_url"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getString("root_url")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("domain_url"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getString("domain_url")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("host_url"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getString("host_url")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("parent_url"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getString("parent_url")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("lang"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getString("lang")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("porn_level"))
			writer.print(doc.getInt("porn_level") + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("fluency_level"))
			writer.print(doc.getInt("fluency_level") + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("spam_level"))
			writer.print(doc.getInt("spam_level") + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("sentiment"))
			writer.print(doc.getInt("sentiment") + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("source_type"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getJSONArray("source_type").toString().replace("[", "").replace("]", "").replaceAll("\"", "")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("post_type"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getJSONArray("post_type").toString().replace("[", "").replace("]", "").replaceAll("\"", "")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("cluster_id"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getString("cluster_id")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("meta_cluster_id"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getString("meta_cluster_id")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("tags_internal"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getJSONArray("tags_internal").toString().replace("[", "").replace("]", "").replaceAll("\"", "")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("tags_marking"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getJSONArray("tags_marking").toString().replace("[", "").replace("]", "").replaceAll("\"", "")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("tags_customer"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getJSONArray("tags_customer").toString().replace("[", "").replace("]", "").replaceAll("\"", "")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("entity_url"))
			writer.print(StringEscapeUtils.escapeCsv(StringEscapeUtils.unescapeHtml3(doc.getJSONArray("entity_url").toString())) + SEPARATOR);
		else
			writer.print("[]" + SEPARATOR);

		if(doc.has("images")){
			JSONArray images = doc.getJSONArray("images");
			if(images.length() >= 1){
				JSONObject image = images.getJSONObject(0);
				if(image.has("url"))
					writer.print(StringEscapeUtils.escapeCsv(image.getString("url")) + SEPARATOR);
				else
					writer.println(SEPARATOR);
				if(image.has("width"))
					writer.print(image.getInt("width") + SEPARATOR);
				else
					writer.print("0" + SEPARATOR);
				if(image.has("height"))
					writer.print(image.getInt("height") + SEPARATOR);
				else
					writer.print("0" + SEPARATOR);
				if(image.has("legend"))
					writer.print(StringEscapeUtils.escapeCsv(image.getString("legend")) + SEPARATOR);
				else
					writer.print(SEPARATOR);
			}

		} else 
			writer.print(SEPARATOR + "0" + SEPARATOR + "0" + SEPARATOR + SEPARATOR);

		if(doc.has("videos")){
			JSONArray images = doc.getJSONArray("videos");
			if(images.length() >= 1){
				JSONObject image = images.getJSONObject(0);
				if(image.has("url"))
					writer.print(StringEscapeUtils.escapeCsv(image.getString("url")) + SEPARATOR);
				else
					writer.println(SEPARATOR);
				if(image.has("width"))
					writer.print(image.getInt("width") + SEPARATOR);
				else
					writer.print("0" + SEPARATOR);
				if(image.has("height"))
					writer.print(image.getInt("height") + SEPARATOR);
				else
					writer.print("0" + SEPARATOR);
				if(image.has("legend"))
					writer.print(StringEscapeUtils.escapeCsv(image.getString("legend")) + SEPARATOR);
				else
					writer.print(SEPARATOR);
			}

		} else 
			writer.print(SEPARATOR + "0" + SEPARATOR + "0" + SEPARATOR + SEPARATOR);

		if(doc.has("pagemonitoring_sitemon_siteid"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getString("pagemonitoring_sitemon_siteid")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("matched_profile")){
			JSONArray topic = doc.getJSONArray("matched_profile");
			String matchedProfiles = "";
			for(int i = 0; i < topic.length(); i++){
				if(i == topic.length() - 1)
					matchedProfiles = matchedProfiles + topics.get(topic.getString(i));
				else
					matchedProfiles = matchedProfiles + topics.get(topic.getString(i)) + SEPARATOR;
			}	
			writer.print(StringEscapeUtils.escapeCsv(matchedProfiles) + SEPARATOR);
		} else
			writer.print(SEPARATOR);

		if(doc.has("article_extended_attributes")){
			JSONObject articleAttributes = doc.getJSONObject("article_extended_attributes");
			if(articleAttributes.has("facebook_shares"))
				writer.print(articleAttributes.getInt("facebook_shares") + SEPARATOR);
			else
				writer.print("0" + SEPARATOR);
			if(articleAttributes.has("facebook_likes"))
				writer.print(articleAttributes.getInt("facebook_likes") + SEPARATOR);
			else
				writer.print("0" + SEPARATOR);
			if(articleAttributes.has("twitter_retweets"))
				writer.print(articleAttributes.getInt("twitter_retweets") + SEPARATOR);
			else
				writer.print("0" + SEPARATOR);
			if(articleAttributes.has("url_views"))
				writer.print(articleAttributes.getLong("url_views") + SEPARATOR);
			else
				writer.print("0" + SEPARATOR);
			if(articleAttributes.has("pinterest_likes"))
				writer.print(articleAttributes.getInt("pinterest_likes") + SEPARATOR);
			else
				writer.print("0" + SEPARATOR);
			if(articleAttributes.has("pinterest_pins"))
				writer.print(articleAttributes.getInt("pinterest_pins") + SEPARATOR);
			else
				writer.print("0" + SEPARATOR);
			if(articleAttributes.has("pinterest_repins"))
				writer.print(articleAttributes.getInt("pinterest_repins") + SEPARATOR);
			else
				writer.print("0" + SEPARATOR);
			if(articleAttributes.has("youtube_views"))
				writer.print(articleAttributes.getInt("youtube_views") + SEPARATOR);
			else
				writer.print("0" + SEPARATOR);
			if(articleAttributes.has("youtube_likes"))
				writer.print(articleAttributes.getInt("youtube_likes") + SEPARATOR);
			else
				writer.print("0" + SEPARATOR);
			if(articleAttributes.has("youtube_dislikes"))
				writer.print(articleAttributes.getInt("youtube_dislikes") + SEPARATOR);
			else
				writer.print("0" + SEPARATOR);
			if(articleAttributes.has("instagram_likes"))
				writer.print(articleAttributes.getInt("instagram_likes") + SEPARATOR);
			else
				writer.print("0" + SEPARATOR);
			if(articleAttributes.has("twitter_shares"))
				writer.print(articleAttributes.getInt("twitter_shares") + SEPARATOR);
			else
				writer.print("0" + SEPARATOR);
			if(articleAttributes.has("num_comments"))
				writer.print(articleAttributes.getInt("num_comments") + SEPARATOR);
			else
				writer.print("0" + SEPARATOR);
		} else
			writer.print("0" + SEPARATOR + "0" + SEPARATOR + "0" + SEPARATOR + "0" + SEPARATOR + "0" + SEPARATOR + "0" + 
					SEPARATOR + "0" + SEPARATOR + "0" + SEPARATOR + "0" + SEPARATOR + "0" + SEPARATOR + "0" + SEPARATOR + "0" + SEPARATOR + "0" + SEPARATOR);

		if(doc.has("source_extended_attributes")){
			JSONObject sourceAttributes = doc.getJSONObject("source_extended_attributes");
			if(sourceAttributes.has("alexa_pageviews")){
				writer.print(sourceAttributes.getLong("alexa_pageviews") + SEPARATOR);
			} else
				writer.print("0" + SEPARATOR);
			if(sourceAttributes.has("facebook_followers")){
				writer.print(sourceAttributes.getInt("facebook_followers") + SEPARATOR);
			} else
				writer.print("0" + SEPARATOR);
			if(sourceAttributes.has("twitter_followers")){
				writer.print(sourceAttributes.getInt("twitter_followers") + SEPARATOR);
			} else
				writer.print("0" + SEPARATOR);
			if(sourceAttributes.has("instagram_followers")){
				writer.print(sourceAttributes.getInt("instagram_followers") + SEPARATOR);
			} else
				writer.print("0" + SEPARATOR);
			if(sourceAttributes.has("pinterest_followers")){
				writer.print(sourceAttributes.getInt("pinterest_followers") + SEPARATOR);
			} else
				writer.print("0" + SEPARATOR);
		} else
			writer.print("0" + SEPARATOR + "0" + SEPARATOR + "0" + SEPARATOR + "0" + SEPARATOR + "0" +  SEPARATOR);

		if(doc.has("extra_article_attributes") && doc.getJSONObject("extra_article_attributes").has("world_data")){
			JSONObject articleAttributes = doc.getJSONObject("extra_article_attributes").getJSONObject("world_data");
			if(articleAttributes.has("continent")){
				writer.print(StringEscapeUtils.escapeCsv(articleAttributes.getString("continent")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);
			if(articleAttributes.has("country")){
				writer.print(StringEscapeUtils.escapeCsv(articleAttributes.getString("country")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);
			if(articleAttributes.has("country_code")){
				writer.print(StringEscapeUtils.escapeCsv(articleAttributes.getString("country_code")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);
			if(articleAttributes.has("region")){
				writer.print(StringEscapeUtils.escapeCsv(articleAttributes.getString("region")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);
			if(articleAttributes.has("city")){
				writer.print(StringEscapeUtils.escapeCsv(articleAttributes.getString("city")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);
			if(articleAttributes.has("longitude")){
				writer.print(articleAttributes.getDouble("longitude") + SEPARATOR);
			} else
				writer.print("0.0" + SEPARATOR);
			if(articleAttributes.has("latitude")){
				writer.print(articleAttributes.getDouble("latitude") + SEPARATOR);
			} else
				writer.print("0.0" + SEPARATOR);

		} else
			writer.print(SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR + "0.0" + SEPARATOR + "0.0" + SEPARATOR);

		if(doc.has("extra_author_attributes")){
			JSONObject authorAttributes = doc.getJSONObject("extra_author_attributes");
			if(authorAttributes.has("id")){
				writer.print(StringEscapeUtils.escapeCsv(authorAttributes.getString("id")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);
			if(authorAttributes.has("type")){
				writer.print(StringEscapeUtils.escapeCsv(authorAttributes.getString("type")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);
			if(authorAttributes.has("name")){
				writer.print(StringEscapeUtils.escapeCsv(authorAttributes.getString("name").replace("\\", "\\\\")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);
			if(authorAttributes.has("birthdate")){
				JSONObject o = authorAttributes.getJSONObject("birthdate");
				o.getString("date");
				writer.print(StringEscapeUtils.escapeCsv(authorAttributes.getJSONObject("birthdate").getString("date")) + SEPARATOR);
				writer.print(StringEscapeUtils.escapeCsv(authorAttributes.getJSONObject("birthdate").getString("resolution")) + SEPARATOR);
			} else
				writer.print("0" + SEPARATOR + SEPARATOR);
			if(authorAttributes.has("gender")){
				writer.print(StringEscapeUtils.escapeCsv(authorAttributes.getString("gender")) + SEPARATOR);
			} else
				writer.print("UNKNOWN" + SEPARATOR);
			if(authorAttributes.has("image_url")){
				writer.print(StringEscapeUtils.escapeCsv(authorAttributes.getString("image_url")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);
			if(authorAttributes.has("short_name")){
				writer.print(StringEscapeUtils.escapeCsv(authorAttributes.getString("short_name").replace("\\", "\\\\")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);
			if(authorAttributes.has("url")){
				writer.print(StringEscapeUtils.escapeCsv(authorAttributes.getString("url")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);

			if(authorAttributes.has("world_data")){
				JSONObject authorWolrdData = doc.getJSONObject("extra_author_attributes").getJSONObject("world_data");
				if(authorWolrdData.has("continent")){
					writer.print(StringEscapeUtils.escapeCsv(authorWolrdData.getString("continent")) + SEPARATOR);
				} else
					writer.print(SEPARATOR);
				if(authorWolrdData.has("country")){
					writer.print(StringEscapeUtils.escapeCsv(authorWolrdData.getString("country")) + SEPARATOR);
				} else
					writer.print(SEPARATOR);
				if(authorWolrdData.has("country_code")){
					writer.print(StringEscapeUtils.escapeCsv(authorWolrdData.getString("country_code")) + SEPARATOR);
				} else
					writer.print(SEPARATOR);
				if(authorWolrdData.has("region")){
					writer.print(StringEscapeUtils.escapeCsv(authorWolrdData.getString("region")) + SEPARATOR);
				} else
					writer.print(SEPARATOR);
				if(authorWolrdData.has("city")){
					writer.print(StringEscapeUtils.escapeCsv(authorWolrdData.getString("city")) + SEPARATOR);
				} else
					writer.print(SEPARATOR);
				if(authorWolrdData.has("longitude")){
					writer.print(authorWolrdData.getDouble("longitude") + SEPARATOR);
				} else
					writer.print("0.0" + SEPARATOR);
				if(authorWolrdData.has("latitude")){
					writer.print(authorWolrdData.getDouble("latitude") + SEPARATOR);
				} else
					writer.print("0.0" + SEPARATOR);
			} else
				writer.print(SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR + "0.0" + SEPARATOR + "0.0" + SEPARATOR);

		} else 
			writer.print(SEPARATOR + SEPARATOR + SEPARATOR + "0" + SEPARATOR + SEPARATOR + "UNKNOWN" + SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR + "0.0" + SEPARATOR + "0.0" + SEPARATOR);

		if(doc.has("extra_source_attributes") && doc.getJSONObject("extra_source_attributes").has("world_data")){
			JSONObject sourceWolrdData = doc.getJSONObject("extra_source_attributes").getJSONObject("world_data");
			if(sourceWolrdData.has("continent")){
				writer.print(StringEscapeUtils.escapeCsv(sourceWolrdData.getString("continent")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);
			if(sourceWolrdData.has("country")){
				writer.print(StringEscapeUtils.escapeCsv(sourceWolrdData.getString("country")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);
			if(sourceWolrdData.has("country_code")){
				writer.print(StringEscapeUtils.escapeCsv(sourceWolrdData.getString("country_code")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);
			if(sourceWolrdData.has("region")){
				writer.print(StringEscapeUtils.escapeCsv(sourceWolrdData.getString("region")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);
			if(sourceWolrdData.has("city")){
				writer.print(StringEscapeUtils.escapeCsv(sourceWolrdData.getString("city")) + SEPARATOR);
			} else
				writer.print(SEPARATOR);
			if(sourceWolrdData.has("longitude")){
				writer.print(sourceWolrdData.getDouble("longitude") + SEPARATOR);
			} else
				writer.print("0.0" + SEPARATOR);
			if(sourceWolrdData.has("latitude")){
				writer.print(sourceWolrdData.getDouble("latitude") + SEPARATOR);
			} else
				writer.print("0.0" + SEPARATOR);
		} else
			writer.print(SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR + SEPARATOR + "0.0" + SEPARATOR + "0.0" + SEPARATOR);

		if(doc.has("engagement"))
			writer.print(doc.getInt("engagement") + SEPARATOR);
		else
			writer.print("0" + SEPARATOR);

		if(doc.has("reach"))
			writer.print(doc.getInt("reach") + SEPARATOR);
		else
			writer.print("0" + SEPARATOR);

		if(doc.has("provider"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getString("provider")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("generator") && doc.getJSONObject("generator").has("type"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getJSONObject("generator").getString("type").replace("\\", "\\\\")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("source_extended_attributes") && doc.getJSONObject("source_extended_attributes").has("alexa_unique_visitors"))
			writer.print(doc.getJSONObject("source_extended_attributes").getInt("alexa_unique_visitors") + SEPARATOR);
		else
			writer.print("0" + SEPARATOR);

		if(doc.has("article_extended_attributes") && doc.getJSONObject("article_extended_attributes").has("twitter_likes"))
			writer.print(doc.getJSONObject("article_extended_attributes").getInt("twitter_likes") + SEPARATOR);
		else
			writer.print("0" + SEPARATOR);

		if(doc.has("extra_author_attributes") && doc.getJSONObject("extra_author_attributes").has("description"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getJSONObject("extra_author_attributes").getString("description")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("article_extended_attributes") && doc.getJSONObject("article_extended_attributes").has("linkedin_shares"))
			writer.print(doc.getJSONObject("article_extended_attributes").getInt("linkedin_shares") + SEPARATOR);
		else
			writer.print("0" + SEPARATOR);

		if(doc.has("extra_source_attributes") && doc.getJSONObject("extra_source_attributes").has("name"))
			writer.print(StringEscapeUtils.escapeCsv(doc.getJSONObject("extra_source_attributes").getString("name").replace("\\", "\\\\")) + SEPARATOR);
		else
			writer.print(SEPARATOR);

		if(doc.has("word_count"))
			writer.print(doc.getInt("word_count") + SEPARATOR);
		else
			writer.print("0" + SEPARATOR);
		writer.println("\"" + projectName + "\"");
	}

	public static void managePossibleMultipleTopicsAndTagsOnSameDocument(JSONObject existingDoc, JSONObject currentDoc){
		// FIXME CI SONO ERRORI DI JSON QUI
		JSONArray currentTopics = existingDoc.has("matched_profile")?existingDoc.getJSONArray("matched_profile"):new JSONArray();
		JSONArray possibleNewTopic = currentDoc.has("matched_profile")?currentDoc.getJSONArray("matched_profile"):new JSONArray();

		List<String> allTopicsWithDuplicates = Arrays.asList((currentTopics.join("§") + "§" + possibleNewTopic.join("§")).replaceAll("[\"]", "").split("§"));
		Set<String> hs = new HashSet<>();
		hs.addAll(allTopicsWithDuplicates);
		JSONArray mergedTopics = new JSONArray(hs);

		JSONArray currentTagsInternal = null;
		JSONArray possibleNewTagsInternal = null;

		if(existingDoc.has("tags_internal"))
			currentTagsInternal = existingDoc.getJSONArray("tags_internal");
		else
			currentTagsInternal = new JSONArray();
		if(currentDoc.has("tags_internal"))
			possibleNewTagsInternal = currentDoc.getJSONArray("tags_internal");
		else
			possibleNewTagsInternal = new JSONArray();
		List<String> allTagsInternalWithDuplicates = Arrays.asList((currentTagsInternal.join("§") + "§" + possibleNewTagsInternal.join("§")).replaceAll("[\"]", "").split("§"));
		hs.clear();
		hs.addAll(allTagsInternalWithDuplicates);
		JSONArray mergedTagsInternal = new JSONArray(hs);


		JSONArray currentTagsMarking = null;
		JSONArray possibleNewTagsMarking = null;
		if(existingDoc.has("tags_marking"))
			currentTagsMarking = existingDoc.getJSONArray("tags_marking");
		else
			currentTagsMarking = new JSONArray();
		if(currentDoc.has("tags_marking"))
			possibleNewTagsMarking = currentDoc.getJSONArray("tags_marking");
		else
			possibleNewTagsMarking = new JSONArray();
		List<String> allTagsMarkingWithDuplicates = Arrays.asList((currentTagsMarking.join("§") + "§" + possibleNewTagsMarking.join("§")).replaceAll("[\"]", "").split("§"));
		hs.clear();
		hs.addAll(allTagsMarkingWithDuplicates);
		JSONArray mergedTagsMarking = new JSONArray(hs);

		JSONArray currentTagsCustomer = null;
		JSONArray possibleNewTagsCustomer = null;
		if(existingDoc.has("tags_customer"))
			currentTagsCustomer = existingDoc.getJSONArray("tags_customer");
		else
			currentTagsCustomer = new JSONArray();
		if(currentDoc.has("tags_customer"))
			possibleNewTagsCustomer = currentDoc.getJSONArray("tags_customer");
		else
			possibleNewTagsCustomer = new JSONArray();
		List<String> allTagsCustomerWithDuplicates = Arrays.asList((currentTagsCustomer.join("§") + "§" + possibleNewTagsCustomer.join("§")).replaceAll("[\"]", "").split("§"));
		hs.clear();
		hs.addAll(allTagsCustomerWithDuplicates);
		JSONArray mergedTagsCustomer = new JSONArray(hs);

		// Saving new values
		if(mergedTopics.length() > 0)
			existingDoc.put("matched_profile", mergedTopics);
		if(mergedTagsInternal.length() > 0)
			existingDoc.put("tags_internal", mergedTagsInternal);
		if(mergedTagsMarking.length() > 0)
			existingDoc.put("tags_marking", mergedTagsMarking);
		if(mergedTagsCustomer.length() > 0)
			existingDoc.put("tags_customer", mergedTagsCustomer);
		ReportGeneration.retrievedDocuments.put(currentDoc.getString("url"), existingDoc);

	}
}
