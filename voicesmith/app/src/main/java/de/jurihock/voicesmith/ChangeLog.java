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

package de.jurihock.voicesmith;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ChangeLog extends AlertDialog.Builder
{
    private final Context context;

    public ChangeLog(Context context)
    {
        super(context);

        this.context = context;

        setTitle(new Utils(context).getVersionString(R.string.ChangeLogTitle));
        setMessage(R.string.ChangeLogMessage);

        setNegativeButton(android.R.string.ok,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                    }
                }
        );
    }

    @Override
    public AlertDialog show()
    {
        Preferences preferences = new Preferences(context);

        if (!preferences.isChangeLogShowed())
        {
            preferences.setChangeLogShowed(true);
            return super.show();
        }

        return null;
        // return super.show(); // TEST: show changelog always
    }
}
