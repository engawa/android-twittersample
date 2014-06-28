package jp.digitalcloud.sample.twitter.auth;

import twitter4j.Twitter;
import twitter4j.auth.RequestToken;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

/**
 * LoaderCallbacks for get a request token.
 * 
 * @author R SATO
 *
 */
public class TwitterOAuthRequestTokenCallbacks implements
		LoaderCallbacks<RequestToken> {

	private Context mContext;
	private Twitter mTwitter;
	private ProgressDialog mProgressDialog;

	/**
	 * Initialize LoaderCallbacks.
	 * 
	 * @param context
	 *            Context
	 * @param twitter
	 *            Twitter object. It is necessary to set the Consumer keys.
	 */
	public TwitterOAuthRequestTokenCallbacks(Context context, Twitter twitter) {
		mContext = context;
		mTwitter = twitter;
	}

	@Override
	public Loader<RequestToken> onCreateLoader(int id, Bundle args) {
		// show ProgressDialog
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(R.string.dialog_title_text);
		mProgressDialog.setMessage(mContext
				.getString(R.string.dialog_message_text));
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();

		// run AsyncTaskLoader
		Loader<RequestToken> loader = new TwitterOAuthRequestTokenLoader(
				mContext, mTwitter);
		loader.startLoading();
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<RequestToken> arg0,
			RequestToken requestToken) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken
				.getAuthorizationURL()));
		mContext.startActivity(intent);

		// close ProgressDialog
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onLoaderReset(Loader<RequestToken> arg0) {
		// close ProgressDialog
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}
}
