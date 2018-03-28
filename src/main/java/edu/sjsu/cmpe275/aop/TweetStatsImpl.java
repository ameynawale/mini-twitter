package edu.sjsu.cmpe275.aop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class TweetStatsImpl implements TweetStats {
	
    
	public static int lengthOfLongestTweetAttempted = 0;
	public static String mostFollowedUser = null;
	public static String mostProductiveUser = null;
	public static String mostBlockedFollower = null;
	public static boolean permanentNetworkFailure = false;
	
	public static TreeMap<String, Integer> tweetLengths = new TreeMap<String, Integer>();
	public static TreeMap<String, HashSet<String>> followersList= new TreeMap<String, HashSet<String>>();
	public static TreeMap<String, HashSet<String>> blockersList = new TreeMap<String, HashSet<String>>();
	
	
	
	/**
	 * This method resets all the data from the previous operations
	 */
	@Override
	public void resetStatsAndSystem() {
		lengthOfLongestTweetAttempted = 0;
		mostFollowedUser = null;
		mostProductiveUser = null;
		mostBlockedFollower = null;
		permanentNetworkFailure = false;
		
		tweetLengths.clear();
		followersList.clear();
		blockersList.clear();
	}
    
	/**
	 * This method returns the length of the longest tweet attempted
	 */
	@Override
	public int getLengthOfLongestTweetAttempted() {
		return lengthOfLongestTweetAttempted;
	}

	/**
	 * This method returns the most followed user
	 */
	@Override
	public String getMostFollowedUser() {
		return mostFollowedUser;
	}

	/**
	 * This method returns the most productive user
	 */
	@Override
	public String getMostProductiveUser() {
		return mostProductiveUser;
	}

	/**
	 * This method returns the most blocked user
	 */
	@Override
	public String getMostBlockedFollower() {
		return mostBlockedFollower;
	}
}