/*
 * *************************************************************************************************
 *                                 Copyright 2016 Universum Studios
 * *************************************************************************************************
 *                  Licensed under the Apache License, Version 2.0 (the "License")
 * -------------------------------------------------------------------------------------------------
 * You may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 * *************************************************************************************************
 */
package universum.studios.android.intent;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static universum.studios.android.intent.CalendarTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
@SuppressWarnings("ResourceType")
public final class CalendarIntentTest extends RobolectricTestCase {

	@Test public void testInstantiation() {
		// Arrange:
		final long currentTime = System.currentTimeMillis();
		// Act:
		final CalendarIntent intent = new CalendarIntent();
		// Assert:
		assertThat(intent.type(), is(CalendarIntent.TYPE_VIEW));
		assertThat(intent.eventId(), is(-1L));
		assertThatTimeIsInRange(intent.time(), currentTime, currentTime + 10);
		assertThatTimeIsInRange(intent.beginTime(), currentTime, currentTime + 10);
		assertThatTimeIsInRange(intent.endTime(), currentTime + 1, currentTime + 11);
		assertThat(intent.title(), is((CharSequence) ""));
		assertThat(intent.description(), is((CharSequence) ""));
		assertThat(intent.location(), is((CharSequence) ""));
		assertThat(intent.availability(), is(CalendarIntent.AVAILABILITY_BUSY));
	}

	@Test public void testType() {
		// Arrange:
		final CalendarIntent intent = new CalendarIntent();
		// Act:
		intent.type(CalendarIntent.TYPE_EDIT_EVENT);
		// Assert:
		assertThat(intent.type(), is(CalendarIntent.TYPE_EDIT_EVENT));
	}

	@Test public void testEventId() {
		// Arrange:
		final CalendarIntent intent = new CalendarIntent();
		// Act:
		intent.eventId(123523);
		// Assert:
		assertThat(intent.eventId(), is(123523L));
	}

	@Test public void testTime() {
		// Arrange:
		final CalendarIntent intent = new CalendarIntent();
		// Act:
		intent.time(3232);
		// Assert:
		assertThat(intent.time(), is(3232L));
	}

	@Test public void testBeginTime() {
		// Arrange:
		final CalendarIntent intent = new CalendarIntent();
		// Act:
		intent.beginTime(20);
		// Assert:
		assertThat(intent.beginTime(), is(20L));
	}

	@Test public void testEndTime() {
		// Arrange:
		final CalendarIntent intent = new CalendarIntent();
		// Act:
		intent.endTime(13);
		// Assert:
		assertThat(intent.endTime(), is(13L));
	}

	@Test public void testTitle() {
		// Arrange:
		final CalendarIntent intent = new CalendarIntent();
		// Act:
		intent.title("New event");
		// Assert:
		assertThat(intent.title().toString(), is("New event"));
	}

	@Test public void testDescription() {
		// Arrange:
		final CalendarIntent intent = new CalendarIntent();
		// Act:
		intent.description("Event's description text.");
		// Assert:
		assertThat(intent.description().toString(), is("Event's description text."));
	}

	@Test public void testLocation() {
		// Arrange:
		final CalendarIntent intent = new CalendarIntent();
		// Act:
		intent.location("Rome");
		// Assert:
		assertThat(intent.location().toString(), is("Rome"));
	}

	@Test public void testAvailability() {
		// Arrange:
		final CalendarIntent intent = new CalendarIntent();
		// Act:
		intent.availability(CalendarIntent.AVAILABILITY_FREE);
		// Assert:
		assertThat(intent.availability(), is(CalendarIntent.AVAILABILITY_FREE));
	}

