package com.markkarlsrud.mynews.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markkarlsrud.mynews.R;
import com.markkarlsrud.mynews.activities.NewsFeedActivity;
import com.markkarlsrud.mynews.model.Article;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArticleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleFragment extends Fragment {
    private static final String ARG_PARAM1 = "article";

    private Article article;
    private Drawable articleImage;

    private OnFragmentInteractionListener mListener;

    public ArticleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param article Article
     * @return A new instance of fragment ArticleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArticleFragment newInstance(Article article) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, article);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            article = (Article) getArguments().getSerializable(ARG_PARAM1);
        }
//        try {
//            articleImage = Drawable.createFromStream(new URL(escapeJava(article.getUrlToImage())).openStream(), "articleImage.png");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);

        TextView title = (TextView) view.findViewById(R.id.article_title);
        title.setText(article.getTitle());

        ImageView imageView = (ImageView) view.findViewById(R.id.article_image);
        ((NewsFeedActivity) getActivity()).addDrawableFromUrl(article.getUrlToImage(), imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(article.getUrl()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setPackage("com.android.chrome");
                getActivity().getApplicationContext().startActivity(intent);
            }
        });

        TextView articleContent = (TextView) view.findViewById(R.id.article_content);
        articleContent.setCustomSelectionActionModeCallback(new StyleCallback(articleContent));
//        Spannable spanText = Spannable.Factory.getInstance().newSpannable(article.getDescription());
        articleContent.setText(article.getDescription());

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class StyleCallback implements ActionMode.Callback {
        private TextView view;

        public StyleCallback(TextView view) {
            this.view = view;
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Log.d("MyNews", "onCreateActionMode");
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            menu.removeItem(android.R.id.selectAll);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Log.d("MyNews", String.format("onActionItemClicked item=%s/%d", item.toString(), item.getItemId()));
            CharacterStyle cs;
            int start = view.getSelectionStart();
            int end = view.getSelectionEnd();
            SpannableStringBuilder ssb = new SpannableStringBuilder(view.getText());

            switch(item.getItemId()) {
                case R.id.comment:
                    cs = new RelativeSizeSpan(1.1f);
                    ssb.setSpan(cs, start, end, 1);
                    view.setText(ssb);
                    return true;
                case R.id.bold:
                    cs = new StyleSpan(Typeface.BOLD);
                    ssb.setSpan(cs, start, end, 1);
                    view.setText(ssb);
                    return true;

                case R.id.italic:
                    cs = new StyleSpan(Typeface.ITALIC);
                    ssb.setSpan(cs, start, end, 1);
                    view.setText(ssb);
                    return true;

                case R.id.underline:
                    cs = new UnderlineSpan();
                    ssb.setSpan(cs, start, end, 1);
                    view.setText(ssb);
                    return true;
            }
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }
    }
}
