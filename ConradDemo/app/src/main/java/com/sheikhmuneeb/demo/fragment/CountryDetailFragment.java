package com.sheikhmuneeb.demo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sheikhmuneeb.demo.R;
import com.sheikhmuneeb.demo.model.CountryModel;
import com.sheikhmuneeb.demo.utility.HttpManager;

import java.util.Random;

public class CountryDetailFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private OnContentUpdateListener mCallback;

    private TextView title;
    private TextView description;
    private ImageView countryFlag;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CountryDetailFragment newInstance(int sectionNumber) {
        CountryDetailFragment fragment = new CountryDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public CountryDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_country_detail, container, false);
        countryFlag = (ImageView) rootView.findViewById(R.id.imageViewFlag);
        title = (TextView) rootView.findViewById(R.id.textViewTitle);
        description = (TextView) rootView.findViewById(R.id.textViewDescription);

        refreshContent();

        return rootView;
    }

    // Container Activity must implement this interface
    public interface OnContentUpdateListener {
        public void onContentRefresh();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnContentUpdateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public void refreshContent() {
        new FetchDetails().execute();
    }

    private class FetchDetails extends AsyncTask<Void, Void, CountryModel> {
        protected CountryModel doInBackground(Void... params) {
            String response = HttpManager.getCountryDetails();
            if (response != null) {
                CountryModel countryModel = CountryModel.fromJson(response);
                return countryModel;
            }
            return null;
        }

        protected void onPostExecute(CountryModel countryModel) {
            if (countryModel != null) {
                int randomNumber = randInt(0, countryModel.worldpopulation.size() - 1);
                CountryModel.Worldpopulation worldPopulation = countryModel.worldpopulation.get(randomNumber);

                title.setText("Country: " + worldPopulation.country);
                description.setText("Population: " + worldPopulation.population);
                ImageLoader.getInstance().displayImage(worldPopulation.flag, countryFlag);
            }
        }
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public int randInt(int min, int max) {
        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
