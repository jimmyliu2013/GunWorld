package com.lim.gunworld.ui;

/*
 * File Name: MyFlowLayout.java
 * History:
 * Created by mwqi on 2014-4-18
 */

import java.util.ArrayList;
import java.util.List;

import com.lim.gunworld.utils.DimensionUtils;

import android.R.integer;
//import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

	private int hspaceDp = 15;
	private int vspaceDp = 15;
	private int hspace;
	private int vspace;
	private Context mContext;

	public FlowLayout(Context context) {
		super(context);
		init(context);
		mContext = context;
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		mContext = context;
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		mContext = context;
	}

	private void init(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// ********************
		// Initialize
		// ********************
		hspace = DimensionUtils.dpToPx(mContext, hspaceDp);
		vspace = DimensionUtils.dpToPx(mContext, vspaceDp);
		int rw = MeasureSpec.getSize(widthMeasureSpec);
		int rh = MeasureSpec.getSize(heightMeasureSpec);
		int h = 0; // current height
		int w = 0; // current width
		int h1 = 0 + vspace, w1 = 0 + hspace; // Current point to hook the child
												// to
		// ********************
		// Loop through children
		// ********************
		int numOfChildren = this.getChildCount();
		for (int i = 0; i < numOfChildren; i++) {
			// ********************
			// Front of the loop
			// ********************
			View child = this.getChildAt(i);
			this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
			int vw = child.getMeasuredWidth();
			int vh = child.getMeasuredHeight();
			if (w1 + vw > rw) {
				// new line: max of current width and current width position
				// when multiple lines are in play w could be maxed out
				// or in uneven sizes is the max of the right side lines
				// all lines don't have to have the same width
				// some may be larger than others
				w = Math.max(w, w1);
				// reposition the point on the next line
				w1 = 0 + hspace; // start of the line
				h1 = h1 + vh + vspace; // add view height to the current height
			}
			// ********************
			// Middle of the loop
			// ********************
			int w2 = 0, h2 = 0; // new point for the next view
			w2 = w1 + vw + hspace;
			h2 = h1;
			// latest height: current point + height of the view
			// however if the previous height is larger use that one
			h = Math.max(h, h1 + vh);
			// ********************
			// Save the current coords for the view
			// in its layout
			// ********************
			LayoutParams lp = (LayoutParams) child.getLayoutParams();
			lp.x = w1;
			lp.y = h1;
			// ********************
			// Restart the loop
			// ********************
			w1 = w2;
			h1 = h2;
		}
		// ********************
		// End of for
		// ********************
		w = Math.max(w1, w);
		h = h + vspace;
		setMeasuredDimension(resolveSize(w, widthMeasureSpec),
				resolveSize(h, heightMeasureSpec));
	};

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		// Call layout() on children
		int numOfChildren = this.getChildCount();
		for (int i = 0; i < numOfChildren; i++) {
			View child = this.getChildAt(i);
			LayoutParams lp = (LayoutParams) child.getLayoutParams();
			child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y
					+ child.getMeasuredHeight());
		}
	}

	// *********************************************************
	// Layout Param Support
	// *********************************************************
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new FlowLayout.LayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	// Override to allow type-checking of LayoutParams.
	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof FlowLayout.LayoutParams;
	}

	// *********************************************************
	// Custom Layout Definition
	// *********************************************************
	public static class LayoutParams extends ViewGroup.MarginLayoutParams {
		// public int spacing = -1;
		public int x = 0;
		public int y = 0;

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
			// TypedArray a =
			// c.obtainStyledAttributes(attrs, R.styleable.FlowLayout_Layout);
			// spacing =
			// a.getDimensionPixelSize(R.styleable.FlowLayout_Layout_layout_space,
			// 0);
			// a.recycle();
		}

		public LayoutParams(int width, int height) {
			super(width, height);
			// spacing = 20;
		}

		public LayoutParams(ViewGroup.LayoutParams p) {
			super(p);
		}

		public LayoutParams(MarginLayoutParams source) {
			super(source);
		}
	}// eof-layout-params
}// eof-class

