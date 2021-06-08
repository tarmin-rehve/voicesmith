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

package de.jurihock.voicesmith.activities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import de.jurihock.voicesmith.DAFX;
import de.jurihock.voicesmith.R;
import de.jurihock.voicesmith.Utils;
import de.jurihock.voicesmith.services.DafxService;
import de.jurihock.voicesmith.services.ServiceFailureReason;
import de.jurihock.voicesmith.services.ServiceListener;
import de.jurihock.voicesmith.widgets.ColoredToggleButton;
import de.jurihock.voicesmith.widgets.DafxPicker;
import de.jurihock.voicesmith.widgets.IntervalPicker;

public final class DafxActivity extends AudioServiceActivity<DafxService>
	implements
	PropertyChangeListener, OnClickListener,
	OnCheckedChangeListener, ServiceListener
{
	// Relevant activity widgets:
	private DafxPicker			viewDafxPicker			= null;
	private IntervalPicker		viewIntervalPicker		= null;
	private CheckBox			viewBluetoothHeadset	= null;
    private CheckBox            viewInternalMic         = null;
	private ColoredToggleButton	viewStartStopButton		= null;

	public DafxActivity()
	{
		super(DafxService.class);
	}

	/**
	 * Initializes the activity, its layout and widgets.
	 * */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setActionBarContentView(R.layout.dafx);

		viewDafxPicker = (DafxPicker) this.findViewById(R.id.viewDafxPicker);
		viewDafxPicker.setPropertyChangeListener(this);

		viewIntervalPicker = (IntervalPicker) this.findViewById(
			R.id.viewIntervalPicker);
		viewIntervalPicker.setPropertyChangeListener(this);

		viewBluetoothHeadset = (CheckBox) this.findViewById(
			R.id.viewBluetoothHeadset);
		viewBluetoothHeadset.setOnCheckedChangeListener(this);

        viewInternalMic = (CheckBox) this.findViewById(
                R.id.viewInternalMic);
        viewInternalMic.setOnCheckedChangeListener(this);

		viewStartStopButton = (ColoredToggleButton) this.findViewById(
			R.id.viewStartStopButton);
		viewStartStopButton.setOnClickListener(this);
	}

	@Override
	protected void onServiceConnected()
	{
		new Utils(this).log("DafxActivity founds the audio service.");

		getService().setActivityVisible(true, this.getClass());
		getService().setListener(this);

		// Update widgets
		viewDafxPicker.setDafx(getService().getDafx());
        viewBluetoothHeadset.setChecked(getService().isBluetoothHeadsetSupportOn());
        viewInternalMic.setChecked(getService().isInternalMicSupportOn());
		viewStartStopButton.setChecked(getService().isThreadRunning());

		if (getService().getDafx() == DAFX.Transpose)
		{
			viewIntervalPicker.setVisibility(View.VISIBLE);

            if(getService().hasThreadPreferences())
			{
				int interval = Integer.parseInt(getService().getThreadPreferences());
				viewIntervalPicker.setInterval(interval);
			}
		}
		else
		{
			viewIntervalPicker.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onServiceDisconnected()
	{
		new Utils(this).log("DafxActivity losts the audio service.");

		if (!this.isFinishing())
		{
			getService().setActivityVisible(false, this.getClass());
		}

		getService().setListener(null);
	}

	public void onClick(View view)
	{
		if (getService().isThreadRunning())
		{
			if (viewStartStopButton.isChecked())
				viewStartStopButton.setChecked(false);

			getService().stopThread(false);
		}
		else
		{
			if (!viewStartStopButton.isChecked())
				viewStartStopButton.setChecked(true);

			getService().startThread();
		}

		// BZZZTT!!1!
		viewStartStopButton.performHapticFeedback(0);
	}

    public void onCheckedChanged(CompoundButton view, boolean value)
    {
        if (view == viewBluetoothHeadset)
        {
            if (getService().isBluetoothHeadsetSupportOn() != value)
            {
                getService().setBluetoothHeadsetSupport(value);
            }
        }
        else if (view == viewInternalMic)
        {
            if (getService().isInternalMicSupportOn() != value)
            {
                getService().setInternalMicSupport(value);
            }
        }
    }

	@Override
	public void propertyChange(PropertyChangeEvent event)
	{
		if (event.getSource().equals(viewDafxPicker))
		{
			DAFX dafx = viewDafxPicker.getDafx();

			getService().setDafx(dafx);

			if (dafx == DAFX.Transpose)
			{
				viewIntervalPicker.setVisibility(View.VISIBLE);

                if(getService().hasThreadPreferences())
                {
                    int interval = Integer.parseInt(getService().getThreadPreferences());
                    viewIntervalPicker.setInterval(interval);
                }
			}
			else
			{
				viewIntervalPicker.setVisibility(View.GONE);
			}
		}
		else if (event.getSource().equals(viewIntervalPicker))
		{
			int interval = viewIntervalPicker.getInterval();
			getService().setThreadPreferences(Integer.toString(interval));
		}
	}
	
	public void onServiceFailed(ServiceFailureReason reason)
	{
		if (viewStartStopButton.isChecked())
			viewStartStopButton.setChecked(false);

		new Utils(this).toast(getString(R.string.ServiceFailureMessage));

		// BZZZTT!!1!
		viewStartStopButton.performHapticFeedback(0);
	}
}
