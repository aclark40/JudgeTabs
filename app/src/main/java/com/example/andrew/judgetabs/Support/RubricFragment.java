package com.example.andrew.judgetabs.Support;

        import android.app.Activity;
        import android.net.Uri;
        import android.os.Bundle;
        import android.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ListView;

        import com.example.andrew.judgetabs.Objects.Criterion;
        import com.example.andrew.judgetabs.Support.Adapters.CriterionAdapter;
        import com.example.andrew.judgetabs.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RubricFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RubricFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    private ListView criteria_lv;

    public RubricFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rubric, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        criteria_lv = (ListView) getView().findViewById(R.id.criteria_lv);
        populateCriteria();
    }

    /**
     * Fills the criteria ListView with a CriterionAdapter that contains all criteria for the current team
     */
    private void populateCriteria() {
        criteria_lv.setAdapter(new CriterionAdapter(getActivity(), new Criterion[17]));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}