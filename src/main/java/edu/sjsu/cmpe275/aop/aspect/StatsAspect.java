package edu.sjsu.cmpe275.aop.aspect;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.TweetStatsImpl;

@Aspect
@Order(0)
public class StatsAspect {
	
    @Autowired TweetStatsImpl stats;
	
    /**
     * This advice will check for the tweet length to be less than 140 characters,
     * update the length of longest tweet attempted if the previously attempted tweet is lesser than the current one,
     * update the most productive user
     * and check for network failure
     * @param joinPoint
     * @throws Throwable
     */
	@Around("execution(public void edu.sjsu.cmpe275.aop.TweetService.tweet(..))")
	public void updateTweetStats(ProceedingJoinPoint joinPoint) throws Throwable
	{
		System.out.println(
				"inside afterReturning of " + joinPoint.getSignature() + " " + joinPoint.getSignature().getName());
		String user = joinPoint.getArgs()[0].toString();
		String message = joinPoint.getArgs()[1].toString();

		if (message.length() > stats.lengthOfLongestTweetAttempted)
			stats.lengthOfLongestTweetAttempted = message.length();

		if (message.length() > 140) {
			System.out.println("Tweet cannot be more than 140 characters.");
		} else {
			joinPoint.proceed();

			if (!stats.permanentNetworkFailure) {
				if (stats.tweetLengths.containsKey(user)) {
					int userTweetsLength = stats.tweetLengths.get(user);
					stats.tweetLengths.put(user, (userTweetsLength + message.length()));
				} else {
					stats.tweetLengths.put(user, message.length());
				}
				if (stats.mostProductiveUser == null)
					stats.mostProductiveUser = user;
				if (stats.tweetLengths.get(user) > stats.tweetLengths.get(stats.mostProductiveUser))
					stats.mostProductiveUser = user;
			}
		}
		stats.permanentNetworkFailure = false;
	}
	
	/**
	 * This advice checks if the user requesting to follow is already blocked,
	 * if the user is trying to follow himself/herself,
	 * check if the user is already following the user for which the follow request is attempted,
	 * checks for the network failure
	 * and update the followers list
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Around("execution(public void edu.sjsu.cmpe275.aop.TweetService.follow(..))")
	public void updateFollowerStats(ProceedingJoinPoint joinPoint) throws Throwable
	{
		System.out.println(
				"inside afterReturning of " + joinPoint.getSignature() + " " + joinPoint.getSignature().getName());
		String follower = joinPoint.getArgs()[0].toString();
		String followee = joinPoint.getArgs()[1].toString();

		if (stats.blockersList.containsKey(follower) && stats.blockersList.get(follower).contains(followee)) {
			System.out.printf("%s is blocked by %s", follower, followee);
		} else if (follower.equals(followee)) {
			System.out.printf("%s cannot follow himself/herself", follower);
		} else if (stats.followersList.containsKey(followee) && stats.followersList.get(followee).contains(follower)) {
			System.out.printf("%s already follows %s", follower, followee);
		} else {
			joinPoint.proceed();
			if (!stats.permanentNetworkFailure) {
				HashSet followers = new HashSet();
				if (stats.followersList.containsKey(followee)) {
					followers = stats.followersList.get(followee);
				}
				followers.add(follower);
				stats.followersList.put(followee, followers);
				if (stats.mostFollowedUser == null)
					stats.mostFollowedUser = followee;
				else {
					if (stats.followersList.get(stats.mostFollowedUser).size() < followers.size()) {
						stats.mostFollowedUser = followee;
					} else if (stats.followersList.get(stats.mostFollowedUser).size() == followers.size()) {
						if (stats.mostFollowedUser.compareTo(followee) > 0)
							stats.mostFollowedUser = followee;
					}
				}
			}
		}
		stats.permanentNetworkFailure = false;
	}

	/**
	 * This advice checks if the user is already blocked by the user,
	 * checks if the user is trying to block himself/herself,
	 * checks for the network failure,
	 * updates the blockers list
	 * and updates the followers list if needed
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Around("execution(public void edu.sjsu.cmpe275.aop.TweetService.block(..))")
	public void updateBlockerStats(ProceedingJoinPoint joinPoint) throws Throwable
	{
		System.out.println(
				"inside afterReturning of " + joinPoint.getSignature() + " " + joinPoint.getSignature().getName());
		String user = joinPoint.getArgs()[0].toString();
		String follower = joinPoint.getArgs()[1].toString();

		if (stats.blockersList.containsKey(follower) && stats.blockersList.get(follower).contains(user)) {
			System.out.printf("%s is already blocked by %s", follower, user);
		} else if (follower.equals(user)) {
			System.out.printf("%s cannot block himself/herself", follower);
		} else {
			joinPoint.proceed();
				if (!stats.permanentNetworkFailure) {
					HashSet blockers = new HashSet();
					if (stats.blockersList.containsKey(follower)) {
						blockers = stats.blockersList.get(follower);
					}
					blockers.add(user);
					stats.blockersList.put(follower, blockers);
					if (stats.mostBlockedFollower == null)
						stats.mostBlockedFollower = follower;
					else {
						if (stats.blockersList.get(stats.mostBlockedFollower).size() < blockers.size()) {
							stats.mostBlockedFollower = follower;
						} else if (stats.blockersList.get(stats.mostBlockedFollower).size() == blockers.size()) {
							if (stats.mostBlockedFollower.compareTo(follower) > 0)
								stats.mostBlockedFollower = follower;
						}
					}
					if (stats.followersList.containsKey(user) && stats.followersList.get(user).contains(follower)) {
						HashSet followers = stats.followersList.get(user);
						followers.remove(follower);
						stats.followersList.put(user, followers);
					}
					if (stats.followersList.containsKey(follower) && stats.followersList.get(follower).contains(user)) {
						HashSet followers = stats.followersList.get(follower);
						followers.remove(user);
						stats.followersList.put(follower, followers);
					}
					int followCount;
					if(stats.followersList.containsKey(user))
						followCount = stats.followersList.get(user).size();
					else
						followCount = 0;
					String followedUser = user;
					for (Map.Entry<String, HashSet<String>> entry : stats.followersList.entrySet()) {
						if ((entry.getValue().size() == followCount && (entry.getKey().compareTo(followedUser) <= 0))
								|| entry.getValue().size() > followCount) {
							followCount = entry.getValue().size();
							followedUser = entry.getKey();
						}
					}
					if (followCount > 0) {
						stats.mostFollowedUser = followedUser;
					} else {
						stats.mostFollowedUser = null;
					} 
				}
		}
		stats.permanentNetworkFailure = false;
	}
}
