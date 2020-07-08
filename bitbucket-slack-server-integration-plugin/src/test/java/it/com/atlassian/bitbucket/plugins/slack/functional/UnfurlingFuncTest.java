package it.com.atlassian.bitbucket.plugins.slack.functional;

import com.atlassian.plugins.slack.test.mockserver.RequestHistoryItem;
import com.github.seratch.jslack.api.methods.request.chat.ChatUnfurlRequest;
import it.com.atlassian.bitbucket.plugins.slack.util.SlackFunctionalTestBase;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.atlassian.bitbucket.test.DefaultFuncTestData.getProject1;
import static com.atlassian.bitbucket.test.DefaultFuncTestData.getProject1Repository1;
import static com.atlassian.plugins.slack.test.RequestMatchers.hasHit;
import static com.atlassian.plugins.slack.test.RequestMatchers.requestEntityProperty;
import static com.github.seratch.jslack.api.methods.Methods.CHAT_UNFURL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;

public class UnfurlingFuncTest extends SlackFunctionalTestBase {
    @Test
    void fileUnfurling() {
        connectToDummyTeamWithCustomApp();
        confirmAdminAccount();

        String fileUrl = client.admin().getDefaultBaseUrl()
                + "/projects/" + getProject1() + "/repos/" + getProject1Repository1()
                + "/browse/add_file/add_file.txt";

        server.clearHistoryExecuteAndWaitForNewRequest(CHAT_UNFURL, () ->
                client.admin().events().linkShared(fileUrl));

        List<RequestHistoryItem> history = server.requestHistoryForTest();
        assertThat(history, hasHit(CHAT_UNFURL, contains(
                requestEntityProperty(ChatUnfurlRequest::getUnfurls, allOf(
                        containsString("add_file.txt"),
                        containsString("in branch")))
        )));
    }
}
