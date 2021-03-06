package pact;

import java.util.logging.Level;
import java.util.logging.Logger;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import au.com.dius.pact.consumer.ConsumerPactTest;
import java.util.*;
import au.com.dius.pact.consumer.PactProviderRule;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Rule;
import org.junit.Assert;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;


public class PactDslArrayLikeTest extends ConsumerPactTest {

    Logger logger = Logger.getLogger(PactDslArrayLikeTest.class.getName());

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("testprovider", "localhost", 1234, this);

    String v3Path = "/v3";
            DslPart body = new PactDslJsonBody()
                    .object("meta")
                        .integerType("Count")
                        .eachLike("base")
                            .id()
                            .stringType("url")
                            .closeObject()
                        .closeArray()
                        .eachLike("stats")
                            .integerType("avg")
                            .integerType("Comments")
                            .integerType("daily")
                            .integerType("views")
                            .integerType("Dislikes")
                            .integerType("Likes")
                            .integerType("Views")
                            .integerType("totalC")
                            .integerType("totalV")
                            .closeObject()
                        .closeArray()
                    .closeObject()
                    .object("result")
                        .eachLike("packages")
                            .booleanType("instore")
                            .booleanType("custom")
                            .stringType("description")
                            .id()
                            .stringType("name")
                            .stringType("shortName")
                            .stringType("uri")
                            .eachLike("parents")
                                .id()
                                .stringType("uri")
                                .closeObject()
                            .closeArray()
                            .eachLike("stats")
                                .integerType("avg")
                                .integerType("comments")
                                .integerType("daily")
                                .integerType("views")
                                .integerType("Dislikes")
                                .integerType("Likes")
                                .integerType("Views")
                                .integerType("totalC")
                                .integerType("totalV")
                                .closeObject()
                            .closeArray()
                            .eachLike("omissions")
                                .closeObject()
                            .closeArray()
                            .eachLike("children")
                                .integerType("id")
                                .stringType("uri")
                                .closeObject()
                            .closeArray()
                            .closeObject()
                        .closeArray()
                    .closeObject();
    protected PactFragment createFragment(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");

        PactFragment fragment = builder
                .uponReceiving("API v3 endpoint response")
                    .path("/")
                    .method("GET")
                    .query("omit=23&descendantDepth=1")
                .willRespondWith()
                    .status(200)
                    .headers(headers)
                    .body(body)
                .toFragment();
        return fragment;
    }

    @Override
    protected String providerName() {
        return "testprovider";
    }

    @Override
    protected String consumerName() {
        return "testconsumer";
    }

    @Override
    protected void runTest(String url) {
        Map actualResponse;
        try {
            actualResponse = new ConsumerClient(url).getAsMap("/", "omit=23&descendantDepth=1");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
