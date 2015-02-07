package services;

import java.util.concurrent.TimeUnit;

import controllers.LostAndFound;
import play.libs.Akka;
import scala.concurrent.duration.Duration;

public class MTALatestDataService {

	public static void scheduleRequestForNewData() {
		Akka.system()
				.scheduler()
				.schedule(Duration.create(0, TimeUnit.MILLISECONDS), Duration.create(60, TimeUnit.MINUTES),
						new Runnable() {
							@Override
							public void run() {
								LostAndFound.getLatestData();
							}
						}, Akka.system().dispatcher());
	}
}
