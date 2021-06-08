/*
 * Voicesmith <http://voicesmith.jurihock.de/>
 *
 * Copyright (c) 2011-2014 Juergen Hock
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.jurihock.voicesmith.services;

import android.content.Intent;
import android.os.IBinder;
import de.jurihock.voicesmith.AAF;
import de.jurihock.voicesmith.io.AudioDevice;
import de.jurihock.voicesmith.threads.AudioThread;

/**
 * Implementation of the vocoder audio service.
 * */
public final class AafService extends AudioService
{
    // FIXME: If init with default value, loading prefs in setter may be ignored during first call
	private AAF	aaf	= null; // AAF.valueOf(0);

	public AAF getAaf()
	{
		return aaf;
	}

	public void setAaf(AAF aaf)
	{
		if (this.aaf == aaf) return;

		if (isThreadRunning())
		{
			stopThread(true);

			this.aaf = aaf;
            preferences.setAaf(aaf);
            setThreadName("Thread_" + aaf.toString());
            setThreadPreferences(preferences.getAudioThreadPreferences(getThreadName()));

			startThread();
		}
		else
		{
            stopThread(false); // FIXME: Find more elegant way to save current preferences

			this.aaf = aaf;
            preferences.setAaf(aaf);
            setThreadName("Thread_" + aaf.toString());
            setThreadPreferences(preferences.getAudioThreadPreferences(getThreadName()));
		}
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		setAaf(preferences.getAaf());
	}

	@Override
	protected AudioThread createAudioThread(AudioDevice input, AudioDevice output)
	{
		return AudioThread.create(this, input, output, aaf);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return new ServiceBinder<AafService>(this);
	}
}
