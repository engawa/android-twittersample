package jp.digitalcloud.sample.twitter.auth;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.widget.Toast;

/**
 * LoaderCallbacks for get a access token.
 *
 * @author R SATO
 *
 */
public class TwitterOAuthAccessTokenCallbacks implements
		LoaderCallbacks<AccessToken> {

	private Context mContext;
	private ProgressDialog mProgressDialog;
	private Twitter mTwitter;
	private String mPin;

	/**
	 * Initialize LoaderCallbacks.
	 *
	 * @param context
	 *            Context
	 * @param twitter
	 *            Twitter object. It is necessary to set the Consumer keys.
	 * @param pin
	 *            PIN
	 */
	public TwitterOAuthAccessTokenCallbacks(Context context, Twitter twitter,
			String pin) {
		mContext = context;
		mTwitter = twitter;
		mPin = pin;
	}

	@Override
	public Loader<AccessToken> onCreateLoader(int id, Bundle args) {
		// show ProgressDialog
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(R.string.dialog_title_text);
		mProgressDialog.setMessage(mContext
				.getString(R.string.dialog_message_text));
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();

		// run AsyncTaskLoader
		AsyncTaskLoader<AccessToken> loader = new TwitterOAuthAccessTokenLoader(
				mContext, mTwitter, mPin);
		loader.startLoading();
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<AccessToken> arg0, AccessToken accessToken) {
		if (accessToken != null) {
			// store access token
			Editor editor = mContext.getSharedPreferences(
					mContext.getString(R.string.shared_pref_name),
					Context.MODE_PRIVATE).edit();
			editor.putString(mContext
					.getString(R.string.shared_pref_key_twitter_access_token),
					accessToken.getToken());
			editor.putString(
					mContext.getString(R.string.shared_pref_key_twitter_access_token_secret),
					accessToken.getTokenSecret());
			editor.commit();

			// notify
			Toast.makeText(mContext,
					mContext.getString(R.string.toast_oauth_complete_text),
					Toast.LENGTH_SHORT).show();

		} else {
			Toast.makeText(mContext,
					mContext.getString(R.string.toast_oauth_failed_text),
					Toast.LENGTH_SHORT).show();
		}

		// close ProgressDialog
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onLoaderReset(Loader<AccessToken> arg0) {
		// close ProgressDialog
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}


}
