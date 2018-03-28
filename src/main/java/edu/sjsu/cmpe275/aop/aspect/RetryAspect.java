package edu.sjsu.cmpe275.aop.aspect;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.TweetStatsImpl;

import org.aspectj.lang.annotation.Around;

@Aspect
@Order(1)
public class RetryAspect {
    
	@Autowired TweetStatsImpl stats;

	/**
	 * This advice retries the tweet, follow and block attempts thrice after the first attempt
	 * in case of a network failure
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Around("execution(public void edu.sjsu.cmpe275.aop.TweetService.*(..))")
	public void retryThreeAttempts(ProceedingJoinPoint joinPoint) throws Throwable{
		try {
			joinPoint.proceed();
		}
		catch(IOException e){
			System.out.println("time" + 1);
			try {
				joinPoint.proceed();
			}
			catch(IOException e1) {
				System.out.println("time" + 2);
				try {
					joinPoint.proceed();
				}
				catch(IOException e2) {
					System.out.println("time" + 3);
					try {
						joinPoint.proceed();
					}
					catch(IOException e3) {
						System.out.println("permanent");
						stats.permanentNetworkFailure = true;
					}
				}
			}
		} catch (IllegalArgumentException illegalArgument) {
			System.out.println(illegalArgument.getMessage());
			System.out.println();
		}
	}

}
