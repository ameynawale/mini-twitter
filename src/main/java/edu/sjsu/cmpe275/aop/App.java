package edu.sjsu.cmpe275.aop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        /***
         * Following is a dummy implementation of App to demonstrate bean creation with Application context.
         * You may make changes to suit your need, but this file is NOT part of the submission.
         */

    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
        TweetService tweeter = (TweetService) ctx.getBean("tweetService");
        TweetStats stats = (TweetStats) ctx.getBean("tweetStats");

        try {
            /*tweeter.tweet("alex", "first tweet321asdfasfd");
        	//tweeter.block("alex", "bob");
            tweeter.follow("bob", "alex");
            tweeter.follow("carl", "alex");
            tweeter.block("alex", "bob");
            tweeter.block("disney", "bob");
            tweeter.follow("bob", "alex"); // to test if the error for user following himself/herself
            tweeter.block("alex", "bob");
        	tweeter.block("bobby", "bobq");
            tweeter.tweet("bob", "first tweet1");
            tweeter.tweet("alex", "first twe");
            //stats.resetStatsAndSystem();
            tweeter.tweet("bob", "first tweet1");
            //tweeter.tweet("alex", "first tweasdfadfagsaga");*/
            
//            tweeter.follow("alex", "bob");
//            tweeter.follow("suhas", "bob");
//            tweeter.block("alex", "bob");
//            tweeter.block("suhas", "bob");
        	
        	/*tweeter.tweet("a", "msg");
        	tweeter.follow("a", "msg");
        	tweeter.block("a", "msg");
        	tweeter.follow("a", "x");
        	*/
        	tweeter.follow("a","x");
			tweeter.block("a","x");
			tweeter.follow("b","x");
			tweeter.block("b","x");
			
			tweeter.follow("b","y");
			//tweeter.follow("z","y");
        	
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Most productive user: " + stats.getMostProductiveUser());
        System.out.println("Most popular user: " + stats.getMostFollowedUser());
        System.out.println("Length of the longest tweet: " + stats.getLengthOfLongestTweetAttempted());
        System.out.println("Most unpopular follower " + stats.getMostBlockedFollower());
        ctx.close();
    }
}
