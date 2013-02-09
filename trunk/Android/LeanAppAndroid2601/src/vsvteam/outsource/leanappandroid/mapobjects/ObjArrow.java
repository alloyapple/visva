package vsvteam.outsource.leanappandroid.mapobjects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;

public abstract class ObjArrow extends MapObject{
	private float kY, kX;
	protected PointF mEdgeFist = new PointF();
	protected PointF mTriangle1 = new PointF();
	protected PointF mTriangle2 = new PointF();
	protected PointF mEdgeEnd = new PointF();
	public MapObject mFisrtObject;
	public MapObject mEndObject;
	public PointF mFisrtPoint = new PointF();
	public PointF mEndPoint = new PointF();

	public ObjArrow(Context pContext, MapObject pFirst, MapObject pEnd) {
		// TODO Auto-generated constructor stub
		super(pContext, 0, 0);
		mFisrtObject = pFirst;
		mEndObject = pEnd;
		mFisrtPoint = mFisrtObject.getCenter();
		mEndPoint = mEndObject.getCenter();

	}

	public void getEdge(PointF pFirstResult, PointF pEndResult, PointF pA,
			PointF pB) {
		kX = kY = 0;
		if (pA.x == pB.x) {
			pFirstResult.x = pEndResult.x = pA.x;
			if (pA.y >= pB.y) {
				pFirstResult.y = mFisrtObject.getParam().topMargin;
				pEndResult.y = mEndObject.getLayout().getBottom();
			} else {
				pFirstResult.y = mFisrtObject.getLayout().getBottom();
				pEndResult.y = mEndObject.getParam().topMargin;
			}
		} else {
			kY = (pA.y - pB.y) / (pA.x - pB.x);
		}
		if (pA.y == pB.y) {
			pFirstResult.y = pEndResult.y = pA.y;
			if (pA.x >= pB.x) {
				pFirstResult.x = mFisrtObject.getParam().leftMargin;
				pEndResult.x = mEndObject.getLayout().getRight();
			} else {
				pFirstResult.x = mFisrtObject.getLayout().getRight();
				pEndResult.x = mEndObject.getParam().leftMargin;
			}
		} else {
			kX = (pA.x - pB.x) / (pA.y - pB.y);
		}
		if (kX != 0 && kY != 0) {
			if (isScopeValueDomain(mFisrtObject.getParam().leftMargin, pA.x,
					pB.x)) {
				float yLeft = value_funtionY(pA,
						mFisrtObject.getParam().leftMargin);
				if (mFisrtObject.getParam().topMargin < yLeft
						&& yLeft < mFisrtObject.getLayout().getBottom()) {
					pFirstResult.x = mFisrtObject.getParam().leftMargin;
					pFirstResult.y = yLeft;
				}
			}
			if (isScopeValueDomain(mFisrtObject.getLayout().getRight(), pA.x,
					pB.x)) {
				float yRight = value_funtionY(pA, mFisrtObject.getLayout()
						.getRight());
				if (mFisrtObject.getParam().topMargin < yRight
						&& yRight < mFisrtObject.getLayout().getBottom()) {
					pFirstResult.x = mFisrtObject.getLayout().getRight();
					pFirstResult.y = yRight;
				}
			}
			if (isScopeValueDomain(mFisrtObject.getParam().topMargin, pA.y,
					pB.y)) {
				float xTop = value_funtionX(pA,
						mFisrtObject.getParam().topMargin);
				if (mFisrtObject.getParam().leftMargin < xTop
						&& xTop < mFisrtObject.getLayout().getRight()) {
					pFirstResult.x = xTop;
					pFirstResult.y = mFisrtObject.getParam().topMargin;
				}
			}
			if (isScopeValueDomain(mFisrtObject.getLayout().getBottom(), pA.y,
					pB.y)) {
				float xBottom = value_funtionX(pA, mFisrtObject.getLayout()
						.getBottom());
				if (mFisrtObject.getParam().leftMargin < xBottom
						&& xBottom < mFisrtObject.getLayout().getRight()) {
					pFirstResult.x = xBottom;
					pFirstResult.y = mFisrtObject.getLayout().getBottom();
				}
			}

			if (isScopeValueDomain(mEndObject.getParam().leftMargin, pA.x, pB.x)) {
				float yLeft = value_funtionY(pA,
						mEndObject.getParam().leftMargin);
				if (mEndObject.getParam().topMargin < yLeft
						&& yLeft < mEndObject.getLayout().getBottom()) {
					pEndResult.x = mEndObject.getParam().leftMargin;
					pEndResult.y = yLeft;
				}
			}
			if (isScopeValueDomain(mEndObject.getLayout().getRight(), pA.x,
					pB.x)) {
				float yRight = value_funtionY(pA, mEndObject.getLayout()
						.getRight());
				if (mEndObject.getParam().topMargin < yRight
						&& yRight < mEndObject.getLayout().getBottom()) {
					pEndResult.x = mEndObject.getLayout().getRight();
					pEndResult.y = yRight;
				}
			}
			if (isScopeValueDomain(mEndObject.getParam().topMargin, pA.y, pB.y)) {
				float xTop = value_funtionX(pA, mEndObject.getParam().topMargin);
				if (mEndObject.getParam().leftMargin < xTop
						&& xTop < mEndObject.getLayout().getRight()) {
					pEndResult.x = xTop;
					pEndResult.y = mEndObject.getParam().topMargin;
				}
			}
			if (isScopeValueDomain(mEndObject.getLayout().getBottom(), pA.y,
					pB.y)) {
				float xBottom = value_funtionX(pA, mEndObject.getLayout()
						.getBottom());
				if (mEndObject.getParam().leftMargin < xBottom
						&& xBottom < mEndObject.getLayout().getRight()) {
					pEndResult.x = xBottom;
					pEndResult.y = mEndObject.getLayout().getBottom();
				}
			}
		}
	}

