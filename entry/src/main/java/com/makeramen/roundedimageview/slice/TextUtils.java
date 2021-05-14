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

public class TextUtils {

    private TextUtils() {
    }

    /**
     * check if the input is empty
     *
     * @param input the input string
     * @return the input string is empty
     */
    public static boolean isEmpty(String input) {
        return input == null || input.length() == 0;
    }

    /**
     * check if the input string is empty
     *
     * @param input the input strings
     * @return the input string is empty
     */
    public static boolean isEmpty(String... input) {
        if (input == null) {
            return true;
        }
        for (String oneItem : input) {
            if (isEmpty(oneItem)) {
                return true;
            }
        }
        return false;
    }
}