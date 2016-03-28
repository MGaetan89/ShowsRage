package com.mgaetan89.showsrage.fragment;

import android.app.Application;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.media.MediaControlIntent;
import android.support.v7.media.MediaItemStatus;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaSessionStatus;
import android.support.v7.media.RemotePlaybackClient;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.ShowsRageApplication;
import com.mgaetan89.showsrage.model.Episode;
import com.mgaetan89.showsrage.model.PlayingVideoData;
import com.mgaetan89.showsrage.model.Show;

import java.lang.ref.WeakReference;

public class RemoteControlFragment extends DialogFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, Runnable {
	private static final long ONE_SECOND_IN_MS = 1_000L;
	private static final long ONE_MINUTE_IN_SECONDS = 60L;
	private static final long ONE_HOUR_IN_SECONDS = 60L * ONE_MINUTE_IN_SECONDS;
	private static final long SEEK_OFFSET_IN_MS = 60L * ONE_SECOND_IN_MS;

	@Nullable
	private TextView episodeCurrentTime = null;

	@Nullable
	private TextView episodeDuration = null;

	@Nullable
	private SeekBar episodeSeekBar = null;

	@NonNull
	private final Handler handler = new Handler();

	@Nullable
	private ImageView play = null;

	private boolean playing = false;

	@Nullable
	private PlayPauseStopCallback playPauseStopCallback = null;

	private long position = 0L;

	@Nullable
	private StatusCallback statusCallback = null;

	private int volume = 0;

	@Nullable
	private ImageView volumeMute = null;