	public PointF getIntersect(PointF pF1, PointF pE1, PointF pF2, PointF pE2) {
		PointF result = new PointF();
		float kY1 = 0, kY2 = 0;
		if (pF1.x == pE1.x) {
			result.x = pF1.x;
		} else {
			kY1 = (pF1.y - pE1.y) / (pF1.x - pE1.x);
		}
		if (pF2.x == pE2.x) {
			result.x = pF2.x;
		} else {
			kY2 = (pF2.y - pE2.y) / (pF2.x - pE2.x);
		}
		
		if (kY1 == 0) {
			result.y = value_funtionY(kY2, pF2, result.x);
		} else if (kY2 == 0) {
			result.y = value_funtionY(kY1, pF1, result.x);
		} else {
			if (kY1 == kY2)
				return null;
			result.x = (kY1 * pF1.x - kY2 * pF2.x + pF2.y - pF1.y)
					/ (kY1 - kY2);
			result.y = value_funtionY(kY2, pF2, result.x);
		}
		return result;
	}

	public float value_funtionY(float k, PointF p0, float pX) {
		return k * (pX - p0.x) + p0.y;
	}

	public void getTriangle(float baseFrac) {
		float deltaX = mEdgeEnd.x - mEdgeFist.x;
		float deltaY = mEdgeEnd.y - mEdgeFist.y;
		float frac = baseFrac
				* (float) (Math.sqrt(260 * 260 + 210 * 210) / getDistance(
						mEdgeFist, mEdgeEnd));
		mTriangle1.x = mEdgeFist.x
				+ (float) ((1 - frac) * deltaX + frac * deltaY);
		mTriangle1.y = mEdgeFist.y
				+ (float) ((1 - frac) * deltaY - frac * deltaX);
		mTriangle2.x = mEdgeFist.x
				+ (float) ((1 - frac) * deltaX - frac * deltaY);
		mTriangle2.y = mEdgeFist.y
				+ (float) ((1 - frac) * deltaY + frac * deltaX);
	}

	public double getCosSlope() {
		return (Math.abs(mFisrtPoint.x - mEndPoint.x) / getDistance(
				mFisrtPoint, mEndPoint));
	}

	public double getSinSlope() {
		return (Math.abs(mFisrtPoint.y - mEndPoint.y) / getDistance(
				mFisrtPoint, mEndPoint));
	}

	public double getDistance(PointF pA, PointF pB) {
		return Math.sqrt(((pA.x - pB.x) * (pA.x - pB.x) + (pA.y - pB.y)
				* (pA.y - pB.y)));
	}

	public boolean isScopeValueDomain(float v, float v1, float v2) {
		if ((v >= v1 && v <= v2) || (v >= v2 && v <= v1))
			return true;
		return false;
	}

	public float value_funtionX(PointF p0, float pY) {
		return kX * (pY - p0.y) + p0.x;
	}

	public float value_funtionY(PointF p0, float pX) {
		return kY * (pX - p0.x) + p0.y;
	}

	public abstract void drawArrow(Canvas cv);

	public abstract void updateParam();

	public abstract void renderArrow(Canvas cv);
	@Override
	public void drawObjects(Canvas cv) {
		// TODO Auto-generated method stub
		
	}
}
