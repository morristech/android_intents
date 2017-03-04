/*
 * =================================================================================================
 *                             Copyright (C) 2016 Universum Studios
 * =================================================================================================
 *         Licensed under the Apache License, Version 2.0 or later (further "License" only).
 * -------------------------------------------------------------------------------------------------
 * You may use this file only in compliance with the License. More details and copy of this License
 * you may obtain at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * You can redistribute, modify or publish any part of the code written within this file but as it
 * is described in the License, the software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES or CONDITIONS OF ANY KIND.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 * =================================================================================================
 */
package universum.studios.android.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * A {@link BaseIntent} builder implementation providing API for building and starting of simple intents.
 * <p>
 * Activity that should be started by an intent can be specified via {@link #activityClass(Class)}.
 * If you want to specify some intent flag, you can do so via {@link #flag(int)} or {@link #flags(int)}.
 * <p>
 * If you want to start activity for some result, you can specify request code via {@link #requestCode(int)}.
 *
 * @author Martin Albedinsky
 */
public class SimpleIntent extends BaseIntent<SimpleIntent> {

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "SimpleIntent";

	/**
	 * Flag indicating that this intent is type of activity intent, so just simple activity will
	 * be started.
	 */
	private static final int TYPE_ACTIVITY = 0x01;

	/**
	 * Flag indicating that this intent is type of action intent, so there was requested some
	 * specific action like {@link Intent#ACTION_SEND} to find all activities which can send a specific
	 * content presented within Intent.
	 */
	private static final int TYPE_ACTION = 0x02;

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Resolved type of the intent depends on the received parameters.
	 */
	private int mType = 0;

	/**
	 * Intent action.
	 */
	private String mAction;

	/**
	 * Class of activity to launch from intent.
	 */
	private Class<? extends Activity> mActivityClass;

	/**
	 * Set of intent flags.
	 */
	private int mFlags;

	/**
	 * Intent request code, for case, when this intent should be started via {@link IntentStarter#startIntentForResult(Intent, int)}.
	 */
	private int mRequestCode = -1;

	/**
	 * Flag indicating whether this intent should be started for result or not.
	 */
	private boolean mForResult;

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * Specifies class of an activity to be launched when this intent is started.
	 * See {@link Intent#Intent(android.content.Context, Class)} for more info.
	 *
	 * @param activityClass Class of the desired activity that should be launched. May be {@code null}
	 *                      to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #activityClass()
	 */
	public SimpleIntent activityClass(@NonNull Class<? extends Activity> activityClass) {
		this.mActivityClass = activityClass;
		this.mType = TYPE_ACTIVITY;
		return this;
	}

	/**
	 * Returns the class of activity that will be launched.
	 *
	 * @return The class of activity or {@code null} if no activity class has been specified yet.
	 * @see #activityClass(Class)
	 */
	@Nullable
	public Class<? extends Activity> activityClass() {
		return mActivityClass;
	}

	/**
	 * Specifies an action that will be used to search for available activities (that can handle
	 * intent with such action) whenever this intent is started. See {@link Intent#Intent(String)}
	 * for more info.
	 * <p>
	 * Default value: <b>{@code ""}</b>
	 *
	 * @param action The desired intent action. See {@link Intent} class for available actions.
	 * @return This intent builder to allow methods chaining.
	 * @see #action()
	 */
	public SimpleIntent action(@NonNull String action) {
		this.mAction = action;
		this.mType = TYPE_ACTION;
		return this;
	}

	/**
	 * Returns the intent action.
	 *
	 * @return Intent action. See {@link Intent} class for allowed actions.
	 * @see #action(String)
	 */
	@NonNull
	public String action() {
		return mAction == null ? "" : mAction;
	}

	/**
	 * Sets <var>flags</var> for the intent to be started. See {@link Intent#setFlags(int)} for more
	 * info.
	 *
	 * @param flags The desired flags for Intent. Use {@code |} to set multiple flags, like:
	 *              {@code Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY}  .
	 * @return This intent builder to allow methods chaining.
	 * @see #flags()
	 */
	public SimpleIntent flags(@IntRange(from = 0, to = Integer.MAX_VALUE) int flags) {
		this.mFlags = flags;
		return this;
	}

	/**
	 * Appends the specified <var>flag</var> to the current intent flags.
	 *
	 * @param flag The desired intent flag to append to the current ones.
	 * @return This intent builder to allow methods chaining.
	 * @see #flags(int)
	 * @see #flags()
	 */
	public SimpleIntent flag(@IntRange(from = 1, to = Integer.MAX_VALUE) int flag) {
		this.mFlags |= flag;
		return this;
	}

	/**
	 * Returns the intent flags.
	 *
	 * @return Intent flags or {@code 0} if no flags has been specified yet.
	 * @see #flags(int)
	 */
	@IntRange(from = 0, to = Integer.MAX_VALUE)
	public int flags() {
		return mFlags;
	}

	/**
	 * Sets a request code for the intent to be started.
	 * <p>
	 * Default value: <b>{@code -1}</b>
	 *
	 * @param requestCode The desired request code to start this intent for result. May be negative
	 *                    number to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #requestCode()
	 */
	public SimpleIntent requestCode(int requestCode) {
		this.mRequestCode = requestCode;
		this.mForResult = mRequestCode >= 0;
		return this;
	}

	/**
	 * Returns the request code used to start this intent for result.
	 *
	 * @return Intent request code.
	 * @see #requestCode(int)
	 */
	public int requestCode() {
		return mRequestCode;
	}

	/**
	 */
	@Override
	protected void ensureCanBuildOrThrow() {
		super.ensureCanBuildOrThrow();
		switch (mType) {
			case TYPE_ACTIVITY:
				if (mActivityClass == null) {
					throw cannotBuildIntentException("No activity class specified.");
				}
				return;
			case TYPE_ACTION:
				if (mAction == null) {
					throw cannotBuildIntentException("No action specified.");
				}
				return;
		}
		throw cannotBuildIntentException("No activity class or action specified.");
	}

	/**
	 */
	@NonNull
	@Override
	protected Intent onBuild(@NonNull Context context) {
		switch (mType) {
			case TYPE_ACTION:
				return new Intent(mAction).setFlags(mFlags);
			default:
				return new Intent(context, mActivityClass).setFlags(mFlags);
		}
	}

	/**
	 */
	@Override
	@SuppressWarnings("ResourceType")
	protected boolean onStartWith(@NonNull IntentStarter starter, @NonNull Intent intent) {
		if (mForResult) starter.startIntentForResult(intent, mRequestCode);
		else starter.startIntent(intent);
		return true;
	}

	/**
	 * Inner classes ===============================================================================
	 */
}
