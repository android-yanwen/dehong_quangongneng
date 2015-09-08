package com.gtafe.taiyuan_sensor;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class CircleMenuLayout extends ViewGroup {

	private int mRadius;
	/**
	 * ��������child item��Ĭ�ϳߴ�
	 */
	private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4f;
	/**
	 * �˵�������child��Ĭ�ϳߴ�
	 */
	private float RADIO_DEFAULT_CENTERITEM_DIMENSION = 1 / 3f;
	/**
	 * ���������ڱ߾�,����padding���ԣ�����߾����øñ���
	 */
	private static final float RADIO_PADDING_LAYOUT = 1 / 12f;

	/**
	 * ��ÿ���ƶ��Ƕȴﵽ��ֵʱ����Ϊ�ǿ����ƶ�
	 */
	private static final int FLINGABLE_VALUE = 300;

	/**
	 * ����ƶ��Ƕȴﵽ��ֵ�������ε��
	 */
	private static final int NOCLICK_VALUE = 3;

	/**
	 * ��ÿ���ƶ��Ƕȴﵽ��ֵʱ����Ϊ�ǿ����ƶ�
	 */
	private int mFlingableValue = FLINGABLE_VALUE;
	/**
	 * ���������ڱ߾�,����padding���ԣ�����߾����øñ���
	 */
	private float mPadding;

	/**
	 * ����ʱ�Ŀ�ʼ�Ƕ�
	 */
	private double mStartAngle = 0;
	/**
	 * �˵�����ı�
	 */
	private String[] mItemTexts;
	/**
	 * �˵����ͼ��
	 */
	private int[] mItemImgs;

	/**
	 * �˵��ĸ���
	 */
	private int mMenuItemCount;

	/**
	 * ��ⰴ�µ�̧��ʱ��ת�ĽǶ�
	 */
	private float mTmpAngle;
	/**
	 * ��ⰴ�µ�̧��ʱʹ�õ�ʱ��
	 */
	private long mDownTime;

	/**
	 * �ж��Ƿ������Զ�����
	 */
	private boolean isFling;

	public CircleMenuLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// ����padding
		setPadding(0, 0, 0, 0);
	}

	/**
	 * ���ò��ֵĿ�ߣ�������menu item���
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int resWidth = 0;
		int resHeight = 0;

		/**
		 * ���ݴ���Ĳ������ֱ��ȡ����ģʽ�Ͳ���ֵ
		 */
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		int height = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		/**
		 * �������߸ߵĲ���ģʽ�Ǿ�ȷֵ
		 */
		if (widthMode != MeasureSpec.EXACTLY
				|| heightMode != MeasureSpec.EXACTLY)
		{
			// ��Ҫ����Ϊ����ͼ�ĸ߶�
			resWidth = getSuggestedMinimumWidth();
			// ���δ���ñ���ͼƬ��������Ϊ��Ļ��ߵ�Ĭ��ֵ
			resWidth = resWidth == 0 ? getDefaultWidth() : resWidth;

			resHeight = getSuggestedMinimumHeight();
			// ���δ���ñ���ͼƬ��������Ϊ��Ļ��ߵ�Ĭ��ֵ
			resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
		} else
		{
			// ���������Ϊ��ȷֵ����ֱ��ȡСֵ��
			resWidth = resHeight = Math.min(width, height);
		}

		setMeasuredDimension(resWidth, resHeight);

		// ��ð뾶
		mRadius = Math.max(getMeasuredWidth(), getMeasuredHeight());

		// menu item����
		final int count = getChildCount();
		// menu item�ߴ�
		int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
		// menu item����ģʽ
		int childMode = MeasureSpec.EXACTLY;

		// ��������
		for (int i = 0; i < count; i++)
		{
			final View child = getChildAt(i);

			if (child.getVisibility() == GONE)
			{
				continue;
			}

			// ����menu item�ĳߴ磻�Լ������úõ�ģʽ��ȥ��item���в���
			int makeMeasureSpec = -1;

			if (child.getId() == R.id.id_circle_menu_item_center)
			{
				makeMeasureSpec = MeasureSpec.makeMeasureSpec(
						(int) (mRadius * RADIO_DEFAULT_CENTERITEM_DIMENSION),
						childMode);
			} else
			{
				makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize,
						childMode);
			}
			child.measure(makeMeasureSpec, makeMeasureSpec);
		}
		mPadding = RADIO_PADDING_LAYOUT * mRadius;
	}

	/**
	 * ���Ĭ�ϸ�layout�ĳߴ�
	 * 
	 * @return
	 */
	private int getDefaultWidth()
	{
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
	}
	
	/**
	 * ����menu item��λ��
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		int layoutRadius = mRadius;

		// Laying out the child views
		final int childCount = getChildCount();

		int left, top;
		// menu item �ĳߴ�
		int cWidth = (int) (layoutRadius * RADIO_DEFAULT_CHILD_DIMENSION);

		// ����menu item�ĸ���������Ƕ�
		float angleDelay = 360 / (getChildCount() - 1);

		// ����ȥ����menuitem��λ��
		for (int i = 0; i < childCount; i++)
		{
			final View child = getChildAt(i);

			if (child.getId() == R.id.id_circle_menu_item_center)
				continue;

			if (child.getVisibility() == GONE)
			{
				continue;
			}

			mStartAngle %= 360;

			// ���㣬���ĵ㵽menu item���ĵľ���
			float tmp = layoutRadius / 2f - cWidth / 2 - mPadding;

			// tmp cosa ��menu item���ĵ�ĺ�����
			left = layoutRadius
					/ 2
					+ (int) Math.round(tmp
							* Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f
							* cWidth);
			// tmp sina ��menu item��������
			top = layoutRadius
					/ 2
					+ (int) Math.round(tmp
							* Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f
							* cWidth);

			child.layout(left, top, left + cWidth, top + cWidth);
			// ���ӳߴ�
			mStartAngle += angleDelay;
		}

		// �ҵ����ĵ�view�������������onclick�¼�
		View cView = findViewById(R.id.id_circle_menu_item_center);
		if (cView != null)
		{
			cView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{

					if (mOnMenuItemClickListener != null)
					{
						mOnMenuItemClickListener.itemCenterClick(v);
					}
				}
			});
		// ����center itemλ��
		int cl = layoutRadius / 2 - cView.getMeasuredWidth() / 2;
		int cr = cl + cView.getMeasuredWidth();
		cView.layout(cl, cl, cr, cr);
		}
	}
	
	/**
	 * ���ò˵���Ŀ��ͼ����ı�
	 * 
	 * @param resIds
	 */
	public void setMenuItemIconsAndTexts(int[] resIds, String[] texts)
	{
		mItemImgs = resIds;
		mItemTexts = texts;

		// �������
		if (resIds == null && texts == null)
		{
			throw new IllegalArgumentException("�˵����ı���ͼƬ����������һ");
		}

		// ��ʼ��mMenuCount
		mMenuItemCount = resIds == null ? texts.length : resIds.length;

		if (resIds != null && texts != null)
		{
			mMenuItemCount = Math.min(resIds.length, texts.length);
		}
		addMenuItems();
	}
	
	/**
	 * ��Ӳ˵���
	 */
	private void addMenuItems()
	{
		LayoutInflater mInflater = LayoutInflater.from(getContext());

		/**
		 * �����û����õĲ�������ʼ��view
		 */
		for (int i = 0; i < mMenuItemCount; i++)
		{
			final int j = i;
			View view = mInflater.inflate(R.layout.circle_menu_item, this,
					false);
			ImageView iv = (ImageView) view
					.findViewById(R.id.id_circle_menu_item_image);
			TextView tv = (TextView) view
					.findViewById(R.id.id_circle_menu_item_text);

			if (iv != null)
			{
				iv.setVisibility(View.VISIBLE);
				iv.setImageResource(mItemImgs[i]);
				iv.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						
						if (mOnMenuItemClickListener != null)
						{
							mOnMenuItemClickListener.itemClick(v, j);
						}
					}
				});
			}
			if (tv != null)
			{
				tv.setVisibility(View.VISIBLE);
				tv.setText(mItemTexts[i]);
			}

			// ���view��������
			addView(view);
		}
	}
	
	/**
	 * MenuItem�ĵ���¼��ӿ�
	 * 
	 * @author zhy
	 * 
	 */
	public interface OnMenuItemClickListener
	{
		void itemClick(View view, int pos);

		void itemCenterClick(View view);
	}

	/**
	 * MenuItem�ĵ���¼��ӿ�
	 */
	private OnMenuItemClickListener mOnMenuItemClickListener;

	/**
	 * ����MenuItem�ĵ���¼��ӿ�
	 * 
	 * @param mOnMenuItemClickListener
	 */
	public void setOnMenuItemClickListener(
			OnMenuItemClickListener mOnMenuItemClickListener)
	{
		this.mOnMenuItemClickListener = mOnMenuItemClickListener;
	}
	
