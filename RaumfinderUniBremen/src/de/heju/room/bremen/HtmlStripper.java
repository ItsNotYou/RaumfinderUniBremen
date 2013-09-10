package de.heju.room.bremen;

import static java.lang.String.format;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import android.util.Log;

public class HtmlStripper {

	public CrosshairedImage retreiveCrosshairedImage(String requesturl) {
		CrosshairedImage result = new CrosshairedImage();

		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node = null;
		try {
			node = cleaner.clean(new URL("http://oracle-web.zfn.uni-bremen.de/lageplan/lageplan?haus=GW2&raum=B1215&pi_anz=2"));
			Object[] mapNodes = node.evaluateXPath("//*[@id=\"middle\"]/table[1]/tbody/tr[5]/td/div/div[1]");
			Object[] crosshairNodes = node.evaluateXPath("//*[@id=\"r9463\"]");

			result.setMap((TagNode) mapNodes[0]);
			result.setCrosshair((TagNode) crosshairNodes[0]);
		} catch (MalformedURLException e) {
			Log.w(HtmlStripper.class.getName(), e);
		} catch (IOException e) {
			Log.w(HtmlStripper.class.getName(), e);
		} catch (XPatherException e) {
			Log.w(HtmlStripper.class.getName(), e);
		}

		return result;
	}

	public String constructImageUrl(String building, String room) {
		String base = "http://oracle-web.zfn.uni-bremen.de/lageplan/lageplan?haus=%s&raum=%s&pi_anz=2";
		return format(base, building, room);
	}

	public String constructHtmlPage(TagNode... elements) {
		TagNode root = new TagNode("html");
		TagNode body = new TagNode("body");

		root.addChild(body);
		for (TagNode element : elements) {
			body.addChild(element);
		}

		return new SimpleHtmlSerializer(new HtmlCleaner().getProperties()).getAsString(root, "UTF-8");
	}
}
