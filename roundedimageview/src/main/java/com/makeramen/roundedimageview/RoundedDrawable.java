/*
 * Copyright (C) 2017 Vincent Mi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.makeramen.roundedimageview;


import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.render.Paint;
import ohos.agp.render.Canvas;
import ohos.agp.render.PixelMapShader;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.render.Shader;
import ohos.agp.utils.Rect;
import ohos.agp.utils.Color;
import ohos.agp.utils.Matrix;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.components.element.Element;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import ohos.agp.components.Image.ScaleMode;
import ohos.agp.utils.RectFloat;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;
import java.util.HashSet;
import java.util.Set;
import java.lang.Throwable;

@SuppressWarnings("UnusedDeclaration")
public class RoundedDrawable extends PixelMapElement {

    public static final String TAG = RoundedDrawable.class.getSimpleName();;
    public static final Color DEFAULT_BORDER_COLOR = Color.BLACK;

    private final Rect mBounds = new Rect();
    private final RectFloat mDrawableRect = new RectFloat();
    private final RectFloat mPixelMapRect = new RectFloat();
    private final PixelMap mPixelMap;
    private final Paint mPixelMapPaint;
    private final int mPixelmapWidth;
    private final int mPixelmapHeight;

    private final RectFloat mBorderRect = new RectFloat();
    private final Paint mBorderPaint;
    private final Matrix mShaderMatrix = new Matrix();
    private final RectFloat mSquareCornersRect = new RectFloat();

    private PixelMapShader.TileMode mTileModeX = PixelMapShader.TileMode.CLAMP_TILEMODE;
    private PixelMapShader.TileMode mTileModeY = PixelMapShader.TileMode.CLAMP_TILEMODE;
    private boolean mRebuildShader = true;

    private float mCornerRadius = 0f;
    // [ topLeft, topRight, bottomLeft, bottomRight ]
    private final boolean[] mCornersRounded = new boolean[] { true, true, true, true };

    private boolean mOval = false;
    private float mBorderWidth = 0;
    private Color mBorderColor = DEFAULT_BORDER_COLOR; 
    private Image.ScaleMode mScaleType = Image.ScaleMode.CLIP_CENTER;


    private void set(RectFloat sourcerect, int left, int top, int width, int height){
        sourcerect.left= left;
        sourcerect.top= top;
        sourcerect.right= left + width;
        sourcerect.bottom= top + height;
    }




    public RoundedDrawable(PixelMap pixelmap) {
        super(pixelmap);
        mPixelMap = pixelmap;
        mPixelmapWidth = pixelmap.getImageInfo().size.width;
        mPixelmapHeight = pixelmap.getImageInfo().size.height;
        set(mPixelMapRect, 0, 0, mPixelmapWidth, mPixelmapHeight);

        mPixelMapPaint = new Paint();
        mPixelMapPaint.setStyle(Paint.Style.FILL_STYLE);
        mPixelMapPaint.setAntiAlias(true);

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE_STYLE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }


    public static RoundedDrawable fromPixelMap(PixelMap pixelmap) {
        if (pixelmap != null) {
            return new RoundedDrawable(pixelmap);
        } else {
            return null;
        }
    }

    public static Element fromElement(Component component, Element drawable, Context context, int mResource) {
        if (drawable != null) {
            if (drawable instanceof RoundedDrawable) {
                // just return if it's already a RoundedDrawable
                return drawable;
            }

            // try to get a bitmap from the drawable and
            PixelMap bm = drawableToPixelmap(component, drawable, context, mResource);
            if (bm != null) {
                return new RoundedDrawable(bm);
            }
        }
        return drawable;
    }

    public static PixelMap drawableToPixelmap(Component component, Element drawable, Context context, int mResource) {
        if (drawable instanceof PixelMapElement) {
            return ((PixelMapElement) drawable).getPixelMap();
        }else if(drawable instanceof ShapeElement) {
            PixelMap pixelMap;
            PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
            int width = Math.max(component.getWidth(), 2);
            int height = Math.max(component.getHeight(), 2);

            initializationOptions.size = new Size(width, height);
            initializationOptions.pixelFormat = PixelFormat.ARGB_8888;
            initializationOptions.editable =true;
            try {

                pixelMap = PixelMap.create( initializationOptions);
                int value =ResUtil.getColor(context, mResource);
                pixelMap.writePixels(value);
            } catch (Throwable e) {
                e.printStackTrace();
                LogUtil.info(TAG, "Failed to create bitmap from drawable!");
                pixelMap = null;
            }

            return pixelMap;
        }

        return null;
    }

    public PixelMap getSourcePixelMap() {
        return mPixelMap;
    }



    private void set(RectFloat rectf, RectFloat mBorderRect){
        rectf.left= mBorderRect.left;
        rectf.top= mBorderRect.top;
        rectf.right= mBorderRect.right;
        rectf.bottom= mBorderRect.bottom;
    }

    private void setNormalRect(Rect destrect, Rect sourcerect){
        destrect.left= sourcerect.left;
        destrect.top= sourcerect.top;
        destrect.right= sourcerect.right;
        destrect.bottom= sourcerect.bottom;
    }

    private void setRect(RectFloat rectf, Rect rect){
        rectf.left= rect.left;
        rectf.top= rect.top;
        rectf.right= rect.right;
        rectf.bottom= rect.bottom;
    }

    private void insetRect(RectFloat rectf, float dx, float dy){
        rectf.left    += dx;
        rectf.top     += dy;
        rectf.right   -= dx;
        rectf.bottom  -= dy;
    }

    public void updateShaderMatrix() {
        float scale;
        float dx;
        float dy;
        float width;
        float height;
        setNormalRect(mBounds, getBounds());

        switch (mScaleType) {
            case CENTER:
                setRect(mBorderRect, mBounds);
                insetRect(mBorderRect,mBorderWidth / 2, mBorderWidth / 2);

                width =mBorderRect.right -mBorderRect.left;
                height =mBorderRect.bottom -mBorderRect.top;

                mShaderMatrix.reset();
                mShaderMatrix.setTranslate((int) ((width - mPixelmapWidth) * 0.5f + 0.5f),
                  (int) ((height - mPixelmapHeight) * 0.5f + 0.5f));
                break;

            // FIT_CENTER
              case ZOOM_CENTER:
                  set(mBorderRect, mPixelMapRect);
                  RectFloat dst =new RectFloat(0,0,0,0);
                  setRect(dst, mBounds);
                  mShaderMatrix.setRectToRect(mPixelMapRect, dst, Matrix.ScaleToFit.CENTER);
                  mShaderMatrix.mapRect(mBorderRect);
                  insetRect(mBorderRect,mBorderWidth / 2, mBorderWidth / 2);
                  mShaderMatrix.setRectToRect(mPixelMapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                  break;

            //FIT_XY
            case STRETCH:
                setRect(mBorderRect, mBounds);
                insetRect(mBorderRect,mBorderWidth / 2, mBorderWidth / 2);

                mShaderMatrix.reset();
                mShaderMatrix.setRectToRect(mPixelMapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                break;
            default:
                break;
        }

        set(mDrawableRect, mBorderRect);
        mRebuildShader = true;
    }

    @Override
    public void drawToCanvas(Canvas canvas){

        if (mRebuildShader) {
            PixelMapShader bitmapShader = new PixelMapShader(new PixelMapHolder(mPixelMap), Shader.TileMode.CLAMP_TILEMODE, Shader.TileMode.CLAMP_TILEMODE);
            bitmapShader.setShaderMatrix(mShaderMatrix);
            mPixelMapPaint.setShader(bitmapShader, Paint.ShaderType.PIXELMAP_SHADER);
            mRebuildShader =false;
        }

        if (mOval) {
            if (mBorderWidth > 0) {
                canvas.drawOval(mDrawableRect, mPixelMapPaint);
                canvas.drawOval(mBorderRect, mBorderPaint);
            } else {
                canvas.drawOval(mDrawableRect, mPixelMapPaint);
            }
        }
        else
        {
            if (any(mCornersRounded)) {
                float radius = mCornerRadius;

                canvas.drawRoundRect(mDrawableRect, radius, radius, mPixelMapPaint);
            } else {

                canvas.drawRect(mDrawableRect, mPixelMapPaint); //Draw Rect
            }
        }
    }


    private void setRectangle(float left, float top, float right, float bottom) {
        mSquareCornersRect.left=left;
        mSquareCornersRect.top=top;
        mSquareCornersRect.right=right;
        mSquareCornersRect.bottom=bottom;
    }

    private void redrawPixelmapForSquareCorners(Canvas canvas) {
        if (all(mCornersRounded)) {
            // no square corners
            return;
        }

        if (mCornerRadius == 0) {
            return; // no round corners
        }

        int left = (int)mDrawableRect.left;  //dinil set method takes only integer parameters
        int top = (int)mDrawableRect.top;
        int right = (int)mDrawableRect.right;
        int bottom = (int)mDrawableRect.bottom;
        int radius = (int) mCornerRadius;

        if (!mCornersRounded[Corner.TOP_LEFT]) {
            //mSquareCornersRect.set(left, top, left + radius, top + radius); //dinil api preent in rect but needed for rectfloat
            setRectangle(left, top, left + radius, top + radius);
            canvas.drawRect(mSquareCornersRect, mPixelMapPaint);
        }

        if (!mCornersRounded[Corner.TOP_RIGHT]) {
            //mSquareCornersRect.set(right - radius, top, right, radius);   //dinil api preent in rect but needed for rectfloat
            setRectangle(right - radius, top, right, radius);
            canvas.drawRect(mSquareCornersRect, mPixelMapPaint);
        }

        if (!mCornersRounded[Corner.BOTTOM_RIGHT]) {
            //mSquareCornersRect.set(right - radius, bottom - radius, right, bottom);  //dinil api preent in rect but needed for rectfloat
            setRectangle(right - radius, bottom - radius, right, bottom);
            canvas.drawRect(mSquareCornersRect, mPixelMapPaint);
        }

        if (!mCornersRounded[Corner.BOTTOM_LEFT]) {
            //mSquareCornersRect.set(left, bottom - radius, left + radius, bottom);  //dinil api preent in rect but needed for rectfloat
            setRectangle(left, bottom - radius, left + radius, bottom);
            canvas.drawRect(mSquareCornersRect, mPixelMapPaint);
        }
    }

    private void redrawBorderForSquareCorners(Canvas canvas) {
        if (all(mCornersRounded)) {
            // no square corners
            return;
        }

        if (mCornerRadius == 0) {
            return; // no round corners
        }

        float left = mDrawableRect.left;
        float top = mDrawableRect.top;
        float right = mDrawableRect.right;
        float bottom =mDrawableRect.bottom;
        float radius = mCornerRadius;
        float offset = (mBorderWidth / 2);


        if (!mCornersRounded[Corner.TOP_LEFT]) {
            canvas.drawLines(new float[]{left - offset, top, left + radius, top}, mBorderPaint);
            canvas.drawLines(new float[]{left, top - offset, left, top + radius}, mBorderPaint);
        }

        if (!mCornersRounded[Corner.TOP_RIGHT]) {
            canvas.drawLines(new float[]{right - radius - offset, top, right, top}, mBorderPaint);
            canvas.drawLines(new float[]{right, top - offset, right, top + radius}, mBorderPaint);
        }

        if (!mCornersRounded[Corner.BOTTOM_RIGHT]) {
            canvas.drawLines(new float[]{right - radius - offset, bottom, right + offset, bottom}, mBorderPaint);
            canvas.drawLines(new float[]{right, bottom - radius, right, bottom}, mBorderPaint);
        }

        if (!mCornersRounded[Corner.BOTTOM_LEFT]) {
            canvas.drawLines(new float[]{left - offset, bottom, left + radius, bottom}, mBorderPaint);
            canvas.drawLines(new float[]{left, bottom - radius, left, bottom}, mBorderPaint);
        }
    }

    @Override
    public int getAlpha() {
        return (int) mPixelMapPaint.getAlpha();
    }

    @Override
    public void setAlpha(int alpha) {
        mPixelMapPaint.setAlpha(alpha);
    }


  public int getIntrinsicWidth() {
    return mPixelmapWidth;
  }


  public int getIntrinsicHeight() {
    return mPixelmapHeight;
  }


    /**
     * function implemented to get CornerRadius
     *
     * @return the corner radius.
     */
    public float getCornerRadius() {
        return mCornerRadius;
    }


    /**
     * function implemented to get CornerRadius or particular corner
     *
     * @param corner the specific corner to get radius of.
     * @return the corner radius of the specified corner.
     */
    public float getCornerRadius(@Corner int corner) {
        return mCornersRounded[corner] ? mCornerRadius : 0f;
    }

    /**
     * Sets all corners to the specified radius.
     *
     * @param radius the radius.
     * @return the {@link RoundedDrawable} for chaining.
     */
    public RoundedDrawable setCornerRadius(float radius) {
        setCornerRadius(radius, radius, radius, radius);
        return this;
    }

    /**
     * Sets the corner radius of one specific corner.
     *
     * @param corner the corner.
     * @param radius the radius.
     * @return the {@link RoundedDrawable} for chaining.
     */
    public RoundedDrawable setCornerRadius(@Corner int corner, float radius) {
        if (radius != 0 && mCornerRadius != 0 && mCornerRadius != radius) {
            throw new IllegalArgumentException("Multiple nonzero corner radii not yet supported.");
        }

        if (radius == 0) {
            if (only(corner, mCornersRounded)) {
                mCornerRadius = 0;
            }
            mCornersRounded[corner] = false;
        } else {
            if (mCornerRadius == 0) {
                mCornerRadius = radius;
            }
            mCornersRounded[corner] = true;
        }

        return this;
    }

    /**
     * Sets the corner radii of all the corners.
     *
     * @param topLeft top left corner radius.
     * @param topRight top right corner radius
     * @param bottomRight bototm right corner radius.
     * @param bottomLeft bottom left corner radius.
     * @return the {@link RoundedDrawable} for chaining.
     */
    public RoundedDrawable setCornerRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        Set<Float> radiusSet = new HashSet<>(4);
        radiusSet.add(topLeft);
        radiusSet.add(topRight);
        radiusSet.add(bottomRight);
        radiusSet.add(bottomLeft);

        radiusSet.remove(0f);

        if (radiusSet.size() > 1) {
            throw new IllegalArgumentException("Multiple nonzero corner radii not yet supported.");
        }

        if (!radiusSet.isEmpty()) {
            float radius = radiusSet.iterator().next();
            if (Float.isInfinite(radius) || Float.isNaN(radius) || radius < 0) {
                throw new IllegalArgumentException("Invalid radius value: " + radius);
            }
            mCornerRadius = radius;
        } else {
            mCornerRadius = 0f;
        }

        mCornersRounded[Corner.TOP_LEFT] = topLeft > 0;
        mCornersRounded[Corner.TOP_RIGHT] = topRight > 0;
        mCornersRounded[Corner.BOTTOM_RIGHT] = bottomRight > 0;
        mCornersRounded[Corner.BOTTOM_LEFT] = bottomLeft > 0;
        return this;
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public RoundedDrawable setBorderWidth(float width) {
        mBorderWidth = width;
        mBorderPaint.setStrokeWidth(mBorderWidth);
        return this;
    }

    public int getBorderColor() {
        return 0;
    }

    public RoundedDrawable setBorderColor(int color) {
        return setBorderColor(color);
    }
  public Color getBorderColors() {

    return mBorderColor;
  }

  public RoundedDrawable setBorderColor(Color colors) {
    
    mBorderColor = colors;
    mBorderPaint.setColor(colors);
    return this;
  }

    public boolean isOval() {
        return mOval;
    }

    public RoundedDrawable setOval(boolean oval) {
        mOval = oval;
        return this;
    }

    public ScaleMode getScaleMode() {
        return mScaleType;
    }


    public RoundedDrawable setScaleMode(ScaleMode scaleType) {
        if (scaleType == null) {
            scaleType = ScaleMode.ZOOM_CENTER;
        }
        if (mScaleType != scaleType) {
            mScaleType = scaleType;
            updateShaderMatrix();
        }
        return this;
    }

    public Shader.TileMode getTileModeX() {
        return mTileModeX;
    }

    public RoundedDrawable setTileModeX(Shader.TileMode tileModeX) {
        if (mTileModeX != tileModeX) {
            mTileModeX = tileModeX;
            mRebuildShader = true;
        }
        return this;
    }

    public Shader.TileMode getTileModeY() {
        return mTileModeY;
    }

    public RoundedDrawable setTileModeY(Shader.TileMode tileModeY) {
        if (mTileModeY != tileModeY) {
            mTileModeY = tileModeY;
            mRebuildShader = true;
        }
        return this;
    }

    private static boolean only(int index, boolean[] booleans) {
        for (int i = 0, len = booleans.length; i < len; i++) {
            if (booleans[i] != (i == index)) {
                return false;
            }
        }
        return true;
    }

    private static boolean any(boolean[] booleans) {
        for (boolean b : booleans) {
            if (b) { return true; }
        }
        return false;
    }

    private static boolean all(boolean[] booleans) {
        for (boolean b : booleans) {
            if (b) { return false; }
        }
        return true;
    }

}