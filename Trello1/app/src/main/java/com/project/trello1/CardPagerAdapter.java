package com.project.trello1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter{

    private ArrayList<CardItem> cardItems = new ArrayList<>();
    private List<CardView> cardViews = new ArrayList<>();
    private float BaseElevation;

    public void addCardItem(String _cardCode, String _cardName, String _finish) {
        cardViews.add(null);
        CardItem cardItem = new CardItem(_cardCode, _cardName, _finish);
        cardItems.add(cardItem);
    }

    @Override
    public float getBaseElevation() {
        return BaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return cardViews.get(position);
    }

    @Override
    public int getCount() {
        return cardItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.viewpager_adapter, container, false);
        container.addView(view);
        bind(cardItems.get(position), view);
        CardView cardView = view.findViewById(R.id.cardView);

        if (BaseElevation == 0) {
            BaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(BaseElevation * MAX_ELEVATION_FACTOR);
        cardViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        cardViews.set(position, null);
    }

    private void bind(CardItem item, View view) {
        TextView cardName = view.findViewById(R.id.cardName);
        // CheckBox checkBox = view.findViewById(R.id.checkBox);
        cardName.setText(item.getCardName());
        // checkBox.setChecked(item.isFinish());
    }
}
