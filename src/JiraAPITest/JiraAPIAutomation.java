package JiraAPITest;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraAPIAutomation {

	public static void main(String[] args) {

		// End to End Automation Testing in Jira

		// Login Process

		RestAssured.baseURI = "http://localhost:2111/";

		SessionFilter session = new SessionFilter();

		String id = given().relaxedHTTPSValidation().log().all().header("Content-Type", "application/json")

				.body("{ \"username\": \"dhokiya1997\", \"password\": \"Job@2022\" }").filter(session).when()

				.post("rest/auth/1/session").then().log().all().assertThat().statusCode(200).extract().response()

				.asString();

		System.out.println(id);

		JsonPath js = new JsonPath(id);

		String nameofSessionId = js.getString("session.name");

		String valueofSessionId = js.getString("session.value");

		System.out.println("This is name of Session id :  " + nameofSessionId);

		System.out.println("This is Session id and will use for further requests :  " + valueofSessionId);

		// Issue Creating

		String issueResponse = given().log().all().header("Content-Type", "application/json")

				.body("{\r\n" + "     \"fields\": {\r\n" + "        \"project\": {\r\n"
						+ "            \"key\": \"API\"\r\n" + "        },\r\n"
						+ "        \"summary\": \" Login Defect \",\r\n"
						+ "        \"description\": \"created issue by API call through RestAssured\",\r\n"
						+ "        \r\n" + "        \"issuetype\": {\r\n" + "            \"name\": \"Bug\"\r\n"
						+ "        }\r\n" + "     }\r\n" + "     }")

				.filter(session).when().post("/rest/api/2/issue/").then().log().all().statusCode(201).extract()

				.response().asString();

		System.out.println(issueResponse);

		JsonPath jn = new JsonPath(issueResponse);

		String keyValue = jn.getString("key");

		System.out.println("This is the key : " + keyValue);

		// Adding Comments in Created Issue

		String AddCommentExpected = "Added Comment by API through RestAssured using Dynamic payload";

		String commentt = given().log().all().pathParam("key", keyValue).header("Content-Type", "application/json")
				.body(" {\r\n" + "    \"body\": \"" + AddCommentExpected + "\",\r\n" + "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n" + "        \"value\": \"Administrators\"\r\n" + "    }\r\n"
						+ "}")

				.filter(session).when().post("rest/api/2/issue/{key}/comment").then().log().all().assertThat()

				.statusCode(201).extract().response().asString();

		JsonPath jc = new JsonPath(commentt);

		String commentid = jc.get("id");

		System.out.println(commentid);

		System.out.println("Comment is Added Successfully Please Check in Jira");

		// Adding Attachemnt by API call

		given().log().all().header("Content-Type", "multipart/form-data").pathParam("key", keyValue)
				.header("X-Atlassian-Token", " no-check")

				.filter(session).multiPart("file", new File("JiraAttachment.txt")).when()

				.post("rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);

		System.out.println("Attachment is Added Successfully Please Check in Jira");

		// Geeting issue details and verify if added comment and attachment exists or
		// not !

		String response = given().log().all().pathParam("key", keyValue).queryParam("fields", "comment").filter(session)

				.when().get("rest/api/2/issue/{key}").then().log().all().assertThat().statusCode(200).extract()
				.response().asString();

		System.out.println(response);

		JsonPath jb = new JsonPath(response);

		int count1 = jb.getInt("fields.comment.comments.size()");

		System.out.println(count1);

		for (int i = 0; i < count1; i++) {

			if ((jb.get("fields.comment.comments[" + i + "].id").toString()).equals(commentid)) {

				String actualCommentinjson = jb.getString("fields.comment.comments[" + i + "].body");

				System.out.println(actualCommentinjson);

				Assert.assertEquals(actualCommentinjson, AddCommentExpected);

				System.out.println("comment verification is successfull");

			}

		}

		System.out.println("End to End Jira API Testing is complete");

	}

}
