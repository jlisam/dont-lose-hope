package services;

import java.util.Iterator;

import models.Category;
import models.LostProperty;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.RangeKeyCondition;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import play.Application;
import play.Logger;
import play.Plugin;

public class DynamoDbPlugin extends Plugin {

	private final Application application;
	private static Table table;
	private static DynamoDB dynamoDB;
	private static AmazonDynamoDBClient dynamoDBClient;
	private static final String AWS_ACCESS_KEY = "aws.access.key";
	private static final String AWS_SECRET_KEY = "aws.secret.key";
	private static final String AWS_DYNAMODB_TABLE = "aws.dynamodb.table";

	public DynamoDbPlugin(Application application) {
		this.application = application;
	}

	/**
	 * Puts a {@link LostProperty} in the default table, which is configured
	 * under application.conf
	 * 
	 * @param LostProperty
	 */
	public static void putAllCategories(LostProperty lostProperty) {
		Logger.info("Persisting categories to Dynamo ...");
		DateTime time = new DateTime();
		for (Category category : lostProperty.getCategories()) {
			JSONObject categoryJSON = new JSONObject(category);
			Item item = new Item().withPrimaryKey("CategoryName", category.getCategoryName())
					.withLong("Timestamp", time.getMillis()).withJSON("document", categoryJSON.toString());
			table.putItem(item);
		}
	}

	/**
	 * Deletes a {@link Category} in the default table based on its name.
	 * 
	 * @param category
	 */
	public static void deleteCategory(Category category) {
		Logger.info("Deleting entire category {} ...", category.getCategoryName());
		table.deleteItem(new DeleteItemSpec().withPrimaryKey("CategoryName", category.getCategoryName()));
	}


	/**
	 * Retrieves the category from DynamoDB given its name
	 * 
	 * @param categoryName
	 * @return JSONObject
	 */
	public static JSONObject getCategory(String categoryName) {
		Logger.info("Getting category {} ...", categoryName);
		Item item = table.getItem(new GetItemSpec().withPrimaryKey("CategoryName", categoryName).withAttributesToGet(
				"document"));
		String categoryJSON = item.getJSONPretty("document");
		if (categoryJSON != null) {
			return new JSONObject(categoryJSON);
		} else {
			return new JSONObject();
		}
	}

	/**
	 * Retrieves a JSONArray containing category data from DynamoDB given its name with a time stamp
	 * greater than the number of days (represented in milliseconds) ago
	 * 
	 * @param categoryName Category Name
	 * @param days Number of days ago
	 * @return {@link JSONArray} containing the Category data
	 */
	public static JSONArray getCategory(String categoryName, int days) {
		Logger.info("Getting category {} for the past {} days...", categoryName, days);
		JSONArray jsonArray = new JSONArray();
		long millisecondsAgo = (new DateTime()).getMillis() - (days * 24L * 60L * 60L * 1000L);
		RangeKeyCondition rangeKeyCondition = new RangeKeyCondition("Timestamp").gt(millisecondsAgo);
		QuerySpec spec = new QuerySpec().withHashKey("CategoryName", categoryName).withRangeKeyCondition(
				rangeKeyCondition);
		ItemCollection<QueryOutcome> items = table.query(spec);
		Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()) {
			jsonArray.put(new JSONObject(iterator.next().toJSON()));
		}
		return jsonArray;
	}

	@Override
	public void onStart() {
		String accessKey = application.configuration().getString(AWS_ACCESS_KEY);
		String secretKey = application.configuration().getString(AWS_SECRET_KEY);
		String tablename = application.configuration().getString(AWS_DYNAMODB_TABLE);
		if (accessKey == null || secretKey == null || accessKey.isEmpty() || secretKey.isEmpty()) {
			dynamoDBClient = new AmazonDynamoDBClient(new DefaultAWSCredentialsProviderChain());
		} else {
			dynamoDBClient = new AmazonDynamoDBClient(new BasicAWSCredentials(accessKey, secretKey));
		}

		// defaults to US_WEST_2
		Region region = (Regions.getCurrentRegion() == null) ? Region.getRegion(Regions.US_WEST_2) : Regions
				.getCurrentRegion();
		dynamoDBClient.setRegion(region);

		dynamoDB = new DynamoDB(dynamoDBClient);
		table = dynamoDB.getTable(tablename);
	}

	@Override
	public boolean enabled() {
		return true;
	}
}
