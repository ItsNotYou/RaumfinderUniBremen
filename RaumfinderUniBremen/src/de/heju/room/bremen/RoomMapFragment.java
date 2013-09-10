package de.heju.room.bremen;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.google.common.io.ByteStreams;

public class RoomMapFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.fragment_room_map, container, false);

		String source = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"de\" lang=\"de\">" + "<body>" + "<div style=\"position:absolute;z-index:1;visibility:visible;left:0px;top:0px;\">\r\n" + "  <img style=\"left:0px;top:0px;\" border=\"0\" src=\"data/images/ebenenansicht/607/GW2B-Ebene-1-web-ohne%20Raum.svg\">\r\n" + "</div>" + "<div id=\"r9463\" style=\"position:absolute;z-index:2;visibility:visible;left:179px;top:287px;\">\r\n" + "    <input type=\"image\" alt=\"B1215\" src=\"http://oracle-web.zfn.uni-bremen.de/images/fadenkreuz.gif\" size=\"40\" height=\"40\" onmouseover=\"my_div_style=document.getElementById('ri9463').style;my_div_style.visibility='visible';\" onmouseout=\"my_div_style=document.getElementById('ri9463').style;my_div_style.visibility='hidden';\">\r\n" + "</div>" + "</body>";

		WebView mapViewer = (WebView) result.findViewById(R.id.room_map_viewer);
		// mapViewer.loadDataWithBaseURL("http://oracle-web.zfn.uni-bremen.de/lageplan/lageplan?haus=GW2&raum=B1215&pi_anz=2", source, "text/html", null, null);
		// mapViewer.getSettings().setBuiltInZoomControls(true);
		// mapViewer.getSettings().setLoadWithOverviewMode(true);
		// mapViewer.getSettings().setUseWideViewPort(true);

		new StripHtml().executeOnExecutor(StripHtml.THREAD_POOL_EXECUTOR);

		// new DownloadMap((ImageView) result.findViewById(R.id.room_map)).execute("http://oracle-web.zfn.uni-bremen.de/lageplaene/originale/SFG-2.gif");

		return result;
	}

	private class DownloadMap extends AsyncTask<String, Void, byte[]> {

		private ImageView view;

		public DownloadMap(ImageView view) {
			this.view = view;
		}

		@Override
		protected byte[] doInBackground(String... params) {
			String imageUri = params[0];

			byte[] result = null;
			InputStream stream = null;
			try {
				stream = new URL(imageUri).openStream();
				result = ByteStreams.toByteArray(stream);
			} catch (MalformedURLException e) {
				Log.w(getTag(), e);
			} catch (IOException e) {
				Log.w(getTag(), e);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						Log.w(getTag(), e);
					}
				}
			}

			return result;
		}

		@Override
		protected void onCancelled(byte[] result) {
			Log.e(getTag(), "Herunterladen abgebrochen");
		}

		@Override
		protected void onPostExecute(byte[] result) {
			Log.e(getTag(), result.length / 1024 + "kb heruntergeladen");

			Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
			view.setImageBitmap(bitmap);
		}
	}

	private class StripHtml extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode node = null;
			try {
				node = cleaner.clean(new URL("http://oracle-web.zfn.uni-bremen.de/lageplan/lageplan?haus=GW2&raum=B1215&pi_anz=2"));
				Object[] mapNodes = node.evaluateXPath("//*[@id=\"middle\"]/table[1]/tbody/tr[5]/td/div/div[1]");
				Object[] crosshairNodes = node.evaluateXPath("//*[@id=\"r9463\"]");

				TagNode strippedMap = new TagNode("myName");
				strippedMap.addChild(mapNodes[0]);
				strippedMap.addChild(crosshairNodes[0]);

				StringWriter outputHtml = new StringWriter();
				new SimpleHtmlSerializer(cleaner.getProperties()).write(strippedMap, outputHtml, "UTF-8");

				String html = outputHtml.toString();
				System.out.println("Show my html");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XPatherException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// TODO Auto-generated method stub
			return null;
		}
	}
}
