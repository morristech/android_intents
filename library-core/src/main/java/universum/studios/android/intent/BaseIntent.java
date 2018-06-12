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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.AnimRes;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * A BaseIntent specifies base API for intent builders. Some Android intents require a lots of data
 * to be supplied to them and the intent builders should provide API to simplify such a set up process.
 *
 * <h3>Implementation</h3>
 * Implementations of the BaseIntent class need to implement these methods:
 * <ul>
 * <li>
 * {@link #build(Context)}
 * <p>
 * This method is called whenever {@link #startWith(IntentStarter)} is invoked and a specific intent
 * builder implementation should build here an intent which is for that type of intent builder specific
 * and configure it with data supplied through its setters API.
 * </li>
 * <li>
 * {@link #onStartWith(IntentStarter, Intent)}
 * <p>
 * It is not required to override this method. Default implementation of this method only starts
 * the intent obtained via {@link #build(Context)} method via {@link IntentStarter#startIntent(Intent)}.
 * If a specific intent builder implementation requires for example starting intent as activity for
 * result, than it should be done within this method.
 * </li>
 * </ul>
 *
 * <h3>Activity transitions</h3>
 * BaseIntent class provides also API to supply activity transitions via {@link #enterTransition(int)}
 * and {@link #exitTransition(int)}. These transitions will be used whenever {@link #onStartWith(IntentStarter, Intent)}
 * is called using the intent starter's {@link IntentStarter#overridePendingTransition(int, int)}.
 *
 * @author Martin Albedinsky
 * @since 1.0
 *
 * @param <I> A type of the BaseIntent implementation to allow proper methods chaining.
 */
public abstract class BaseIntent<I extends BaseIntent> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "BaseIntent";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Title text for the activity chooser dialog.
	 */
	CharSequence mDialogTitle = "Choose";

	/**
	 * Message text for the toast, in case, when there is no activity to process requested intent.
	 */
	private CharSequence mActivityNotFoundMessage = "No application found to handle this action";

	/**
	 * Window transition resource id.
	 */
	private int mEnterTransition, mExitTransition;

	/**
	 * Flag indicating whether to apply window transition or not.
	 */
	private boolean mApplyTransitions;

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Sets a title for the chooser dialog.
	 * <p>
	 * Default value: <b>Choose</b>
	 *
	 * @param title The desired dialog title text. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #dialogTitle()
	 */
	@SuppressWarnings("unchecked")
	public I dialogTitle(@Nullable final CharSequence title) {
		this.mDialogTitle = title;
		return (I) this;
	}

	/**
	 * Returns the title text for the chooser dialog.
	 *
	 * @return Title text.
	 */
	@NonNull
	public CharSequence dialogTitle() {
		return mDialogTitle == null ? "" : mDialogTitle;
	}

	/**
	 * Sets a message for the toast that is shown in case, when there wasn't found any activity that
	 * would handle the started intent.
	 *
	 * @param message The desired message text. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #activityNotFoundMessage()
	 */
	@SuppressWarnings("unchecked")
	public I activityNotFoundMessage(@Nullable final CharSequence message) {
		this.mActivityNotFoundMessage = message;
		return (I) this;
	}

	/**
	 * Returns the message text for the toast that is shown in case, when there wasn't found any
	 * activity for the started intent.
	 *
	 * @return Message text.
	 * @see #activityNotFoundMessage(CharSequence)
	 */
	@NonNull
	public CharSequence activityNotFoundMessage() {
		return mActivityNotFoundMessage == null ? "" : mActivityNotFoundMessage;
	}

	/**
	 * Sets a window transition used to override default pending window enter transition when starting
	 * this intent.
	 * <p>
	 * See {@link Activity#overridePendingTransition(int, int)} for more info.
	 *
	 * @param transition Resource id of the desired window transition (animation).
	 * @return This intent builder to allow methods chaining.
	 * @see #enterTransition()
	 */
	public I enterTransition(@AnimRes final int transition) {
		return transitions(transition, mExitTransition);
	}

	/**
	 * Returns the window enter transition used to override window's pending transition.
	 *
	 * @return Resource id of the enter transition or {@code 0} if there was no transition specified.
	 * @see #enterTransition(int)
	 */
	@AnimRes
	public int enterTransition() {
		return mEnterTransition;
	}

	/**
	 * Sets a window transition used to override default pending window exit transition when starting
	 * this intent.
	 * <p>
	 * See {@link Activity#overridePendingTransition(int, int)} for more info.
	 *
	 * @param transition Resource id of the desired window transition (animation).
	 * @return This intent builder to allow methods chaining.
	 * @see #exitTransition()
	 */
	public I exitTransition(@AnimRes final int transition) {
		return transitions(mEnterTransition, transition);
	}

	/**
	 * Returns the window exit transition used to override window's pending transition.
	 *
	 * @return Resource id of the enter transition or {@code 0} if there was no transition specified.
	 * @see #exitTransition(int)
	 */
	@AnimRes
	public int exitTransition() {
		return mExitTransition;
	}

	/**
	 * Sets both, enter + exit, window transitions used to override default pending window transition
	 * when starting this intent.
	 *
	 * @param enterTransition Resource id of the desired window enter transition (animation).
	 * @param exitTransition  Resource id of the desired window exit transition (animation).
	 * @return This intent builder to allow methods chaining.
	 * @see #enterTransition(int)
	 * @see #exitTransition(int)
	 */
	@SuppressWarnings("unchecked")
	public I transitions(@AnimRes final int enterTransition, @AnimRes final int exitTransition) {
		this.mEnterTransition = Math.max(0, enterTransition);
		this.mExitTransition = Math.max(0, exitTransition);
		this.mApplyTransitions = enterTransition >= 0 || exitTransition >= 0;
		return (I) this;
	}

	/**
	 * Starts na intent specific for this intent builder created via {@link #build(Context)} using
	 * the given intent <var>starter</var>.
	 *
	 * @param starter The desired starter to be used to start the intent. See {@link IntentStarters}
	 *                for default available starters.
	 * @return {@code True} if the intent has been successfully started, {@code false} otherwise.
	 */
	public boolean startWith(@NonNull final IntentStarter starter) {
		final Context context = starter.getContext();
		final Intent intent = build(context);
		if (isActivityForIntentAvailable(context, intent)) {
			return onStartWith(starter, intent);
		}
		notifyActivityNotFound(context);
		return false;
	}

	/**
	 * Called to create an instance of {@link Intent} from the current data of this intent builder.
	 *
	 * @param context Context obtained from the {@link IntentStarter}.
	 * @return An instance of Intent specific for this intent builder.
	 * @throws IllegalArgumentException If this builder does not have all required data to build the
	 *                                  requested intent.
	 */
	@NonNull
	public Intent build(@NonNull final Context context) {
		ensureCanBuildOrThrow();
		return onBuild(context);
	}

	/**
	 * Called to ensure that this builder can build its Intent from its current data.
	 * <p>
	 * If there are some required data missing/not specified, an exception indicating such state
	 * should be thrown. The default exception may be created via {@link #cannotBuildIntentException(String)}.
	 */
	protected void ensureCanBuildOrThrow() {
		// Inheritance hierarchies should perform here checks whether an Intent from the current
		// data may be built or not. If Intent cannot be properly built, an exception should be thrown.
	}

	/**
	 * Invoked from {@link #build(Context)} after {@link #ensureCanBuildOrThrow()} to build intent
	 * specific for this intent builder from the current data.
	 *
	 * @param context Context obtained from the {@link IntentStarter}.
	 * @return Intent instance with current data.
	 */
	@NonNull
	protected abstract Intent onBuild(@NonNull Context context);

	/**
	 * Checks whether there is any activity that can handle the specified <var>intent</var> available
	 * on the current Android device.
	 *
	 * @param context Context used to obtain package manager.
	 * @param intent  The intent for which to resolve activity.
	 * @return {@code True} if activity for the intent has been resolved/found, {@code false} otherwise
	 * which means that there is no activity currently installed on the current Android device that
	 * could handle that intent.
	 */
	@CheckResult
	@SuppressWarnings("ConstantConditions")
	public static boolean isActivityForIntentAvailable(@NonNull final Context context, @NonNull final Intent intent) {
		return intent.resolveActivity(context.getPackageManager()) != null;
	}

	/**
	 * Creates an instance of {@link IllegalArgumentException} indicating that one of required parameters
	 * for this intent builder has not been specified, but {@link #build(Context)} has been invoked.
	 *
	 * @param message Message to be included into exception.
	 */
	protected final IllegalArgumentException cannotBuildIntentException(@NonNull final String message) {
		return new IllegalArgumentException("Cannot build " + getClass().getSimpleName() + ". " + message);
	}

	/**
	 * Invoked whenever {@link #startWith(IntentStarter)} is called and there is an activity available
	 * that can handle the given <var>intent</var> that has been created via {@link #build(Context)}.
	 * <p>
	 * This implementation always returns {@code true}.
	 *
	 * @param starter The starter with which to start the intent.
	 * @param intent  The intent instance created by this intent builder.
	 * @return {@code True} to indicate that the intent has been started, {@code false} otherwise.
	 */
	protected boolean onStartWith(@NonNull final IntentStarter starter, @NonNull final Intent intent) {
		starter.startIntent(intent);
		if (mApplyTransitions) {
			starter.overridePendingTransition(mEnterTransition, mExitTransition);
		}
		return true;
	}

	/**
	 * Notifies situation that there wasn't found any activity to handle the started intent.
	 * <p>
	 * This implementation by default shows a toast with the message specified via {@link #activityNotFoundMessage(CharSequence)},
	 * if any.
	 *
	 * @param context The context obtained from the {@link IntentStarter}.
	 * @see #activityNotFoundMessage(CharSequence)
	 */
	protected void notifyActivityNotFound(@NonNull final Context context) {
		Toast.makeText(context, mActivityNotFoundMessage, Toast.LENGTH_LONG).show();
	}

	/*
	 * Inner classes ===============================================================================
	 */
}