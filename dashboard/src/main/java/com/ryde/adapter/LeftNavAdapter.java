package com.ryde.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ryde.R;
import com.ryde.model.Data;

import java.util.ArrayList;

/**
 * The Adapter class for the ListView displayed in the left navigation drawer.
 */
public class LeftNavAdapter extends BaseAdapter {
	private int[] imageIndex = { R.drawable.ic_msetting,
								R.drawable.ic_mpayment,
								R.drawable.ic_mfeedbacks,
								R.drawable.ic_mtell_a_friend,
								R.drawable.ic_mrateus,
								R.drawable.ic_mabout_us
	};

	/** The items. */
	private ArrayList<Data> items;

	/** The context. */
	private Context context;

	/** The selection. */
	private int selection;

	/**
	 * Checks if is selection.
	 * 
	 * @return the int
	 */
	public int isSelection()
	{
		return selection;
	}

	/**
	 * Sets the selection.
	 * 
	 * @param selection
	 *            the new selection
	 */
	public void setSelection(int selection)
	{
		this.selection = selection;
		notifyDataSetChanged();
	}

	/**
	 * Instantiates a new left navigation adapter.
	 * 
	 * @param context
	 *            the context of activity
	 * @param items
	 *            the array of items to be displayed on ListView
	 */
	public LeftNavAdapter(Context context, ArrayList<Data> items)
	{
		this.context = context;
		this.items = items;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount()
	{
		return items.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Data getItem(int arg0)
	{
		return items.get(arg0);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position)
	{
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
			convertView = LayoutInflater.from(context).inflate(R.layout.left_nav_item, null);

		Data f = getItem(position);
		TextView lbl = (TextView) convertView.findViewById(R.id.lbl);
		TextView textViewImage = (TextView) convertView.findViewById(R.id.textViewImage);

//		Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");
//		textViewImage.setTypeface(myTypeface);

		textViewImage.setBackgroundResource(imageIndex[position]);

		lbl.setText(f.getTexts()[0]);

		if (selection == position)
		{
			lbl.setTextColor(context.getResources().getColor(R.color.black));
			textViewImage.setTextColor(context.getResources().getColor(R.color.black));
//			textViewImage.setText(f.getResources()[0]);
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.main_blue));
		}
		else
		{
			lbl.setTextColor(context.getResources().getColor(R.color.black));
			textViewImage.setTextColor(context.getResources().getColor(R.color.black));
			convertView.setBackgroundResource(0);
		}

		return convertView;
	}

}
