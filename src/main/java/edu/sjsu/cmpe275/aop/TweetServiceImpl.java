package edu.sjsu.cmpe275.aop;

import java.io.IOException;

public class TweetServiceImpl implements TweetService {

    /***
     * Following is a dummy implementation.
     * You can tweak the implementation to suit your need, but this file is NOT part of the submission.
     */
	
	public int tweetAttempts = 0;
	public int followAttempts = 0;
	public int blockAttempts = 0;

	@Override
    public void tweet(String user, String message) throws IllegalArgumentException, IOException {
    	if(tweetAttempts < 3)
    	{
    		tweetAttempts++;
    		throw new IOException();
    	}
    	else if(message.length() > 140)
    	{
    		throw new IllegalArgumentException("Message length more than 140 characters.");
    	}
    	System.out.printf("User %s tweeted message: %s\n", user, message);
    	
    }

	@Override
    public void follow(String follower, String followee) throws IOException {
    	if(followAttempts < 3)
    	{
    		followAttempts++;
    		throw new IOException();
    	}
       	System.out.printf("User %s followed user %s \n", follower, followee);
    }

	@Override
	public void block(String user, String follower) throws IOException {
    	if(blockAttempts < 3)
    	{
    		blockAttempts++;
    		throw new IOException();
    	}
       	System.out.printf("User %s blocked user %s \n", user, follower);		
	}

}