/****************************************************************************************************/
	/**
	 * ��¼��һ�ε�x��y����
	 */
	private float mLastX;
	private float mLastY;

	/**
	 * �Զ�������Runnable
	 */
	private AutoFlingRunnable mFlingRunnable;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		float x = event.getX();
		float y = event.getY();

		Log.e("TAG", "x = " + x + " , y = " + y);

		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:

			mLastX = x;
			mLastY = y;
			mDownTime = System.currentTimeMillis();
			mTmpAngle = 0;

			// �����ǰ�Ѿ��ڿ��ٹ���
			if (isFling)
			{
				// �Ƴ����ٹ����Ļص�
				removeCallbacks(mFlingRunnable);
				isFling = false;
				return true;
			}

			break;
		case MotionEvent.ACTION_MOVE:

			/**
			 * ��ÿ�ʼ�ĽǶ�
			 */
			float start = getAngle(mLastX, mLastY);
			/**
			 * ��õ�ǰ�ĽǶ�
			 */
			float end = getAngle(x, y);

			// Log.e("TAG", "start = " + start + " , end =" + end);
			// �����һ�������ޣ���ֱ��end-start���Ƕ�ֵ������ֵ
			if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4)
			{
				mStartAngle += end - start;
				mTmpAngle += end - start;
			} else
			// ���������ޣ�ɫ�Ƕ�ֵ�Ǹ�ֵ
			{
				mStartAngle += start - end;
				mTmpAngle += start - end;
			}
			// ���²���
			requestLayout();

			mLastX = x;
			mLastY = y;

			break;
		case MotionEvent.ACTION_UP:

			// ���㣬ÿ���ƶ��ĽǶ�
			float anglePerSecond = mTmpAngle * 1000
					/ (System.currentTimeMillis() - mDownTime);

			// Log.e("TAG", anglePrMillionSecond + " , mTmpAngel = " +
			// mTmpAngle);

			// ����ﵽ��ֵ��Ϊ�ǿ����ƶ�
			if (Math.abs(anglePerSecond) > mFlingableValue && !isFling)
			{
				// postһ������ȥ�Զ�����
				post(mFlingRunnable = new AutoFlingRunnable(anglePerSecond));

				return true;
			}

			// �����ǰ��ת�Ƕȳ���NOCLICK_VALUE���ε��
			if (Math.abs(mTmpAngle) > NOCLICK_VALUE)
			{
				return true;
			}

			break;
		}
		return super.dispatchTouchEvent(event);
	}
	/**
	 * ���ݴ�����λ�ã�����Ƕ�
	 * 
	 * @param xTouch
	 * @param yTouch
	 * @return
	 */
	private float getAngle(float xTouch, float yTouch)
	{
		double x = xTouch - (mRadius / 2d);
		double y = yTouch - (mRadius / 2d);
		return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
	}
	
	/**
	 * ���ݵ�ǰλ�ü�������
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int getQuadrant(float x, float y)
	{
		int tmpX = (int) (x - mRadius / 2);
		int tmpY = (int) (y - mRadius / 2);
		if (tmpX >= 0)
		{
			return tmpY >= 0 ? 4 : 1;
		} else
		{
			return tmpY >= 0 ? 3 : 2;
		}

	}
	
	/**
	 * �Զ�����������
	 * 
	 * @author zhy
	 * 
	 */
	private class AutoFlingRunnable implements Runnable
	{

		private float angelPerSecond;

		public AutoFlingRunnable(float velocity)
		{
			this.angelPerSecond = velocity;
		}

		public void run()
		{
			// ���С��20,��ֹͣ
			if ((int) Math.abs(angelPerSecond) < 20)
			{
				isFling = false;
				return;
			}
			isFling = true;
			// ���ϸı�mStartAngle�����������/30Ϊ�˱������̫��
			mStartAngle += (angelPerSecond / 30);
			// �𽥼�С���ֵ
			angelPerSecond /= 1.0666F;
			postDelayed(this, 30);
			// ���²���
			requestLayout();
		}
	}
	
}
