package universum.studios.android.intent;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
public class DialerIntentTest extends RobolectricTestCase {

	@SuppressWarnings("unused")
	private static final String TAG = "DialerIntentTest";

	@Test
	public void testUriScheme() {
		assertThat(DialerIntent.URI_SCHEME, is("tel"));
	}

	@Test
	public void testPhoneNumberDefault() {
		assertThat(new DialerIntent().phoneNumber(), is(""));
	}
}
