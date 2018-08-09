package tw.holidaybear.graphql.android

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.rx2.Rx2Apollo
import com.google.common.truth.Truth.assertThat
import okhttp3.OkHttpClient

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import tw.holidaybear.graphql.android.type.SearchType
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class Rx2ApolloTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apolloClient: ApolloClient

    @Before
    @Throws(IOException::class)
    fun createService() {
        mockWebServer = MockWebServer()
        apolloClient = ApolloClient.builder()
                .serverUrl(mockWebServer.url("/"))
                .okHttpClient(OkHttpClient.Builder().build())
                .build()
    }

    @After
    @Throws(IOException::class)
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun testResponseFormat() {
        enqueueResponse("search.json")

        Rx2Apollo.from(apolloClient.query(TrendQuery(Input.fromNullable(5), "test", SearchType.REPOSITORY)))
            .test()
            .awaitDone(5, TimeUnit.SECONDS)
            .assertNoErrors()
            .assertComplete()
            .assertValue { response ->
                assertThat((response.data()?.search()?.edges()?.first()?.node() as TrendQuery.AsRepository).owner().login()).isEqualTo("google")
                true
            }
    }

    @Throws(IOException::class)
    fun enqueueResponse(fileName: String) {
        val inputStream = javaClass.classLoader.getResourceAsStream(fileName)
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody(source.readString(StandardCharsets.UTF_8)))
    }
}