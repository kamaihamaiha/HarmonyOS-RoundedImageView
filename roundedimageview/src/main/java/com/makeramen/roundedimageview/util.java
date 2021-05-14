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
package com.makeramen.roundedimageview;

import ohos.global.resource.NotExistException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;
import ohos.global.resource.Resource;
import ohos.agp.components.element.PixelMapElement;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class util {

    public static PixelMapElement prepareElement(Resource resource) throws IOException, NotExistException {
        return new PixelMapElement(preparePixelmap(resource));
    }

    public static PixelMap preparePixelmap(Resource resource) throws IOException, NotExistException {
        ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
        ImageSource imageSource = null;
        try {
            imageSource = ImageSource.create(readResource(resource), srcOpts);
        } finally {
            close(resource);
        }
        if (imageSource == null) {
            throw new FileNotFoundException();
        }
        ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
        decodingOpts.desiredSize = new Size(0, 0);
        decodingOpts.desiredRegion = new Rect(0, 0, 0, 0);
        decodingOpts.desiredPixelFormat = PixelFormat.ARGB_8888;

        PixelMap pixelmap = imageSource.createPixelmap(decodingOpts);
        return pixelmap;
    }

    private static void close(Resource resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
            }
        }
    }

    private static byte[] readResource(Resource resource) {
        final int bufferSize = 1024;
        final int ioEnd = -1;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[bufferSize];
        while (true) {
            try {
                int readLen = resource.read(buffer, 0, bufferSize);
                if (readLen == ioEnd) {
                    break;
                }
                output.write(buffer, 0, readLen);
            } catch (IOException e) {
                break;
            }
        }
        return output.toByteArray();
    }
}
