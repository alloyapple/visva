package vn.com.shoppie.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class MagicTextView extends TextView
{
//	final boolean topDown;

	public MagicTextView( Context context, 
			AttributeSet attrs )
	{
		super( context, attrs );
//		final int gravity = getGravity();
//		if ( Gravity.isVertical( gravity )
//				&& ( gravity & Gravity.VERTICAL_GRAVITY_MASK ) 
//				== Gravity.BOTTOM )
//		{
//			setGravity( 
//					( gravity & Gravity.HORIZONTAL_GRAVITY_MASK )
//					| Gravity.TOP );
//			topDown = false;
//		}
//		else
//		{
//			topDown = true;
//		}
		setGravity(Gravity.BOTTOM);
	}

	@Override
	protected void onMeasure( int widthMeasureSpec, 
			int heightMeasureSpec )
	{
		super.onMeasure( heightMeasureSpec, 
				widthMeasureSpec );
		setMeasuredDimension( getMeasuredHeight(), 
				getMeasuredWidth() );
	}

	@Override
	protected void onDraw( Canvas canvas )
	{
		TextPaint textPaint = getPaint();
		textPaint.setColor( getCurrentTextColor() );
		textPaint.drawableState = getDrawableState();
		
		int length = (int) textPaint.measureText(getText().toString());
		
		canvas.save();

		canvas.rotate(45, length - getWidth() , getTextSize() * 1.8f);

//		canvas.translate( getCompoundPaddingLeft(), 
//				getHeight() / 2 );

		getLayout().draw( canvas );
		canvas.restore();
	}
}