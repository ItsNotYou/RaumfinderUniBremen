package de.heju.room.bremen.test;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;

import android.test.AndroidTestCase;
import de.heju.room.bremen.CrosshairedImage;
import de.heju.room.bremen.HtmlStripper;

public class HtmlStripperTest extends AndroidTestCase {

	private static final String requestUrl = "http://oracle-web.zfn.uni-bremen.de/lageplan/lageplan?haus=GW2&raum=B1215&pi_anz=2";
	private HtmlStripper sut;

	private static String retreiveHtml(TagNode node) {
		return new SimpleHtmlSerializer(new HtmlCleaner().getProperties()).getAsString(node, "UTF-8");
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.sut = new HtmlStripper();
	}

	public void testMapIsRetreived() {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<div style=\"position:absolute;z-index:1;visibility:visible;left:0px;top:0px;\">\n" + "  <img style=\"left:0px;top:0px;\" border=\"0\" src=\"data/images/ebenenansicht/607/GW2B-Ebene-1-web-ohne%20Raum.svg\" />\n" + "</div>";
		CrosshairedImage result = sut.retreiveCrosshairedImage(requestUrl);

		assertEquals(expected, retreiveHtml(result.getMap()));
	}

	public void testCrosshairIsRetreived() {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<div id=\"r9463\" style=\"position:absolute;z-index:2;visibility:visible;left:179px;top:287px;\">\n" + "    <input type=\"image\" alt=\"B1215\" src=\"http://oracle-web.zfn.uni-bremen.de/images/fadenkreuz.gif\" size=\"40\" height=\"40\" onmouseover=\"my_div_style=document.getElementById('ri9463').style;my_div_style.visibility='visible';\" onmouseout=\"my_div_style=document.getElementById('ri9463').style;my_div_style.visibility='hidden';\" />\n" + "</div>";
		CrosshairedImage result = sut.retreiveCrosshairedImage(requestUrl);

		assertEquals(expected, retreiveHtml(result.getCrosshair()));
	}

	public void testGW2RoomB1215IsConstructed() {
		String expected = "http://oracle-web.zfn.uni-bremen.de/lageplan/lageplan?haus=GW2&raum=B1215&pi_anz=2";
		String building = "GW2";
		String room = "B1215";

		String result = sut.constructImageUrl(building, room);

		assertEquals(expected, result);
	}

	public void testHtmlPageIsConstructed() {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<html><body>" + "<div1></div1><div2></div2>" + "</body></html>";

		TagNode div1 = new TagNode("div1");
		TagNode div2 = new TagNode("div2");

		String result = sut.constructHtmlPage(div1, div2);

		assertEquals(expected, result);
	}
}
