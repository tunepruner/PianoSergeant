/*
 * Copyright (C) 2015 The Android Open Source Project
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
package com.tunepruner.musictraining.midi

import android.media.midi.MidiReceiver
import com.tunepruner.musictraining.model.music.Pitch
import java.io.IOException

class NoteReceiver(noteSender: NoteSender) : MidiReceiver() {
    private val noteSender: NoteSender = noteSender
    @Throws(IOException::class)

    override fun onSend(data: ByteArray, offset: Int, count: Int, timestamp: Long) {
        if (MidiParser.isNoteOn(data, offset, count)) {
            noteSender.send(Pitch(data[offset + 1].toInt(), null).pitchClass.name)
        }
    }
}