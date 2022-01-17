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

package com.tunepruner.musictraining.midi;

import android.media.midi.MidiReceiver;

import com.tunepruner.musictraining.model.music.Pitch;

import java.io.IOException;

/**
 * Convert incoming MIDI messages to a string and write them to a ScopeLogger.
 * Assume that messages have been aligned using a MidiFramer.
 */
public class NoteReceiver extends MidiReceiver {
    public static final String TAG = "MidiScope";
    private static final long NANOS_PER_MILLISECOND = 1000000L;
    private static final long NANOS_PER_SECOND = NANOS_PER_MILLISECOND * 1000L;
    private long mStartTime;
    private NoteSender noteSender;
    private long mLastTimeStamp = 0;

    public NoteReceiver(NoteSender noteSender) {
        mStartTime = System.nanoTime();
        this.noteSender = noteSender;
    }

    @Override
    public void onSend(byte[] data, int offset, int count, long timestamp)
            throws IOException {
        if (MidiParser.isNoteOn(data, offset, count)) {
            noteSender.send(new Pitch(data[offset + 1], null).getPitchClass().getName());
        }

    }

}
