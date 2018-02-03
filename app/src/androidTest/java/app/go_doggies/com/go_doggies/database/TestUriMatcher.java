package app.go_doggies.com.go_doggies.database;


import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by anto004 on 1/19/18.
 */

public class TestUriMatcher extends AndroidTestCase {
    // content://app.go_doggies.com.go_doggies
    private static final Uri TEST_CLIENT_DIR = DoggieContract.ClientEntry.CONTENT_URI;
    private static final Uri TEST_CLIENT_DETAILS_DIR = DoggieContract.ClientEntry.buildClientDetailUri(TestUtilities.TEST_CLIENT_ID_2);
    private static final Uri TEST_DOG_DIR = DoggieContract.DogEntry.CONTENT_URI;
    private static final Uri TEST_DOG_DIR_WITH_CLIENT_ID_DIR = DoggieContract.DogEntry.buildDogUriWithClientId(TestUtilities.TEST_CLIENT_ID_2);


    public void testUriMatcher() {
        UriMatcher testMatcher = DoggieProvider.buildUriMatcher();

        assertEquals("Error: The CLIENT URI was matched incorrectly.",
                testMatcher.match(TEST_CLIENT_DIR), DoggieProvider.CLIENTS);
        assertEquals("Error: The CLIENT WITH DETAILS URI was matched incorrectly.",
                testMatcher.match(TEST_CLIENT_DETAILS_DIR), DoggieProvider.CLIENTS_DETAILS);
        assertEquals("Error: The DOG URI was matched incorrectly.",
                testMatcher.match(TEST_DOG_DIR), DoggieProvider.DOGS);
        assertEquals("Error: The DOG WITH CLIENT ID URI was matched incorrectly.",
                testMatcher.match(TEST_DOG_DIR_WITH_CLIENT_ID_DIR), DoggieProvider.DOGS_WITH_CLIENT_ID);
    }
}