	@Test public void testBuildTypeOfView() {
		// Arrange:
		final long currentTime = System.currentTimeMillis();
		final CalendarIntent calendarIntent = new CalendarIntent();
		calendarIntent.type(CalendarIntent.TYPE_VIEW);
		final long time = currentTime - 1000 * 60 * 60 * 24;
		calendarIntent.time(time);
		// Act:
		final Intent intent = calendarIntent.build(application);
		// Assert:
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getData(), is(Uri.parse("content://" + CalendarContract.AUTHORITY + "/time/" + Long.toString(time))));
	}

	@Test public void testBuildTypeOfViewWithInvalidTime() {
		// Arrange:
		final CalendarIntent intent = new CalendarIntent();
		intent.type(CalendarIntent.TYPE_VIEW);
		intent.time(-1000);
		// Act + Assert:
		assertThatBuildThrowsExceptionWithMessage(
				application,
				intent,
				"Specified invalid time(-1000) where to open calendar for view. Must be none-negative time value."
		);
	}

	@Test public void testBuildTypeOfInsertEvent() {
		// Arrange:
		final long currentTime = System.currentTimeMillis();
		final CalendarIntent calendarIntent = new CalendarIntent();
		// INSERT EVENT type is the default one.
		// intent.type(CalendarIntent.TYPE_INSERT_EVENT);
		final long beginTime = currentTime;
		final long endTime = beginTime + 1000 * 60 * 60 * 12;
		calendarIntent.type(CalendarIntent.TYPE_INSERT_EVENT)
				.title("Event title")
				.description("Event description.")
				.location("Location of the event")
				.beginTime(beginTime).endTime(endTime)
				.availability(CalendarIntent.AVAILABILITY_FREE);
		// Act:
		final Intent intent = calendarIntent.build(application);
		// Assert:
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getLongExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, -1), is(beginTime));
		assertThat(intent.getLongExtra(CalendarContract.EXTRA_EVENT_END_TIME, -1), is(endTime));
		assertThat(intent.getCharSequenceExtra(CalendarContract.Events.TITLE).toString(), is("Event title"));
		assertThat(intent.getCharSequenceExtra(CalendarContract.Events.DESCRIPTION).toString(), is("Event description."));
		assertThat(intent.getCharSequenceExtra(CalendarContract.Events.EVENT_LOCATION).toString(), is("Location of the event"));
		assertThat(intent.getIntExtra(CalendarContract.Events.AVAILABILITY, -1), is(CalendarIntent.AVAILABILITY_FREE));
	}

	@Test public void testBuildTypeOfInsertEventWithInvalidBeginTime() {
		// Arrange:
		final CalendarIntent intent = new CalendarIntent();
		intent.type(CalendarIntent.TYPE_INSERT_EVENT);
		intent.beginTime(-1);
		// Act + Assert:
		assertThatBuildThrowsExceptionWithMessage(
				application,
				intent,
				"Specified invalid begin time(-1). Must be none-negative time value."
		);
	}

	@Test public void testBuildTypeOfInsertEventWithInvalidEndTime() {
		// Arrange:
		final CalendarIntent intent = new CalendarIntent();
		intent.type(CalendarIntent.TYPE_INSERT_EVENT);
		intent.endTime(-5);
		// Act + Assert:
		assertThatBuildThrowsExceptionWithMessage(
				application,
				intent,
				"Specified invalid end time(-5). Must be none-negative time value."
		);
	}

	@Test public void testBuildTypeOfInsertEventWithInvalidEndVsBeginTime() {
		// Arrange:
		final CalendarIntent intent = new CalendarIntent();
		intent.type(CalendarIntent.TYPE_INSERT_EVENT);
		intent.endTime(0);
		try {
			// Act:
			intent.build(application);
		} catch (IllegalArgumentException e) {
			// Assert:
			final String message = "Cannot build " + intent.getClass().getSimpleName() + ". Specified end time(0) is before/at begin";
			final String eMessage = e.getMessage();
			if (!eMessage.startsWith(message)) {
				throw new AssertionError(
						"Expected exception with message <" + message + "...> but message was <" + eMessage + "...>"
				);
			}
			return;
		}
		final String intentName = intent.getClass().getSimpleName();
		throw new AssertionError("No exception has been thrown while building intent(" + intentName + ").");
	}

	@Test public void testBuildTypeOfEditEvent() {
		// Arrange:
		final CalendarIntent calendarIntent = new CalendarIntent();
		calendarIntent.type(CalendarIntent.TYPE_EDIT_EVENT);
		calendarIntent.eventId(123);
		calendarIntent.title("New event title");
		// Act:
		final Intent intent = calendarIntent.build(application);
		// Assert:
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_EDIT));
		assertThat(intent.getData(), is(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, 123)));
		assertThat(intent.getCharSequenceExtra(CalendarContract.Events.TITLE).toString(), is("New event title"));
	}

	@Test public void testBuildTypeOfEditEventWithoutTitle() {
		// Arrange:
		final CalendarIntent calendarIntent = new CalendarIntent();
		calendarIntent.type(CalendarIntent.TYPE_EDIT_EVENT);
		calendarIntent.eventId(123);
		// Act:
		final Intent intent = calendarIntent.build(application);
		// Assert:
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_EDIT));
		assertThat(intent.getData(), is(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, 123)));
	}

	@Test public void testBuildTypeOfEditEventWithInvalidEventId() {
		// Arrange:
		final CalendarIntent intent = new CalendarIntent();
		intent.type(CalendarIntent.TYPE_EDIT_EVENT);
		intent.eventId(-123);
		// Act + Assert:
		assertThatBuildThrowsExceptionWithMessage(
				application,
				intent,
				"Specified invalid event id(-123)."
		);
	}

	@Test public void testBuildTypeOfViewEvent() {
		// Arrange:
		final CalendarIntent calendarIntent = new CalendarIntent();
		calendarIntent.type(CalendarIntent.TYPE_VIEW_EVENT);
		calendarIntent.eventId(123);
		// Act:
		final Intent intent = calendarIntent.build(application);
		// Assert:
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, 123)));
	}

	@Test public void testBuildTypeOfViewEventWithInvalidEventId() {
		// Arrange:
		final CalendarIntent intent = new CalendarIntent();
		intent.type(CalendarIntent.TYPE_VIEW_EVENT);
		intent.eventId(-123);
		// Act + Assert:
		assertThatBuildThrowsExceptionWithMessage(
				application,
				intent,
				"Specified invalid event id(-123)."
		);
	}

	private static void assertThatTimeIsInRange(long time, long rangeStart, long rangeEnd) {
		assertTrue(
				"Time<" + time + "> is not in the range[" + rangeStart + ", " + rangeEnd + "]!",
				time >= rangeStart && time <= rangeEnd
		);
	}
}