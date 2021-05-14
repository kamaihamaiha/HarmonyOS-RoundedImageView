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
import ohos.agp.components.Image;

public class CircleXmlImageviewSlice extends AbilitySlice {
    public static final String TAG = CircleXmlImageviewSlice.class.getSimpleName();
    private int testcase_value = 0;
    private RoundedImageView riv = null;

    @Override
    public void onStop() {
        if (null != riv)
            riv.release();
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        Component component;
        IntentParams ip = intent.getParams();
        testcase_value = (int) ip.getParam("testcase");

        if (Const.TEST_CASE_4 == testcase_value) {
            setUIContent(ResourceTable.Layout_rounded_circlexmlitem_layout);
            component = findComponentById(ResourceTable.Id_two_riv);
        } else {
            setUIContent(ResourceTable.Layout_circlexmlitem_layout);
            component = findComponentById(ResourceTable.Id_one_riv);
        }

        if (component instanceof RoundedImageView) {
            riv = (RoundedImageView) component;

            //Create ImageView based on testcase
            switch (testcase_value) {
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
                default:
                    break;
            }
        }
    }

    private void RIV_Testcase1() {
        try {

            LogUtil.info(TAG, "RIV_Testcase1");
            riv.setImageResource(ResourceTable.Media_photo5);
            riv.setScaleMode(Image.ScaleMode.CENTER);

        } catch (Exception e) {
        }
    }

    private void RIV_Testcase2() {
        try {
            // WORKING CODE
            LogUtil.info(TAG, "RIV_Testcase2");
            riv.setImageResource(ResourceTable.Media_photo5);
            riv.setScaleMode(Image.ScaleMode.ZOOM_CENTER);
        } catch (Exception e) {
        }

    }

    private void RIV_Testcase3() {
        try {

            LogUtil.info(TAG, "RIV_Testcase3");
            riv.setImageResource(ResourceTable.Media_photo5);
            riv.setScaleMode(Image.ScaleMode.STRETCH);
        } catch (Exception e) {
        }

    }

    private void RIV_Testcase4() {
        try {

            LogUtil.info(TAG, "RIV_Testcase4");
            riv.setImageResource(ResourceTable.Media_photo5);
            riv.setCornerRadius(80);
            riv.setScaleMode(Image.ScaleMode.STRETCH);
        } catch (Exception e) {
        }

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
