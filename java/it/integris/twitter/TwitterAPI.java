/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.integris.twitter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.integris.talkwalker.TWSearchAPI;
import twitter4j.GeoLocation;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.auth.AccessToken;

public class TwitterAPI {

	private ArrayList<TwitterCredentials> poolTwitterCredentials = null;
	private int sleepTime = -1; 
	private int currentTwitterAccount = 0;

	public TwitterAPI(int sleepTime){
		this.sleepTime = sleepTime;
		poolTwitterCredentials = new ArrayList<>();
	}

	public int getSleeptime() {
		return this.sleepTime;
	}

	public int setSleeptime() {
		return this.sleepTime;
	}

	public boolean addTwitterCredentials(TwitterCredentials twCredentials) {
		return this.poolTwitterCredentials.add(twCredentials);
	}

	public boolean removeTwitterCredentials(String consumerKey) {
		int i = 0;
		boolean found = false;
		while(!found && i < this.poolTwitterCredentials.size()){
			if(this.poolTwitterCredentials.get(i).getConsumerKey().equals(consumerKey)) {
				this.poolTwitterCredentials.remove(i);
				found = true;
			}
			i++;
		}
		return found;
	}

	public void cleanCredentialsPool() {
		// Clean the pool
		this.poolTwitterCredentials.clear();
		this.poolTwitterCredentials = new ArrayList<>();
	}

	public ArrayList<JSONObject> twitterLookup(HashMap<Long, JSONObject> twitterDocuments) {
		// Return twitter lookup
		ArrayList<JSONObject> returnValue = new ArrayList<>();
		try {
			TwitterCredentials credentials = this.poolTwitterCredentials.get(this.currentTwitterAccount);
			this.currentTwitterAccount = (this.currentTwitterAccount + 1) % this.poolTwitterCredentials.size();
			// Cleaning name
			TwitterFactory factory = new TwitterFactory();
			AccessToken accessToken = new AccessToken(credentials.getAccessToken(), credentials.getAccessTokenSecret()); 
			Twitter twitter = factory.getInstance();
			twitter.setOAuthConsumer(credentials.getConsumerKey(), credentials.getConsumerSecret());
			twitter.setOAuthAccessToken(accessToken);
			ResponseList<Status> lookupedTweets;
			Long[] twitterIDsLong = twitterDocuments.keySet().toArray(new Long[twitterDocuments.size()]);
			ArrayList<Long> enrichedTweets = new ArrayList<>();
			lookupedTweets = twitter.lookup(ArrayUtils.toPrimitive(twitterIDsLong));
			for(Status lookupedTweet : lookupedTweets) {
				JSONObject currentTweet = twitterDocuments.get(lookupedTweet.getId());
				URL aURL = null;
				try {
					//********** INTERVENTO PER AGGIORNAMENTO POLICY TWITTER DEL 3/09/2018***********//

//					aURL = new URL(currentTweet.getString("url"));
//					currentTweet.put("root_url", "http://" + aURL.getHost() + "/" + aURL.getPath().split("[/]")[1] + "/");

					aURL = new URL("https://twitter.com/statuses/");
					currentTweet.put("root_url", "http://" + aURL.getHost() + "/" + aURL.getPath().split("[/]")[1] + "/");
					currentTweet.put("host_url", "http://twitter.com/");
					currentTweet.put("domain_url", "http://twitter.com/");
					//********** FINE ***********//

				} catch (MalformedURLException | JSONException e) {
					e.printStackTrace();
					continue;
				}
				currentTweet.put("published", lookupedTweet.getCreatedAt().getTime());
				JSONObject articleAttributes = new JSONObject();
				articleAttributes.put("twitter_retweets", lookupedTweet.getRetweetCount());
				currentTweet.put("article_extended_attributes", articleAttributes);
				JSONObject authorAttributes = new JSONObject();
				authorAttributes.put("short_name", lookupedTweet.getUser().getScreenName());
				authorAttributes.put("name", lookupedTweet.getUser().getName());
				if(currentTweet.has("external_author_id"))
					authorAttributes.put("id", "tw:" + currentTweet.getLong("external_author_id"));
				currentTweet.put("extra_author_attributes", authorAttributes);
				currentTweet.put("content", lookupedTweet.getText());

				returnValue.add(currentTweet);
				enrichedTweets.add(lookupedTweet.getId());
			}
			if(enrichedTweets.size() < twitterIDsLong.length){
				// Log all the documents that fails enrichment
				for(int i = 0; i < twitterIDsLong.length; i++){
					if(!enrichedTweets.contains(twitterIDsLong[i]))
						// Not enrichedDocument -> log it
						TWSearchAPI.LOGGER.info("TWITTER ENRICHMENT PHASE FAILED: Tweet-url = " + twitterDocuments.get(twitterIDsLong[i]).getString("url"));
					
				}
				
			}
		} catch (TwitterException te) {
			te.printStackTrace();
			return null;
		}
		return returnValue;
	}
}
