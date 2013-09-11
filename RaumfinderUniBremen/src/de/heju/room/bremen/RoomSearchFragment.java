package de.heju.room.bremen;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RoomSearchFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.fragment_room_search, container);

		Context c = container.getContext();

		// Spinner spinner = (Spinner) result.findViewById(R.id.room_search_building);
		// ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.buildings_array, android.R.layout.simple_spinner_item);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// spinner.setAdapter(adapter);

		return result;
	}
}
