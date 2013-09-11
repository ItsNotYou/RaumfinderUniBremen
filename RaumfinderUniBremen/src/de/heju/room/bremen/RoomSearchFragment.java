package de.heju.room.bremen;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class RoomSearchFragment extends Fragment {

	private RoomFindable mListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.fragment_room_search, container);

		Spinner spinner = (Spinner) result.findViewById(R.id.room_search_building);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.buildings_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		EditText loginName = (EditText) result.findViewById(R.id.room_search_room);
		loginName.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == R.id.room_search_start || actionId == EditorInfo.IME_NULL) {
					searchRequested();
					return true;
				}
				return false;
			}
		});

		result.findViewById(R.id.room_search_start).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				searchRequested();
			}
		});

		return result;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mListener = (RoomFindable) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement " + RoomFindable.class.getName());
		}
	}

	private void searchRequested() {
		Spinner spinner = (Spinner) getView().findViewById(R.id.room_search_building);
		String building = spinner.getSelectedItem().toString();

		EditText text = (EditText) getView().findViewById(R.id.room_search_room);
		String room = text.getText().toString();

		RoomFindingRequest request = new RoomFindingRequest();
		request.setBuilding(building);
		request.setRoom(room);

		mListener.requestRoomFinding(request);
	}
}
