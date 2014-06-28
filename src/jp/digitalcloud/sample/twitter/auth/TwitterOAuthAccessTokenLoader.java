package jp.digitalcloud.sample.twitter.auth;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * AsyncTaskLoader for get a access token.
 * 
 * @author R SATO
 *
 */
public class TwitterOAuthAccessTokenLoader extends AsyncTaskLoader<AccessToken> {

	private Twitter mTwitter;
	private String mPin;
	private AccessToken mResult;

	/**
	 * Initialize AsyncTaskLoader.
	 * 
	 * @param context
	 *            Context
	 * @param twitter
	 *            Twitter
	 * @param pin
	 *            PIN
	 */
	public TwitterOAuthAccessTokenLoader(Context context, Twitter twitter,
			String pin) {
		super(context);
		mTwitter = twitter;
		mPin = pin;
	}

	@Override
	protected void onStartLoading() {
		if (mResult != null) {
			deliverResult(mResult);
		}

		if (takeContentChanged() || mResult == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	public void deliverResult(AccessToken data) {
		mResult = data;
		super.deliverResult(data);
	}

	@Override
	public AccessToken loadInBackground() {
		AccessToken accessToken = null;
		try {
			accessToken = mTwitter.getOAuthAccessToken(mPin);
		} catch (TwitterException e) {
			accessToken = null;
		}
		return accessToken;
	}

}