	public RemoteControlFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		this.playPauseStopCallback = new PlayPauseStopCallback(this);
		this.statusCallback = new StatusCallback(this);
	}

	@Override
	public void onClick(View view) {
		if (view == null) {
			return;
		}

		int id = view.getId();

		switch (id) {
			case R.id.remote_fast_forward:
				this.fastForward();

				break;

			case R.id.remote_fast_rewind:
				this.fastRewind();

				break;

			case R.id.remote_play_pause:
				this.playPause();

				break;

			case R.id.remote_volume_down:
				this.volumeDown();

				break;

			case R.id.remote_volume_mute:
				this.volumeMute();

				break;

			case R.id.remote_volume_up:
				this.volumeUp();

				break;

			case R.id.remote_stop:
				this.stop();

				break;
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		PlayingVideoData playingVideo = this.getPlayingVideo();
		View view = LayoutInflater.from(this.getActivity()).inflate(R.layout.fragment_remote_control, null);

		if (view != null) {
			this.episodeCurrentTime = (TextView) view.findViewById(R.id.episode_current_time);
			this.episodeDuration = (TextView) view.findViewById(R.id.episode_duration);
			this.episodeSeekBar = (SeekBar) view.findViewById(R.id.episode_seek_bar);
			this.play = (ImageView) view.findViewById(R.id.remote_play_pause);
			this.volumeMute = (ImageView) view.findViewById(R.id.remote_volume_mute);

			if (this.episodeSeekBar != null) {
				this.episodeSeekBar.setOnSeekBarChangeListener(this);
			}

			if (this.play != null) {
				this.play.setOnClickListener(this);
			}

			if (this.volumeMute != null) {
				this.volumeMute.setOnClickListener(this);
			}

			TextView episodeName = (TextView) view.findViewById(R.id.episode_name);
			ImageView fastForward = (ImageView) view.findViewById(R.id.remote_fast_forward);
			ImageView fastRewind = (ImageView) view.findViewById(R.id.remote_fast_rewind);
			ImageView volumeDown = (ImageView) view.findViewById(R.id.remote_volume_down);
			ImageView volumeUp = (ImageView) view.findViewById(R.id.remote_volume_up);
			ImageView stop = (ImageView) view.findViewById(R.id.remote_stop);

			if (episodeName != null) {
				if (playingVideo != null) {
					Episode episode = playingVideo.getEpisode();

					if (episode != null) {
						episodeName.setText(episode.getName());
						episodeName.setVisibility(View.VISIBLE);
					} else {
						episodeName.setVisibility(View.GONE);
					}
				} else {
					episodeName.setVisibility(View.GONE);
				}
			}

			if (fastForward != null) {
				fastForward.setOnClickListener(this);
			}

			if (fastRewind != null) {
				fastRewind.setOnClickListener(this);
			}

			if (volumeDown != null) {
				volumeDown.setOnClickListener(this);
			}

			if (volumeUp != null) {
				volumeUp.setOnClickListener(this);
			}

			if (stop != null) {
				stop.setOnClickListener(this);
			}
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

		if (playingVideo != null) {
			MediaRouter.RouteInfo route = playingVideo.getRoute();
			Show show = playingVideo.getShow();

			if (show != null) {
				builder.setTitle(show.getShowName());
			}

			if (route != null) {
				this.volume = route.getVolume();
			}
		}

		builder.setView(view);
		builder.setPositiveButton("Close", null);

		return builder.show();
	}

	@Override
	public void onDestroyView() {
		this.episodeCurrentTime = null;
		this.episodeDuration = null;
		this.episodeSeekBar = null;
		this.play = null;
		this.volumeMute = null;

		super.onDestroyView();
	}

	@Override
	public void onPause() {
		this.handler.removeCallbacks(this);

		super.onPause();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (!fromUser || !this.playing) {
			return;
		}

		PlayingVideoData playingVideo = this.getPlayingVideo();

		if (playingVideo == null) {
			return;
		}

		String itemId = playingVideo.getItemId();
		RemotePlaybackClient remotePlaybackClient = playingVideo.getRemotePlaybackClient();

		if (!TextUtils.isEmpty(itemId) && remotePlaybackClient != null) {
			this.position = progress;

			remotePlaybackClient.seek(itemId, this.position, null, new RemotePlaybackClient.ItemActionCallback() {
			});
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		this.handler.post(this);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void run() {
		PlayingVideoData playingVideo = this.getPlayingVideo();

		if (playingVideo == null) {
			return;
		}

		String itemId = playingVideo.getItemId();
		RemotePlaybackClient remotePlaybackClient = playingVideo.getRemotePlaybackClient();

		if (!TextUtils.isEmpty(itemId) && remotePlaybackClient != null) {
			remotePlaybackClient.getStatus(itemId, null, this.statusCallback);

			this.handler.postDelayed(this, ONE_SECOND_IN_MS);
		}
	}

	private void fastForward() {
		PlayingVideoData playingVideo = this.getPlayingVideo();

		if (playingVideo == null) {
			return;
		}

		String itemId = playingVideo.getItemId();
		RemotePlaybackClient remotePlaybackClient = playingVideo.getRemotePlaybackClient();

		if (!TextUtils.isEmpty(itemId) && remotePlaybackClient != null) {
			this.position += SEEK_OFFSET_IN_MS;

			remotePlaybackClient.seek(itemId, this.position, null, new RemotePlaybackClient.ItemActionCallback() {
			});
		}
	}

	private void fastRewind() {
		PlayingVideoData playingVideo = this.getPlayingVideo();

		if (playingVideo == null) {
			return;
		}

		String itemId = playingVideo.getItemId();
		RemotePlaybackClient remotePlaybackClient = playingVideo.getRemotePlaybackClient();

		if (!TextUtils.isEmpty(itemId) && remotePlaybackClient != null) {
			this.position = Math.max(this.position - SEEK_OFFSET_IN_MS, 0L);

			remotePlaybackClient.seek(itemId, this.position, null, new RemotePlaybackClient.ItemActionCallback() {
			});
		}
	}

	@NonNull
	/* package */ static String formatTime(long time) {
		time = Math.max(time, 0L);
		time /= ONE_SECOND_IN_MS;
		long hours = time / ONE_HOUR_IN_SECONDS;

		time %= ONE_HOUR_IN_SECONDS;
		long minutes = time / ONE_MINUTE_IN_SECONDS;

		time %= ONE_MINUTE_IN_SECONDS;
		String formattedTime = "";

		if (hours > 0L) {
			formattedTime += String.format("%02d:", hours);
		}

		return formattedTime + String.format("%02d:", minutes) + String.format("%02d", time);
	}

	@Nullable
	private PlayingVideoData getPlayingVideo() {
		FragmentActivity activity = this.getActivity();

		if (activity == null) {
			return null;
		}

		Application application = activity.getApplication();

		if (application instanceof ShowsRageApplication) {
			return ((ShowsRageApplication) application).getPlayingVideo();
		}

		return null;
	}

	private void playPause() {
		PlayingVideoData playingVideo = this.getPlayingVideo();

		if (playingVideo == null) {
			return;
		}

		RemotePlaybackClient remotePlaybackClient = playingVideo.getRemotePlaybackClient();

		if (remotePlaybackClient == null) {
			return;
		}

		if (this.playing) {
			remotePlaybackClient.pause(null, this.playPauseStopCallback);
		} else {
			remotePlaybackClient.resume(null, this.playPauseStopCallback);
		}
	}

	private void setVolumeUpIconColor(@ColorRes int iconColor) {
		if (this.volumeMute != null) {
			Drawable drawable = DrawableCompat.wrap(this.volumeMute.getDrawable());
			DrawableCompat.setTint(drawable, ContextCompat.getColor(this.getActivity(), iconColor));
		}
	}

	private void stop() {
		PlayingVideoData playingVideo = this.getPlayingVideo();

		if (playingVideo == null) {
			return;
		}

		RemotePlaybackClient remotePlaybackClient = playingVideo.getRemotePlaybackClient();

		if (remotePlaybackClient != null) {
			remotePlaybackClient.stop(null, this.playPauseStopCallback);
		}
	}

	private void updatePlayPauseIcon() {
		if (this.play == null) {
			return;
		}

		if (this.playing) {
			this.play.setImageResource(R.drawable.ic_pause_white_24dp);
		} else {
			this.play.setImageResource(R.drawable.ic_play_arrow_white_24dp);
		}
	}

	private void volumeDown() {
		PlayingVideoData playingVideo = this.getPlayingVideo();

		if (playingVideo == null) {
			return;
		}

		MediaRouter.RouteInfo route = playingVideo.getRoute();

		if (route == null) {
			return;
		}

		this.volume -= route.getVolumeMax() / 10;
		this.volume = Math.max(this.volume, 0);

		route.requestSetVolume(this.volume);

		this.setVolumeUpIconColor(android.R.color.white);
	}

	private void volumeMute() {
		PlayingVideoData playingVideo = this.getPlayingVideo();

		if (playingVideo == null) {
			return;
		}

		MediaRouter.RouteInfo route = playingVideo.getRoute();

		if (route == null) {
			return;
		}

		int currentVolume = route.getVolume();
		int iconColor;

		if (currentVolume > 0) {
			iconColor = R.color.accent;

			route.requestSetVolume(0);
		} else {
			iconColor = android.R.color.white;

			route.requestSetVolume(this.volume);
		}

		this.volume = currentVolume;

		this.setVolumeUpIconColor(iconColor);
	}

	private void volumeUp() {
		PlayingVideoData playingVideo = this.getPlayingVideo();

		if (playingVideo == null) {
			return;
		}

		MediaRouter.RouteInfo route = playingVideo.getRoute();

		if (route == null) {
			return;
		}

		this.volume += route.getVolumeMax() / 10;
		this.volume = Math.min(this.volume, route.getVolumeMax());

		route.requestSetVolume(this.volume);

		this.setVolumeUpIconColor(android.R.color.white);
	}

	private static final class PlayPauseStopCallback extends RemotePlaybackClient.SessionActionCallback {
		@NonNull
		private WeakReference<RemoteControlFragment> fragmentReference = new WeakReference<>(null);

		private PlayPauseStopCallback(RemoteControlFragment fragment) {
			this.fragmentReference = new WeakReference<>(fragment);
		}

		@Override
		public void onResult(Bundle data, String sessionId, MediaSessionStatus sessionStatus) {
			super.onResult(data, sessionId, sessionStatus);

			RemoteControlFragment fragment = this.fragmentReference.get();

			if (fragment == null) {
				return;
			}

			fragment.playing = !fragment.playing;

			fragment.updatePlayPauseIcon();
		}
	}

	private static final class StatusCallback extends RemotePlaybackClient.ItemActionCallback {
		@NonNull
		private WeakReference<RemoteControlFragment> fragmentReference = new WeakReference<>(null);

		private StatusCallback(RemoteControlFragment fragment) {
			this.fragmentReference = new WeakReference<>(fragment);
		}

		@Override
		public void onResult(Bundle data, String sessionId, MediaSessionStatus sessionStatus, String itemId, MediaItemStatus itemStatus) {
			super.onResult(data, sessionId, sessionStatus, itemId, itemStatus);

			RemoteControlFragment fragment = this.fragmentReference.get();

			if (fragment == null) {
				return;
			}

			MediaItemStatus status = MediaItemStatus.fromBundle(data.getBundle(MediaControlIntent.EXTRA_ITEM_STATUS));

			fragment.playing = status.getPlaybackState() == MediaItemStatus.PLAYBACK_STATE_PLAYING;
			fragment.position = status.getContentPosition();

			fragment.updatePlayPauseIcon();

			if (fragment.episodeCurrentTime != null) {
				fragment.episodeCurrentTime.setText(formatTime(status.getContentPosition()));
				fragment.episodeCurrentTime.setVisibility(View.VISIBLE);
			}

			if (fragment.episodeDuration != null) {
				fragment.episodeDuration.setText(formatTime(status.getContentDuration()));
				fragment.episodeDuration.setVisibility(View.VISIBLE);
			}

			if (fragment.episodeSeekBar != null) {
				fragment.episodeSeekBar.setMax((int) status.getContentDuration());
				fragment.episodeSeekBar.setProgress((int) status.getContentPosition());
				fragment.episodeSeekBar.setVisibility(View.VISIBLE);
			}
		}
	}
}
