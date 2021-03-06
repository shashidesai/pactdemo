package pact;

import java.util.logging.Level;
import java.util.logging.Logger;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import au.com.dius.pact.consumer.ConsumerPactTest;
import java.util.Map;
import java.util.HashMap;
import au.com.dius.pact.consumer.PactProviderRule;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Rule;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import org.junit.Assert;


public class PactDslNestedJsonTest extends ConsumerPactTest {

    Logger logger = Logger.getLogger(PactDslJsonTest.class.getName());

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("test_provider-3", "localhost", 1234, this);

    private DslPart body = new PactDslJsonBody()
            .id()
            .object("2")
                .id()
                .stringValue("test", null)
            .closeObject()
            .array("numbers")
                .id()
                .number(100)
                .numberValue(101)
                .hexValue()
                .object()
                    .id()
                    .stringValue("name", "Rogger the Dogger")
                    .timestamp()
                    .date("dob", "MM/dd/yyyy")
                .closeObject()
            .closeArray();
    protected PactFragment createFragment(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");

        PactFragment fragment = builder
                .uponReceiving("API nested json response")
                .path("/")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(headers)
                .body(body)
                .toFragment();
        return fragment;
    }

    @Override
    protected String providerName() {
        return "test_provider-3";
    }

    @Override
    protected String consumerName() {
        return "test_consumer-3";
    }

    @Override
    protected void runTest(String url) {
        Map actualResponse;
        try {
            actualResponse = new ConsumerClient(url).getAsMap("/", "");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
