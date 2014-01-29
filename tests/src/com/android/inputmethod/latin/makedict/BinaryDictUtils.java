/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.android.inputmethod.latin.makedict;

import com.android.inputmethod.latin.makedict.FormatSpec.FileHeader;
import com.android.inputmethod.latin.makedict.FormatSpec.FormatOptions;
import com.android.inputmethod.latin.makedict.FusionDictionary.DictionaryOptions;

import java.io.File;
import java.util.HashMap;

public class BinaryDictUtils {
    public static final int USE_BYTE_ARRAY = 1;
    public static final int USE_BYTE_BUFFER = 2;

    public static final String TEST_DICT_FILE_EXTENSION = ".testDict";

    public static final FormatSpec.FormatOptions VERSION2_OPTIONS =
            new FormatSpec.FormatOptions(FormatSpec.VERSION2);
    public static final FormatSpec.FormatOptions VERSION4_OPTIONS_WITHOUT_TIMESTAMP =
            new FormatSpec.FormatOptions(FormatSpec.VERSION4, false /* hasTimestamp */);
    public static final FormatSpec.FormatOptions VERSION4_OPTIONS_WITH_TIMESTAMP =
            new FormatSpec.FormatOptions(FormatSpec.VERSION4, true /* hasTimestamp */);

    public static DictionaryOptions makeDictionaryOptions(final String id, final String version,
            final FormatSpec.FormatOptions formatOptions) {
        final DictionaryOptions options = new DictionaryOptions(new HashMap<String, String>());
        options.mAttributes.put(FileHeader.DICTIONARY_LOCALE_ATTRIBUTE, "en_US");
        options.mAttributes.put(FileHeader.DICTIONARY_ID_ATTRIBUTE, id);
        options.mAttributes.put(FileHeader.DICTIONARY_VERSION_ATTRIBUTE, version);
        if (formatOptions.mHasTimestamp) {
            options.mAttributes.put(FileHeader.HAS_HISTORICAL_INFO_ATTRIBUTE,
                    FileHeader.ATTRIBUTE_VALUE_TRUE);
            options.mAttributes.put(FileHeader.USES_FORGETTING_CURVE_ATTRIBUTE,
                    FileHeader.ATTRIBUTE_VALUE_TRUE);
        }
        return options;
    }

    public static File getDictFile(final String name, final String version,
            final FormatOptions formatOptions, final File directory) {
        if (formatOptions.mVersion == FormatSpec.VERSION2) {
            return new File(directory, name + "." + version + TEST_DICT_FILE_EXTENSION);
        } else if (formatOptions.mVersion == FormatSpec.VERSION4) {
            return new File(directory, name + "." + version);
        } else {
            throw new RuntimeException("the format option has a wrong version : "
                    + formatOptions.mVersion);
        }
    }

    public static DictEncoder getDictEncoder(final File file, final FormatOptions formatOptions) {
        if (formatOptions.mVersion == FormatSpec.VERSION4) {
            if (!file.isDirectory()) {
                file.mkdir();
            }
            return new Ver4DictEncoder(file);
        } else if (formatOptions.mVersion == FormatSpec.VERSION2) {
            return new Ver2DictEncoder(file);
        } else {
            throw new RuntimeException("The format option has a wrong version : "
                    + formatOptions.mVersion);
        }
    }
}
