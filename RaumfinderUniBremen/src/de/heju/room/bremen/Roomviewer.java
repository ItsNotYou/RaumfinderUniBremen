package de.heju.room.bremen;

import android.app.Activity;
import android.os.Bundle;

public class Roomviewer extends Activity implements RoomFindable {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roomviewer);
	}

	@Override
	public void requestRoomFinding(RoomFindingRequest request) {
		RoomMapFragment map = (RoomMapFragment) getFragmentManager().findFragmentById(R.id.room_map_fragment);
		map.requestRoomFinding(request);
	}
}
