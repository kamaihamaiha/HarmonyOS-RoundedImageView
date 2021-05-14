/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package com.makeramen.roundedimageview.slice;

import com.makeramen.roundedimageview.LogUtil;
import com.makeramen.roundedimageview.ResourceTable;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.utils.Const;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.app.Context;
import ohos.app.dispatcher.TaskDispatcher;

public class RoundedImageviewSlice extends AbilitySlice {
    private static String TAG = RoundedImageviewSlice.class.getSimpleName();
    private static final long mDelayDuration =50;
    private final int padding = 32;
    private int testcase_value =0;
    private RoundedImageView imageView;
    private Context classcontext;

    @Override
    public void onStop(){
        if(null != imageView)
            imageView.release();
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        classcontext = getContext();

        DirectionalLayout directionLayout = new DirectionalLayout(this);
// Step 2 Set the layout size.
        directionLayout.setWidth(ComponentContainer.LayoutConfig.MATCH_PARENT);
        directionLayout.setHeight(ComponentContainer.LayoutConfig.MATCH_PARENT);
// Step 3 Set the layout attributes and ID (set the ID as required).
        directionLayout.setOrientation(Component.VERTICAL);
        directionLayout.setPadding(padding, padding, padding, padding);

        IntentParams ip = intent.getParams();
        testcase_value = (int)ip.getParam("testcase");
        LogUtil.info(TAG,"testcase_value is" + testcase_value);

        imageView = new RoundedImageView( classcontext);
        run_testcases();
        DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT,
                ComponentContainer.LayoutConfig.MATCH_PARENT);
        imageView.setLayoutConfig(layoutConfig);


        directionLayout.addComponent(imageView);
        super.setUIContent(directionLayout);
    }

    private void run_testcases(){
        //Set parameters to ImageView based on testcase
        switch (testcase_value){
            case Const.TEST_CASE_1:
                RIV_Testcase1();
                break;
            case Const.TEST_CASE_2:
                RIV_Testcase2();
                break;
            case Const.TEST_CASE_3:
                RIV_Testcase3();
                break;
            case Const.TEST_CASE_4:
                RIV_Testcase4();
                break;
            case Const.TEST_CASE_5:
                RIV_Testcase5();
                break;
            case Const.TEST_CASE_6:
                RIV_Testcase6();
                break;
            case Const.TEST_CASE_7:
                RIV_Testcase7();
                break;
            default:
                break;
        }

    }



    private void RIV_Testcase1() {  //Rectangel
        try{

            LogUtil.info(TAG,"RIV_Testcase1");
            imageView.setImageResource(ResourceTable.Media_jellyfish);
            imageView.setScaleMode(Image.ScaleMode.CENTER);

        } catch (Exception e) { }
    }

    private void RIV_Testcase2() {  //Rounded Rectangle
        try{
            LogUtil.info(TAG,"RIV_Testcase2");
            imageView.setImageResource(ResourceTable.Media_jellyfish);
            imageView.setCornerRadius(50);
            imageView.setScaleMode(Image.ScaleMode.CENTER);

        } catch (Exception e) { }
    }

    private void RIV_Testcase3() {
        try{

            LogUtil.info(TAG,"RIV_Testcase3");
            imageView.setImageResource(ResourceTable.Media_photo5);
            imageView.setOval(true);
            imageView.setBorderWidth(9);
            imageView.setScaleMode(Image.ScaleMode.CENTER);

        } catch (Exception e) { }
    }

    private void RIV_Testcase4() {  //Rounded Rectangle
        try{
            LogUtil.info(TAG,"RIV_Testcase4");
            imageView.setImageResource(ResourceTable.Media_photo5);
            imageView.setOval(true);
            imageView.setBorderWidth(9);
            imageView.setScaleMode(Image.ScaleMode.ZOOM_CENTER);

        } catch (Exception e) { }
    }

    private void RIV_Testcase5() {  //Oval Imageview
        try{

            LogUtil.info(TAG,"RIV_Testcase5");
            imageView.setImageResource(ResourceTable.Media_photo5);
            imageView.setOval(true);
            imageView.setBorderWidth(9);
            imageView.setScaleMode(Image.ScaleMode.STRETCH);

        } catch (Exception e) { }
    }


    private void RIV_Testcase6() {  //Color Drawable
        try{
            LogUtil.info(TAG,"RIV_Testcase6");
            imageView.setWidth(1024);
            imageView.setHeight(600);
            imageView.setColorResource(ResourceTable.Color_material_lighter_gray);
            imageView.setOval(true);
            imageView.setScaleMode(Image.ScaleMode.CENTER);

        } catch (Exception e) { }
    }

    private void RIV_Testcase7() {  //Color Drawable
        try{
            LogUtil.info(TAG,"RIV_Testcase7");
            imageView.setWidth(1024);
            imageView.setHeight(1024);
            imageView.setColorResource(ResourceTable.Color_black);
            imageView.setCornerRadius(50);
            imageView.setScaleMode(Image.ScaleMode.CENTER);

        } catch (Exception e) { }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
