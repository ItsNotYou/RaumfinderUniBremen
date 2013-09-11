package de.heju.room.bremen;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class RoomMapFragment extends Fragment implements RoomFindable {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_room_map, container, false);
	}

	@Override
	public void requestRoomFinding(RoomFindingRequest request) {
		View view = getView();
		if (view != null) {
			WebView map = (WebView) view.findViewById(R.id.room_map_viewer);
			TextView progress = (TextView) view.findViewById(R.id.room_map_info);
			new DownloadMap(map, progress).execute(request);
		}
	}

	private class DownloadMap extends AsyncTask<RoomFindingRequest, String, HtmlMapPage> {

		private WebView view;
		private TextView progress;

		private DownloadMap(WebView view, TextView progress) {
			this.view = view;
			this.progress = progress;
		}

		@Override
		protected void onPreExecute() {
			view.setVisibility(View.INVISIBLE);
		}

		@Override
		protected HtmlMapPage doInBackground(RoomFindingRequest... params) {
			RoomFindingRequest request = params[0];

			HtmlMapPage result = new HtmlMapPage();
			try {
				HtmlStripper stripper = new HtmlStripper();

				publishProgress("Ermittle Ziel");
				String imageUrl = stripper.constructImageUrl(request.getBuilding(), request.getRoom());
				result.setUrl(imageUrl);

				publishProgress("Lade Daten");
				CrosshairedImage crosshairedImage = stripper.retreiveCrosshairedImage(imageUrl);

				publishProgress("Optimiere Darstellung");
				String htmlSource = stripper.constructHtmlPage(crosshairedImage.getMap(), crosshairedImage.getCrosshair());
				result.setSource(htmlSource);

				publishProgress("Lade Karte");
			} catch (Exception e) {
				cancel(false);
				result.setException(e);
			}

			return result;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			progress.setText(values[0]);
		}

		@Override
		protected void onCancelled(HtmlMapPage result) {
			publishProgress("Fehler aufgetreten: " + result.toString());
		}

		@Override
		protected void onPostExecute(HtmlMapPage result) {
			view.loadDataWithBaseURL(result.getUrl(), result.getSource(), "text/html", null, null);
			view.getSettings().setBuiltInZoomControls(true);
			view.getSettings().setLoadWithOverviewMode(true);
			view.getSettings().setUseWideViewPort(true);
			view.setVisibility(View.VISIBLE);

			Toast.makeText(getActivity(), result.getUrl(), Toast.LENGTH_LONG).show();
		}
	}
}
