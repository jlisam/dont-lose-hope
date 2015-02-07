package controllers;

import java.io.StringReader;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import models.Category;
import models.LostProperty;
import play.Configuration;
import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.mvc.Controller;
import play.mvc.Result;
import services.DynamoDbPlugin;

import static services.DynamoDbPlugin.getCategory;
import static services.DynamoDbPlugin.putAllCategories;

public class LostAndFound extends Controller {

	private static final String APPLICATION_JSON_TYPE = "application/json";
	private static final String LOST_PROPERTY_MTA_URL = "mta.lostproperty.url";
	private static Configuration config = Play.application().configuration();
	public static String LOST_PROPERTY_MTA_API_URL = config.getString(LOST_PROPERTY_MTA_URL);

	public static Promise<Result> getLatestData() {
		Promise<LostProperty> lostPropertyPromise = getLatestDataFromMTA();
		LostProperty lostProperty = lostPropertyPromise.get(30, TimeUnit.SECONDS);
		storeLostProperty(lostProperty);
		Promise<JSONObject> lostPropertyJson = null;
		lostPropertyJson = lostPropertyPromise.map(lostProp -> new JSONObject(lostProp));
		return lostPropertyJson.map(json -> ok("" + json).as(APPLICATION_JSON_TYPE));
	}

	public static Promise<Result> getLastItems(String categoryName, int days) {
		JSONArray pastDaysCategories = getCategory(categoryName, days);
		return Promise.promise(() -> ok("" + pastDaysCategories).as(APPLICATION_JSON_TYPE));
	}

	public static Promise<Result> getDataForCategory(String categoryName) {
		Promise<JSONObject> categoryJSON = null;
		JSONObject selectedCategoryJSON = DynamoDbPlugin.getCategory(categoryName);
		if (selectedCategoryJSON.length() == 0) {
			Promise<LostProperty> lostPropertyPromise = getLatestDataFromMTA();
			Promise<Category> categoryPromise = lostPropertyPromise.map(lostProperty -> lostProperty.getCategories()
					.stream().filter(c -> c.getCategoryName().equals(categoryName)).findFirst().get());

			categoryJSON = categoryPromise.map(category -> new JSONObject(category));

			return categoryJSON.map(json -> ok("" + json.toString()));
		} else {
			return Promise.promise(() -> ok("" + selectedCategoryJSON).as(APPLICATION_JSON_TYPE));
		}
	}

	private static Promise<LostProperty> getLatestDataFromMTA() {
		WSRequestHolder holder = WS.url(LOST_PROPERTY_MTA_API_URL);
		Promise<String> result = holder.get().map(response -> response.getBody());
		JAXBContext jaxbContext;
		Promise<LostProperty> lostPropertyPromise = null;
		try {
			jaxbContext = JAXBContext.newInstance(LostProperty.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			lostPropertyPromise = result.map(xml -> (LostProperty) unmarshaller.unmarshal(new StringReader(xml)));

		} catch (JAXBException e) {
			Logger.error("Something went wrong while unmarshalling data from MTA", ExceptionUtils.getRootCause(e));
		}
		return lostPropertyPromise;
	}

	private static void storeLostProperty(LostProperty lostProperty) {
		putAllCategories(lostProperty);
	}

}
